package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityDilophosaurus;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Created by jnad325 on 3/14/15.
 */
public class EntitySpit extends ThrowableProjectile {
    public EntitySpit(EntityType<? extends EntitySpit> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntitySpit(Level world) {
        this(JCEntityRegistry.DILO_SPIT.get(), world);
    }

    public EntitySpit(EntityType<? extends EntitySpit> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public EntitySpit(Level world, LivingEntity entity) {
        this(JCEntityRegistry.DILO_SPIT.get(), entity, world);
    }

    public EntitySpit(EntityType<? extends EntitySpit> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public EntitySpit(Level world, double x, double y, double z) {
        this(JCEntityRegistry.DILO_SPIT.get(), x, y, z, world);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void onHit(HitResult result) {
        Entity thrower = this.getOwner();

        if (thrower instanceof EntityDilophosaurus) {
            EntityDilophosaurus spitter = (EntityDilophosaurus) thrower;

            if (result.getType() == HitResult.Type.ENTITY && result instanceof EntityHitResult) {
                Entity entityHit = ((EntityHitResult) result).getEntity();

                if (entityHit != null && entityHit instanceof LivingEntity && entityHit != spitter && (entityHit == spitter.getTarget() || !(entityHit instanceof EntityDilophosaurus))) {
                    LivingEntity entityHitLiving = (LivingEntity) entityHit;

                    entityHitLiving.hurt(this.level().damageSources().thrown(this, this.getOwner()), 1.0f);
                    entityHitLiving.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 1, false, false));
                    entityHitLiving.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 1, false, false));
                }
            }
        }

        float size = 3F;
        for (int i = 0; i < 8; ++i) {
            this.level().addParticle(DustParticleOptions.REDSTONE, this.getX() + (size * Math.random() - size / 2), this.getY() + (size * Math.random() - size / 2), this.getZ() + (size * Math.random() - size / 2), 0.2D, 0.2D, 0.2D);
        }

        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        float size = 0.35F;

        for (int i = 0; i < 6; ++i) {
            this.level().addParticle(DustParticleOptions.REDSTONE, this.getX() + (size * Math.random() - size / 2), this.getY() + (size * Math.random() - size / 2), this.getZ() + (size * Math.random() - size / 2), 0.2D, 0.2D, 0.2D);
        }
    }
}
