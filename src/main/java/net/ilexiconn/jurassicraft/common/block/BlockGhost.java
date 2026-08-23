package net.ilexiconn.jurassicraft.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlockGhost extends Block {
    public int[] blocksToBreak;
    public int guiToOpen, guiId;
    public boolean openGui;
    public Block block;

    public BlockGhost(String name, Block block, float hardness, int[] blocks) {
        super(BlockBehaviour.Properties.of().strength(hardness).noOcclusion());
        this.block = block;
        blocksToBreak = blocks;
    }

    public BlockGhost(String name, Block block, float hardness, int[] blocks, int guiBlock, int guiID) {
        this(name, block, hardness, blocks);
        guiToOpen = guiBlock;
        guiId = guiID;
        openGui = true;
    }

    public BlockGhost(String name, Block block, float hardness, int[] blocks, float x, float y, float z, float x1, float y1, float z1) {
        this(name, block, hardness, blocks);
    }

    public BlockGhost(String name, Block block, float hardness, int[] blocks, int guiBlock, int guiID, float x, float y, float z, float x1, float y1, float z1) {
        this(name, block, hardness, blocks, guiBlock, guiID);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return List.of(new ItemStack(block.asItem()));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            for (int thing : blocksToBreak) {
                BlockPos target = pos.offset(0, thing, 0);
                worldIn.setBlock(target, Blocks.AIR.defaultBlockState(), 3);

                if (worldIn.getBlockEntity(target) != null)
                    worldIn.removeBlockEntity(target);
            }
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (!openGui)
            return InteractionResult.PASS;

        // TODO: 1.20.1 — GUI opening is not ported yet (IGuiHandler removed).
        //  The old code opened the GUI with id guiId at pos + (0, guiToOpen, 0):
        //  playerIn.openGui(JurassiCraft.instance, guiId, worldIn, pos.getX(), pos.getY() + guiToOpen, pos.getZ());
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }
}
