package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.client.model.base.ControlledParam;
import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftAggressive;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCharges;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntityTriceratops extends EntityJurassiCraftCharges implements IHerbivore {
    public ControlledParam flailDegree = new ControlledParam(0.0F, 0.0F, 1.0F, 0.0F);
    public ControlledAnimation defendingPosition = new ControlledAnimation(40);
    public ChainBuffer tailBuffer = new ChainBuffer(5);

    public EntityTriceratops(EntityType<? extends EntityTriceratops> type, Level world) {
        super(type, world);
    }

    public EntityTriceratops(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 200));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(1, new JurassiCraftAIWander(this, 45, 0.8D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(2, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(4, new JurassiCraftAIDefensiveReaction(this, 16.0D, 36.0D, true, JurassiCraftAnimationIDs.CHARGE.animID(), true));
            this.goalSelector.addGoal(5, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(6, new JurassiCraftAIFollowFood(this, 50, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(7, new AvoidEntityGoal(this, EntityTyrannosaurus.class, 12.0F, this.getCreatureSpeed(), 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(10, new JurassiCraftAIHerdBehavior(this, 96, 2000, 20, 0.7D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(10, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    
            this.setCreatureExperiencePoints(3500);
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getAnimationId() == JurassiCraftAnimationIDs.CHARGE.animID()) {
            if (this.getAnimationTick() < 5) {
                float animationProgress = (float) this.getAnimationTick() / 5.0F;
                return 0.91D * (double) this.getYBouningBox() - (0.3F * Mth.sin(animationProgress));
            } else if (this.getAnimationTick() < 18) {
                float animationProgress = (float) (this.getAnimationTick() - 5) / 13.0F;
                return 0.91D * (double) this.getYBouningBox() + (0.6F * Mth.sin(animationProgress));
            } else if (this.getAnimationTick() < 39) {
                float animationProgress = (float) (this.getAnimationTick() - 18) / 21.0F;
                return 0.91D * (double) this.getYBouningBox() - (0.5F * Mth.sin(animationProgress));
            }
        }

        return 0.91D * (double) this.getYBouningBox();
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
        this.flailDegree.update();

        if (this.animID == JurassiCraftAnimationIDs.CHARGE.animID() && this.animTick == 1)
            this.flailDegree.thereAndBack(0F, 0.1F, 1F, 5);

        if (this.isDefending()) {
            this.defendingPosition.increaseTimer();

            if (this.creatureToAttack != null) {
                float yaw = this.creatureToAttack.getYRot() + 3.14159265359F;
                this.setYRot(yaw);
                this.yBodyRot = yaw;
            }
        } else {
            this.defendingPosition.decreaseTimer(2);

            if (this.getRandom().nextInt(40) == 0 && this.isCreatureOlderThan(0.6F)) {
                this.creatureToAttack = this.getClosestEntityAggressive(this, 20, 8, 20);

                if (this.creatureToAttack != null)
                    this.setDefending(((EntityJurassiCraftAggressive) this.creatureToAttack).isCreatureOlderThan(0.5F));
            }
        }

        this.tailBuffer.calculateChainSwingBuffer(40.0F, 5, 3.0F, this);
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
