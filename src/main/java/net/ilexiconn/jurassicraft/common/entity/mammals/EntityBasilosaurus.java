package net.ilexiconn.jurassicraft.common.entity.mammals;

import net.ilexiconn.jurassicraft.common.entity.EntitySwimmingNew;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityBasilosaurus extends EntitySwimmingNew {
    public EntityBasilosaurus(EntityType<? extends EntityBasilosaurus> type, Level world) {
        super(type, world);
    }

    public EntityBasilosaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.swimRadius = 16.0F;
            this.swimRadiusHeight = 10.0F;
            this.swimSpeed = (float) this.getCreatureSpeed();
            this.jumpOnLand = false;
            this.attackInterval = 1;
            this.isAgressive = true;
    
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIFollowFood(this, 100, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Squid.class, 100, 0.2F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Player.class, 100, 0.3F, 1.0F));
    
            this.setCreatureExperiencePoints(5000);
    }

    @Override
    protected Entity findEntityToAttack() {
        AABB area = this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);

        Player player = (Player) super.findEntityToAttack();
        if (player != null)
            return player;

        List<Animal> animals = this.level().getEntitiesOfClass(Animal.class, area);
        Entity nearestAnimal = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Animal animal : animals) {
            if (animal.getId() != this.getId()) {
                double distance = this.distanceToSqr(animal);

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestAnimal = animal;
                }
            }
        }

        return nearestAnimal;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (4.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (5.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);
    }
}
