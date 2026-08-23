package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.ChatFormatting;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemGrowthSerum extends Item {
    public ItemGrowthSerum() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal(ChatFormatting.GRAY + I18nCompat.get("item.growthSerum.info")));
    }
}
