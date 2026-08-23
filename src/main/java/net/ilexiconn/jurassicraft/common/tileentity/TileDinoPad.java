package net.ilexiconn.jurassicraft.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileDinoPad extends BlockEntity {
    public TileDinoPad() {
        this(BlockPos.ZERO, null);
    }

    public TileDinoPad(BlockPos pos, BlockState state) {
        super(JCTileEntityRegistry.DINO_PAD.get(), pos, state);
    }
}
