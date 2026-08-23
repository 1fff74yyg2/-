package net.ilexiconn.jurassicraft.common.block.fence;

import net.ilexiconn.jurassicraft.common.api.IFenceBase;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceMediumBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.BlockPos;

public class BlockSecurityFenceHighBase extends BlockSecurityFence implements IFenceBase {
    public BlockSecurityFenceHighBase() {
        super(10.0F, 150.0F, 2, "high_security_fence_base");
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Original 1.12.2 code used the medium-base tile entity here; kept as-is.
        return new TileSecurityFenceMediumBase(pos, state);
    }
}
