package net.ilexiconn.jurassicraft.common.block.fence;

import net.ilexiconn.jurassicraft.common.api.IFenceBase;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.BlockPos;

public class BlockSecurityFenceLowBase extends BlockSecurityFence implements IFenceBase {
    public BlockSecurityFenceLowBase() {
        super(10.0F, 150.0F, 2, "low_security_fence_base");
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileSecurityFenceLowBase(pos, state);
    }
}
