package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.world.item.Item;

public class ItemOnAStick extends Item {
    public ItemOnAStick(String foodOnAStick) {
        super(new Item.Properties().stacksTo(1).durability(8000));
    }
}
