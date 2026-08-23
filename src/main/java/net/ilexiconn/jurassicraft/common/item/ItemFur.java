package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemFur extends ItemGenericDNASource {
    public ItemFur(String name) {
        super(name, "Fur");
    }

    @Override
    public Component getName(ItemStack itemStack) {
        // 1.20.1: use the creature name stored in this.name (damage/metadata is gone,
        // so the old getCreatureFromId(getDamageValue()) lookup no longer works).
        return Component.literal(I18nCompat.get("entity." + this.name + ".name") + " " + I18nCompat.get("item.dino_fur.name"));
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Fur");
    }
}
