package fr.hugman.universal_ores.block;

import net.minecraft.block.Block;

import java.util.function.Consumer;

public record OverworldOreBlocks(
        Block coal,
        Block iron,
        Block gold,
        Block copper,
        Block lapis,
        Block redstone,
        Block emerald,
        Block diamond
) {
    public void forEach(Consumer<Block> consumer) {
        consumer.accept(coal);
        consumer.accept(iron);
        consumer.accept(gold);
        consumer.accept(copper);
        consumer.accept(lapis);
        consumer.accept(redstone);
        consumer.accept(emerald);
        consumer.accept(diamond);
    }
}
