package net.ilexiconn.jurassicraft.common.entity.reptiles;

import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftAggressive;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityGallimimus;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityOviraptor;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityLeptictidium;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityMoeritherium;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityDeinosuchus extends EntityJurassiCraftAggressive {
    public ChainBuffer tailBuffer = new ChainBuffer(5);

    public EntityDeinosuchus(EntityType<? extends EntityDeinosuchus> type, Level world) {
        super(type, world);
    }

    public EntityDeinosuchus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(3, new JurassiCraftAIWander(this, 40, this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIFollowFood(this, 100, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityGallimimus.class, 40, 0.4F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityMoeritherium.class, 40, 0.4F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityOviraptor.class, 40, 0.3F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityLeptictidium.class, 40, 0.3F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Horse.class, 50, 0.25F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Cow.class, 30, 0.2F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Pig.class, 30, 0.15F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Sheep.class, 30, 0.2F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Chicken.class, 10, 0.1F, 1.0F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Player.class, 40, 0.3F, 1.0F));
    
            this.setCreatureExperiencePoints(4000);
    }

    public int getTalkInterval() {
        return 350;
    }

    @Override
    public void tick() {
        super.tick();

        this.tailBuffer.calculateChainSwingBuffer(45.0F, 5, 2.5F, this);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (3.5F * developmentFraction) + this.getRandom().nextInt(1 + (int) (3.5F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);
    }
}
