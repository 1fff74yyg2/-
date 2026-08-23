package net.ilexiconn.jurassicraft.common.block;

import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.ilexiconn.jurassicraft.common.tileentity.TileDinoPad;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import java.util.List;

public class BlockDinoPad extends BaseEntityBlock {
    public BlockDinoPad() {
        super(BlockBehaviour.Properties.of().strength(0.0F, 0.0F).sound(SoundType.STONE).noOcclusion().randomTicks());
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        // The dino pad is dropped manually by onRemove / neighborChanged / randomTick.
        return List.of();
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!worldIn.isClientSide) {
                BlockEntity tileEntity = worldIn.getBlockEntity(pos);

                if (tileEntity instanceof TileDinoPad) {
                    Block.popResource(worldIn, pos, new ItemStack(JCItemRegistry.dinoPad));
                }
            }
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (!playerIn.getMainHandItem().isEmpty()) {
            return InteractionResult.PASS;
        } else {
            worldIn.removeBlockEntity(pos);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            playerIn.getInventory().add(new ItemStack(JCItemRegistry.dinoPad, 1));

            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!this.canBlockStay(worldIn, pos)) {
            Block.popResource(worldIn, pos, new ItemStack(JCItemRegistry.dinoPad, 1));
            worldIn.removeBlockEntity(pos);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return this.canBlockStay(worldIn, pos);
    }

    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!this.canBlockStay(worldIn, pos)) {
            Block.popResource(worldIn, pos, new ItemStack(JCItemRegistry.dinoPad, 1));
            worldIn.removeBlockEntity(pos);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public boolean canBlockStay(LevelReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).isSolid();
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileDinoPad(pos, state);
    }
}
