package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAICharge;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityJurassiCraftCharges extends EntityJurassiCraftProtective {
    public boolean charging = false;
    public float distanceFromTarget;
    public int timeSinceCharge = 0;
    public int stepCount = 0;

    public EntityJurassiCraftCharges(EntityType<? extends EntityJurassiCraftCharges> type, Level world) {
        super(type, world);

        this.goalSelector.addGoal(2, new AnimationAICharge(this));
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftCharges(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (getTarget() != null) {
            // Charge AI
            if (getTarget() != null)
                distanceFromTarget = (float) Math.sqrt(Math.pow((this.getX() - getTarget().getX()), 2) + Math.pow((this.getZ() - getTarget().getZ()), 2));
            else
                distanceFromTarget = -1;
            if (this.getTarget() != null && this.onGround() && timeSinceCharge == 0 && !this.isFleeing() && this.getCreatureAgeInDays() >= 17)
                AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.CHARGE.animID());
        } else {
            this.distanceFromTarget = -1.0F;
        }

        if (timeSinceCharge != 0)
            timeSinceCharge--;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.stepCount <= 0 && this.charging) {
            this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:gallop")), 3.0F, this.getSoundPitch() - 0.5F);
            this.stepCount = 10;
        }

        this.stepCount -= 1;
    }

    @Override
    public void push(Entity victim) {
        super.push(victim);

        if (this.charging && (isTamed() || !getClass().equals(victim.getClass()))) {
            victim.hurt(this.level().damageSources().mobAttack(this), 20);
            double deltaX = victim.getX() - this.getX();
            double deltaZ = victim.getZ() - this.getZ();
            double angleYaw = (float) Math.atan2(deltaZ, deltaX);
            victim.setDeltaMovement(new Vec3(this.getDeltaMovement().x + Math.cos(angleYaw), this.getDeltaMovement().y, this.getDeltaMovement().z));
            victim.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + Math.sin(angleYaw)));
            victim.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.3, this.getDeltaMovement().z));
        }
    }

    public void ridingPlayerRightClick() {
        if (this.onGround() && this.timeSinceCharge < 75 && this.getCreatureAgeInDays() >= 17 && ((Player) this.getPassengers().get(0)).getMainHandItem() != null && this.getCreature().isRidingItem(((Player) this.getPassengers().get(0)).getMainHandItem().getItem())) {
            this.decreaseHeldItemDurability(40);
            AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.CHARGE.animID());
        }
    }
}
