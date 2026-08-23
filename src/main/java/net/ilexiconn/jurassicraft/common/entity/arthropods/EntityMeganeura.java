package net.ilexiconn.jurassicraft.common.entity.arthropods;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftFlyingCreature;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIGliding;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;

public class EntityMeganeura extends EntityJurassiCraftFlyingCreature {
    private static final EntityDataAccessor<Byte> FLYING = SynchedEntityData.defineId(EntityMeganeura.class, EntityDataSerializers.BYTE);

    public int courseChangeCooldown = 0;
    public double waypointX;
    public double waypointY;
    public double waypointZ;

    public EntityMeganeura(EntityType<? extends EntityMeganeura> type, Level world) {
        super(type, world);
    }

    public EntityMeganeura(Level world) {
        super(world, "grassandleaves");
        this.goalSelector.addGoal(0, new JurassiCraftAIGliding(this));
        this.setCreatureExperiencePoints(20);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(FLYING, Byte.valueOf((byte) 0));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateFlyingMovement();
    }

    private void updateFlyingMovement() {
        this.checkDespawn();

        double var1 = this.waypointX - this.getX();
        double var3 = this.waypointY - this.getY();
        double var5 = this.waypointZ - this.getZ();
        double var7 = var1 * var1 + var3 * var3 + var5 * var5;

        if (var7 < 1.0D || var7 > 3600.0D) {
            this.waypointX = this.getX() + (double) ((this.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = this.getY() + (double) ((this.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = this.getZ() + (double) ((this.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.getRandom().nextInt(5) + 2;
            var7 = (double) Math.sqrt(var7);

            if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7)) {
                this.setDeltaMovement(new net.minecraft.world.phys.Vec3(this.getDeltaMovement().x + var1 / var7 * 0.1D, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new net.minecraft.world.phys.Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + var3 / var7 * 0.1D, this.getDeltaMovement().z));
                this.setDeltaMovement(new net.minecraft.world.phys.Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + var5 / var7 * 0.1D));
            } else {
                this.waypointX = this.getX();
                this.waypointY = this.getY();
                this.waypointZ = this.getZ();
            }
        }
    }

    /**
     * True if the ghast has an unobstructed line of travel to the waypoint.
     */
    private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
        double var9 = (this.waypointX - this.getX()) / par7;
        double var11 = (this.waypointY - this.getY()) / par7;
        double var13 = (this.waypointZ - this.getZ()) / par7;

        AABB boundingBox = this.getBoundingBox();

        for (int var16 = 1; (double) var16 < par7; ++var16) {
            boundingBox = boundingBox.move(var9, var11, var13);

            if (!!this.level().getBlockCollisions(this, boundingBox).iterator().hasNext()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the entityOLD's current position is a valid location to spawn this entityOLD.
     */
    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return this.getRandom().nextInt(20) == 0 && super.checkSpawnObstruction(levelReader);
    }

    public float spiderScaleAmount() {
        return 1.5F;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        if (this.getRandom().nextFloat() < 0.1F) {
            this.dropCreatureItem(this.getCreature().getSkull(), 1);
        } else {
            if (!this.isOnFire()) {
                this.dropCreatureItem(this.getCreature().getMeat(), 1);
            } else {
                this.spawnCreatureItem(this.getCreature().getSteak(), 1);
            }
        }
    }
}
