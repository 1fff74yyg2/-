package net.ilexiconn.jurassicraft.common.api;

import net.minecraft.world.item.ItemStack;

public interface IDNASample {
    String getDNASequence(ItemStack dnaSample);

    int getQuality(ItemStack dnaSample);
}
