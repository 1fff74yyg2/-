package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntityJurassiCraftRidableFlying extends EntityJurassiCraftRidable {
    private static int MAX_TAKEOFF_TIME = 60;
    public float adjustYaw = 0;
    private int takeOffTimer;
    private int flapDelay;

    public EntityJurassiCraftRidableFlying(EntityType<? extends EntityJurassiCraftRidableFlying> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftRidableFlying(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof Player) {
            Player player = (Player) this.getPassengers().get(0);

            if (this.isFlying() || (player.getMainHandItem() != null && this.checkRidingItem(player.getMainHandItem()))) {
                /** There is a valid rider. */

                /** This starts the taking off. Player should jump and move forward. After some ticks, the state will change from taking off to flying */
                if (!this.level().isClientSide) {
                    if ((this.isTakingOff() || !this.isFlying()) && player.zza > 0.0F) {
                        this.increaseTakeOffTimer();

                        if (!this.isTakingOff())
                            this.setTakingOff(true);
                    } else {
                        this.decreaseTakeOffTimer(2);

                        if (this.isTakingOff()) {
                            this.setTakingOff(false);
                            this.resetTakeOffTimer();
                        }
                    }
                }

                /** If creature collided with a wall, it should stop flying and taking off. */
                if (this.horizontalCollision) {
                    this.setTakingOff(false);
                    this.setFlying(false);
                    this.resetTakeOffTimer();
                }

                /** SET MOVEMENTS AND ROTATIONS DEPENDING ON THE CREATURE STATE (TAKING OFF/FLYING). */
                if (this.isTakingOff()) {
                    this.setMaxUpStep(0.5F);
                    this.setSpeed(this.getSpeed() * 0.1F);
                    // SERVER AND CLIENT CHECK LATER
                    this.onUpdateTakingOffClient();
                    this.onUpdateTakingOffServer();
                    /** END METHOD HERE IF TAKING OFF. */
                    return;
                }

                if (this.isFlying()) {
                    /**
                     * If creature collided with the ground while flying, it should stop flying and taking off, and also reduce its speed.
                     */
                    if (this.onGround()) {
                        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x * 0.75F, this.getDeltaMovement().y, this.getDeltaMovement().z));
                        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.75F));

                        this.setTakingOff(false);
                        this.setFlying(false);
                        this.resetTakeOffTimer();
                    }

                    this.setMaxUpStep(1.25F);
                    this.setSpeed(this.getSpeed() * 0.1F);
                    // SERVER AND CLIENT CHECK LATER
                    this.onUpdateFlyingClient();
                    this.onUpdateFlyingServerGlide();
                    /** END METHOD HERE IF FLYING. */
                    return;
                }
            } else {
                /** There is a invalid rider (no riding item). Decrease speed slowly. */
                if (this.isFlying()) {
                    if (this.onGround())
                        this.setFlying(false);
                }
            }
        }
        /**
         * If there is no rider and creature is flying, decrease motion slowly and remove flying state if true.
         */
        if (this.isFlying()) {
            if (this.onGround())
                this.setFlying(false);
        }
        /** Remove takingOff state if true and there is no rider. */
        if (this.isTakingOff()) {
            this.setTakingOff(false);
            this.resetTakeOffTimer();
        }
        /** No rider normal travel(). */
        super.travel(travelVector);
    }

    private void onUpdateTakingOffClient() {
        this.setYRot(this.yRotO);
        this.setXRot(this.xRotO);
        this.setYRot(this.getYRot());
        this.setXRot(this.getXRot());
        this.yBodyRot = this.getYRot();
        this.setYHeadRot(this.getYRot());
        this.handleLimbMovement();
    }

    private void onUpdateTakingOffServer() {
        float newAngleYaw = 0.01745329251F * this.getYRot() + 1.57079632679F;

        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.025D * (double) this.getTakingOffMotionY(), this.getDeltaMovement().z));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x + 0.01D * Math.cos(newAngleYaw), this.getDeltaMovement().y, this.getDeltaMovement().z));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + 0.01D * Math.sin(newAngleYaw)));

        this.move(MoverType.SELF, new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z));

        if (this.getTakeOffProgress() >= 1.0F) {
            this.setFlying(true);
            this.setTakingOff(false);
            this.resetTakeOffTimer();
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, -0.1F, this.getDeltaMovement().z));
        }
    }

    private void onUpdateFlyingClient() {
        this.setMaxUpStep(0.0F);

        if (adjustYaw > 0)
            adjustYaw -= 0.1;

        if (adjustYaw < 0)
            adjustYaw += 0.1;

        // 1.20.1: read the rider's horizontal input (xxa) instead of the client-only Options
        if (this.getFirstPassenger() instanceof Player) {
            Player rider = (Player) this.getFirstPassenger();
            if (rider.xxa > 0.0F && adjustYaw < 2.4F) {
                adjustYaw += 0.2F;
            }

            if (rider.xxa < 0.0F && adjustYaw > -2.4F) {
                adjustYaw -= 0.2F;
            }
        }

        this.setYRot(Mth.wrapDegrees(this.getYRot() + adjustYaw));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        this.setYRot(this.getYRot());
        this.setXRot(this.getXRot());
        this.handleLimbMovement();
    }

    private void onUpdateFlyingServerGlide() {
        this.setDeltaMovement(new Vec3((double) (2.0F * this.getMountingSpeed() * Mth.cos(0.01745329251F * this.getXRot()) * Mth.sin(3.14159265359F + 0.01745329251F * this.getYRot())), this.getDeltaMovement().y, this.getDeltaMovement().z));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, (double) (2.0F * this.getMountingSpeed() * Mth.cos(0.01745329251F * this.getXRot()) * Mth.cos(0.01745329251F * this.getYRot()))));

        /** Flap wings and go up if delay is negative */
        boolean riderForward = this.getFirstPassenger() instanceof Player && ((Player) this.getFirstPassenger()).zza > 0.0F;
        if (this.getFlapDelay() < 0 && riderForward) {
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 1.5D, this.getDeltaMovement().z));
            this.setFlapDelay(20);
        } else {
            this.decreaseFlapDelay();
            /** Decrease motion Y if delay is positive. Decrease more first. */
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.7D + 0.15D * Math.sin(1.57079632679D * (double) this.getFlapDelay() / 20.0D), this.getDeltaMovement().z));
        }

        /** Go down if delay is negative, creature should keep flapping its wings */
        if (this.getFlapDelay() < 0) {
            this.move(MoverType.SELF, new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.2D, this.getDeltaMovement().z));
        }
        /** Keep the motion Y, which is decreasing to 0 gradually, if delay is positive */
        else {
            this.move(MoverType.SELF, new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z));
        }
    }

    private void onUpdateFlyingServerFreeMovement() {
        // 1.20.1: read the rider's forward input (zza) instead of the client-only Options

        float riderForward = this.getFirstPassenger() instanceof Player ? ((Player) this.getFirstPassenger()).zza : 0.0F;

        if (this.getDeltaMovement().y <= 2.0F * this.getMountingSpeed() && riderForward > 0.0F)
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.02D, this.getDeltaMovement().z));

        if (this.getDeltaMovement().y >= -2.0F * this.getMountingSpeed() && riderForward < 0.0F)
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.02D, this.getDeltaMovement().z));

        this.setDeltaMovement(new Vec3((double) (2.0F * this.getMountingSpeed() * Mth.cos(0.01745329251F * this.getXRot()) * Mth.sin(3.14159265359F + 0.01745329251F * this.getYRot())), this.getDeltaMovement().y, this.getDeltaMovement().z));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, (double) (2.0F * this.getMountingSpeed() * Mth.cos(0.01745329251F * this.getXRot()) * Mth.cos(0.01745329251F * this.getYRot()))));

        this.move(MoverType.SELF, new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.05D, this.getDeltaMovement().z));
    }

    /**
     * Returns if the creature is flyable or not. Value can be set using the creature's assets.jurassicraft.json file.
     */
    public boolean isCreatureFlyable() {
        return this.getCreature().isFlyingCreature();
    }

    /**
     * Returns the number of required ticks to start flying. Override this to set a new value different from default.
     */
    protected int getMaximumTakeOffTime() {
        return MAX_TAKEOFF_TIME;
    }

    /**
     * Sets the number of required ticks to start flying.
     */
    public void setMaximumTakeOffTime(int maxTime) {
        MAX_TAKEOFF_TIME = maxTime;
    }

    private void increaseTakeOffTimer() {
        if (this.getTakeOffTimer() <= this.getMaximumTakeOffTime())
            this.takeOffTimer++;
    }

    private void decreaseTakeOffTimer() {
        if (this.getTakeOffTimer() >= 0)
            this.takeOffTimer--;
    }

    private void decreaseTakeOffTimer(int value) {
        if (this.getTakeOffTimer() - value >= 0)
            this.takeOffTimer -= value;
    }

    private void resetTakeOffTimer() {
        this.takeOffTimer = 0;
    }

    public int getTakeOffTimer() {
        return this.takeOffTimer;
    }

    private void setTakeOffTimer(int time) {
        this.takeOffTimer = time;
    }

    public float getTakeOffProgress() {
        return (float) (this.getTakeOffTimer() / this.getMaximumTakeOffTime());
    }

    public float getTakingOffMotionY() {
        float result = Mth.sin(1.57079632679F * this.getTakeOffTimer() / this.getMaximumTakeOffTime());
        return result * result;
    }

    private void decreaseFlapDelay() {
        this.flapDelay--;
    }

    public int getFlapDelay() {
        return this.flapDelay;
    }

    private void setFlapDelay(int time) {
        this.flapDelay = time;
    }

    @Override
    public void push(Entity target) {
        super.push(target);

        if (this.isFlying()) {
            double deltaX = target.getX() - target.getX();
            double deltaZ = target.getZ() - target.getZ();
            double angleYaw = (float) Math.atan2(deltaZ, deltaX);
            target.setDeltaMovement(new Vec3(this.getDeltaMovement().x + Math.cos(angleYaw), this.getDeltaMovement().y, this.getDeltaMovement().z));
            target.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + Math.sin(angleYaw)));
            target.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.2, this.getDeltaMovement().z));
        }
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double distanceFallen, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putInt("TakeOffTimer", this.getTakeOffTimer());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setTakeOffTimer(compound.getInt("TakeOffTimer"));
    }
}
