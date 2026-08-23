package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class JurassiCraftAISwimming extends Goal {
    private EntityJurassiCraftSmart creature;
    private float bBoxPercentage;
    private float creatureStepHeight;

    public JurassiCraftAISwimming(EntityJurassiCraftSmart entity, float bBoxPercentage) {
        this.creature = entity;
        this.bBoxPercentage = bBoxPercentage;
        this.creatureStepHeight = this.creature.maxUpStep();
        if (entity.getNavigation() instanceof GroundPathNavigation)
            ((GroundPathNavigation) entity.getNavigation()).setCanFloat(true);
        this.setFlags(java.util.EnumSet.of(Goal.Flag.JUMP));
    }

    public boolean canUse() {
        return this.creature.isInWater() || this.creature.isInLava();
    }

    public void start() {
        this.creature.setSitting(false, null);
        this.creature.setMaxUpStep(this.bBoxPercentage * this.creature.getYBouningBox());
    }

    public void tick() {
        if (!this.creature.level().isEmptyBlock(new BlockPos((int) (this.creature.getX() + 0.5D), (int) (this.creature.getY() + this.bBoxPercentage * this.creature.getYBouningBox()), (int) (this.creature.getZ() + 0.5D))))
            this.creature.setDeltaMovement(new Vec3(this.creature.getDeltaMovement().x, 0.06D, this.creature.getDeltaMovement().z));
    }

    public void stop() {
        this.creature.setMaxUpStep(this.creatureStepHeight);
    }
}
