package fr.hugman.universal_ores.platform;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Helpers for {@link UniversalOresPlatform#forEachModDataFile}.
 */
public final class DataFiles {
    private DataFiles() {
    }

    /**
     * Turns a path relative to a mod's root ({@code data/<namespace>/<directory>/<path>.json}) into
     * {@code <namespace>:<path>}, or {@code null} when it isn't a JSON file of that directory.
     */
    public static @Nullable Identifier idOf(String relativePath, String directory) {
        if (!relativePath.startsWith("data/") || !relativePath.endsWith(".json")) {
            return null;
        }
        String rest = relativePath.substring("data/".length(), relativePath.length() - ".json".length());
        int slash = rest.indexOf('/');
        if (slash < 0) {
            return null;
        }
        String namespace = rest.substring(0, slash);
        String path = rest.substring(slash + 1);
        if (!path.startsWith(directory + "/")) {
            return null;
        }
        return Identifier.tryBuild(namespace, path.substring(directory.length() + 1));
    }
}
