package fr.hugman.universal_ores.client.texture;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import fr.hugman.universal_ores.client.texture.TestImages.Image;
import fr.hugman.universal_ores.platform.DataFiles;
import fr.hugman.universal_ores.variant.OreType;
import fr.hugman.universal_ores.variant.OreVariant;
import fr.hugman.universal_ores.variant.OreVariants;
import fr.hugman.universal_ores.variant.StoneType;
import net.minecraft.resources.Identifier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Draws every ore variant that the stone and ore types found in some mods would make, without starting the game.
 * Textures and definitions are read from the given mod jars (or folders), then from Universal Ores and Minecraft.
 * <p>
 * Arguments: the output PNG, then the mods separated by the platform's path separator.
 */
public final class OreTexturePreview {
    private static final OreTextureSettings SETTINGS = OreTextureSettings.DEFAULT;

    public static void main(String[] args) throws IOException, URISyntaxException {
        Path output = Path.of(args[0]);
        List<Path> roots = openMods(args.length > 1 ? args[1] : "");
        // Universal Ores' own definitions
        Path ownData = Path.of(Objects.requireNonNull(OreTexturePreview.class.getResource("/data/minecraft/universal_ores")).toURI());
        roots.add(ownData.getParent().getParent().getParent());

        Map<Identifier, StoneType> stones = new LinkedHashMap<>();
        Map<Identifier, OreType> ores = new LinkedHashMap<>();
        for (Path root : roots) {
            readDefinitions(root, OreVariants.STONE_TYPE_DIRECTORY, StoneType.CODEC, stones);
            readDefinitions(root, OreVariants.ORE_TYPE_DIRECTORY, OreType.CODEC, ores);
        }
        Map<Identifier, OreVariant> variants = OreVariants.combine(stones, ores);

        Map<Identifier, Optional<OreTemplate>> templates = new HashMap<>();
        List<BufferedImage> sheets = new ArrayList<>();
        Set<String> categories = new TreeSet<>();
        ores.values().forEach(ore -> categories.add(ore.category()));
        for (String category : categories) {
            List<Identifier> oreIds = ores.entrySet().stream().filter(e -> e.getValue().category().equals(category)).map(Map.Entry::getKey).toList();
            List<String> columns = new ArrayList<>(List.of("stone"));
            List<Image> header = new ArrayList<>();
            header.add(null);
            for (Identifier oreId : oreIds) {
                columns.add(oreId.getPath());
                header.add(texture(roots, ores.get(oreId).textureOrDefault()));
            }
            List<TestImages.Row> rows = new ArrayList<>();
            rows.add(new TestImages.Row(category + " ores", header));
            int drawn = 0;
            for (var stoneEntry : stones.entrySet()) {
                if (!stoneEntry.getValue().category().equals(category)) {
                    continue;
                }
                Identifier stoneTexture = stoneTexture(roots, stoneEntry.getValue());
                Image stone = stoneTexture == null ? null : texture(roots, stoneTexture);
                if (stone == null) {
                    System.out.println("Skipping " + stoneEntry.getKey() + ": no texture found");
                    continue;
                }
                List<Image> images = new ArrayList<>();
                images.add(stone);
                for (Identifier oreId : oreIds) {
                    OreVariant variant = variants.get(OreVariant.idOf(stoneEntry.getValue(), ores.get(oreId)));
                    if (variant == null) {
                        images.add(null);
                        continue;
                    }
                    Optional<OreTemplate> template = templates.computeIfAbsent(oreId, id -> template(roots, ores.get(id)));
                    images.add(template.map(t -> new Image(stone.width(), stone.height(), OreTextureGenerator.generate(t, stone.width(), stone.height(), stone.pixels(), SETTINGS))).orElse(null));
                    drawn++;
                }
                rows.add(new TestImages.Row(stoneEntry.getKey().toString(), images));
            }
            System.out.println(category + ": " + drawn + " variants drawn");
            sheets.add(TestImages.sheet(columns, rows, 4));
        }

        int width = sheets.stream().mapToInt(BufferedImage::getWidth).max().orElse(1);
        int height = sheets.stream().mapToInt(BufferedImage::getHeight).sum() + 12 * sheets.size();
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var graphics = combined.createGraphics();
        graphics.setColor(new java.awt.Color(20, 20, 24));
        graphics.fillRect(0, 0, width, height);
        int y = 0;
        for (BufferedImage sheet : sheets) {
            graphics.drawImage(sheet, 0, y, null);
            y += sheet.getHeight() + 12;
        }
        graphics.dispose();
        TestImages.write(combined, output);
        System.out.println("Wrote " + output.toAbsolutePath());
    }

    private static Optional<OreTemplate> template(List<Path> roots, OreType ore) {
        Image texture = texture(roots, ore.textureOrDefault());
        if (texture == null) {
            System.out.println("No texture for " + ore.block());
            return Optional.empty();
        }
        Image host = texture(roots, ore.hostTextureOrDefault());
        if (host == null) {
            return Optional.of(OreTemplate.fromOverlay(texture.width(), texture.height(), texture.pixels(), SETTINGS));
        }
        host = host.scaledTo(texture.width(), texture.height());
        Identifier hint = Identifier.fromNamespaceAndPath("universal_ores", "ore_hint/" + ore.block().getNamespace() + "/" + ore.block().getPath());
        Image oreHint = texture(roots, hint);
        Image haloHint = texture(roots, hint.withSuffix("_halo"));
        return Optional.of(OreTemplate.extract(texture.width(), texture.height(), texture.pixels(), host.pixels(),
                oreHint == null || oreHint.width() != texture.width() ? null : oreHint.mask(),
                haloHint == null || haloHint.width() != texture.width() ? null : haloHint.mask(), SETTINGS));
    }

    private static Identifier stoneTexture(List<Path> roots, StoneType stone) {
        var textures = stone.textures();
        if (textures.isPresent() && textures.get().all().isPresent()) {
            return textures.get().all().get();
        }
        if (textures.isPresent() && textures.get().side().isPresent()) {
            return textures.get().side().get();
        }
        Identifier all = stone.block().withPrefix("block/");
        return texture(roots, all) != null ? all : all.withSuffix("_side");
    }

    private static Image texture(List<Path> roots, Identifier id) {
        String path = "assets/" + id.getNamespace() + "/textures/" + id.getPath() + ".png";
        for (Path root : roots) {
            Path file = root.resolve(path);
            if (Files.isRegularFile(file)) {
                try (InputStream stream = Files.newInputStream(file)) {
                    return TestImages.read(stream);
                } catch (IOException e) {
                    System.out.println("Can't read " + file + ": " + e);
                }
            }
        }
        InputStream stream = OreTexturePreview.class.getResourceAsStream("/" + path);
        if (stream == null) {
            return null;
        }
        try (stream) {
            return TestImages.read(stream);
        } catch (IOException e) {
            return null;
        }
    }

    private static <T> void readDefinitions(Path root, String directory, Codec<T> codec, Map<Identifier, T> into) throws IOException {
        Path data = root.resolve("data");
        if (!Files.isDirectory(data)) {
            return;
        }
        try (Stream<Path> files = Files.walk(data)) {
            for (Path file : files.filter(Files::isRegularFile).toList()) {
                Identifier id = DataFiles.idOf(root.relativize(file).toString().replace('\\', '/'), directory);
                if (id == null) {
                    continue;
                }
                try (var reader = new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8)) {
                    codec.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader))
                            .ifSuccess(value -> into.putIfAbsent(id, value))
                            .ifError(error -> System.out.println("Invalid definition " + id + ": " + error.message()));
                }
            }
        }
    }

    /**
     * The root of each mod: the folder itself, or the inside of the jar.
     */
    private static List<Path> openMods(String mods) throws IOException {
        List<Path> roots = new ArrayList<>();
        for (String mod : mods.split(File.pathSeparator)) {
            if (mod.isBlank()) {
                continue;
            }
            Path path = Path.of(mod.trim());
            roots.add(Files.isDirectory(path) ? path : FileSystems.newFileSystem(path).getPath("/"));
        }
        return roots;
    }
}
