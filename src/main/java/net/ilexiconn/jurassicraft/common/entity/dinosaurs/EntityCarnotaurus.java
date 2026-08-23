package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftGroupAggressive;
import net.ilexiconn.jurassicraft.common.entity.IntermittentAnimation;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIGroupAttack;
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
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntityCarnotaurus extends EntityJurassiCraftGroupAggressive {
    public IntermittentAnimation restingHeadProgress = new IntermittentAnimation(30, 200, 100, 100);
    public ControlledAnimation sittingProgress = new ControlledAnimation(40);
    public ChainBuffer tailBuffer = new ChainBuffer(5);

    public EntityCarnotaurus(EntityType<? extends EntityCarnotaurus> type, Level world) {
        super(type, world);
    }

    public EntityCarnotaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIWander(this, 40, 0.8D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(4, new JurassiCraftAISitNatural(this, 800, 125, 400));
            this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20, true, JurassiCraftAnimationIDs.BITE.animID()));
            this.goalSelector.addGoal(7, new JurassiCraftAIFollowFood(this, 100, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(9, new HerdAIFollowHerd(this, true, getCreatureSpeed()));
            this.goalSelector.addGoal(9, new HerdAIGroupAttack(this));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityBrachiosaurus.class, 120, 0.7F, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityStegosaurus.class, 80, 0.6F, 0.7F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityTriceratops.class, 70, 0.6F, 0.7F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityGallimimus.class, 0, 0.6F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityHypsilophodon.class, 40, 0.55F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityLeaellynasaura.class, 40, 0.55F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityOviraptor.class, 0, 0.5F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Chicken.class, 0, 0.9F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Cow.class, 0, 0.9F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Pig.class, 0, 0.9F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Sheep.class, 0, 0.9F));
    
            this.setCreatureExperiencePoints(4000);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.95D * (double) (this.getYBouningBox() + 0.175F * (this.walkAnimation.speed() - this.walkAnimation.speed() * Mth.sin(0.55F * this.walkAnimation.position())));
    }

    @Override
    public void tick() {
        super.tick();

        /** Sitting Animation */
        if (this.level().isClientSide) {
            if (this.isSitting()) {
                this.sittingProgress.increaseTimer();
                this.restingHeadProgress.runAnimation();
            } else {
                this.sittingProgress.decreaseTimer();
                this.restingHeadProgress.stopAnimation();
            }
        }

        this.tailBuffer.calculateChainSwingBuffer(65.0F, 5, 4.0F, this);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int countMeat = Math.round(1 + (3.5F * developmentFraction) + this.getRandom().nextInt(1 + (int) (3.5F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), countMeat);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);

        if (this.getRandom().nextFloat() < 0.1F)
            this.dropCreatureItem(this.getCreature().getSkull(), 1);
    }
}
