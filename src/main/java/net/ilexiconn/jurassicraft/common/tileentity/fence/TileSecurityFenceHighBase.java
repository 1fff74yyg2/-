package net.ilexiconn.jurassicraft.common.tileentity.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSecurityFenceHighBase extends BlockEntity {
    public TileSecurityFenceHighBase() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFenceHighBase(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
}
