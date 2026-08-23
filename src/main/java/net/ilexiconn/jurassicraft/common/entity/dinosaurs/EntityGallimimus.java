package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftProtective;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIGallimimusBeingEaten;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntityGallimimus extends EntityJurassiCraftProtective implements IHerbivore {
    public ControlledAnimation sittingProgress = new ControlledAnimation(40);
    public ChainBuffer tailBuffer = new ChainBuffer(4);
    public float swallowScale = 1;

    public EntityGallimimus(EntityType<? extends EntityGallimimus> type, Level world) {
        super(type, world);
    }

    public EntityGallimimus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 150));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 80, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAISitNatural(this, 900, 175, 375));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(4, new JurassiCraftAIFollowFood(this, 30, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(4, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(4, new AnimationAIGallimimusBeingEaten(this));
            this.goalSelector.addGoal(5, new JurassiCraftAIWander(this, 30, 0.7D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(5, new AvoidEntityGoal(this, EntityTyrannosaurus.class, 12.0F, this.getCreatureSpeed(), 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(7, new JurassiCraftAIHerdBehavior(this, 128, 2500, 24, this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    
            this.setCreatureExperiencePoints(1000);
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        float creatureSizeParam = 0.25F * this.getCreatureLength();
        float extraX = creatureSizeParam * Mth.sin(3.14159265359F + 0.01745329251F * this.getYRot());
        float extraZ = creatureSizeParam * Mth.cos(0.01745329251F * this.getYRot());
        float extraY = 1.2F * this.getYBouningBox() + 0.16F * (this.walkAnimation.speed() - this.walkAnimation.speed() * Mth.sin(this.walkAnimation.position()));

        moveFunction.accept(passenger, this.getX() - (double) extraX, this.getY() + (double) extraY, this.getZ() - (double) extraZ);
    }

    public int getNumberOfAllies() {
        return 2;
    }

    public int getTalkInterval() {
        return 350;
    }

    @Override
    public void tick() {
        super.tick();

        /** Sitting Animation */
        if (this.level().isClientSide) {
            if (this.isSitting())
                this.sittingProgress.increaseTimer();
            else
                this.sittingProgress.decreaseTimer();
        }

        this.tailBuffer.calculateChainSwingBuffer(45.0F, 3, 3.8F, this);

        if (getAnimationId() == JurassiCraftAnimationIDs.BEING_EATEN.animID() && getAnimationTick() >= 35 && swallowScale > 0)
            swallowScale -= 0.1;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.getVehicle() instanceof EntityTyrannosaurus) {
            if (this.getTarget() == this.getVehicle())
                this.setTarget(null);

            return false;
        } else {
            return super.doHurtTarget(entity);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (2.5F * developmentFraction) + this.getRandom().nextInt(1 + (int) (2.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);
    }
}
