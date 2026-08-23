package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftAggressive;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftProtective;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
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

public class EntityStegosaurus extends EntityJurassiCraftProtective implements IHerbivore {
    public ControlledAnimation tailWhipPosition = new ControlledAnimation(30);
    public ChainBuffer tailBuffer = new ChainBuffer(5);

    public EntityStegosaurus(EntityType<? extends EntityStegosaurus> type, Level world) {
        super(type, world);
    }

    public EntityStegosaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 200));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(1, new JurassiCraftAIWander(this, 45, 0.7D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(3, new JurassiCraftAIDefensiveReaction(this, 8.0D, 30.0D, true, JurassiCraftAnimationIDs.TAIL_WHIP.animID(), false));
            this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(5, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(6, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(7, new JurassiCraftAIFollowFood(this, 50, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(8, new AvoidEntityGoal(this, EntityTyrannosaurus.class, 12.0F, this.getCreatureSpeed(), 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(11, new JurassiCraftAIHerdBehavior(this, 128, 2500, 24, this.getCreatureSpeed()));
            this.goalSelector.addGoal(11, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
    
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    
            this.setCreatureExperiencePoints(4000);
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

        if (this.isDefending() && this.creatureToAttack != null) {
            this.tailWhipPosition.increaseTimer();

            if (this.creatureToAttack != null && this.getAnimationId() != JurassiCraftAnimationIDs.TAIL_WHIP.animID()) {
                this.setYRot(this.getYRot() + (this.creatureToAttack.getYRot() - this.getYRot()) / 10.0F);
                this.yBodyRot = this.getYRot() + 3.14159265359F;
            }

            if (this.getRandom().nextInt(60) == 0) {
                this.creatureToAttack = this.getClosestEntityAggressive(this, 20, 8, 20);
                if (this.creatureToAttack != null)
                    this.setDefending(((EntityJurassiCraftAggressive) this.creatureToAttack).isCreatureOlderThan(0.5F));
            }
        } else {
            this.tailWhipPosition.decreaseTimer();

            if (this.getRandom().nextInt(35) == 0 && this.isCreatureOlderThan(0.5F)) {
                this.creatureToAttack = this.getClosestEntityAggressive(this, 20, 8, 20);

                if (this.creatureToAttack != null)
                    this.setDefending(((EntityJurassiCraftAggressive) this.creatureToAttack).isCreatureOlderThan(0.5F));
            }
        }

        this.tailBuffer.calculateChainSwingBuffer(45.0F, 5, 3.0F, this);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.getRandom().nextInt(3) == 0 && this.animID == 0)
            AnimationHandler.sendAnimationPacket(this, JurassiCraftAnimationIDs.TAIL_WHIP.animID());

        // The actual damage is always dealt by the Protective implementation.
        return super.doHurtTarget(entity);
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
