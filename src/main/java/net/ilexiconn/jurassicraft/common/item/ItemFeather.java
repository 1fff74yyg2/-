package net.ilexiconn.jurassicraft.common.item;

public class ItemFeather extends ItemGenericDNASource {
    public ItemFeather(String name) {
        super(name, "Feather");
    }

    public ItemDNA getCorrespondingDNA() {
        return this.getCorrespondingDNA("Feather");
    }
}
