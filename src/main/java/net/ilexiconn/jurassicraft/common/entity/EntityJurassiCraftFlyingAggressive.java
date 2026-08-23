package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.jurassicraft.common.entity.ai.stats.FlyingParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityJurassiCraftFlyingAggressive extends EntityJurassiCraftAggressive {
    public FlyingParameters flyingParameters;
    private String landingMaterial;

    public EntityJurassiCraftFlyingAggressive(EntityType<? extends EntityJurassiCraftFlyingAggressive> type, Level world) {
        super(type, world);

        this.setLandingMaterial("default");
        this.setFlyingParameters(new FlyingParameters(63, 80, 10, 10, 10, 10, 10, 10, 10, this.landingMaterial));
        this.setFlying(false);
    }

    public EntityJurassiCraftFlyingAggressive(Level world) {
        this(world, "default");
    }

    public EntityJurassiCraftFlyingAggressive(Level world, String landingMaterial) {
        super(world);

        this.setLandingMaterial(landingMaterial);
        this.setFlyingParameters(new FlyingParameters(63, 80, 10, 10, 10, 10, 10, 10, 10, this.landingMaterial));
        this.setFlying(false);
    }

    @Override
    public void aiStep() {
        if (this.isFlyingCreature()) {
            if (this.getPassengers().isEmpty()) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.04F + 0.06F * this.flyingParameters.flySpeedModifier / 500.0F, this.getDeltaMovement().z));
                this.setFlying(true);
            }

            if (this.onGround() && this.isFlying())
                this.setFlying(false);
        }

        super.aiStep();
    }

    public FlyingParameters getFlyingParameters() {
        return flyingParameters;
    }

    public void setFlyingParameters(FlyingParameters flyingParameters) {
        this.flyingParameters = flyingParameters;
    }

    public String getLandingMaterial() {
        return landingMaterial;
    }

    public void setLandingMaterial(String landingMaterial) {
        this.landingMaterial = landingMaterial;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putString("LandingMaterial", this.landingMaterial);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setLandingMaterial(compound.getString("LandingMaterial"));
        this.setFlyingParameters(new FlyingParameters(63, 80, 10, 10, 10, 10, 10, 10, 10, compound.getString("LandingMaterial")));
    }
}
