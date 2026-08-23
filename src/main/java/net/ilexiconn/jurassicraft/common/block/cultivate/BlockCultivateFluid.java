package net.ilexiconn.jurassicraft.common.block.cultivate;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Placeholder for the old 1.12.2 fluid block.
 * <p>
 * 1.20.1 removed {@code BlockFluidClassic}, {@code Fluid} and {@code FluidRegistry}.
 * See MIGRATION_GUIDE_1201.md section 13: when the cultivate fluid is ported it
 * should become a {@link net.minecraft.world.level.block.LiquidBlock} backed by a
 * {@code ForgeFlowingFluid} pair. Until then this stays a plain solid block so the
 * mod compiles.
 */
public class BlockCultivateFluid extends Block {
    public BlockCultivateFluid() {
        super(BlockBehaviour.Properties.of().strength(100.0F).noCollission());
        // TODO: 1.20.1 — replace with LiquidBlock + ForgeFlowingFluid once fluids are ported.
    }
}
