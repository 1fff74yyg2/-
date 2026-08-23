package net.ilexiconn.jurassicraft.common.handler;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.creativetab.JCCreativeTabRegistry;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 1.20.1 creative mode tabs are populated via BuildCreativeModeTabContentsEvent
 * (Item.Properties has no creativeModeTab in this Forge version).
 */
public class CreativeTabHandler {
    @SubscribeEvent
    public void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == JCCreativeTabRegistry.items) {
            addItem(event, JCItemRegistry.amber);
            addItem(event, JCItemRegistry.fossil);
            addItem(event, JCItemRegistry.dinoBone);
            addItem(event, JCItemRegistry.growthSerum);
            addItem(event, JCItemRegistry.gypsumPowder);
            addItem(event, JCItemRegistry.dinoPad);
            addItem(event, JCItemRegistry.appleOnAStick);
            addItem(event, JCItemRegistry.beefOnAStick);
            addItem(event, JCItemRegistry.carrotOnAStick);
            addItem(event, JCItemRegistry.fishOnAStick);
            addItem(event, JCItemRegistry.porkOnAStick);
            addItem(event, JCItemRegistry.wheatOnAStick);
            addItem(event, JCItemRegistry.net);
            // 1.12.2 parity: the items tab also holds every creature "part"
            // item (skin/skull/fur/tooth/bristles/scale/feather).
            for (Creature creature : CreatureHandler.getCreatures()) {
                addItem(event, creature.getSkin());
                addItem(event, creature.getSkull());
                addItem(event, creature.getFur());
                addItem(event, creature.getTooth());
                addItem(event, creature.getBristles());
                addItem(event, creature.getScale());
                addItem(event, creature.getFeather());
            }
        } else if (event.getTab() == JCCreativeTabRegistry.blocks) {
            addItem(event, new ItemStack(JCBlockRegistry.cultivateBottomOff));
            addItem(event, new ItemStack(JCBlockRegistry.cultivateTopOff));
            addItem(event, new ItemStack(JCBlockRegistry.dnaExtractor));
            addItem(event, new ItemStack(JCBlockRegistry.dnaCombinator));
            addItem(event, new ItemStack(JCBlockRegistry.gypsumBlock));
            addItem(event, new ItemStack(JCBlockRegistry.gypsumBrick));
            addItem(event, new ItemStack(JCBlockRegistry.gypsumCobblestone));
            addItem(event, new ItemStack(JCBlockRegistry.amberOre));
            addItem(event, new ItemStack(JCBlockRegistry.fossilOre));
            addItem(event, new ItemStack(JCBlockRegistry.sandstoneFossilOre));
            addItem(event, new ItemStack(JCBlockRegistry.clayFossilOre));
            addItem(event, new ItemStack(JCBlockRegistry.dinoPad));
            addItem(event, new ItemStack(JCBlockRegistry.securityFenceLowCorner));
            addItem(event, new ItemStack(JCBlockRegistry.securityFenceLowPole));
            addItem(event, new ItemStack(JCBlockRegistry.securityFenceLowBase));
            addItem(event, new ItemStack(JCBlockRegistry.securityFenceLowGrid));
            addItem(event, new ItemStack(JCBlockRegistry.clearGlass));
        } else if (event.getTab() == JCCreativeTabRegistry.dnas) {
            for (Creature creature : CreatureHandler.getCreatures()) {
                if (creature.getDNA() != null)
                    addItem(event, new ItemStack(creature.getDNA()));
            }
        } else if (event.getTab() == JCCreativeTabRegistry.syringesEggs) {
            for (Creature creature : CreatureHandler.getCreatures()) {
                if (creature.getEgg() != null)
                    addItem(event, new ItemStack(creature.getEgg()));
                if (creature.getMammalSyringe() != null)
                    addItem(event, new ItemStack(creature.getMammalSyringe()));
            }
        } else if (event.getTab() == JCCreativeTabRegistry.spawnEggs) {
            addItem(event, JCItemRegistry.spawnEgg);
            // 1.20.1: item metadata is gone, so each creature gets its own spawn-egg
            // stack carrying the creature id in the NBT tag.
            for (Creature creature : CreatureHandler.getCreatures()) {
                net.minecraft.world.item.ItemStack egg = new net.minecraft.world.item.ItemStack(JCItemRegistry.spawnEgg);
                egg.getOrCreateTag().putInt("CreatureID", creature.getCreatureID());
                addItem(event, egg);
            }
        } else if (event.getTab() == JCCreativeTabRegistry.itemsFood) {
            for (Creature creature : CreatureHandler.getCreatures()) {
                if (creature.getMeat() != null)
                    addItem(event, new ItemStack(creature.getMeat()));
                if (creature.getSteak() != null)
                    addItem(event, new ItemStack(creature.getSteak()));
            }
        }
    }

    private void addItem(BuildCreativeModeTabContentsEvent event, net.minecraft.world.item.Item item) {
        if (item != null)
            event.accept(new ItemStack(item));
    }

    private void addItem(BuildCreativeModeTabContentsEvent event, ItemStack stack) {
        if (stack != null && !stack.isEmpty())
            event.accept(stack);
    }
}
