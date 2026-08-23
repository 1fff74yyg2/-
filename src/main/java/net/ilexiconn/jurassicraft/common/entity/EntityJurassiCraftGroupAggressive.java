package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityJurassiCraftGroupAggressive extends EntityJurassiCraftAggressive {
    public EntityJurassiCraftGroupAggressive(EntityType<? extends EntityJurassiCraftGroupAggressive> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftGroupAggressive(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    protected void setCreatureAngry(EntityJurassiCraftAggressive creature, Entity entity) {
        if (entity instanceof LivingEntity && entity.getClass() != creature.getClass()) {
            LivingEntity attacker = (LivingEntity) entity;
            List neighbours = creature.level().getEntities(creature, creature.getBoundingBox().inflate(16.0D, 8.0D, 16.0D));

            for (Object object : neighbours) {
                Entity neighbour = (Entity) object;

                if (neighbour.getClass() == this.getClass()) {
                    EntityJurassiCraftAggressive angryNeighbour = (EntityJurassiCraftAggressive) neighbour;

                    if (angryNeighbour.checkTargetBeforeAttacking(attacker)) {
                        angryNeighbour.becomeAngry(attacker, 0.0F);
                    }
                }
            }
        }

        super.setCreatureAngry(creature, entity);
    }
}
