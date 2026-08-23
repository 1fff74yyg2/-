package net.ilexiconn.jurassicraft.common.tileentity.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSecurityFenceMediumGrid extends BlockEntity {
    public TileSecurityFenceMediumGrid() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFenceMediumGrid(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
}
