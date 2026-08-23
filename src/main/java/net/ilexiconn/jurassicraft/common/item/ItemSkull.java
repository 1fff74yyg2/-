package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemSkull extends ItemGenericDNASource {
    public ItemSkull(String name) {
        super(name, "Skull");
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Skull");
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.dino_skull.name"));
    }

}
