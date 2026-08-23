package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;

public class AnimationAISpit extends AIAnimation {
    private EntityJurassiCraftCreature entitySpitting;
    private LivingEntity entityTarget;
    private int duration;
    private int spitFrame;

    public AnimationAISpit(EntityJurassiCraftCreature dino, int duration, int spitFrame) {
        super(dino);
        setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.entitySpitting = dino;
        this.entityTarget = null;
        this.duration = duration;
        this.spitFrame = spitFrame;
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.SPITTING.animID();
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return this.duration;
    }

    public void start() {
        super.start();

        this.entityTarget = this.entitySpitting.getTarget();
    }

    public void tick() {
        super.tick();

        if (this.entityTarget != null) {
            if (entitySpitting.getAnimationTick() <= spitFrame)
                entitySpitting.getLookControl().setLookAt(entityTarget, 30F, 30F);

            if (entitySpitting.getAnimationTick() == spitFrame)
                ((RangedAttackMob) entitySpitting).performRangedAttack(entityTarget, 0);
        }
    }

    public void stop() {
        super.stop();
    }
}
