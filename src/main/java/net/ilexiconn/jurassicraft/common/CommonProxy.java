package net.ilexiconn.jurassicraft.common;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.ilexiconn.jurassicraft.common.message.MessageAnimation;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class CommonProxy {
    public float getPartialTick() {
        return 1f;
    }

    public Level getWorldClient() {
        return null;
    }

    public void init() throws Exception {
    }

    /**
     * Client-side handling of {@link MessageAnimation}. Server-side no-op;
     * overridden in ClientProxy (which may freely use client-only classes).
     */
    public void onAnimationMessage(MessageAnimation message, int entityId, byte animationId) {
    }

    /**
     * Client-side handling of {@link net.ilexiconn.jurassicraft.common.message.MessagePregnancy}.
     * Server-side no-op; overridden in ClientProxy.
     */
    public void onPregnancyMessage(int entityId, String mammalName, int quality, int progress, int speed) {
    }

    public void openCultivatorProgress(TileCultivate tile) {
    }

    public void openDinoPad(Entity entity) {
    }

    public void openEntityGui(EntityJurassiCraftSmart creature) {
    }
}
