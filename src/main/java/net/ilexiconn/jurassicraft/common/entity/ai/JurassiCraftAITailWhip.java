package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class JurassiCraftAITailWhip extends Goal {
    private double distanceSqFromAttacker;
    private double minimumSquaredDistance;
    private double maximumSquaredDistance;
    private EntityJurassiCraftSmart creature;
    private LivingEntity attacker;
    private boolean sendWhipAnimation;

    /**
     * @author RafaMv
     */
    public JurassiCraftAITailWhip(EntityJurassiCraftSmart creature, double minimumSquaredDistance, double maximumSquaredDistance, boolean sendWhipAnimation) {
        this.creature = creature;
        this.distanceSqFromAttacker = 0;
        this.attacker = null;
        this.minimumSquaredDistance = minimumSquaredDistance;
        this.maximumSquaredDistance = maximumSquaredDistance;
        this.sendWhipAnimation = sendWhipAnimation;
    }

    public boolean canUse() {
        if (this.creature.isDefending() && !this.creature.isSitting() && !this.creature.isSleeping() && !this.creature.isTakingOff() && !this.creature.isFlying() && !this.creature.isFleeing())
            return this.creature.getCreatureToAttack() != null;

        return false;
    }

    public void start() {
        this.creature.getNavigation().stop();

        if (this.creature.isTakingOff())
            this.creature.setTakingOff(false);

        if (this.creature.isFlying())
            this.creature.setFlying(false);

        if (this.creature.isDefending())
            this.creature.setDefending(false);

        if (this.creature.isAttacking())
            this.creature.setAttacking(false);

        if (this.creature.isFleeing())
            this.creature.setFleeing(false);

        if (this.creature.isEating())
            this.creature.setEating(false);

        if (this.creature.isDrinking())
            this.creature.setDrinking(false);

        if (this.creature.isSleeping())
            this.creature.setSleeping(false);

        if (this.creature.isSitting())
            this.creature.setSitting(false, null);

        if (this.creature.isBreeding())
            this.creature.setBreeding(false);

        if (this.creature.isInLove())
            this.creature.setInLove(false);

        if (this.creature.isStalking())
            this.creature.setStalking(false);

        this.attacker = this.creature.getCreatureToAttack();
        this.distanceSqFromAttacker = this.creature.distanceToSqr(this.attacker);
    }

    public void tick() {
        if (this.creature.getRandom().nextInt(5) == 0)
            this.distanceSqFromAttacker = this.creature.distanceToSqr(this.attacker);

        if (this.distanceSqFromAttacker < this.minimumSquaredDistance) {
            this.creature.setTarget(this.attacker);

            if (this.sendWhipAnimation && this.creature.getAnimationId() == 0)
                AnimationHandler.sendAnimationPacket(this.creature, JurassiCraftAnimationIDs.TAIL_WHIP.animID());
        }
    }

    public boolean canContinueToUse() {
        return this.attacker.isAlive() && this.creature.isDefending() && this.distanceSqFromAttacker < this.maximumSquaredDistance && !this.creature.isSitting() && this.creature.getPassengers().isEmpty();
    }

    public void stop() {
        this.creature.setCreatureToAttack(null);
        this.creature.setDefending(false);
        this.distanceSqFromAttacker = 0;
        this.attacker = null;
    }
}
