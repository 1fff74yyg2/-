package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;

public class JurassiCraftAIAvoidEntityIfNotTamed extends EntityAIAvoidEntity {
    private EntityJurassiCraftSmart creature;

    public JurassiCraftAIAvoidEntityIfNotTamed(EntityJurassiCraftSmart entity, Class avoidClass, float distanceFromEntity, double farSpeed, double nearSpeed) {
        super(entity, avoidClass, distanceFromEntity, farSpeed, nearSpeed);
        this.creature = entity;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return !this.creature.isTamed() && super.canUse();
    }
}
