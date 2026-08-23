package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.client.model.base.ControlledParam;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftAggressive;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIRoar;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIWalkRoar;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIGroupAttack;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityLeptictidium;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityMoeritherium;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

public class EntitySpinosaurus extends EntityJurassiCraftAggressive {
    public ControlledParam roarCount = new ControlledParam(0F, 0F, 0.5F, 0F);
    public ControlledParam roarTiltDegree = new ControlledParam(0F, 0F, 1F, 0F);
    public ChainBuffer tailBuffer = new ChainBuffer(6);

    private int stepCount = 0;

    public EntitySpinosaurus(EntityType<? extends EntitySpinosaurus> type, Level world) {
        super(type, world);
    }

    public EntitySpinosaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(2, new AnimationAIRoar(this, 75));
            this.goalSelector.addGoal(8, new AnimationAIWalkRoar(this, 75));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.4F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(3, new JurassiCraftAIWander(this, 40, this.getCreatureSpeed()));
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
            this.targetSelector.addGoal(3, new JurassiCraftAITargetIfHasAgeAndNonTamed(this, EntityTyrannosaurus.class, 40, 0.7F));
    
            this.setCreatureExperiencePoints(5500);
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

        this.stepCount -= this.zza * 9.5;

        this.tailBuffer.calculateChainSwingBuffer(55.0F, 5, 3.0F, this);
    }
}
