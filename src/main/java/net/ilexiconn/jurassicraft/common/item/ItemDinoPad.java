package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemDinoPad extends Item {
    public ItemDinoPad() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }
        Direction facing = context.getClickedFace();
        BlockPos pos = context.getClickedPos().relative(facing);
        ItemStack stack = context.getItemInHand();

        if (!player.mayUseItemAt(pos, facing, stack) || !player.isShiftKeyDown()) {
            return InteractionResult.FAIL;
        } else {
            if (!worldIn.isEmptyBlock(pos.below()) && worldIn.isEmptyBlock(pos) && stack.getItem() instanceof ItemDinoPad) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);

                    if (stack.isEmpty()) {
                        player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                    }
                }

                // TODO 1.20.1: block metadata (getStateFromMeta/getMetaFromState) was removed, so the old
                // player-rotation metadata switch is gone; the rotation is handled by the TileDinoPad
                // block entity instead. The default block state is the only state BlockDinoPad has.
                worldIn.setBlock(pos, JCBlockRegistry.dinoPad.defaultBlockState(), 2);
                worldIn.playSound(player, pos, SoundEvents.STONE_STEP, SoundSource.BLOCKS, 1.0F, worldIn.random.nextFloat() * 0.4F + 0.8F);
            }

            return InteractionResult.SUCCESS;
        }
    }
}
