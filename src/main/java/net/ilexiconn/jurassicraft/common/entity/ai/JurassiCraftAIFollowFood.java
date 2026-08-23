package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class JurassiCraftAIFollowFood extends Goal {
    private EntityJurassiCraftSmart creature;
    private Player temptingPlayer;
    private boolean avoidWater;
    private double speed;
    private int stealFoodChance;
    private int stealFoodTimer;

    public JurassiCraftAIFollowFood(EntityJurassiCraftSmart entity, int stealFoodChance, double velocity) {
        this.creature = entity;
        this.speed = velocity;
        this.stealFoodChance = stealFoodChance;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.creature.isSitting() || this.creature.isTakingOff() || this.creature.isFlying() || !this.creature.getPassengers().isEmpty() || this.creature.isSleeping() || this.creature.isEating() || this.creature.isDrinking()) {
            return false;
        } else {
            this.temptingPlayer = this.creature.level().getNearestPlayer(this.creature, 10.0D);

            if (this.temptingPlayer == null) {
                return false;
            } else {
                ItemStack stack = this.temptingPlayer.getMainHandItem();
                return stack == null ? false : (this.creature.getTarget() == null && this.creature.getCreature().isFavoriteFood(stack.getItem()));
            }
        }
    }

    public void start() {
        this.creature.setPlaying(false);
        this.creature.setSocializing(false);
        this.creature.setEating(false);
        this.creature.setDrinking(false);
        this.creature.setDefending(false);
        this.creature.setAttacking(false);
        this.creature.setBreeding(false);
        this.creature.setSitting(false, null);
        this.avoidWater = false;
        this.stealFoodTimer = 50;
    }

    public void tick() {
        this.creature.getLookControl().setLookAt(this.temptingPlayer, 30.0F, (float) this.creature.getMaxHeadXRot());

        double distance = this.creature.distanceToSqr(this.temptingPlayer);

        if (distance < 6.25D * this.creature.getGeneticQuality()) {
            this.creature.getNavigation().stop();

            if (distance < 5.5D && this.creature.getRandom().nextInt(this.stealFoodChance) == 0 && stealFoodTimer < 0) {
                this.stealFoodTimer = 50;
                ItemStack heldItem = this.temptingPlayer.getMainHandItem();
                heldItem.shrink(1);

                if (heldItem.getCount() < 1)
                    heldItem = null;
            } else {
                this.stealFoodTimer--;
            }
        } else {
            // 1.20.1: moveTo speed is a multiplier of the MOVEMENT_SPEED attribute.
            // getCreatureSpeed() now returns 1.0, so this.speed (1.0-1.2) is the
            // intended relative chase speed.
            this.creature.getNavigation().moveTo(this.temptingPlayer, this.speed);
        }
    }

    public boolean canContinueToUse() {
        ItemStack stack = this.temptingPlayer.getMainHandItem();

        return this.creature.isAlive() && this.temptingPlayer.isAlive() && !this.creature.isSitting() && !this.creature.hasBeenHurt() && this.creature.getPassengers().isEmpty() && (stack != null && this.creature.getCreature().isFavoriteFood(stack.getItem()));
    }

    public void stop() {
        this.temptingPlayer = null;
        this.creature.getNavigation().stop();
    }
}
