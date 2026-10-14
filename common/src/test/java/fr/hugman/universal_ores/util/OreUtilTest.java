package fr.hugman.universal_ores.util;

import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The registries are frozen once the game is bootstrapped, so vanilla blocks stand for the variant blocks.
 */
class OreUtilTest {
    private static final RandomSource RANDOM = RandomSource.create(0);

    private static RegisteredOreVariant graniteCoalOre;
    private static RegisteredOreVariant graniteRedstoneOre;
    private static RegisteredOreVariant dioriteCoalOre;
    private static RegisteredOreVariant basaltCoalOre;
    private static List<RegisteredOreVariant> variants;

    @BeforeAll
    static void bootstrap() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        graniteCoalOre = new RegisteredOreVariant(null, Blocks.COAL_BLOCK, Blocks.GRANITE, Blocks.COAL_ORE);
        graniteRedstoneOre = new RegisteredOreVariant(null, Blocks.REDSTONE_BLOCK, Blocks.GRANITE, Blocks.REDSTONE_ORE);
        dioriteCoalOre = new RegisteredOreVariant(null, Blocks.DIAMOND_BLOCK, Blocks.DIORITE, Blocks.COAL_ORE);
        basaltCoalOre = new RegisteredOreVariant(null, Blocks.OAK_LOG, Blocks.BASALT, Blocks.COAL_ORE);
        variants = List.of(graniteCoalOre, graniteRedstoneOre, dioriteCoalOre, basaltCoalOre);
    }

    /**
     * What an ore feature with these targets places over this block, like {@code OreFeature} does: the state of the
     * first target that matches.
     */
    private static BlockState placedOver(List<OreConfiguration.TargetBlockState> targets, BlockState state) {
        for (var target : targets) {
            if (target.target.test(state, RANDOM)) {
                return target.state;
            }
        }
        return state;
    }

    private static List<OreConfiguration.TargetBlockState> withVariants(List<OreConfiguration.TargetBlockState> targets) {
        var newTargets = OreUtil.getNewTargets(targets, variants);
        assertNotNull(newTargets);
        return newTargets;
    }

    @Test
    void oresGenerateAsTheirVariantsInTheirStones() {
        var targets = withVariants(List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.STONE), Blocks.COAL_ORE.defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.DEEPSLATE), Blocks.DEEPSLATE_COAL_ORE.defaultBlockState())
        ));
        assertEquals(Blocks.COAL_ORE.defaultBlockState(), placedOver(targets, Blocks.STONE.defaultBlockState()));
        assertEquals(Blocks.DEEPSLATE_COAL_ORE.defaultBlockState(), placedOver(targets, Blocks.DEEPSLATE.defaultBlockState()));
        assertEquals(Blocks.COAL_BLOCK.defaultBlockState(), placedOver(targets, Blocks.GRANITE.defaultBlockState()));
        assertEquals(Blocks.DIAMOND_BLOCK.defaultBlockState(), placedOver(targets, Blocks.DIORITE.defaultBlockState()));
        // A coal ore feature doesn't place stones, so it doesn't turn ores into variants
        assertEquals(Blocks.REDSTONE_ORE.defaultBlockState(), placedOver(targets, Blocks.REDSTONE_ORE.defaultBlockState()));
    }

    @Test
    void stonesTurnTheOresTheyCoverIntoVariants() {
        // A blob whose own target would match the ores too: ours must come first
        var targets = withVariants(List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.STONE), Blocks.GRANITE.defaultBlockState()),
                OreConfiguration.target(AlwaysTrueTest.INSTANCE, Blocks.GRANITE.defaultBlockState())
        ));
        assertEquals(Blocks.GRANITE.defaultBlockState(), placedOver(targets, Blocks.STONE.defaultBlockState()));
        assertEquals(Blocks.COAL_BLOCK.defaultBlockState(), placedOver(targets, Blocks.COAL_ORE.defaultBlockState()));
        // Ores with states are matched by block
        BlockState litRedstoneOre = Blocks.REDSTONE_ORE.defaultBlockState().setValue(BlockStateProperties.LIT, true);
        assertEquals(Blocks.REDSTONE_BLOCK.defaultBlockState(), placedOver(targets, litRedstoneOre));
        // Only the variants in that stone
        assertEquals(4, targets.size());
    }

    @Test
    void oresWithoutVariantInTheStoneStay() {
        var targets = withVariants(List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.STONE), Blocks.DIORITE.defaultBlockState())
        ));
        assertEquals(Blocks.DIAMOND_BLOCK.defaultBlockState(), placedOver(targets, Blocks.COAL_ORE.defaultBlockState()));
        assertEquals(Blocks.DEEPSLATE_COAL_ORE.defaultBlockState(), placedOver(targets, Blocks.DEEPSLATE_COAL_ORE.defaultBlockState()));
        assertEquals(Blocks.REDSTONE_ORE.defaultBlockState(), placedOver(targets, Blocks.REDSTONE_ORE.defaultBlockState()));
    }

    @Test
    void variantsOfPillarStonesLieLikeTheirStone() {
        BlockState basalt = Blocks.BASALT.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        var targets = withVariants(List.of(OreConfiguration.target(new BlockMatchTest(Blocks.STONE), basalt)));
        assertEquals(Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X), placedOver(targets, Blocks.COAL_ORE.defaultBlockState()));
    }

    @Test
    void variantsAreNotAddedTwice() {
        var targets = withVariants(List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.STONE), Blocks.COAL_ORE.defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.ANDESITE), Blocks.GRANITE.defaultBlockState())
        ));
        // Granite coal ore comes from the coal ore only, granite redstone ore from the granite
        assertEquals(List.of(Blocks.COAL_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.OAK_LOG, Blocks.REDSTONE_BLOCK), targets.subList(0, 4).stream().map(target -> target.state.getBlock()).toList());
        assertEquals(6, targets.size());
        // Building the configuration again from ours, like a codec would, changes nothing
        assertNull(OreUtil.getNewTargets(targets, variants));
    }

    @Test
    void otherFeaturesAreLeftAsTheyAre() {
        assertNull(OreUtil.getNewTargets(List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.STONE), Blocks.DIRT.defaultBlockState())
        ), variants));
    }
}
