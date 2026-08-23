package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class JurassiCraftAINearestAttackableTarget extends TargetGoal {
    private final Class targetClass;
    private final JurassiCraftAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    private final Predicate<Entity> targetEntitySelector;
    private final EntityJurassiCraftSmart attackerCreature;
    private LivingEntity targetEntity;

    public JurassiCraftAINearestAttackableTarget(EntityJurassiCraftSmart creature, Class target, boolean flag) {
        super(creature, flag, false);

        this.targetClass = target;
        this.attackerCreature = creature;
        this.theNearestAttackableTargetSorter = new JurassiCraftAINearestAttackableTarget.Sorter(creature);
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));

        this.targetEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                return !(entity instanceof LivingEntity) ? false : (!JurassiCraftAINearestAttackableTarget.this.canAttack((LivingEntity) entity, TargetingConditions.forCombat().range(JurassiCraftAINearestAttackableTarget.this.getFollowDistance())) ? false : attackerCreature.isCreatureAdult());
            }
        };
    }

    public boolean canUse() {
        double targetDist = this.getFollowDistance();

        List nearEntities = this.mob.level().getEntitiesOfClass(this.targetClass, this.mob.getBoundingBox().inflate(targetDist, 4.0D, targetDist), this.targetEntitySelector);
        Collections.sort(nearEntities, this.theNearestAttackableTargetSorter);

        if (nearEntities.isEmpty())
            return false;
        else {
            this.targetEntity = (LivingEntity) nearEntities.get(0);
            return this.attackerCreature.checkTargetBeforeAttacking(targetEntity);
        }
    }

    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    public static class Sorter implements Comparator {
        private final Entity theEntity;

        public Sorter(Entity par1Entity) {
            this.theEntity = par1Entity;
        }

        public int compare(Entity entity1, Entity entity2) {
            double d0 = this.theEntity.distanceToSqr(entity1);
            double d1 = this.theEntity.distanceToSqr(entity2);

            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }

        public int compare(Object par1Obj, Object par2Obj) {
            return this.compare((Entity) par1Obj, (Entity) par2Obj);
        }
    }
}