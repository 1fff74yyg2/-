package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.client.model.base.ControlledParam;
import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftProtective;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIParasaurolophusTrumpet;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EntityParasaurolophus extends EntityJurassiCraftProtective implements IHerbivore {
    public ChainBuffer tailBuffer = new ChainBuffer(6);
    public ControlledParam walkLean = new ControlledParam(0, 0, (float) Math.PI / 2, 0);
    public int timeUntilCanCall = 0;

    public EntityParasaurolophus(EntityType<? extends EntityParasaurolophus> type, Level world) {
        super(type, world);
    }

    public EntityParasaurolophus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 200));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(2, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(2, new AnimationAIParasaurolophusTrumpet(this));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(4, new JurassiCraftAIFollowFood(this, 50, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(4, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(5, new JurassiCraftAIWander(this, 45, 0.7D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(5, new AvoidEntityGoal(this, EntityTyrannosaurus.class, 12.0F, this.getCreatureSpeed(), 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(7, new JurassiCraftAIHerdBehavior(this, 128, 2500, 24, this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.setCreatureExperiencePoints(1800);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 1.1D * (double) this.getYBouningBox();
    }

    public int getNumberOfAllies() {
        return 1;
    }

    public int getTalkInterval() {
        return 350;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.zza != 0)
            this.walkLean.change = 0.1F;

        if (this.zza == 0)
            this.walkLean.change = -0.1F;

        this.walkLean.update();

        if (timeUntilCanCall > 0)
            timeUntilCanCall--;

        this.tailBuffer.calculateChainSwingBuffer(48.0F, 3, 5.0F, this);
    }

    @Override
    public SoundEvent getAmbientSound() {
        int sound = this.getRandom().nextInt(3);

        if (sound <= 1) {
            this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:" + this.getCreatureName().toLowerCase())), this.getSoundVolume(), this.getSoundPitch());
            return null;
        } else {
            if (timeUntilCanCall == 0 && animID == 0)
                AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.TRUMPET.animID());

            return null;
        }
    }

    public List<EntityParasaurolophus> getParasaurolophusNearby(double distanceX, double distanceY, double distanceZ) {
        List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        ArrayList<EntityParasaurolophus> listParasaurolophus = new ArrayList<EntityParasaurolophus>();

        for (Entity entityNeighbor : list) {
            if (entityNeighbor instanceof EntityParasaurolophus && entityNeighbor != this)
                listParasaurolophus.add((EntityParasaurolophus) entityNeighbor);
        }

        return listParasaurolophus;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (4.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (4.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);

        if (this.getRandom().nextFloat() < 0.1F)
            this.dropCreatureItem(this.getCreature().getSkull(), 1);

        if (this.isMale() && this.getRandom().nextFloat() < 0.25F)
            this.dropCreatureItem(this.getCreature().getSkin(), 1);
    }
}
