package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityJurassiCraftProtective extends EntityJurassiCraftRidable {
    protected int attackTime;

    public EntityJurassiCraftProtective(EntityType<? extends EntityJurassiCraftProtective> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftProtective(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            List<Entity> neighbours = this.level().getEntities(this, this.getBoundingBox().inflate(16.0D, 8.0D, 16.0D));

            ArrayList<EntityJurassiCraftProtective> listChildren = new ArrayList<EntityJurassiCraftProtective>();
            ArrayList<EntityJurassiCraftProtective> listAdult = new ArrayList<EntityJurassiCraftProtective>();
            ArrayList<LivingEntity> listAttackers = new ArrayList<LivingEntity>();

            Entity entity = damageSource.getEntity();

            if (entity instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) entity;

                int count = 0;

                if (this.isCreatureAdult()) {
                    listAdult.add(this);
                } else {
                    listChildren.add(this);
                }

                for (Entity entityNeighbor : neighbours) {
                    if (entityNeighbor.getClass() == this.getClass()) {
                        EntityJurassiCraftProtective validEntityNeighbor = (EntityJurassiCraftProtective) entityNeighbor;

                        if (validEntityNeighbor.isCreatureAdult()) {
                            listAdult.add(validEntityNeighbor);

                            count++;
                        } else {
                            listChildren.add(validEntityNeighbor);
                        }
                    } else if (entityNeighbor.getClass() == attacker.getClass()) {
                        listAttackers.add((LivingEntity) entityNeighbor);
                    }
                }

                for (EntityJurassiCraftProtective children : listChildren) {
                    children.startFleeing();
                }

                if (!this.isCreatureAdult()) {
                    for (EntityJurassiCraftProtective adult : listAdult) {
                        adult.becomeAngry(attacker, 0.0F);
                    }
                } else {
                    if (attacker != this.getOwner()) {
                        if (count >= this.numberOfAllies && !(count < listAttackers.size() * 2)) {
                            for (EntityJurassiCraftProtective adult : listAdult) {
                                adult.becomeAngry(attacker, 0.0F);
                            }
                        } else {
                            for (EntityJurassiCraftProtective adult : listAdult) {
                                adult.startFleeing();
                            }
                        }
                    }
                }
            }

            return super.hurt(damageSource, damage);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.attackTime <= 0 && target.getBoundingBox().maxY > this.getBoundingBox().minY && target.getBoundingBox().minY < this.getBoundingBox().maxY) {
            this.attackTime = 20;

            float attackDamage = (float) this.getCreatureAttack();

            int knockback = 0;

            if (target instanceof LivingEntity) {
                attackDamage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), MobType.UNDEFINED);
                knockback += EnchantmentHelper.getKnockbackBonus(this);
            }

            boolean canAttack = target.hurt(this.level().damageSources().mobAttack(this), attackDamage);

            if (canAttack) {
                if (knockback > 0) {
                    target.push((double) (-Mth.sin(this.getYRot() * (float) Math.PI / 180.0F) * (float) knockback * 0.5F), 0.1D, (double) (Mth.cos(this.getYRot() * (float) Math.PI / 180.0F) * (float) knockback * 0.5F));

                    this.setDeltaMovement(new Vec3(this.getDeltaMovement().x * 0.6D, this.getDeltaMovement().y, this.getDeltaMovement().z));
                    this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.6D));
                }

                int fireAspect = EnchantmentHelper.getFireAspect(this);

                if (fireAspect > 0) {
                    target.setSecondsOnFire(fireAspect * 4);
                }

                if (target instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects((LivingEntity) target, this);
                }

                EnchantmentHelper.doPostDamageEffects(this, target);
            }

            return canAttack;
        } else {
            return false;
        }
    }
}
