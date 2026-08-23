package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIBite;
import net.ilexiconn.jurassicraft.common.entity.birds.EntityTitanis;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityCarnotaurus;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityHerrerasaurus;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityTyrannosaurus;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityVelociraptor;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class EntityJurassiCraftAggressive extends EntityJurassiCraftRidable {
    protected int attackTime;
    public float distanceFromTarget;

    public EntityJurassiCraftAggressive(EntityType<? extends EntityJurassiCraftAggressive> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, new AnimationAIBite(this, this.getBiteAnimationDuration()));
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftAggressive(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            Entity entity = damageSource.getEntity();

            if (entity != null && entity instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) entity;

                if (this.checkTargetBeforeAttacking(attacker)) {
                    this.setCreatureAngry(this, attacker);
                }
            }

            return super.hurt(damageSource, damage);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (getTarget() != null)
            distanceFromTarget = (float) Math.sqrt(Math.pow((this.getX() - getTarget().getX()), 2) + Math.pow((this.getZ() - getTarget().getZ()), 2));
        else
            distanceFromTarget = -1;
    }

    protected int getBiteAnimationDuration() {
        if (this instanceof EntityTyrannosaurus)
            return 20;
        else if (this instanceof EntityCarnotaurus)
            return 20;
        else if (this instanceof EntityVelociraptor)
            return 10;
        else if (this instanceof EntityHerrerasaurus)
            return 10;
        else if (this instanceof EntityTitanis)
            return 10;
        else
            return 10;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this instanceof EntityTyrannosaurus && distanceFromTarget >= 5.5F)
            return false;

        // Trigger the bite animation (visual only; the actual damage is dealt
        // below so the attack always works even if the animation AI misses its
        // damage tick).
        if (this.animID == 0)
            AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.BITE.animID());

        float attackDamage = (float) this.getCreatureAttack();
        int knockback = 0;

        if (entity instanceof LivingEntity) {
            attackDamage += net.minecraft.world.item.enchantment.EnchantmentHelper.getDamageBonus(this.getMainHandItem(), net.minecraft.world.entity.MobType.UNDEFINED);
            knockback += net.minecraft.world.item.enchantment.EnchantmentHelper.getKnockbackBonus(this);
        }

        boolean flag = entity.hurt(this.level().damageSources().mobAttack(this), attackDamage);

        if (flag) {
            if (knockback > 0) {
                entity.push((double) (-net.minecraft.util.Mth.sin(this.getYRot() * (float) Math.PI / 180.0F) * (float) knockback * 0.5F), 0.1D, (double) (net.minecraft.util.Mth.cos(this.getYRot() * (float) Math.PI / 180.0F) * (float) knockback * 0.5F));

                this.setDeltaMovement(new net.minecraft.world.phys.Vec3(this.getDeltaMovement().x * 0.6D, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new net.minecraft.world.phys.Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.6D));
            }

            int fireAspect = net.minecraft.world.item.enchantment.EnchantmentHelper.getFireAspect(this);

            if (fireAspect > 0)
                entity.setSecondsOnFire(fireAspect * 4);
        }

        return flag;
    }

    protected void attackEntity(Entity entity, float par2) {
        if (this.attackTime <= 0 && par2 < 2.0F && entity.getBoundingBox().maxY > this.getBoundingBox().minY && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
            this.attackTime = 20;
            this.doHurtTarget(entity);
        }
    }
}
