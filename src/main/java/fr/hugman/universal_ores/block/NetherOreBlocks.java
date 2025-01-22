package fr.hugman.universal_ores.block;

import net.minecraft.block.Block;

import java.util.function.Consumer;

public record NetherOreBlocks(
        Block gold,
        Block quartz
) {
    public void forEach(Consumer<Block> consumer) {
        consumer.accept(gold);
        consumer.accept(quartz);
    }
}
