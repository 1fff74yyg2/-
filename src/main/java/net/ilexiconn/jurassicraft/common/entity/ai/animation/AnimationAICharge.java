package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCharges;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public class AnimationAICharge extends AIAnimation {
    private EntityJurassiCraftCharges entityCharging;
    private LivingEntity attackTarget;
    private float chargeAcceleration;
    private float chargeSpeed;
    private float angleYaw;
    private float startX;
    private float startZ;
    private float distanceTravelled;
    private float distanceOfTargetFromStart;

    public AnimationAICharge(EntityJurassiCraftCharges charging) {
        super(charging);
        this.entityCharging = charging;
        this.attackTarget = null;
        this.chargeAcceleration = 0.2F;
        this.chargeSpeed = 1;
        this.angleYaw = 0.0F;
        this.startX = 0.0F;
        this.startZ = 0.0F;
        this.distanceTravelled = 0.0F;
        this.distanceOfTargetFromStart = 0.0F;
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.CHARGE.animID();
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 100;
    }

    public void start() {
        super.start();

        this.attackTarget = this.entityCharging.getTarget();
        this.startX = (float) this.entityCharging.getX();
        this.startZ = (float) this.entityCharging.getZ();

        if (this.attackTarget != null)
            this.distanceOfTargetFromStart = (float) Math.sqrt((this.startX - this.attackTarget.getX()) * (this.startX - this.attackTarget.getX()) + (this.startZ - this.attackTarget.getZ()) * (this.startZ - this.attackTarget.getZ()));
    }

    public void stop() {
        super.stop();

        this.entityCharging.timeSinceCharge = 150;
        this.entityCharging.charging = false;
        this.entityCharging.setTarget(null);
    }

    public void tick() {
        if (this.attackTarget != null)
            this.entityCharging.getLookControl().setLookAt(this.attackTarget, 30F, 30F);

        this.distanceTravelled = (float) Math.sqrt((this.startX - this.entityCharging.getX()) * (this.startX - this.entityCharging.getX()) + (this.startZ - this.entityCharging.getZ()) * (this.startZ - this.entityCharging.getZ()));

        if (this.entityCharging.getAnimationTick() == 1)
            this.entityCharging.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:triceratopscharge")), 1.0F, 1.0F);

        if (this.entityCharging.getAnimationTick() >= 35 && this.entityCharging.getAnimationTick() <= 40 && this.attackTarget != null) {
            double deltaX = this.attackTarget.getX() - this.entityCharging.getX();
            double deltaZ = this.attackTarget.getZ() - this.entityCharging.getZ();
            this.angleYaw = (float) Math.atan2(deltaZ, deltaX);
        }

        if (this.entityCharging.getAnimationTick() > 40) {
            if (this.attackTarget != null || !this.entityCharging.getPassengers().isEmpty()) {
                if (!this.entityCharging.getPassengers().isEmpty() && this.entityCharging.getPassengers().get(0) instanceof Player) {
                    this.angleYaw = this.entityCharging.getPassengers().get(0).getYRot() * 0.01745329251F + 1.57079632679F;
                    this.entityCharging.setYRot(this.entityCharging.getPassengers().get(0).getYRot());
                    this.chargeAcceleration = 0.3F;
                }

                this.entityCharging.charging = true;

                if (attackTarget != null && this.distanceOfTargetFromStart > distanceTravelled) {
                    double deltaX = this.attackTarget.getX() - this.entityCharging.getX();
                    double deltaZ = this.attackTarget.getZ() - this.entityCharging.getZ();
                    float destAngleYaw = (float) Math.atan2(deltaZ, deltaX);

                    if (angleYaw - destAngleYaw >= 0.1)
                        angleYaw -= 0.1;
                    else if (angleYaw - destAngleYaw <= -0.1)
                        angleYaw += 0.1;
                }

                if (Math.sqrt(this.entityCharging.getDeltaMovement().x * this.entityCharging.getDeltaMovement().x + this.entityCharging.getDeltaMovement().z * this.entityCharging.getDeltaMovement().z) < this.chargeSpeed - 0.2) {
                    this.entityCharging.setDeltaMovement(new Vec3(this.entityCharging.getDeltaMovement().x + this.chargeAcceleration * Math.cos(this.angleYaw), this.entityCharging.getDeltaMovement().y, this.entityCharging.getDeltaMovement().z));
                    this.entityCharging.setDeltaMovement(new Vec3(this.entityCharging.getDeltaMovement().x, this.entityCharging.getDeltaMovement().y, this.entityCharging.getDeltaMovement().z + this.chargeAcceleration * Math.sin(this.angleYaw)));
                } else {
                    this.entityCharging.setDeltaMovement(new Vec3(this.chargeSpeed * Math.cos(this.angleYaw), this.entityCharging.getDeltaMovement().y, this.entityCharging.getDeltaMovement().z));
                    this.entityCharging.setDeltaMovement(new Vec3(this.entityCharging.getDeltaMovement().x, this.entityCharging.getDeltaMovement().y, this.chargeSpeed * Math.sin(this.angleYaw)));
                }

                entityCharging.setYRot((float) (angleYaw * (180 / Math.PI) - 90));
                entityCharging.yBodyRot = (float) (angleYaw * (180 / Math.PI) - 90);
            }
        }
    }
}
