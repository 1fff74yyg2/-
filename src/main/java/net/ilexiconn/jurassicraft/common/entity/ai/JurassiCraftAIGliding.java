package net.ilexiconn.jurassicraft.common.entity.ai;

/*
 * Old flying code :P
 */

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftFlyingCreature;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class JurassiCraftAIGliding extends Goal {
    private final long OWNER_FIND_INTERVAL = 5000L;
    private final long SITTINGSPOT_REACHTIME = 3000L;
    private final double OWNER_DISTANCE_TO_TAKEOFF = 100D;
    private final EntityJurassiCraftFlyingCreature creature;
    boolean lastChangeDirection;
    int flightTicks = 0;
    double takeOffSpeed = 0;
    int targetHeight = 0;
    private BlockPos currentFlightTarget;
    private RandomSource rand;
    private long nextOwnerCheckTime;
    private long sittingSpotAbortTime;
    private boolean takingOff = false;
    private int nextWingBeat = 10;
    private int wingBeatTick = 0;

    public JurassiCraftAIGliding(EntityJurassiCraftFlyingCreature entity) {
        creature = entity;
        rand = entity.getRandom();
        nextOwnerCheckTime = System.currentTimeMillis();
        sittingSpotAbortTime = -1L;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!creature.onGround() || creature.flyingParameters == null)
            return false;

        return checkTakeOffConditions();
    }

    public boolean canContinueToUse() {
        return !creature.onGround();
    }

    public void start() {
        takeOff();
    }

    public void stop() {
        super.stop();
    }

    public void tick() {
        flightTicks++;

        if (flightTicks > 30 && takingOff || (takingOff && creature.getY() >= targetHeight)) {
            takingOff = false;
            flightTicks = 0;
        }

        if (takingOff) {
            creature.travel(new Vec3(creature.flyingParameters.flySpeedModifier / 500f, 0, 0));
            creature.setDeltaMovement(new Vec3(creature.getDeltaMovement().x, takeOffSpeed, creature.getDeltaMovement().z));
        }

        checkForLandingSpot();

        HitResult mop = creature.level().clip(new ClipContext(new Vec3(creature.getX(), creature.getBoundingBox().minY, creature.getZ()), new Vec3(creature.getX() + creature.getDeltaMovement().x * 100, creature.getBoundingBox().minY, creature.getZ() + creature.getDeltaMovement().z * 100), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, creature));

        if (mop == null)
            mop = creature.level().clip(new ClipContext(new Vec3(creature.getX(), creature.getBoundingBox().maxY, creature.getZ()), new Vec3(creature.getX() + creature.getDeltaMovement().x * 100, creature.getBoundingBox().maxY, creature.getZ() + creature.getDeltaMovement().z * 100), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, creature));

        if (hasLandingSpot()) {
            if (mop == null) {
                double d0 = (double) this.currentFlightTarget.getX() + 0.5D - creature.getX();
                double d1 = (double) this.currentFlightTarget.getY() + 0.1D - creature.getY();
                double d2 = (double) this.currentFlightTarget.getZ() + 0.5D - creature.getZ();

                creature.setDeltaMovement(new Vec3(creature.getDeltaMovement().x + (Math.signum(d0) * 1D - creature.getDeltaMovement().x) * 0.10000000149011612D, creature.getDeltaMovement().y, creature.getDeltaMovement().z));
                creature.setDeltaMovement(new Vec3(creature.getDeltaMovement().x, creature.getDeltaMovement().y + (Math.signum(d1) * 0.699999988079071D - creature.getDeltaMovement().y) * 0.10000000149011612D, creature.getDeltaMovement().z));
                creature.setDeltaMovement(new Vec3(creature.getDeltaMovement().x, creature.getDeltaMovement().y, creature.getDeltaMovement().z + (Math.signum(d2) * 1D - creature.getDeltaMovement().z) * 0.10000000149011612D));

                float f = (float) (Math.atan2(creature.getDeltaMovement().z, creature.getDeltaMovement().x) * 180.0D / Math.PI) - 90.0F;
                float f1 = Mth.wrapDegrees(f - creature.getYRot());

                creature.setZza(0.5F);
                creature.setYRot(creature.getYRot() + f1);
            }
        } else {
            maintainFlight(mop != null);
        }

        super.tick();
    }

    private void checkForLandingSpot() {
        if (this.currentFlightTarget != null && (!creature.level().isEmptyBlock(this.currentFlightTarget) || this.currentFlightTarget.getY() < 1))
            this.currentFlightTarget = null;

        if (this.currentFlightTarget == null || creature.getRandom().nextInt(30) == 0) {
            this.currentFlightTarget = new BlockPos((int) (creature.getX() + creature.getDeltaMovement().x * 200 + creature.getRandom().nextInt(10) - 5), 0, (int) (creature.getZ() + creature.getDeltaMovement().z * 200 + creature.getRandom().nextInt(10) - 5));

            this.currentFlightTarget = new BlockPos(this.currentFlightTarget.getX(), creature.level().getHeight(Heightmap.Types.MOTION_BLOCKING, this.currentFlightTarget.getX(), this.currentFlightTarget.getZ()) + 1, this.currentFlightTarget.getZ());

            BlockState landingBlockState = creature.level().getBlockState(new BlockPos(currentFlightTarget.getX(), currentFlightTarget.getY() - 1, currentFlightTarget.getZ()));

            if (creature.flyingParameters != null && !creature.flyingParameters.canLandOn(landingBlockState.getBlock()) || !creature.level().isEmptyBlock(this.currentFlightTarget))
                this.currentFlightTarget = null;
        }
    }

    private boolean hasLandingSpot() {
        return currentFlightTarget != null;
    }

    private void maintainFlight(boolean hasObstacle) {
        wingBeatTick++;
        if (hasObstacle || wingBeatTick >= nextWingBeat) {
            pickDirection(hasObstacle);
            nextWingBeat = creature.flyingParameters.flapRate + (int) (Math.random() * 0.4 * creature.flyingParameters.flapRate - 0.2 * creature.flyingParameters.flapRate);
            creature.travel(new Vec3(0, 0, 4.0f + creature.flyingParameters.flySpeedModifier / 100f * creature.flyingParameters.flySpeedModifier));
            creature.setDeltaMovement(new Vec3(creature.getDeltaMovement().x, (creature.flyingParameters.flapRate + 1) * 0.01, creature.getDeltaMovement().z));
            wingBeatTick = 0;
        }
    }

    public void pickDirection(boolean useLastChangeDirection) {
        double rotAmt;
        if (useLastChangeDirection) {
            rotAmt = creature.getRandom().nextInt(5) + 5;
            if (lastChangeDirection)
                rotAmt *= -1;
            String extra = creature.getTarget() != null ? " has target" : " no target";
        } else {
            rotAmt = creature.getRandom().nextInt(10) - 5;
            lastChangeDirection = rotAmt > 0;
        }
        creature.setYRot(creature.getYRot() + (float) rotAmt);

    }

    private void lookForOwnerEntity() {
        if (System.currentTimeMillis() > nextOwnerCheckTime) {
            nextOwnerCheckTime = System.currentTimeMillis() + OWNER_FIND_INTERVAL;
        }
    }

    private boolean checkTakeOffConditions() {
        Player nearest = creature.level().getNearestPlayer(creature, 6.0D);
        if (nearest != null) {
            return true;
        }
        return Math.random() < 0.015;
    }

    private void land() {
        sittingSpotAbortTime = -1L;
        creature.moveTo(currentFlightTarget.getX() + 0.5D, currentFlightTarget.getY() + 0.5D, currentFlightTarget.getZ() + 0.5D);
    }

    private void takeOff() {
        creature.setFlying(true);
        takingOff = true;
        flightTicks = 0;
        targetHeight = (int) creature.getY() + (int) (Math.random() * (creature.flyingParameters.flyHeightMax - creature.flyingParameters.flyHeightMin)) + creature.flyingParameters.flyHeightMin;
        creature.moveTo(creature.getX(), creature.getY() - 1D, creature.getZ());
        creature.level().levelEvent(null, 1015, new BlockPos((int) creature.getX(), (int) creature.getY(), (int) creature.getZ()), 0);
        takeOffSpeed = 0.22 + creature.flyingParameters.flySpeedModifier / 300f;
        creature.move(MoverType.SELF, new Vec3(creature.getRandom().nextDouble() - 0.5, takeOffSpeed, creature.getRandom().nextDouble() - 0.5));
    }
}