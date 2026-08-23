package net.ilexiconn.jurassicraft.common.block.fence;

import net.ilexiconn.jurassicraft.common.api.IFenceGrid;
import net.ilexiconn.jurassicraft.common.api.IFencePole;
import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowPole;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class BlockSecurityFenceLowPole extends BlockSecurityFence implements IFencePole {
    public BlockSecurityFenceLowPole() {
        super(7.5F, 112.5F, 2, "low_security_fence_pole");
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (world.getBlockState(pos.above()).getBlock() instanceof IFencePole) {
                if (world.getBlockEntity(pos.above()) != null)
                    world.removeBlockEntity(pos.above());

                world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);

                if (!world.isClientSide)
                    this.dropPole(world, world.random, pos);
            }

            if (world.getBlockState(pos.south()).getBlock() instanceof IFenceGrid) {
                if (world.getBlockEntity(pos.south()) != null)
                    world.removeBlockEntity(pos.south());

                world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }

            if (world.getBlockState(pos.west()).getBlock() instanceof IFenceGrid) {
                if (world.getBlockEntity(pos.west()) != null)
                    world.removeBlockEntity(pos.west());

                world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }

            if (world.getBlockState(pos.north()).getBlock() instanceof IFenceGrid) {
                if (world.getBlockEntity(pos.north()) != null)
                    world.removeBlockEntity(pos.north());

                world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }

            if (world.getBlockState(pos.east()).getBlock() instanceof IFenceGrid) {
                if (world.getBlockEntity(pos.east()) != null)
                    world.removeBlockEntity(pos.east());

                world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    public void dropPole(Level world, RandomSource rand, BlockPos pos) {
        float xRand = rand.nextFloat() * 0.8F + 0.1F;
        float yRand = rand.nextFloat() * 0.8F + 0.1F;
        float zRand = rand.nextFloat() * 0.8F + 0.1F;

        world.addFreshEntity(new ItemEntity(world, (double) ((float) pos.getX() + xRand), (double) ((float) pos.getY() + yRand), (double) ((float) pos.getZ() + zRand), new ItemStack(JCBlockRegistry.securityFenceLowPole, 1)));
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileSecurityFenceLowPole(pos, state);
    }
}
