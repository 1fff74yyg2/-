package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemFossilModel extends Item {
    public ItemFossilModel(String fossilName) {
        super(new Item.Properties().stacksTo(8));
    }

    public int getFossil(ItemStack fossil) {
        if (fossil.hasTag()) {
            if (fossil.getTag().contains("FossilID")) {
                return fossil.getTag().getInt("FossilID");
            }
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack fossil, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        if (fossil.hasTag()) {
            if (fossil.getTag().contains("FossilID")) {
                list.add(Component.literal(ChatFormatting.GREEN + String.valueOf(fossil.getTag().getInt("FossilID"))));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack fossil = player.getItemInHand(hand);
        if (player.getAbilities().instabuild && player.isShiftKeyDown()) {
            if (fossil.hasTag()) {
                if (fossil.getTag().contains("FossilID")) {
                    // PLACE logic was never implemented in the original mod either.
                }
            }
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, fossil);
    }
}
