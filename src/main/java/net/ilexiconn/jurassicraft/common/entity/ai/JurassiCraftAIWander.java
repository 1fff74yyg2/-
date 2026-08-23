package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

/**
 * This AI makes an EntityJurassiCraftSmart walk if the creature is not sitting, sleeping, flying, or being ridden.
 *
 * @author RafaMv
 */
public class JurassiCraftAIWander extends Goal {
    private EntityJurassiCraftSmart creature;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int maxDistance;
    private int maxHeight;
    private int chanceToWalk;
    private int timer;

    public JurassiCraftAIWander(EntityJurassiCraftSmart entity, int chanceToWalk, double velocity) {
        this(entity, chanceToWalk, 16, 6, velocity);
    }

    public JurassiCraftAIWander(EntityJurassiCraftSmart entity, int chanceToWalk, int distance, int height, double velocity) {
        this.creature = entity;
        this.chanceToWalk = chanceToWalk;
        this.speed = velocity;
        this.maxDistance = distance;
        this.maxHeight = height;
        this.timer = 0;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.creature.isSitting() || this.creature.isFlying() || !this.creature.getPassengers().isEmpty() || this.creature.isSleeping() || this.creature.isAngry() || this.creature.isFleeing())
            return false;
        else {
            if (timer < 0 && this.creature.getRandom().nextInt(this.chanceToWalk) == 0) {
                // 1.20.1: use DefaultRandomPos.getPos (same as the vanilla
                // RandomStrollGoal) - the old getPosTowards call with a zeroed
                // target vector returns null, so creatures never wander.
                Vec3 randTarget = DefaultRandomPos.getPos(this.creature, this.maxDistance, this.maxHeight);

                if (randTarget == null) {
                    return false;
                } else {
                    this.xPosition = randTarget.x;
                    this.yPosition = randTarget.y;
                    this.zPosition = randTarget.z;

                    return true;
                }
            } else {
                this.timer--;

                return false;
            }
        }
    }

    public void start() {
        this.creature.setSitting(false, null);
        this.creature.setSleeping(false);
        this.creature.setFlying(false);
        // 1.20.1: moveTo speed is a multiplier of the MOVEMENT_SPEED attribute
        // (1.0 = full speed). getCreatureSpeed() now returns 1.0, so this.speed
        // (0.7-1.0) is the intended relative wander speed.
        this.creature.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public boolean canContinueToUse() {
        return !this.creature.getNavigation().isDone() && !this.creature.isSitting() && this.creature.getPassengers().isEmpty() && this.creature.getTarget() == null;
    }

    public void stop() {
        this.creature.getNavigation().stop();
        this.timer = 50;
    }
}
