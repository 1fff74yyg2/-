package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * This AI makes an EntityJurassiCraftSmart attack a desirable mob when the creature is not tamed and has enough age. It will also check if the target is younger than a minimum age.
 *
 * @author RafaMv
 */
public class JurassiCraftAITargetIfHasAgeAndNonTamed extends TargetGoal {
    private final Class targetClass;
    private final int targetChance;
    private final JurassiCraftAITargetIfHasAgeAndNonTamed.Sorter theNearestAttackableTargetSorter;
    private final Predicate<Entity> targetEntitySelector;
    private EntityJurassiCraftSmart creature;
    private LivingEntity target;
    private float minimumCreatureAge;
    private float maximumTargetAge;

    public JurassiCraftAITargetIfHasAgeAndNonTamed(EntityJurassiCraftSmart creature, Class targetClass, int chanceToAttack, float minimumCreatureAge) {
        this(creature, targetClass, chanceToAttack, minimumCreatureAge, 1.0F);
    }

    public JurassiCraftAITargetIfHasAgeAndNonTamed(EntityJurassiCraftSmart creature, Class targetClass, int chanceToAttack, float minimumCreatureAge, float maximumTargetAge) {
        super(creature, true, false);

        this.creature = creature;
        this.minimumCreatureAge = minimumCreatureAge;
        this.maximumTargetAge = maximumTargetAge;
        this.targetClass = targetClass;
        this.targetChance = chanceToAttack;
        this.theNearestAttackableTargetSorter = new JurassiCraftAITargetIfHasAgeAndNonTamed.Sorter(creature);

        this.targetEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                return !(entity instanceof LivingEntity) ? false : JurassiCraftAITargetIfHasAgeAndNonTamed.this.canAttack((LivingEntity) entity, TargetingConditions.forCombat().range(JurassiCraftAITargetIfHasAgeAndNonTamed.this.getFollowDistance()));
            }
        };

        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        RandomSource random = this.mob.getRandom();

        boolean isTamed = this.creature.isTamed();
        boolean isAttacking = this.creature.isAttacking();
        boolean olderThanMinAge = this.creature.isCreatureOlderThan(this.minimumCreatureAge);

        if (this.targetChance > 0) {
            if (random.nextInt(this.targetChance) != 0 || isTamed || isAttacking || !olderThanMinAge) {
                return false;
            } else {
                double searchDistance = this.getFollowDistance();

                List nearby = this.mob.level().getEntitiesOfClass(this.targetClass, this.mob.getBoundingBox().inflate(searchDistance, 5.0D, searchDistance), this.targetEntitySelector);
                Collections.sort(nearby, this.theNearestAttackableTargetSorter);

                if (nearby.isEmpty()) {
                    return false;
                } else {
                    this.target = (LivingEntity) nearby.get(0);

                    if (this.target instanceof EntityJurassiCraftSmart)
                        return ((EntityJurassiCraftSmart) this.target).isCreatureOlderThan(this.maximumTargetAge) ? this.maximumTargetAge >= 1.0F : true;
                    else
                        return true;
                }
            }
        }

        return false;
    }

    public void start() {
        if (this.creature.isSitting())
            this.creature.setSitting(false, null);
        if (this.creature.isSleeping())
            this.creature.setSleeping(false);
        if (this.creature.isBreeding())
            this.creature.setBreeding(false);
        if (this.creature.isSocializing())
            this.creature.setSocializing(false);
        if (this.creature.isStalking())
            this.creature.setStalking(false);
        if (this.creature.isEating())
            this.creature.setEating(false);
        if (this.creature.isDrinking())
            this.creature.setDrinking(false);

        this.creature.setAttacking(true);
        this.mob.setTarget(this.target);

        super.start();
    }

    public void stop() {
        super.stop();

        this.creature.setAttacking(false);
    }

    public static class Sorter implements Comparator {
        private final Entity entity;

        public Sorter(Entity entity) {
            this.entity = entity;
        }

        public int compare(Entity entity1, Entity entity2) {
            double distance1 = this.entity.distanceToSqr(entity1);
            double distance2 = this.entity.distanceToSqr(entity2);

            return distance1 < distance2 ? -1 : (distance1 > distance2 ? 1 : 0);
        }

        public int compare(Object obj1, Object obj2) {
            return this.compare((Entity) obj1, (Entity) obj2);
        }
    }
}