package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
import net.minecraft.world.entity.ai.goal.Goal;

public class JurassiCraftAIEating extends Goal {
    private EntityJurassiCraftSmart creature;
    private boolean shouldAnimate;
    private int animationID;
    private int duration;
    private int timer;

    public JurassiCraftAIEating(EntityJurassiCraftSmart creature, int duration) {
        this(creature, duration, false, 0);
    }

    public JurassiCraftAIEating(EntityJurassiCraftSmart creature, int duration, boolean shouldAnimate, int animationID) {
        this.creature = creature;

        if (duration > 0)
            this.duration = duration;
        else
            this.duration = 10;

        this.shouldAnimate = shouldAnimate;
        this.animationID = animationID;
        this.timer = 0;
    }

    public boolean canUse() {
        return this.creature.isEating();
    }

    public void start() {
        this.timer = this.duration;

        if (this.creature.isDrinking())
            this.creature.setDrinking(false);

        if (this.shouldAnimate && this.creature.getAnimationId() == 0)
            AnimationHandler.sendAnimationPacket(this.creature, this.animationID);
    }

    public void tick() {
        this.timer--;
    }

    public boolean canContinueToUse() {
        return this.timer >= 0 && !this.creature.isSitting() && !this.creature.isSleeping() && !this.creature.hasBeenHurt() && this.creature.getPassengers().isEmpty();
    }

    public void stop() {
        this.creature.setEating(false);
        this.timer = 0;
    }
}
