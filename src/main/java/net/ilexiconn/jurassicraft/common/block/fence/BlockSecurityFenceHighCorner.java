package net.ilexiconn.jurassicraft.common.block.fence;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowPole;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceMediumCorner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class BlockSecurityFenceHighCorner extends BlockSecurityFence {
    public BlockSecurityFenceHighCorner() {
        super(10.0F, 150.0F, 2, "high_security_fence_main");
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            // TODO: 1.20.1 — GUI opening is not ported yet (IGuiHandler removed).
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);

            if (tileEntity instanceof TileSecurityFenceMediumCorner) {
                TileSecurityFenceMediumCorner fence = (TileSecurityFenceMediumCorner) tileEntity;

                if (!world.isClientSide && fence.hasItems()) {
                    for (int i = 0; i < fence.getSizeInventory(); i++) {
                        ItemStack stack = fence.getStackInSlot(i);

                        if (stack != null && !stack.isEmpty()) {
                            RandomSource rand = world.random;

                            float f = rand.nextFloat() * 0.8F + 0.1F;
                            float f1 = rand.nextFloat() * 0.8F + 0.1F;
                            float f2 = rand.nextFloat() * 0.8F + 0.1F;

                            while (stack.getCount() > 0) {
                                int j = rand.nextInt(21) + 10;

                                if (j > stack.getCount()) {
                                    j = stack.getCount();
                                }

                                stack.shrink(j);

                                ItemEntity item = new ItemEntity(world, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2), new ItemStack(stack.getItem(), j));

                                if (stack.hasTag()) {
                                    item.getItem().setTag(stack.getTag().copy());
                                }

                                world.addFreshEntity(item);
                            }
                        }
                    }

                    for (int side = 0; side < 4; side++) {
                        if (fence.hasFenceAt(side)) {
                            TileSecurityFenceMediumCorner neighborFence = fence.getNextMediumSecurityCornerFenceBlock(fence, side, 128);
                            if (neighborFence != null) {
                                switch (side) {
                                    /** South */
                                    case 0:
                                        neighborFence.setFenceAt(2, false);
                                        neighborFence.setFenceOff(2);
                                        break;
                                    /** West */
                                    case 1:
                                        neighborFence.setFenceAt(3, false);
                                        neighborFence.setFenceOff(3);
                                        break;
                                    /** North */
                                    case 2:
                                        neighborFence.setFenceAt(0, false);
                                        neighborFence.setFenceOff(0);
                                        break;
                                    /** East */
                                    case 3:
                                        neighborFence.setFenceAt(1, false);
                                        neighborFence.setFenceOff(1);
                                        break;
                                }
                                neighborFence.setChanged();
                            }
                        }
                    }

                    world.updateNeighborsAt(pos, state.getBlock());
                }
            }

            tileEntity = world.getBlockEntity(pos.above());

            if (tileEntity instanceof TileSecurityFenceLowPole) {
                world.removeBlockEntity(pos.above());
                world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);

                if (!world.isClientSide)
                    this.dropPole(world, world.random, pos);
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
        return new TileSecurityFenceMediumCorner(pos, state);
    }
}
