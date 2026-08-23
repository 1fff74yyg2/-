package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.reptiles.EntityCearadactylus;
import net.minecraft.world.entity.ai.goal.Goal;

public class EntityAICearadactylus extends Goal {
    private EntityCearadactylus theCearadactylus;
    private boolean isFlying;

    public EntityAICearadactylus(EntityCearadactylus dactylus) {
        this.theCearadactylus = dactylus;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return this.isFlying;
    }

    public void start() {
        this.theCearadactylus.getNavigation().stop();
        this.theCearadactylus.setFlying(true);
    }

    public void stop() {
        this.theCearadactylus.setFlying(false);
    }

    public void setFlying(boolean flying) {
        this.isFlying = flying;
    }
}