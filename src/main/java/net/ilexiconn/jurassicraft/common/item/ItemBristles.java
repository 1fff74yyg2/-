package net.ilexiconn.jurassicraft.common.item;

public class ItemBristles extends ItemGenericDNASource {
    public ItemBristles(String name) {
        super(name, "Bristles");
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Bristles");
    }
}
