package net.ilexiconn.jurassicraft.common.block.fence;

import net.ilexiconn.jurassicraft.common.api.IFenceGrid;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceMediumGrid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlockSecurityFenceMediumGrid extends BlockSecurityFence implements IFenceGrid {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockSecurityFenceMediumGrid() {
        super(5.0F, 75.0F, 2, "medium_security_fence_grid");
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(POWERED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return List.of();
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction facing = state.getValue(FACING);

            if (facing == Direction.SOUTH || facing == Direction.NORTH) {
                if (world.getBlockState(pos.west()).getBlock() instanceof BlockSecurityFenceMediumGrid) {
                    if (world.getBlockEntity(pos.west()) != null)
                        world.removeBlockEntity(pos.west());

                    world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                }

                if (world.getBlockState(pos.east()).getBlock() instanceof BlockSecurityFenceMediumGrid) {
                    if (world.getBlockEntity(pos.east()) != null)
                        world.removeBlockEntity(pos.east());

                    world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                }
            } else {
                if (world.getBlockState(pos.south()).getBlock() instanceof BlockSecurityFenceMediumGrid) {
                    if (world.getBlockEntity(pos.south()) != null)
                        world.removeBlockEntity(pos.south());

                    world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                }

                if (world.getBlockState(pos.north()).getBlock() instanceof BlockSecurityFenceMediumGrid) {
                    if (world.getBlockEntity(pos.north()) != null)
                        world.removeBlockEntity(pos.north());

                    world.setBlock(pos.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);

        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            return Block.box(0.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D);
        } else {
            return Block.box(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 16.0D);
        }
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);

        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            return Block.box(0.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D);
        } else {
            return Block.box(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 16.0D);
        }
    }

    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (state.getValue(POWERED))
            entity.hurt(entity.damageSources().generic(), 4.0F);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileSecurityFenceMediumGrid(pos, state);
    }
}
