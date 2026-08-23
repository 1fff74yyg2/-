package net.ilexiconn.jurassicraft.common.handler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.common.data.CreatureContainer;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.entity.JCEntityRegistry;
import net.ilexiconn.jurassicraft.common.item.ItemDNA;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreatureHandler {
    private static List<Creature> creatures = new ArrayList<Creature>();

    public static List<Creature> getCreatures() {
        return creatures;
    }

    public static String[] getCreatureNames() {
        List<String> list = new ArrayList<String>();

        for (Creature creature : getCreatures())
            list.add(creature.getCreatureName());

        return list.toArray(new String[list.size()]);
    }

    public static Creature getCreatureFromId(int creatureID) {
        for (Creature creature : creatures) {
            if (creature.getCreatureID() == creatureID) {
                return creature;
            }
        }

        return null;
    }

    public static String getCategoryFromCreatureName(String name) {
        for (Creature creature : creatures) {
            if (creature.getCreatureName().toLowerCase().equals(name.toLowerCase()))
                return creature.getCreatureCategory();
        }

        return null;
    }

    public static Creature classToCreature(Class clazz) {
        for (Creature creature : creatures) {
            if (creature.getCreatureClass().equals(clazz)) {
                return creature;
            }
        }

        return null;
    }

    public static Creature getCreatureFromDNA(ItemDNA itemDNA) {
        if (itemDNA != null) {
            for (Creature creature : creatures) {
                ItemDNA currentDNA = creature.getDNA();

                if (itemDNA.equals(currentDNA)) {
                    return creature;
                }
            }
        }

        return null;
    }

    public static Creature getCreatureFromName(String name) {
        for (Creature creature : creatures) {
            if (creature.getCreatureName().equalsIgnoreCase(name)) {
                return creature;
            }
        }

        return null;
    }

    public static void addCreature(CreatureContainer creature, String category) {
        try {
            String creatureName = creature.creatureName;

            Class entity = Class.forName("net.ilexiconn.jurassicraft.common.entity." + category + ".Entity" + creatureName);
            creatures.add(new Creature(category, creature, entity));

            // 1.20.1: EntityRegistry.registerModEntity no longer exists; the per-creature
            // EntityType is registered dynamically through JCEntityRegistry during the
            // entity RegisterEvent.
            float width = (float) (creature.xzBoxMin + creature.xzBoxDelta);
            float height = (float) (creature.yBoxMin + creature.yBoxDelta);
            JCEntityRegistry.registerDynamic(entity, creatureName.toLowerCase(Locale.ROOT), width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addCreatureRenderer(CreatureContainer dino, String category) {
        // TODO 1.20.1: per-creature entity renderers are not wired up yet. The
        // RenderJurassicraftCreature instances should be registered against the
        // dynamically registered EntityTypes in JCRenderRegistry (EntityRenderersEvent).
        // The old reflective Class-based EntityRendererProvider registration is gone.
    }
}
