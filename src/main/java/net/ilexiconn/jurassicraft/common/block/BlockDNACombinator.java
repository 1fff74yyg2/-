package net.ilexiconn.jurassicraft.common.block;

import net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNAExtractor;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BlockDNACombinator extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockDNACombinator() {
        super(BlockBehaviour.Properties.of().strength(3.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            if (!world.isClientSide) {
                net.minecraft.world.level.block.entity.BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator) {
                    net.minecraftforge.network.NetworkHooks.openScreen((net.minecraft.server.level.ServerPlayer) player,
                            new net.minecraft.world.SimpleMenuProvider(
                                    (id, inv, p) -> new net.ilexiconn.jurassicraft.common.container.ContainerDNACombinator(id, inv, (net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator) tileEntity),
                                    net.minecraft.network.chat.Component.translatable("tile.dnaCombinator.name")), pos);
                }
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        int l = Mth.floor((double) (entity.getYRot() * 4.0F / 360.0F) + 0.5D) & 3;

        Direction facing;
        switch (l) {
            case 0:
                facing = Direction.SOUTH;
                break;
            case 1:
                facing = Direction.WEST;
                break;
            case 2:
                facing = Direction.NORTH;
                break;
            default:
                facing = Direction.EAST;
                break;
        }

        world.setBlock(pos, state.setValue(FACING, facing), 2);
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);

            // Original 1.12.2 code checked TileDNAExtractor here even though this is
            // the combinator block; kept as-is.
            if (tileEntity instanceof TileDNAExtractor) {
                TileDNAExtractor tileEntityDNAExtractor = (TileDNAExtractor) tileEntity;

                if (tileEntityDNAExtractor.hasItems()) {
                    for (int i = 0; i < tileEntityDNAExtractor.getSizeInventory(); i++) {
                        ItemStack stack = tileEntityDNAExtractor.getStackInSlot(i);

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

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileDNACombinator(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == JCTileEntityRegistry.DNA_COMBINATOR.get() ? (lvl, pos, st, be) -> ((TileDNACombinator) be).tick(lvl, pos, st, be) : null;
    }
}
