package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemSkin extends ItemGenericDNASource {
    public ItemSkin(String name) {
        super(name, "Skin");
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Skin");
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.skin_" + (itemStack.getDamageValue() == 0 ? "male" : "female") + ".name"));
    }

    // TODO 1.20.1: creative-tab subtype entries are populated via BuildCreativeModeTabContentsEvent;
    // the old getSubItems(CreativeModeTab, NonNullList) hook was removed in 1.20.1.
}
