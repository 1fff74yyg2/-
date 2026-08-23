package net.ilexiconn.jurassicraft.common.container.slot;

import net.ilexiconn.jurassicraft.common.api.IDNASample;
import net.ilexiconn.jurassicraft.common.item.ItemEgg;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotDNASampleAndEgg extends Slot {
    public SlotDNASampleAndEgg(Container inventory, int number, int x, int y) {
        super(inventory, number, x, y);
    }

    public int getMaxStackSize() {
        return 1;
    }

    public boolean mayPlace(ItemStack itemstack) {
        return (itemstack.getItem() instanceof IDNASample || itemstack.getItem() instanceof ItemEgg);
    }
}
