package fr.hugman.universal_ores.client.texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Reading, comparing and laying out textures for the tests and the preview tool.
 */
final class TestImages {
    private TestImages() {
    }

    record Image(int width, int height, int[] pixels) {
        Image firstFrame() {
            if (this.height > this.width && this.height % this.width == 0) {
                int[] frame = new int[this.width * this.width];
                System.arraycopy(this.pixels, 0, frame, 0, frame.length);
                return new Image(this.width, this.width, frame);
            }
            return this;
        }

        Image scaledTo(int width, int height) {
            if (width == this.width && height == this.height) {
                return this;
            }
            int[] scaled = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    scaled[y * width + x] = this.pixels[(y * this.height / height) * this.width + x * this.width / width];
                }
            }
            return new Image(width, height, scaled);
        }

        boolean[] mask() {
            boolean[] mask = new boolean[this.pixels.length];
            for (int i = 0; i < mask.length; i++) {
                mask[i] = (this.pixels[i] >>> 24) != 0;
            }
            return mask;
        }
    }

    static Image read(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        if (image == null) {
            throw new IOException("Not an image");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        // getRGB() treats grey PNGs (like vanilla's stone) as linear and lightens them, so their samples are read as they are
        if (image.getColorModel().getColorSpace().getType() == ColorSpace.TYPE_GRAY) {
            Raster raster = image.getRaster();
            int bands = raster.getNumBands();
            int shift = raster.getSampleModel().getSampleSize(0) - 8;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int grey = raster.getSample(x, y, 0) >> shift;
                    int alpha = bands > 1 ? raster.getSample(x, y, 1) >> shift : 0xFF;
                    pixels[y * width + x] = alpha << 24 | grey << 16 | grey << 8 | grey;
                }
            }
        } else {
            image.getRGB(0, 0, width, height, pixels, 0, width);
        }
        return new Image(width, height, pixels).firstFrame();
    }

    static Image resource(String path) {
        try (InputStream stream = TestImages.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Missing test resource " + path);
            }
            return read(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Mean OKLab distance between two textures of the same size, in hundredths.
     */
    static double meanDistance(Image first, Image second) {
        double sum = 0;
        for (int i = 0; i < first.pixels.length; i++) {
            sum += OkLab.distance(OkLab.fromArgb(first.pixels[i]), OkLab.fromArgb(second.pixels[i]));
        }
        return 100 * sum / first.pixels.length;
    }

    /**
     * Mean OKLab distance between a generated ore texture and its reference, in hundredths, over the ore and its
     * surroundings only (the pixels next to the ore, and those the reference changed), so that the untouched stone
     * doesn't dilute it. The area doesn't depend on the generated texture, so that generators can be compared.
     */
    static double oreDistance(Image generated, Image reference, Image stone, OreTemplate template) {
        double sum = 0;
        int count = 0;
        for (int y = 0; y < generated.height; y++) {
            for (int x = 0; x < generated.width; x++) {
                int i = y * generated.width + x;
                if (reference.pixels[i] != stone.pixels[i] || isNearOre(template, x * template.width() / generated.width, y * template.height() / generated.height)) {
                    sum += OkLab.distance(OkLab.fromArgb(generated.pixels[i]), OkLab.fromArgb(reference.pixels[i]));
                    count++;
                }
            }
        }
        return count == 0 ? 0 : 100 * sum / count;
    }

    private static boolean isNearOre(OreTemplate template, int x, int y) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && ny >= 0 && nx < template.width() && ny < template.height() && template.ore(ny * template.width() + nx) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A labelled grid of textures, scaled up without smoothing.
     */
    static BufferedImage sheet(List<String> columnLabels, List<Row> rows, int scale) {
        int cell = 16 * scale;
        int gap = 4;
        int labelWidth = 150;
        int headerHeight = 18;
        int width = labelWidth + columnLabels.size() * (cell + gap);
        int height = headerHeight + rows.size() * (cell + gap);
        BufferedImage sheet = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = sheet.createGraphics();
        graphics.setColor(new Color(32, 32, 36));
        graphics.fillRect(0, 0, width, height);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        graphics.setColor(Color.WHITE);
        for (int column = 0; column < columnLabels.size(); column++) {
            graphics.drawString(columnLabels.get(column), labelWidth + column * (cell + gap) + 2, 13);
        }
        for (int row = 0; row < rows.size(); row++) {
            int y = headerHeight + row * (cell + gap);
            graphics.setColor(Color.WHITE);
            graphics.drawString(rows.get(row).label(), 4, y + cell / 2);
            List<Image> images = rows.get(row).images();
            for (int column = 0; column < images.size(); column++) {
                Image image = images.get(column);
                if (image == null) {
                    continue;
                }
                BufferedImage tile = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_ARGB);
                tile.setRGB(0, 0, image.width(), image.height(), image.pixels(), 0, image.width());
                graphics.drawImage(tile, labelWidth + column * (cell + gap), y, cell, cell, null);
            }
        }
        graphics.dispose();
        return sheet;
    }

    record Row(String label, List<Image> images) {
    }

    static void write(Image image, Path file) {
        BufferedImage buffered = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_ARGB);
        buffered.setRGB(0, 0, image.width(), image.height(), image.pixels(), 0, image.width());
        write(buffered, file);
    }

    static void write(BufferedImage image, Path file) {
        try {
            Files.createDirectories(file.toAbsolutePath().getParent());
            ImageIO.write(image, "png", file.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
