package net.ilexiconn.jurassicraft.common.block.fence;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class BlockSecurityFence extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockSecurityFence(float hardness, float resistance, int harvestLevel, String unlocalizedName) {
        super(BlockBehaviour.Properties.of().strength(hardness, resistance).sound(SoundType.METAL).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack itemStack) {
        int direction = Mth.floor((double) (entity.getYRot() * 4.0F / 360.0F) + 0.5D) & 3;

        Direction facing;
        switch (direction) {
            /** South */
            case 0:
                facing = Direction.SOUTH;
                break;
            /** West */
            case 1:
                facing = Direction.WEST;
                break;
            /** North */
            case 2:
                facing = Direction.NORTH;
                break;
            /** East */
            default:
                facing = Direction.EAST;
                break;
        }

        world.setBlock(pos, state.setValue(FACING, facing), 2);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
