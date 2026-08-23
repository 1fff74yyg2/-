package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftFlyingCreature;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class JurassiCraftAISelfFlying extends Goal {
    private final EntityJurassiCraftFlyingCreature creature;
    private float minimumAgeToFly;
    private int flyingChance;
    private int height;

    public JurassiCraftAISelfFlying(EntityJurassiCraftFlyingCreature entity, int flyingChance, float minimumAgeToFly) {
        this.creature = entity;
        this.flyingChance = flyingChance;
        this.minimumAgeToFly = minimumAgeToFly;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.creature.getRandom().nextInt(this.flyingChance) == 0)
            if (this.creature.getPassengers().isEmpty())
                if (!this.creature.isFlying())
                    if (this.creature.onGround() && !this.creature.isSitting() && !this.creature.isSleeping() && !this.creature.isTakingOff())
                        return this.creature.isCreatureOlderThan(this.minimumAgeToFly);
        return false;
    }

    public void start() {
        if (this.creature.isSleeping())
            this.creature.setSleeping(false);
        if (this.creature.isSitting())
            this.creature.setSitting(false, null);
        if (this.creature.isTakingOff())
            this.creature.setTakingOff(false);
        if (this.creature.isEating())
            this.creature.setEating(false);
        if (this.creature.isDrinking())
            this.creature.setDrinking(false);
        if (this.creature.isBreeding())
            this.creature.setBreeding(false);

        this.creature.setFlying(true);
        this.goUp();
    }

    public void tick() {
        if (this.creature.getRandom().nextBoolean())
            this.height = this.getHeight();

        if (this.height < 6)
            this.goUp();

        if (this.creature.getRandom().nextInt(200) == 0)
            this.creature.getNavigation().createPath(this.creature.getX() + this.creature.getRandom().nextInt(20), this.creature.getY() + this.creature.getRandom().nextInt(5), this.creature.getZ() + this.creature.getRandom().nextInt(20), 1);
        else if (this.creature.getRandom().nextInt(200) == 0)
            this.creature.getNavigation().createPath(this.creature.getX() - this.creature.getRandom().nextInt(20), this.creature.getY() + this.creature.getRandom().nextInt(5), this.creature.getZ() + this.creature.getRandom().nextInt(20), 1);
        else if (this.creature.getRandom().nextInt(200) == 0)
            this.creature.getNavigation().createPath(this.creature.getX() + this.creature.getRandom().nextInt(20), this.creature.getY() + this.creature.getRandom().nextInt(5), this.creature.getZ() - this.creature.getRandom().nextInt(20), 1);
        else if (this.creature.getRandom().nextInt(200) == 0)
            this.creature.getNavigation().createPath(this.creature.getX() - this.creature.getRandom().nextInt(20), this.creature.getY() + this.creature.getRandom().nextInt(5), this.creature.getZ() - this.creature.getRandom().nextInt(20), 1);
    }

    public boolean canContinueToUse() {
        return !creature.onGround();
    }

    public void stop() {
        super.stop();
        this.creature.setFlying(false);
        this.height = 0;
    }

    public int getHeight() {
        return (int) this.creature.getY() - this.creature.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int) this.creature.getX(), (int) this.creature.getZ());
    }

    private void goUp() {
        this.creature.setDeltaMovement(new Vec3((double) (1.2F * this.creature.getMountingSpeed() * Mth.sin(3.14159265359F + 0.01745329251F * this.creature.yBodyRot)), this.creature.getDeltaMovement().y, this.creature.getDeltaMovement().z));
        this.creature.setDeltaMovement(new Vec3(this.creature.getDeltaMovement().x, this.creature.getDeltaMovement().y, (double) (1.2F * this.creature.getMountingSpeed() * Mth.cos(0.01745329251F * this.creature.yBodyRot))));
        this.creature.setDeltaMovement(new Vec3(this.creature.getDeltaMovement().x, 0.5F, this.creature.getDeltaMovement().z));
    }
}
