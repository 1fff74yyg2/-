package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.List;

public class JurassiCraftAIEatDroppedFood extends Goal {
    private EntityJurassiCraftSmart creature;
    private ItemEntity droppedFood;
    private double timeTryingToEat;
    private double searchDistance;

    public JurassiCraftAIEatDroppedFood(EntityJurassiCraftSmart entity, double distance) {
        this.creature = entity;
        this.searchDistance = distance;
        this.timeTryingToEat = 0;
    }

    public boolean canUse() {
        if (this.creature.getTarget() != null || this.creature.isSitting() || this.creature.isTakingOff() || this.creature.isFlying() || this.creature.isFleeing() || this.creature.isAttacking() || this.creature.isDefending() || this.creature.isEating() || this.creature.isDrinking()) {
            return false;
        } else if (this.creature.getRandom().nextInt(25) == 0) {
            List nearEntityList = this.creature.level().getEntities(this.creature, this.creature.getBoundingBox().inflate(this.searchDistance, this.searchDistance / 2.0D, this.searchDistance), e -> true);

            if (!nearEntityList.isEmpty()) {
                for (int i = nearEntityList.size() - 1; i > -1; i--) {
                    Entity item = (Entity) nearEntityList.get(i);

                    if (item instanceof ItemEntity) {
                        this.droppedFood = (ItemEntity) item;
                        if (this.creature.getCreature().isFavoriteFood(this.droppedFood.getItem().getItem())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void start() {
        this.creature.setDefending(false);
        this.creature.setAttacking(false);
        this.creature.setBreeding(false);
        this.creature.setPlaying(false);
        this.creature.setSocializing(false);
        this.creature.setEating(false);
        this.creature.setDrinking(false);
        this.creature.setSitting(false, null);
        this.creature.getNavigation().moveTo(droppedFood.getX(), droppedFood.getY(), droppedFood.getZ(), this.creature.getCreatureSpeed());
        this.timeTryingToEat = 0;

        super.start();
    }

    public void tick() {
        double distance = Math.sqrt(Math.pow(this.creature.getX() - this.droppedFood.getX(), 2.0D) + Math.pow(this.creature.getY() - this.droppedFood.getY(), 2.0D) + Math.pow(this.creature.getZ() - this.droppedFood.getZ(), 2.0D));

        if (distance < 1.2D) {
            this.droppedFood.discard();
            this.creature.setEating(true);
        } else {
            this.timeTryingToEat++;

            if (this.creature.getNavigation().isDone())
                this.creature.getNavigation().moveTo(this.droppedFood.getX(), this.droppedFood.getY(), this.droppedFood.getZ(), this.creature.getCreatureSpeed());
        }
    }

    public boolean canContinueToUse() {
        return this.timeTryingToEat < 125 && this.droppedFood.isAlive() && this.creature.isAlive() && !this.creature.isSitting() && this.creature.getPassengers().isEmpty() && !this.creature.isAttacking() && !this.creature.isDefending();
    }

    public void stop() {
        this.creature.getNavigation().stop();
        this.droppedFood = null;
        this.timeTryingToEat = 0;
    }
}