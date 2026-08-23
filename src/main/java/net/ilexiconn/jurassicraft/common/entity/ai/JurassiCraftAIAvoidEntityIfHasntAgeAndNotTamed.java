package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;

public class JurassiCraftAIAvoidEntityIfHasntAgeAndNotTamed extends EntityAIAvoidEntity {
    private EntityJurassiCraftSmart creature;
    private float age;

    public JurassiCraftAIAvoidEntityIfHasntAgeAndNotTamed(EntityJurassiCraftSmart entity, Class avoidClass, float minAgeToAvoid, float distanceFromEntity, double farSpeed, double nearSpeed) {
        super(entity, avoidClass, distanceFromEntity, farSpeed, nearSpeed);
        this.creature = entity;
        this.age = minAgeToAvoid;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return !this.creature.isTamed() && !this.creature.isCreatureOlderThan(this.age) && super.canUse();
    }
}
