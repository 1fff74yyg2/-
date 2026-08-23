package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemSteak extends Item {
    private String name;

    public ItemSteak(String name) {
        super(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.2f).meat().build()));
        this.name = name;
    }

    @Override
    public void appendHoverText(ItemStack meat, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        list.add(Component.literal(I18nCompat.get("item.dna.info.none")));
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.dino_steak.name"));
    }
}
