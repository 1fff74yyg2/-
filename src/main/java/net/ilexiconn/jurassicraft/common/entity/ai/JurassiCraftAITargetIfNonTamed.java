package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

/**
 * This AI makes an EntityJurassiCraftSmart attack a desirable mob when the creature is not tamed.
 *
 * @author RafaMv
 */
public class JurassiCraftAITargetIfNonTamed extends NearestAttackableTargetGoal {
    private EntityJurassiCraftSmart creature;

    public JurassiCraftAITargetIfNonTamed(EntityJurassiCraftSmart entity, Class targetClass, int chanceToAttack) {
        super(entity, targetClass, chanceToAttack, false, false, null);
        this.creature = entity;
    }

    public boolean canUse() {
        return !this.creature.isTamed() && !this.creature.isSleeping() && !this.creature.isAttacking() && super.canUse();
    }

    public void start() {
        super.start();
        this.creature.setAttacking(true);
    }

    public void stop() {
        super.stop();
        this.creature.setAttacking(false);
    }
}
