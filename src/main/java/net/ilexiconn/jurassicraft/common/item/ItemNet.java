package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemNet extends Item {
    public ItemNet() {
        super(new Item.Properties().durability(60));
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return true;
    }
}
