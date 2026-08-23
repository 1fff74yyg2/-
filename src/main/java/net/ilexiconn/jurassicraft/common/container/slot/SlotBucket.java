package net.ilexiconn.jurassicraft.common.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;

public class SlotBucket extends Slot {
    public SlotBucket(Container inventory, int number, int x, int y) {
        super(inventory, number, x, y);
    }

    public int getMaxStackSize() {
        return 16;
    }

    public boolean mayPlace(ItemStack itemstack) {
        return itemstack.getItem() instanceof BucketItem;
    }
}
