package net.ilexiconn.jurassicraft.common.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SlotFence extends Slot {
    public SlotFence(Container inventory, int number, int x, int y) {
        super(inventory, number, x, y);
    }

    public int getMaxStackSize() {
        return 64;
    }

    public boolean mayPlace(ItemStack itemstack) {
        return itemstack.getItem() == Items.REDSTONE || itemstack.getItem() == Items.IRON_INGOT;
    }
}
