package net.ilexiconn.jurassicraft.common.item;

public class ItemTooth extends ItemGenericDNASource {
    public ItemTooth(String name) {
        super(name, "Tooth");
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Tooth");
    }
}
