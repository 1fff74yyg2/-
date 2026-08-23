package net.ilexiconn.jurassicraft.common.entity.ai.herds;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityVelociraptor;
import net.minecraft.world.entity.LivingEntity;

public class VelociraptorHerd extends CreatureHerd {
    private LivingEntity target;

    public VelociraptorHerd() {
        super(true);
    }

    public boolean isAcceptable(EntityJurassiCraftCreature creature) {
        return creature instanceof EntityVelociraptor && super.isAcceptable(creature);
    }

    public void attack(LivingEntity target) {
        this.target = target;
    }

    public boolean isSneakingUp() {
        if (target != null && target.isRemoved()) {
            target = null;
            return false;
        }

        return target != null;
    }

    public LivingEntity getCurrentTarget() {
        return target;
    }
}
