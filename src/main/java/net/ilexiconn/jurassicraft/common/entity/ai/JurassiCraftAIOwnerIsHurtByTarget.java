package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class JurassiCraftAIOwnerIsHurtByTarget extends TargetGoal {
    private EntityJurassiCraftSmart creature;
    private LivingEntity target;

    public JurassiCraftAIOwnerIsHurtByTarget(EntityJurassiCraftSmart entity) {
        super(entity, false);
        this.creature = entity;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.creature.isTamed() || this.creature.isSleeping())
            return false;
        else {
            LivingEntity owner = this.creature.getOwner();

            if (owner == null)
                return false;
            else {
                this.target = owner.getLastHurtByMob();

                if (this.target instanceof EntityJurassiCraftSmart)
                    return this.canAttack(this.target, TargetingConditions.forCombat().range(this.getFollowDistance())) && !((EntityJurassiCraftSmart) this.target).isOwner(owner);
                else
                    return this.canAttack(this.target, TargetingConditions.forCombat().range(this.getFollowDistance()));
            }
        }
    }

    public void start() {
        this.creature.setPlaying(false);
        this.creature.setSocializing(false);
        this.creature.setEating(false);
        this.creature.setDrinking(false);
        this.creature.setDefending(false);
        this.creature.setBreeding(false);
        this.creature.setInLove(false);
        this.creature.setStalking(false);
        this.creature.setSitting(false, null);
        this.creature.setTarget(this.target);
        this.creature.setAttacking(true);

        super.start();
    }

    public boolean canContinueToUse() {
        return !this.creature.isSitting() && this.creature.getPassengers().isEmpty() && super.canContinueToUse();
    }

    public void stop() {
        this.target = null;
        this.creature.setAttacking(false);

        super.stop();
    }
}
