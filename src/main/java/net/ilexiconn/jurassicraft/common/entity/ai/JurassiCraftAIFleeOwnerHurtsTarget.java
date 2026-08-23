package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class JurassiCraftAIFleeOwnerHurtsTarget extends TargetGoal {
    private EntityJurassiCraftSmart creature;
    private LivingEntity target;

    public JurassiCraftAIFleeOwnerHurtsTarget(EntityJurassiCraftSmart entity) {
        super(entity, false);
        this.creature = entity;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.creature.isTamed() || this.creature.isSleeping() || !this.creature.isFleeing()) {
            return false;
        } else {
            LivingEntity owner = this.creature.getOwner();

            if (owner == null) {
                return false;
            } else {
                this.target = owner.getLastHurtMob();

                return target != null && owner.distanceToSqr(this.creature) < 256.0D;
            }
        }
    }

    public void start() {
        this.creature.setFleeing(true);
        super.start();
    }

    public boolean canContinueToUse() {
        this.target = null;
        return this.creature.isFleeing();
    }
}
