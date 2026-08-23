package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EntityJurassiCraftCoward extends EntityJurassiCraftRidable {
    public EntityJurassiCraftCoward(EntityType<? extends EntityJurassiCraftCoward> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftCoward(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(18.0D, 8.0D, 18.0D));

            ArrayList<EntityJurassiCraftCoward> listOfCowards = new ArrayList<EntityJurassiCraftCoward>();

            listOfCowards.add(this);

            for (int i = 0; i < list.size(); ++i) {
                Entity entityNeighbor = list.get(i);

                if (entityNeighbor.getClass() == this.getClass() && entityNeighbor != this) {
                    listOfCowards.add((EntityJurassiCraftCoward) entityNeighbor);
                }
            }

            if (!listOfCowards.isEmpty()) {
                for (EntityJurassiCraftCoward creatures : listOfCowards) {
                    creatures.startFleeing();
                }
            }

            return super.hurt(damageSource, damage);
        }
    }
}
