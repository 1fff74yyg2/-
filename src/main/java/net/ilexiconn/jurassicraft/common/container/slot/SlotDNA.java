package net.ilexiconn.jurassicraft.common.container.slot;

import net.ilexiconn.jurassicraft.common.item.ItemDNA;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotDNA extends Slot {
    public SlotDNA(Container inventory, int x, int y, int z) {
        super(inventory, x, y, z);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemDNA;
    }
}
