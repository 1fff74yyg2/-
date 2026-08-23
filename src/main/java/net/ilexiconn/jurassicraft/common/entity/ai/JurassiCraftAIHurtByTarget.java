package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class JurassiCraftAIHurtByTarget extends HurtByTargetGoal {
    private EntityJurassiCraftSmart creature;

    public JurassiCraftAIHurtByTarget(EntityJurassiCraftSmart entity, boolean friendsShouldHelp) {
        super(entity);
        this.creature = entity;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return this.creature.isSleeping() ? false : super.canUse();
    }

    public void start() {
        this.creature.setSleeping(false);
        this.creature.setEating(false);
        this.creature.setDrinking(false);
        this.creature.setDefending(false);
        this.creature.setPlaying(false);
        this.creature.setBreeding(false);
        this.creature.setSocializing(false);
        this.creature.setStalking(false);
        this.creature.setInLove(false);
        this.creature.setAttacking(true);

        super.start();
    }

    public void stop() {
        this.creature.setAttacking(false);

        super.stop();
    }
}
