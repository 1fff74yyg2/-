package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityVelociraptor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public class AnimationAIVelociraptorLeap extends AIAnimation {
    private EntityVelociraptor entityRaptor;
    private LivingEntity attackTarget;

    private double destX;
    private double destZ;
    private double targetSpeedX;
    private double targetSpeedZ;
    private double targetPrevPosX;
    private double targetPrevPosZ;

    public AnimationAIVelociraptorLeap(EntityVelociraptor raptor) {
        super(raptor);

        this.entityRaptor = raptor;
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.LEAP.animID();
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 20;
    }

    public void start() {
        super.start();
        this.attackTarget = entityRaptor.getTarget();
    }

    public void stop() {
        super.stop();
    }

    public void tick() {
        if (this.entityRaptor.getAnimationTick() < 10) {
            if (this.attackTarget != null)
                this.entityRaptor.getLookControl().setLookAt(this.attackTarget, 30F, 30F);
        }

        if (this.entityRaptor.getAnimationTick() == 9) {
            if (this.attackTarget != null) {
                this.targetPrevPosX = attackTarget.getX();
                this.targetPrevPosZ = attackTarget.getZ();
            }
        }

        if (entityRaptor.getAnimationTick() == 10) {
            if (this.attackTarget != null) {
                this.targetSpeedX = attackTarget.getX() - targetPrevPosX;
                this.targetSpeedZ = attackTarget.getZ() - targetPrevPosZ;

                double leapDuration = 6;

                this.destX = attackTarget.getX() + targetSpeedX * leapDuration * 2;
                this.destZ = attackTarget.getZ() + targetSpeedZ * leapDuration * 2;

                double d = Math.sqrt((destX - entityRaptor.getX()) * (destX - entityRaptor.getX()) + (destZ - entityRaptor.getZ()) * (destZ - entityRaptor.getZ()));
                double a = Math.atan2((destZ - entityRaptor.getZ()), (destX - entityRaptor.getX()));

                this.entityRaptor.setDeltaMovement(new Vec3((d / leapDuration) * Math.cos(a), this.entityRaptor.getDeltaMovement().y, this.entityRaptor.getDeltaMovement().z));
                this.entityRaptor.setDeltaMovement(new Vec3(this.entityRaptor.getDeltaMovement().x, this.entityRaptor.getDeltaMovement().y, (d / leapDuration) * Math.sin(a)));
                this.entityRaptor.setDeltaMovement(new Vec3(this.entityRaptor.getDeltaMovement().x, 0.6D, this.entityRaptor.getDeltaMovement().z));
                this.entityRaptor.timeSinceLeap = 150;

                entityRaptor.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:velociraptorcall")), 1.0F, 1.0F);
            }
        }
    }
}