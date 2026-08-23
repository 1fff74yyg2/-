package net.ilexiconn.jurassicraft.common.tileentity.fence;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSecurityFenceLowBase extends BlockEntity {
    public TileSecurityFenceLowBase() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFenceLowBase(BlockPos pos, BlockState state) {
        super(JCTileEntityRegistry.FENCE_LOW_BASE.get(), pos, state);
    }
}
