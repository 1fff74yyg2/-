package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;

public class JurassiCraftAIAngry extends Goal {
    private EntityJurassiCraftSmart creature;
    private int angryTime;

    public JurassiCraftAIAngry(EntityJurassiCraftSmart entity, int duration) {
        this.creature = entity;
        this.angryTime = duration;
    }

    public boolean canUse() {
        return this.creature.isAngry() && !this.creature.isSleeping();
    }

    public void start() {
        if (this.creature.isTakingOff())
            this.creature.setTakingOff(false);

        if (this.creature.isFlying())
            this.creature.setFlying(false);

        if (this.creature.isPlaying())
            this.creature.setPlaying(false);

        if (this.creature.isSocializing())
            this.creature.setSocializing(false);

        if (this.creature.isEating())
            this.creature.setEating(false);

        if (this.creature.isDrinking())
            this.creature.setDrinking(false);

        if (this.creature.isFleeing())
            this.creature.setFleeing(false);

        if (this.creature.isDefending())
            this.creature.setDefending(false);

        if (this.creature.isBreeding())
            this.creature.setBreeding(false);

        if (this.creature.isInLove())
            this.creature.setInLove(false);

        if (this.creature.isSitting())
            this.creature.setSitting(false, null);

        if (this.creature.isSleeping())
            this.creature.setSleeping(false);

        this.creature.setAngerLevel(this.angryTime + (int) (this.angryTime * 0.6F * this.creature.getRandom().nextFloat()));
        this.creature.setAttacking(true);
    }

    public void tick() {
        this.creature.setAngerLevel(creature.getAngerLevel() - 1);
    }

    public boolean canContinueToUse() {
        return this.creature.getAngerLevel() > 0 && this.creature.getTarget() != null && !this.creature.isSitting() && !this.creature.isSleeping() && this.creature.getPassengers().isEmpty();
    }

    public void stop() {
        this.creature.setAngerLevel(0);
        this.creature.setAttacking(false);
        this.creature.setAngry(false);
        this.creature.setTarget(null);
    }
}
