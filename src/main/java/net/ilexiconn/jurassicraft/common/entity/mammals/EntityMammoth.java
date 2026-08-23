package net.ilexiconn.jurassicraft.common.entity.mammals;

import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftProtective;
import net.ilexiconn.jurassicraft.common.entity.IntermittentAnimation;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIBite;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIRoar;
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
import net.minecraft.world.level.Level;

public class EntityMammoth extends EntityJurassiCraftProtective implements IHerbivore {
    public IntermittentAnimation trunkLift = new IntermittentAnimation(20, 30, 10, 1);
    public IntermittentAnimation trunkSwing = new IntermittentAnimation(20, 50, 10, 1);
    public IntermittentAnimation earFlap = new IntermittentAnimation(20, 20, 10, 1);
    public IntermittentAnimation tailSwing = new IntermittentAnimation(20, 30, 10, 1);

    public EntityMammoth(EntityType<? extends EntityMammoth> type, Level world) {
        super(type, world);
    }

    public EntityMammoth(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(2, new AnimationAIBite(this, 24));
            this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 200));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(2, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(4, new JurassiCraftAIFollowFood(this, 50, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(4, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(5, new JurassiCraftAIWander(this, 45, 0.7D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(7, new AnimationAIRoar(this, 40));
            this.goalSelector.addGoal(7, new JurassiCraftAIHerdBehavior(this, 128, 2500, 24, this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    
            this.setCreatureExperiencePoints(3500);
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getYBouningBox() + 0.5;
    }

    public int getNumberOfAllies() {
        return 1;
    }

    public int getTalkInterval() {
        return 400;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;
        int count = Math.round(1 + (3.5F * developmentFraction) + this.getRandom().nextInt(1 + (int) (4.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);
    }

    @Override
    public void tick() {
        super.tick();

        if (trunkSwing.getTimer() == 0)
            trunkLift.runAnimation();

        if (trunkLift.getTimer() == 0)
            trunkSwing.runAnimation();

        earFlap.runAnimation();
        tailSwing.runAnimation();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        // Trigger the bite animation; the actual damage is dealt by the
        // Protective implementation (super.doHurtTarget).
        if (this.animID == 0)
            AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.BITE.animID());

        return super.doHurtTarget(entity);
    }
}
