package net.ilexiconn.jurassicraft.common.tileentity.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSecurityFenceHighGrid extends BlockEntity {
    public TileSecurityFenceHighGrid() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFenceHighGrid(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
}
