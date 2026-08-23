package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class JurassiCraftAIFlee extends Goal {
    private EntityJurassiCraftSmart creature;
    private int fleeingTime;
    private double randPosX;
    private double randPosY;
    private double randPosZ;
    private double speed;

    public JurassiCraftAIFlee(EntityJurassiCraftSmart entity, int duration, double velocity) {
        this.creature = entity;
        this.speed = velocity;
        this.fleeingTime = duration;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.creature.isSleeping()) {
            return false;
        } else if (this.creature.isFleeing()) {
            Vec3 randomTarget = DefaultRandomPos.getPos(this.creature, 5, 4);

            if (randomTarget == null) {
                return false;
            } else {
                this.randPosX = randomTarget.x;
                this.randPosY = randomTarget.y;
                this.randPosZ = randomTarget.z;

                return true;
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.creature.setPlaying(false);
        this.creature.setSocializing(false);
        this.creature.setEating(false);
        this.creature.setDrinking(false);
        this.creature.setDefending(false);
        this.creature.setAttacking(false);
        this.creature.setBreeding(false);
        this.creature.setInLove(false);
        this.creature.setSitting(false, null);
        this.creature.setFleeingTick(this.fleeingTime + (int) (this.fleeingTime * 0.6F * this.creature.getRandom().nextFloat()));
    }

    public void tick() {
        this.creature.setFleeingTick(this.creature.getFleeingTick() - 1);

        if (this.creature.getNavigation().isDone()) {
            Vec3 randomTarget = DefaultRandomPos.getPos(this.creature, 5, 4);

            if (randomTarget != null) {
                this.randPosX = randomTarget.x;
                this.randPosY = randomTarget.y;
                this.randPosZ = randomTarget.z;
                this.creature.getNavigation().moveTo(this.randPosX, this.randPosY, this.randPosZ, this.speed);
            }
        }
    }

    public boolean canContinueToUse() {
        return this.creature.getFleeingTick() > 0 && !this.creature.isSitting() && !this.creature.isSleeping() && this.creature.getPassengers().isEmpty();
    }

    public void stop() {
        this.creature.setFleeingTick(0);
        this.creature.setFleeing(false);

        if (this.creature.getTarget() != null) {
            this.creature.setTarget(null);
        }
    }
}
