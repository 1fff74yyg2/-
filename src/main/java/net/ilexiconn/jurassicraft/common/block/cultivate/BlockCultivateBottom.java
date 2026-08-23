package net.ilexiconn.jurassicraft.common.block.cultivate;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.api.ISubBlocksBlock;
import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.item.ItemBlockCultivate;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

import javax.annotation.Nullable;

import java.util.List;

public class BlockCultivateBottom extends BaseEntityBlock implements ISubBlocksBlock {
    public static final String[] iconVariationsNames = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };
    public boolean isLit;

    public BlockCultivateBottom(boolean lit) {
        super(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion().lightLevel(state -> lit ? 15 : 0));
        this.isLit = lit;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);

            if (tileEntity instanceof TileCultivate) {
                TileCultivate tileCultivate = (TileCultivate) tileEntity;

                if (tileCultivate.isUseableByPlayer(player)) {
                    if (!tileCultivate.isHatching()) {
                        if (!world.isClientSide) {
                            net.minecraftforge.network.NetworkHooks.openScreen((net.minecraft.server.level.ServerPlayer) player,
                                    new net.minecraft.world.SimpleMenuProvider(
                                            (id, inv, p) -> new net.ilexiconn.jurassicraft.common.container.ContainerCultivate(id, inv, tileCultivate),
                                            net.minecraft.network.chat.Component.translatable("container.cultivate")), pos);
                        }
                        return InteractionResult.sidedSuccess(world.isClientSide);
                    } else {
                        if (world.isClientSide) {
                            JurassiCraft.proxy.openCultivatorProgress(tileCultivate);
                        }

                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return List.of(new ItemStack(JCBlockRegistry.cultivateBottomOff));
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);

            if (tileEntity instanceof TileCultivate) {
                TileCultivate cultivator = (TileCultivate) tileEntity;

                if (cultivator.hasItems()) {
                    for (int i = 0; i < cultivator.getSizeInventory(); i++) {
                        ItemStack itemstack = cultivator.getStackInSlot(i);

                        if (itemstack != null && !itemstack.isEmpty()) {
                            float f = world.random.nextFloat() * 0.8F + 0.1F;
                            float f1 = world.random.nextFloat() * 0.8F + 0.1F;
                            float f2 = world.random.nextFloat() * 0.8F + 0.1F;

                            while (itemstack.getCount() > 0) {
                                int j = world.random.nextInt(21) + 10;

                                if (j > itemstack.getCount()) {
                                    j = itemstack.getCount();
                                }

                                itemstack.shrink(j);

                                ItemEntity item = new ItemEntity(world, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2), new ItemStack(itemstack.getItem(), j));

                                if (itemstack.hasTag()) {
                                    item.getItem().setTag(itemstack.getTag().copy());
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
        world.removeBlockEntity(pos.above());
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileCultivate(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == JCTileEntityRegistry.CULTIVATOR.get() ? (lvl, pos, st, be) -> ((TileCultivate) be).tick(lvl, pos, st, be) : null;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return buildShape(BlockCultivate.boxes[1]);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return buildShape(BlockCultivate.boxes[1]);
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
