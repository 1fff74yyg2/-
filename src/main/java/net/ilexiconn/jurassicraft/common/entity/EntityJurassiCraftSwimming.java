package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

/**
 * Inherithed from Animals+ by Click_Me
 */
public abstract class EntityJurassiCraftSwimming extends EntityJurassiCraftRidable {
    protected float swimRadius = 4.0F;
    protected float swimRadiusHeight = 4.0F;
    protected boolean isAgressive = false;
    protected int attackInterval = 50;
    protected float attackSpeed = 1.2F;
    protected float swimSpeed = 0.5F;
    protected boolean jumpOnLand = true;
    private double swimTargetX;
    private double swimTargetY;
    private double swimTargetZ;
    private Entity targetEntity;
    private boolean isAttacking;

    public EntityJurassiCraftSwimming(EntityType<? extends EntityJurassiCraftSwimming> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftSwimming(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return this.level().getBlockStates(this.getBoundingBox()).anyMatch(state -> state.getFluidState().is(FluidTags.WATER));
    }

    @Override
    public void tick() {
        int air = this.getAirSupply();

        super.tick();

        if (this.isInWater())
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.1D, this.getDeltaMovement().z));

        if (this.isAlive() && !this.isInWater()) {
            --air;
            this.setAirSupply(air);

            if (this.getAirSupply() == -10) {
                this.setAirSupply(0);
                this.hurt(this.level().damageSources().drown(), 2.0F);
            }
        } else
            this.setAirSupply(300);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.isInWater()) {
            double dx = this.swimTargetX - this.getX();
            double dy = this.swimTargetY - this.getY();
            double dz = this.swimTargetZ - this.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (dist < 1.0D || dist > 1000.0D) {
                this.swimTargetX = this.getX() + (double) ((this.getRandom().nextFloat() * 2.0F - 1.0F) * this.swimRadius);
                this.swimTargetY = this.getY() + (double) ((this.getRandom().nextFloat() * 2.0F - 1.0F) * this.swimRadiusHeight);
                this.swimTargetZ = this.getZ() + (double) ((this.getRandom().nextFloat() * 2.0F - 1.0F) * this.swimRadius);
                this.isAttacking = false;
            }

            if (this.level().getBlockState(new BlockPos(Mth.floor(this.swimTargetX), Mth.floor(this.swimTargetY + this.getBbHeight()), Mth.floor(this.swimTargetZ))).getFluidState().is(FluidTags.WATER)) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x + dx / dist * 0.05D * (double) this.swimSpeed, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + dy / dist * 0.1D * (double) this.swimSpeed, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + dz / dist * 0.05D * (double) this.swimSpeed));
            } else {
                this.swimTargetX = this.getX();
                this.swimTargetY = this.getY() + 0.1D;
                this.swimTargetZ = this.getZ();
            }

            if (this.isAttacking) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x * this.attackSpeed, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y * this.attackSpeed, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * this.attackSpeed));
            }

            if (this.isAgressive && this.getRandom().nextInt(this.attackInterval) == 0) {
                this.targetEntity = this.findEntityToAttack();
                if (this.targetEntity != null && this.targetEntity.isInWater()) {
                    this.swimTargetX = this.targetEntity.getX();
                    this.swimTargetY = this.targetEntity.getY();
                    this.swimTargetZ = this.targetEntity.getZ();
                    this.isAttacking = true;
                }
            }

            this.yBodyRot += (-((float) Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z)) * 180.0F / (float) Math.PI - this.yBodyRot) * 0.5F;
            this.setYRot(this.yBodyRot);
            float f = (float) Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
            this.setXRot(this.getXRot() + ((float) Math.atan2(this.getDeltaMovement().y, (double) f) * 180.0F / (float) Math.PI - this.getXRot()) * 0.5F);
        } else {
            if (this.jumpOnLand && this.onGround() && this.getRandom().nextInt(30) == 0) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, 0.3F, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(-0.2F + this.getRandom().nextFloat() * 0.4F, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, -0.2F + this.getRandom().nextFloat() * 0.4F));
            }
        }
    }

    protected Entity findEntityToAttack() {
        Player player = this.level().getNearestPlayer(this, 16.0D);

        return player != null && this.hasLineOfSight(player) ? player : null;
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);

        if (this.isAgressive && this.targetEntity == entity) {
            this.doHurtTarget(entity);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();

        return entity.hurt(this.level().damageSources().mobAttack(this), f);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.noCollision(this.getBoundingBox());
    }
}
