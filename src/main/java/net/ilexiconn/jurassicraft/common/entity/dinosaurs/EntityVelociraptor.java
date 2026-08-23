package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftGroupAggressive;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIRoar;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIVelociraptorLeap;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIVelociraptorSniff;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIVelociraptorTwitchHead;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIGroupAttack;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityLeptictidium;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityMoeritherium;
import net.ilexiconn.jurassicraft.common.handler.AnimationHandler;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

public class EntityVelociraptor extends EntityJurassiCraftGroupAggressive {
    public ControlledAnimation sittingProgress = new ControlledAnimation(35);
    public ChainBuffer tailBuffer = new ChainBuffer(6);
    public boolean leaping = false;
    public int timeSinceLeap;

    public EntityVelociraptor(EntityType<? extends EntityVelociraptor> type, Level world) {
        super(type, world);
    }

    public EntityVelociraptor(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new AnimationAIVelociraptorLeap(this));
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(3, new JurassiCraftAIWander(this, 40, 0.8D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAISitNatural(this, 1000, 150, 350));
            this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIFollowFood(this, 100, 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20, true, JurassiCraftAnimationIDs.BITE.animID()));
            this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(7, new AnimationAIVelociraptorTwitchHead(this));
            this.goalSelector.addGoal(7, new AnimationAIVelociraptorSniff(this));
            this.goalSelector.addGoal(7, new AnimationAIRoar(this, 20));
            this.goalSelector.addGoal(7, new HerdAIGroupAttack(this));
            // this.goalSelector.addGoal(7, new HerdAISurprise(this)); // WIP
            this.goalSelector.addGoal(7, new HerdAIVelociraptorFollowHerd(this, getCreatureSpeed()));
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityBrachiosaurus.class, 60, 0.9F, 0.15F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityTriceratops.class, 60, 0.9F, 0.25F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityStegosaurus.class, 60, 0.9F, 0.25F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityGallimimus.class, 60, 0.7F, 0.7F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityHypsilophodon.class, 50, 0.5F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityMoeritherium.class, 50, 0.5F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityLeptictidium.class, 50, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityLeaellynasaura.class, 50, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityOviraptor.class, 50, 0.4F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Horse.class, 50, 0.6F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Cow.class, 50, 0.6F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Pig.class, 30, 0.6F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Sheep.class, 30, 0.6F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Chicken.class, 20, 0.2F));
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, Player.class, 50, 0.5F));
    
            this.setCreatureExperiencePoints(2500);
    }

    @Override
    public void aiStep() {
        // Leap AI
        // float distanceFromTarget;
        // if (getTarget() != null)
        // {
        // distanceFromTarget = (float) Math.sqrt(Math.pow((posX - getTarget().getX()), 2) + Math.pow((posZ - getTarget().getZ()), 2));
        // }
        // else
        // {
        // distanceFromTarget = -1;
        // }
        if (distanceFromTarget >= 5 && distanceFromTarget <= 6 && this.onGround() && this.timeSinceLeap == 0 && this.animID == 0)
            AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.LEAP.animID());

        if (this.onGround())
            setLeaping(false);

        if (timeSinceLeap != 0)
            timeSinceLeap--;

        // Misc
        super.aiStep();
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

        this.tailBuffer.calculateChainSwingBuffer(68.0F, 5, 4.0F, this);
    }

    public void setLeaping(boolean flag) {
        this.leaping = flag;
    }

    @Override
    public SoundEvent getAmbientSound() {
        if (this.animID == 0)
            AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.ROAR.animID());

        return SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:velociraptorhiss"));
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (3.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (2.5F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);

        if (this.isMale() && this.getRandom().nextFloat() < 0.25F)
            this.dropCreatureItem(this.getCreature().getSkin(), 1);
    }
}
