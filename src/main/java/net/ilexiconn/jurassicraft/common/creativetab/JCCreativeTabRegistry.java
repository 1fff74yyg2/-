package net.ilexiconn.jurassicraft.common.creativetab;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegisterEvent;

public class JCCreativeTabRegistry {
    public static CreativeModeTab items;
    public static CreativeModeTab blocks;
    public static CreativeModeTab dnas;
    public static CreativeModeTab syringesEggs;
    public static CreativeModeTab spawnEggs;
    public static CreativeModeTab itemsFood;

    /**
     * Creates the tab objects. Safe to call from the mod constructor (the tab
     * constructor only performs read-only registry lookups); the actual registry
     * registration happens in {@link #registerTabs} during the RegisterEvent.
     */
    public void init() {
        items = CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicraft.items"))
                .icon(() -> new ItemStack(JCItemRegistry.amber))
                .build();

        blocks = CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicraft.blocks"))
                .icon(() -> new ItemStack(JCBlockRegistry.cultivateBottomOff))
                .build();

        dnas = CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicraft.dnas"))
                .icon(() -> new ItemStack(CreatureHandler.getCreatureFromName("Tyrannosaurus").getDNA()))
                .build();

        syringesEggs = CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicraft.syringesEggs"))
                .icon(() -> new ItemStack(CreatureHandler.getCreatureFromName("Tyrannosaurus").getEgg()))
                .build();

        spawnEggs = CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicraft.spawnEggs"))
                .icon(() -> new ItemStack(JCItemRegistry.spawnEgg))
                .build();

        itemsFood = CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicraft.itemsFood"))
                .icon(() -> new ItemStack(CreatureHandler.getCreatureFromName("Tyrannosaurus").getMeat()))
                .build();
    }

    public static void registerTabs(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(JurassiCraft.MODID, "items"), () -> items);
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(JurassiCraft.MODID, "blocks"), () -> blocks);
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(JurassiCraft.MODID, "dnas"), () -> dnas);
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(JurassiCraft.MODID, "syringes_eggs"), () -> syringesEggs);
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(JurassiCraft.MODID, "spawn_eggs"), () -> spawnEggs);
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(JurassiCraft.MODID, "items_food"), () -> itemsFood);
        }
    }
}
