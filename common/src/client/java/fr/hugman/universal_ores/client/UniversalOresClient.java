package fr.hugman.universal_ores.client;

/**
 * Client initialization shared by both loaders.
 */
public final class UniversalOresClient {
    private UniversalOresClient() {
    }

    public static void init() {
        OreVariantAssets.init();
    }
}
