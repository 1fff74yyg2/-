package net.ilexiconn.jurassicraft.common.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

/**
 * 1.20.1 compatibility class. In 1.12.2 the mod subclassed the vanilla
 * {@code net.minecraft.entity.ai.EntityAIAvoidEntity}; in 1.20.1 that class was
 * renamed to {@link AvoidEntityGoal}. This wrapper keeps the old name so the
 * existing mod code (and the JurassiCraft*AvoidEntity AI classes) keeps working.
 */
@SuppressWarnings("rawtypes")
public class EntityAIAvoidEntity extends AvoidEntityGoal {
    public EntityAIAvoidEntity(PathfinderMob mob, Class avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
        super(mob, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
    }
}
