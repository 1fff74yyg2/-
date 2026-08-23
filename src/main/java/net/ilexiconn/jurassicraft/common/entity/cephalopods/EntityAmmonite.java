package net.ilexiconn.jurassicraft.common.entity.cephalopods;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftWaterCreature;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Ammonite creature entity. The cephalopods.json entry marks it as not yet
 * implemented (addItemTypes = 0), so this is a minimal water-creature class that
 * only exists so the reflective creature registration (CreatureHandler.addCreature)
 * can load it. Rendering uses ModelAmmonite via RenderJurassicraftCreature.
 */
public class EntityAmmonite extends EntityJurassiCraftWaterCreature {
    public EntityAmmonite(EntityType<? extends EntityAmmonite> type, Level world) {
        super(type, world);
    }

    public EntityAmmonite(Level world) {
        super(world);
    }
}
