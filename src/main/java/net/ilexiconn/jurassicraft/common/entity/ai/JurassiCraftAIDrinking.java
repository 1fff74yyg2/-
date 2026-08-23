package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;

public class JurassiCraftAIDrinking extends Goal {
    private EntityJurassiCraftSmart creature;
    private int duration;
    private int timer;

    public JurassiCraftAIDrinking(EntityJurassiCraftSmart creature, int duration) {
        this.creature = creature;

        if (duration > 0)
            this.duration = duration;
        else
            this.duration = 10;

        this.timer = 0;
    }

    public boolean canUse() {
        return this.creature.isDrinking();
    }

    public void start() {
        if (this.creature.isEating())
            this.creature.setEating(false);

        this.timer = this.duration;
    }

    public void tick() {
        this.timer--;
    }

    public boolean canContinueToUse() {
        return this.timer >= 0 && !this.creature.isSitting() && !this.creature.isSleeping() && !this.creature.hasBeenHurt() && this.creature.getPassengers().isEmpty();
    }

    public void stop() {
        this.creature.setDrinking(false);
        this.timer = 0;
    }
}
