package net.ilexiconn.jurassicraft.common.block.cultivate;

import net.ilexiconn.jurassicraft.common.api.ISubBlocksBlock;
import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.item.ItemBlockCultivate;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlockCultivateTop extends Block implements ISubBlocksBlock {
    public static final String[] iconVariationsNames = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };
    public boolean isLit;

    public BlockCultivateTop(boolean lit) {
        super(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion().lightLevel(state -> lit ? 15 : 0));
        this.isLit = lit;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return List.of(new ItemStack(JCBlockRegistry.cultivateBottomOff));
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);

            if (tileEntity instanceof TileCultivate) {
                TileCultivate tileCultivate = (TileCultivate) tileEntity;

                if (tileCultivate.hasItems()) {
                    for (int i = 0; i < tileCultivate.getSizeInventory(); i++) {
                        ItemStack stack = tileCultivate.getStackInSlot(i);

                        if (stack != null && !stack.isEmpty()) {
                            float f = world.random.nextFloat() * 0.8F + 0.1F;
                            float f1 = world.random.nextFloat() * 0.8F + 0.1F;
                            float f2 = world.random.nextFloat() * 0.8F + 0.1F;

                            while (stack.getCount() > 0) {
                                int j = world.random.nextInt(21) + 10;

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

                    world.updateNeighborsAt(pos, state.getBlock());
                }
            }
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
        world.removeBlockEntity(pos.below());
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = world.getBlockEntity(pos.below());

        if (tileEntity instanceof TileCultivate) {
            BlockState blockState = world.getBlockState(pos.below());

            return blockState.getBlock().use(blockState, world, pos.below(), player, hand, hit);
        }

        return InteractionResult.PASS;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return buildShape(BlockCultivate.boxes[0]);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return buildShape(BlockCultivate.boxes[0]);
    }

    private static VoxelShape buildShape(AABB[] aabbs) {
        VoxelShape shape = Shapes.empty();
        for (AABB aabb : aabbs) {
            shape = Shapes.or(shape, Shapes.create(aabb));
        }
        return shape;
    }

    public Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockCultivate.class;
    }
}
