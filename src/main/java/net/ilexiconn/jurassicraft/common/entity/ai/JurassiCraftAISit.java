package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class JurassiCraftAISit extends Goal {
    private EntityJurassiCraftSmart creature;

    public JurassiCraftAISit(EntityJurassiCraftSmart creature) {
        this.creature = creature;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.creature.isInWater() || !this.creature.onGround() || !this.creature.isTamed() || this.creature.isTakingOff() || this.creature.isFlying() || !this.creature.getPassengers().isEmpty() || this.creature.isEating() || this.creature.isDrinking() || this.creature.isPlaying() || this.creature.isAttacking() || this.creature.isDefending() || this.creature.isBreeding())
            return false;

        // Tamed creatures keep the sitting state the owner set with right-click.
        // The AI never auto-stands a tamed creature.
        return this.creature.isSitting();
    }

    public void start() {
        this.creature.getNavigation().stop();
        this.creature.setTakingOff(false);
        this.creature.setFlying(false);
        this.creature.setEating(false);
        this.creature.setDrinking(false);
        this.creature.setDefending(false);
        this.creature.setPlaying(false);
        this.creature.setBreeding(false);
        this.creature.setSitting(true, (Player) this.creature.getOwner());
    }

    public boolean canContinueToUse() {
        return this.creature.isSitting() && !this.creature.hasBeenHurt();
    }

    public void stop() {
        if (this.creature.isSitting())
            this.creature.setSitting(false, null);
    }
}
