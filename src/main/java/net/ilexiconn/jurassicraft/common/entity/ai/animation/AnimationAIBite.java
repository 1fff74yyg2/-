package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityGallimimus;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityTyrannosaurus;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
import net.minecraft.world.entity.LivingEntity;

public class AnimationAIBite extends AIAnimation {
    private EntityJurassiCraftCreature entityBiting;
    private LivingEntity entityTarget;
    private int duration;
    private boolean eat;

    public AnimationAIBite(EntityJurassiCraftCreature dino, int duration) {
        super(dino);
        this.entityBiting = dino;
        this.entityTarget = null;
        this.duration = duration;
        eat = false;
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.BITE.animID();
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return this.duration;
    }

    public void start() {
        super.start();

        this.entityTarget = this.entityBiting.getTarget();
    }

    public void tick() {
        if (this.entityTarget != null) {
            if (this.entityBiting.getAnimationTick() < ((this.duration / 2) - 2))
                this.entityBiting.getLookControl().setLookAt(this.entityTarget, 30F, 30F);

            if (this.entityBiting.getAnimationTick() == ((this.duration / 2) - 2)) {
                float damage = (float) this.entityBiting.getCreatureAttack();

                // 1.20.1: the actual damage is dealt directly in doHurtTarget
                // (EntityJurassiCraftAggressive / EntityJurassiCraftProtective),
                // so this AI only decides whether the T-Rex should eat a
                // Gallimimus instead - otherwise the target would take the
                // damage twice per attack.
                if ((this.entityTarget.getHealth() - damage <= 0.0F) && this.entityBiting instanceof EntityTyrannosaurus && this.entityTarget instanceof EntityGallimimus)
                    eat = true;
                else
                    eat = false;
            }
        }
    }

    public void stop() {
        /** Eating animations, should not use super.resetTask, or the eating animation ID will be replaced */
        if (eat && this.entityTarget instanceof EntityGallimimus && entityTarget.getVehicle() == null) {
            super.stop();
            this.entityTarget.startRiding(this.entityBiting);
            this.entityBiting.setTarget(null);
            this.entityBiting.getNavigation().stop();
            entityBiting.setAnimationTick(0);
            AnimationHandler.sendAnimationPacket(this.entityBiting, JurassiCraftAnimationIDs.EATING.animID());
            EntityGallimimus gallimimus = (EntityGallimimus) this.entityTarget;
            gallimimus.setTarget(null);
            gallimimus.getNavigation().stop();
            AnimationHandler.sendAnimationPacket(gallimimus, JurassiCraftAnimationIDs.BEING_EATEN.animID());
            this.entityTarget = null;
            return;
        }

        this.entityTarget = null;
        super.stop();
    }
}
