package net.ilexiconn.jurassicraft.common.item;

public class ItemScale extends ItemGenericDNASource {
    public ItemScale(String name) {
        super(name, "Scale");
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Scale");
    }
}
