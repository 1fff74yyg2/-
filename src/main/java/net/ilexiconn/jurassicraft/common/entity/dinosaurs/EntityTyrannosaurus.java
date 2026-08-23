package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.client.model.base.ControlledParam;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftAggressive;
import net.ilexiconn.jurassicraft.common.entity.IntermittentAnimation;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIRoar;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAITyrannosaurusEatingGallimimus;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIWalkRoar;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIGroupAttack;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityLeptictidium;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityMoeritherium;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntityTyrannosaurus extends EntityJurassiCraftAggressive {
    public ControlledParam roarCount = new ControlledParam(0F, 0F, 0.5F, 0F);
    public ControlledParam roarTiltDegree = new ControlledParam(0F, 0F, 1F, 0F);
    public IntermittentAnimation restingHeadProgress = new IntermittentAnimation(30, 200, 100, 100);
    public ControlledAnimation sittingProgress = new ControlledAnimation(50);
    public ControlledAnimation shakePrey = new ControlledAnimation(10);
    public ChainBuffer tailBuffer = new ChainBuffer(6);
    private int stepCount = 0;

    public EntityTyrannosaurus(EntityType<? extends EntityTyrannosaurus> type, Level world) {
        super(type, world);
    }

    public EntityTyrannosaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(0, new AnimationAITyrannosaurusEatingGallimimus(this));
            this.goalSelector.addGoal(2, new AnimationAIRoar(this, 75));
            this.goalSelector.addGoal(8, new AnimationAIWalkRoar(this, 75));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.4F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(3, new JurassiCraftAIWander(this, 40, this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAISitNatural(this, 800, 125, 400));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20, true, JurassiCraftAnimationIDs.BITE.animID()));
            this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new JurassiCraftAIFollowFood(this, 100, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(7, new HerdAIFollowHerd(this, true, getCreatureSpeed()));
            this.goalSelector.addGoal(7, new HerdAIGroupAttack(this));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityBrachiosaurus.class, 120, 0.7F, 0.7F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityStegosaurus.class, 80, 0.6F, 0.9F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityTriceratops.class, 70, 0.6F, 0.9F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityParasaurolophus.class, 40, 0.55F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityGallimimus.class, 40, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityHypsilophodon.class, 40, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityLeaellynasaura.class, 40, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityMoeritherium.class, 40, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityOviraptor.class, 40, 0.3F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityLeptictidium.class, 40, 0.3F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Horse.class, 50, 0.25F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Cow.class, 30, 0.2F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Pig.class, 30, 0.15F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Sheep.class, 30, 0.2F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Chicken.class, 10, 0.1F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Player.class, 40, 0.3F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntitySpinosaurus.class, 40, 0.7F));
    
            this.setCreatureExperiencePoints(5500);    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (passenger != null) {
            if (passenger instanceof EntityGallimimus) {
                this.setYRot(this.yBodyRot);
                EntityGallimimus gallimimus = (EntityGallimimus) passenger;

                gallimimus.yHeadRot = passenger.getYRot();

                float shakeProgress = shakePrey.getAnimationProgressSinSqrt();
                float radius = 0.4F * this.getCreatureLength();
                float angle = (float) (0.01745329251F * this.yBodyRot + (0.05 * this.getCreatureLength() * shakeProgress * Math.cos(frame * 0.6 + 1)));
                passenger.setYRot((float) (angle * (180 / Math.PI) - 150.0F));

                gallimimus.yBodyRot = (float) (angle * (180 / Math.PI) - 150.0F);
                double extraY = this.getCreatureHeight() * (0.425 - shakeProgress * 0.21);

                if (getAnimationTick() > 30) {
                    extraY += 0.38 * Math.sin((getAnimationTick() - 30) * 0.2) * getCreatureHeight();
                    radius -= 0.001 * (getAnimationTick() - 30) * (getAnimationTick() - 30) * this.getCreatureLength();
                }

                double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle)));
                double extraZ = (double) (radius * Mth.cos(angle));
                moveFunction.accept(passenger, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
            } else {
                moveFunction.accept(passenger, this.getX(), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.getZ());
            }
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        int sound = this.getRandom().nextInt(4) + 1;

        if (sound == 1 && this.getCreatureAgeInDays() >= 25) {
            this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:tyrannosaurus01")), 5.0F, this.getSoundPitch());

            if (animID == 0) {
                if (this.zza == 0) {
                    if (!this.isSitting())
                        AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.ROAR.animID());
                } else
                    AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.WALK_ROAR.animID());
            }

            return null;
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.roarCount.update();
        this.roarTiltDegree.update();

        /** Step Sound */
        if (this.zza > 0 && this.stepCount <= 0 && this.getCreatureAgeInDays() >= 25) {
            this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:footstep")), 2.0F, this.getSoundPitch());
            stepCount = 65;
        }

        if (animID == JurassiCraftAnimationIDs.ROAR.animID() && animTick == 22)
            this.roarTiltDegree.thereAndBack(0F, 0.1F, 1F, 20);

        if (animID == JurassiCraftAnimationIDs.WALK_ROAR.animID() && animTick == 22)
            this.roarTiltDegree.thereAndBack(0F, 0.1F, 1F, 20);

        this.stepCount -= this.zza * 9.5;

        if (this.frame % 62 == 28)
            this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:tyrannosaurusbreath")), 1.0F, this.getSoundPitch());

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

        this.tailBuffer.calculateChainSwingBuffer(55.0F, 5, 3.0F, this);

        if (this.getPassengers().contains(this.getTarget()))
            this.setTarget(null);

        if (getAnimationId() == JurassiCraftAnimationIDs.EATING.animID() && getAnimationTick() <= 20)
            shakePrey.increaseTimer();

        if (getAnimationId() == JurassiCraftAnimationIDs.EATING.animID() && getAnimationTick() > 20)
            shakePrey.decreaseTimer();
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int countMeat = Math.round(1 + (5.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (3.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));
        int countTeeth = Math.round(1.5F * developmentFraction + this.getRandom().nextInt(1 + (int) (2.0F * developmentFraction)));

        if (!this.isOnFire()) {
            this.dropCreatureItem(this.getCreature().getMeat(), countMeat);
            this.dropCreatureItem(this.getCreature().getTooth(), 1);
        } else {
            this.spawnCreatureItem(this.getCreature().getSteak(), countMeat);
            this.dropCreatureItem(this.getCreature().getTooth(), countTeeth);
        }

        if (this.getRandom().nextFloat() < 0.1F)
            this.dropCreatureItem(this.getCreature().getSkull(), 1);

        if (this.isMale() && this.getRandom().nextFloat() < 0.25F)
            this.dropCreatureItem(this.getCreature().getSkin(), 1);
    }
}
