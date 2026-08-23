package net.ilexiconn.jurassicraft.common.entity.reptiles;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftRidableFlying;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIEatDroppedFood;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIFollowFood;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAISit;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIWander;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntityCearadactylus extends EntityJurassiCraftRidableFlying {
    private static final EntityDataAccessor<Byte> FLYING = SynchedEntityData.defineId(EntityCearadactylus.class, EntityDataSerializers.BYTE);

    public BlockPos currentTarget;
    public int maxHeight = 130;
    private boolean isFlying;

    public EntityCearadactylus(EntityType<? extends EntityCearadactylus> type, Level world) {
        super(type, world);
    }

    public EntityCearadactylus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(1, new FloatGoal(this));
            this.goalSelector.addGoal(2, new JurassiCraftAIWander(this, 40, this.getCreatureSpeed()));
            this.goalSelector.addGoal(3, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(5, new JurassiCraftAIFollowFood(this, 60, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(5, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    
            this.setCreatureExperiencePoints(1500);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.getEntityData().define(FLYING, Byte.valueOf((byte) 0));
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getYBouningBox() * 0.6D;
    }

    public int getTalkInterval() {
        return 350;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbttag) {
        super.readAdditionalSaveData(nbttag);

        this.getEntityData().set(FLYING, Byte.valueOf(nbttag.getByte("Flying")));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttag) {
        super.addAdditionalSaveData(nbttag);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (3.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (2.5F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);

        if (this.getRandom().nextFloat() < 0.1F)
            this.dropCreatureItem(this.getCreature().getSkull(), 1);
    }

    public void setFlying(boolean state) {
        isFlying = state;

    }

    /*
     * Makes it fly... Pretty neet :) Thanks Alexthe666 for the majority of the code ;)
     */

    // TODO: Fix the wondering on the ground :P

    /*
     * Updates the creature every tick to decide what its going to do :)
     */
    @Override
    public void aiStep() {

        if (this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.6D, this.getDeltaMovement().z));
        }
        if (this.getPassengers().isEmpty()) {
            if (!this.level().isClientSide) {
                if (getTarget() == null) {
                    if (this.getRandom().nextInt(400) == 0)
                        if (!isFlying)
                            setFlying(true);
                        else
                            setFlying(false);

                    if (isFlying) {
                        flyAround();
                    } else {

                    }

                    if (getTarget() != null) {
                        currentTarget = new BlockPos((int) getTarget().getX(), (int) (getTarget().getY() + getTarget().getEyeHeight()), (int) getTarget().getZ());
                        setFlying(false);
                        flyTowardsTarget();
                    }
                }
            }
        }
        super.aiStep();
    }

    /*
     * Makes the animals fly towards its location
     */
    public void flyTowardsTarget() {
        if (currentTarget != null) {
            double targetX = currentTarget.getX() + 0.5D - this.getX();
            double targetY = currentTarget.getY() + 1D - this.getY();
            double targetZ = currentTarget.getZ() + 0.5D - this.getZ();
            Vec3 motion = this.getDeltaMovement();
            double newMotionX = motion.x + (Math.signum(targetX) * 0.5D - motion.x) * 0.10000000149011612D;
            double newMotionY = motion.y + (Math.signum(targetY) * 0.699999988079071D - motion.y) * 0.10000000149011612D;
            double newMotionZ = motion.z + (Math.signum(targetZ) * 0.5D - motion.z) * 0.10000000149011612D;
            this.setDeltaMovement(new Vec3(newMotionX, newMotionY, newMotionZ));
            float angle = (float) (Math.atan2(newMotionZ, newMotionX) * 180.0D / Math.PI) - 90.0F;
            float rotation = Mth.wrapDegrees(angle - this.getYRot());
            this.setYRot(this.getYRot() + rotation);
            if (this.getY() > maxHeight) {
                currentTarget = new BlockPos(currentTarget.getX(), -1, currentTarget.getZ());
            }
        }
    }

    /*
     * base of the flying
     */
    public void flyAround() {
        if (currentTarget != null)
            if (!this.level().isEmptyBlock(currentTarget) || currentTarget.getY() < 1)
                currentTarget = null;

        if (currentTarget == null || this.getRandom().nextInt(30) == 0 || currentTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 10.0D)
            currentTarget = new BlockPos((int) this.getX() + this.getRandom().nextInt(90) - this.getRandom().nextInt(60), (int) this.getY() + this.getRandom().nextInt(60) - 2, (int) this.getZ() + this.getRandom().nextInt(90) - this.getRandom().nextInt(60));

        flyTowardsTarget();
    }

    /*
     * Checks if the animal is on ground (Not perfect, but it works)
     */
    public boolean checkGround(EntityCearadactylus reptile) {
        reptile = this;
        return !reptile.level().isEmptyBlock(new BlockPos((int) reptile.getX(), (int) reptile.getY() - 1, (int) reptile.getZ()));
    }
}
