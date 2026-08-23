package net.ilexiconn.jurassicraft.common.entity.ai.herds;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class HerdAIFollowHerd extends EntityAIHerd {
    private double speed;

    public HerdAIFollowHerd(EntityJurassiCraftCreature creature, boolean groupAttack, double speed) {
        super(creature, groupAttack);
        this.speed = speed;
    }

    public void start() {
        super.start();
    }

    public void tick() {
        if (getHerd() != null) {
            Vec3 center = getHerd().computeCenter();

            Path path = getCreature().getNavigation().createPath(BlockPos.containing(center.x, center.y, center.z), 1);

            if (path != null)
                getCreature().getNavigation().moveTo(path, speed);
        }
    }

    public boolean canContinueToUse() {
        return getHerd() != null && getHerd().getDistanceFrom(getCreature()) >= 10 && getCreature().getTarget() == null;
    }

    public boolean canUse() {
        boolean herdIsFar = true;

        if (getHerd() != null) {
            herdIsFar = getHerd().getDistanceFrom(getCreature()) > 15;
        }

        return Math.random() < 0.25 && herdIsFar && getCreature().getTarget() == null;
    }
}
