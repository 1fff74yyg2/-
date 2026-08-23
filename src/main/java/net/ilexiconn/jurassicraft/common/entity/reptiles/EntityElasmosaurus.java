package net.ilexiconn.jurassicraft.common.entity.reptiles;

import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSwimmingBase;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.item.ItemMeat;
import net.ilexiconn.jurassicraft.common.item.ItemSkin;
import net.ilexiconn.jurassicraft.common.item.ItemSkull;
import net.ilexiconn.jurassicraft.common.item.ItemSteak;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityElasmosaurus extends EntityJurassiCraftSwimmingBase {
    public ChainBuffer tailBuffer = new ChainBuffer(12);

    public EntityElasmosaurus(EntityType<? extends EntityElasmosaurus> type, Level world) {
        super(type, world);
    }

    public EntityElasmosaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.swimSpeed = 2.2F;
            huntingInterval = 600;
            this.setHungry(huntingInterval);
    
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIFollowFood(this, 100, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Squid.class, 100, 0.15F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Player.class, 100, 0.25F, 1.0F));
    
            this.setCreatureExperiencePoints(5000);
    }

    @Override
    public void tick() {
        super.tick();
        this.tailBuffer.calculateChainSwingBuffer(120.0F, 5, 8.0F, this);
    }

    @Override
    public LivingEntity getTargetPriority(LivingEntity target, LivingEntity entity) {
        if (target != null) {
            if (target instanceof Player)
                return target;
            else if (target instanceof EntityElasmosaurus) // Won't go for other Elasmosaurus unless nothing else around
                return entity;
            else
                return target;
        } else
            return entity;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (5.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (5.5F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        Creature creature = this.getCreature();

        if (!this.isOnFire()) {
            ItemMeat meat = creature.getMeat();

            if (meat != null)
                this.dropItemStackWithGenetics(new ItemStack(meat, count));
            else
                System.err.println("Meat was null for Elasmosaurus!");
        } else {
            ItemSteak steak = creature.getSteak();

            if (steak != null)
                this.spawnAtLocation(new ItemStack(steak));
            else
                System.err.println("Steak was null for Elasmosaurus!");
        }

        if (this.getRandom().nextFloat() < 0.1F) {
            ItemSkull skull = creature.getSkull();

            if (skull != null)
                this.dropItemStackWithGenetics(new ItemStack(skull));
            else
                System.err.println("Skull was null for Elasmosaurus!");
        }

        if (this.isMale() && this.getRandom().nextFloat() < 0.25F) {
            ItemSkin skin = creature.getSkin();

            if (skin != null)
                this.dropItemStackWithGenetics(new ItemStack(skin));
            else
                System.err.println("Skin was null for Elasmosaurus!");
        }
    }
}
