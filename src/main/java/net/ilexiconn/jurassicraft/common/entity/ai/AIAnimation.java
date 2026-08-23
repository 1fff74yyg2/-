package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class AIAnimation extends Goal {
    private IAnimatedEntity animatedEntity;

    public AIAnimation(IAnimatedEntity entity) {
        this.animatedEntity = entity;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    public abstract int getAnimationId();

    public <T extends Mob> T getEntity() {
        return (T) this.animatedEntity;
    }

    public abstract boolean isAutomatic();

    public abstract int getDuration();

    public boolean shouldAnimate() {
        return false;
    }

    public boolean canUse() {
        if (isAutomatic())
            return this.animatedEntity.getAnimationId() == getAnimationId();

        return shouldAnimate();
    }

    public void start() {
        if (!isAutomatic())
            AnimationHandler.sendAnimationPacket(this.animatedEntity, getAnimationId());

        this.animatedEntity.setAnimationTick(0);
    }

    public boolean canContinueToUse() {
        return this.animatedEntity.getAnimationTick() < getDuration();
    }

    public void stop() {
        AnimationHandler.sendAnimationPacket(this.animatedEntity, 0);
    }
}
