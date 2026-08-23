package net.ilexiconn.jurassicraft.common.handler;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.message.MessageAnimation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;

public class AnimationHandler {
    public static boolean isClient() {
        return EffectiveSide.get().isClient();
    }

    public static boolean isEffectiveClient() {
        return EffectiveSide.get().isClient();
    }

    public static void sendAnimationPacket(IAnimatedEntity entity, int animationId) {
        if (isEffectiveClient())
            return;
        entity.setAnimationId(animationId);
        JurassiCraft.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> (Entity) entity),
                new MessageAnimation((byte) animationId, ((Entity) entity).getId()));
    }
}