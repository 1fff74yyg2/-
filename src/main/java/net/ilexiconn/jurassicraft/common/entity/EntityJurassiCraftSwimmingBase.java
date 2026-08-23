package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityJurassiCraftSwimmingBase extends EntityJurassiCraftRidable {
    public Vec3 territory;
    public int attackTimer = 40;
    protected float swimSpeed = 1;
    protected Entity hungryTarget;
    protected int huntingInterval = 1200;
    protected int hungry = huntingInterval;
    int movementSkip = 0;
    int xMovement = -1, yMovement = -1, zMovement = -1;
    float counter = 0;
    private Vec3 currentSwimTarget;
    private int newTarget = 500;

    public EntityJurassiCraftSwimmingBase(EntityType<? extends EntityJurassiCraftSwimmingBase> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftSwimmingBase(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public int getArmorValue() {
        return 0;
    }

    public float getDistanceToTarget() {
        return this.hungryTarget == null ? 20 : this.distanceTo(hungryTarget);
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
    }

    /**
     * randomly selected ChunkCoordinates in a 7x6x7 box around the bat (y offset -2 to 4) towards which it will fly. upon getting close a new target will be selected
     */
    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);

        if (currentSwimTarget == null) {
            currentSwimTarget = new Vec3(x, y, z);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (territory == null) {
            territory = new Vec3(this.getX(), this.getY(), this.getZ());
        }

        if (this.isInWater()) {
            --hungry;

            currentSwimTarget = findRandomTarget(this.getX(), this.getY(), this.getZ(), false);

            if (hungry <= -2400) {
                hungry = -1;
                this.actuallyHurt(this.level().damageSources().generic(), 1.0F);
            }

            if (hungry <= 0) {
                --attackTimer;

                if (hungryTarget == null)
                    this.currentSwimTarget = this.getHungryTarget();
                else {
                    --newTarget;
                    territory = null;
                    this.currentSwimTarget = new Vec3(hungryTarget.getX(), hungryTarget.getY(), hungryTarget.getZ());
                }

            } else
                currentSwimTarget = findRandomTarget(this.getX(), this.getY(), this.getZ(), false);

            if (newTarget <= 0 || (hungryTarget != null && this.distanceTo(hungryTarget) > 15)) {
                newTarget = 500;
                hungryTarget = null;
            }

            if (this.currentSwimTarget != null && territory != null && Math.sqrt(currentSwimTarget.x * territory.x + currentSwimTarget.y * territory.y + currentSwimTarget.z * territory.z) > 30)
                currentSwimTarget = findRandomTarget(this.getX(), this.getY(), this.getZ(), true);

            if (this.currentSwimTarget != null) {
                approachTarget(this.getX(), this.getY(), this.getZ(), swimSpeed);
            }

            if (!this.level().isEmptyBlock(new BlockPos((int) this.getX(), (int) this.getY() + 1, (int) this.getZ())) && !this.level().isEmptyBlock(new BlockPos((int) this.getX(), (int) this.getY() + 2, (int) this.getZ()))) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.0110829F, this.getDeltaMovement().z));
            }

        } else {
            this.setJumping(true);

            if (this.onGround()) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x * 0.031, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.031));
            }
        }

    }

    public int getHungry() {
        return hungry;
    }

    public void setHungry(int var1) {
        hungry = var1;
        hungryTarget = null;
    }

    public Vec3 getHungryTarget() {
        LivingEntity target = null;

        Vec3 normalizer = new Vec3(1, 1, 1).normalize();
        List entities = this.level().getEntities(this, this.getBoundingBox().inflate(Math.abs(normalizer.x * 7.0D) + 7, Math.abs(normalizer.y * 7.0D) + 7, Math.abs(normalizer.z * 7.0D) + 7));

        for (Object object : entities) {
            Entity entity = (Entity) object;

            if (entity.isInWater() && entity instanceof LivingEntity) {
                target = getTargetPriority(target, (LivingEntity) entity);
                hungryTarget = target;
            }
        }

        if (target == null) {
            return findRandomTarget(this.getX(), this.getY(), this.getZ(), false);
        } else if (attackTimer <= 0) {
            return new Vec3(target.getX(), target.getY(), target.getZ());
        } else {
            return currentSwimTarget;
        }
    }

    /*
     * Get a prioritized Target by comparing current target to new entity in range. Target = current target. Null at beginning of search entity1 = next entity in range to compare with target. Should never be null
     */
    public LivingEntity getTargetPriority(LivingEntity target, LivingEntity entity) {
        if (target != null) {
            if (target instanceof Player)
                return target;
            else
                return entity;
        } else {
            return entity;
        }
    }

    // Math converted to use more accurate and readable Vec3's
    private void approachTarget(double x, double y, double z, float speed) {
        Vec3 target = new Vec3(this.currentSwimTarget.x + 0.5D - x, this.currentSwimTarget.y + 0.1D - y, this.currentSwimTarget.z + 0.5D - z).normalize();

        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x + (target.x * 0.5D - this.getDeltaMovement().x) * 0.05000000149011612D * speed, this.getDeltaMovement().y, this.getDeltaMovement().z));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + (target.y * 0.699999988079071D - this.getDeltaMovement().y) * 0.05000000149011612D * speed, this.getDeltaMovement().z));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + (target.z * 0.5D - this.getDeltaMovement().z) * 0.05000000149011612D * speed));

        float f = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0D / Math.PI) - 90.0F;
        float f1 = Mth.wrapDegrees(f - this.getYRot());
        this.zza = 0.15F * speed;
        this.setYRot(this.getYRot() + f1);
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.0110829F, this.getDeltaMovement().z));

        BlockState state = this.level().getBlockState(new BlockPos(Mth.floor(this.currentSwimTarget.x), Mth.floor(this.currentSwimTarget.y), Mth.floor(this.currentSwimTarget.z)));

        if (currentSwimTarget != null && !state.getFluidState().is(FluidTags.WATER))
            currentSwimTarget = null;
    }

    public Vec3 findRandomTarget(double x, double y, double z, boolean force) {
        if (isInWater()) {
            movementSkip--;

            if (movementSkip <= 0) {
                // Will cause server and client to produce random yet identical rolls.
                this.getRandom().setSeed(this.getId() + ((int) this.getX() >> 4) + ((int) this.getY() >> 4) + ((int) this.getZ() >> 4));
                movementSkip = this.getRandom().nextInt(15);
                int randomWaterCheck = 0;

                BlockState block1 = null;
                BlockState block2 = null;
                Vec3 newPos = null;
                // Give up after 20 rolls
                // Bad luck or cannot move in the current conditions

                while (randomWaterCheck < 20) {
                    // reuse last movement unless a wall is there.
                    if (randomWaterCheck == 0 && xMovement != -1 && !force) {
                        newPos = new Vec3(x + xMovement, y + yMovement, z + zMovement);
                        block1 = this.level().getBlockState(new BlockPos(Mth.floor(newPos.x - (this.getBbWidth() / 2.0f)), Mth.floor(newPos.y), Mth.floor(newPos.z - (this.getBbWidth() / 2.0f))));
                        block2 = this.level().getBlockState(new BlockPos(Mth.floor(newPos.x + (this.getBbWidth() / 2.0f)), Mth.floor(newPos.y), Mth.floor(newPos.z + (this.getBbWidth() / 2.0f))));

                        if (block1.getFluidState().is(FluidTags.WATER) && block2.getFluidState().is(FluidTags.WATER)) {
                            break;
                        }

                        block1 = null;
                        block2 = null;
                    }

                    randomWaterCheck++;
                    xMovement = (this.getRandom().nextInt(10) - 5);
                    yMovement = (this.getRandom().nextInt(2) - 1);
                    zMovement = (this.getRandom().nextInt(10) - 5);
                    newPos = new Vec3(x + xMovement, y + yMovement, z + zMovement);

                    block1 = this.level().getBlockState(new BlockPos(Mth.floor(newPos.x - (this.getBbWidth() / 2.0f)), Mth.floor(newPos.y), Mth.floor(newPos.z - (this.getBbWidth() / 2.0f))));
                    block2 = this.level().getBlockState(new BlockPos(Mth.floor(newPos.x + (this.getBbWidth() / 2.0f)), Mth.floor(newPos.y), Mth.floor(newPos.z + (this.getBbWidth() / 2.0f))));

                    if (block1.getFluidState().is(FluidTags.WATER) && block2.getFluidState().is(FluidTags.WATER)) {
                        break;
                    }

                    block1 = null;
                    block2 = null;
                }

                if (block1 != null) {
                    this.swimSpeed = 1;
                    return newPos;
                } else {
                    return currentSwimTarget;
                }
            }
        }

        return currentSwimTarget;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);

        if (hungry <= 0 && entity == hungryTarget) {
            this.doHurtTarget(entity);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float attackDamage = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();

        this.attackTimer = 60;
        // onKillEntity is not a part of JurrasicCraftEntityRidable, but I needed when this entity kills another....
        if (entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() <= attackDamage)
            this.setHungry(huntingInterval);

        return entity.hurt(this.level().damageSources().mobAttack(this), attackDamage);
    }

    @Override
    public void tick() {
        super.tick();

        int air = this.getAirSupply();

        if (this.isAlive() && !this.isInWater()) {
            --air;

            this.setAirSupply(air);

            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.level().damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }

        // this.yBodyRot += (-((float) Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z)) * 180.0F / (float) Math.PI - this.yBodyRot) * 0.5F;
        // this.setYRot(this.yBodyRot);
        // float f = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        // this.setXRot(this.getXRot() + ((float) Math.atan2(this.getDeltaMovement().y, (double) f) * 180.0F / (float) Math.PI - this.getXRot()));
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     *
     * We dont need to protected boolean canDespawn() { return this.hasCustomName() ? false : true; }
     **/
}
