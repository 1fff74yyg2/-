package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftProtective;
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
import net.minecraft.world.level.Level;

public class EntityAnkylosaurus extends EntityJurassiCraftProtective implements IHerbivore {
    public ChainBuffer tailBuffer = new ChainBuffer(5);

    public EntityAnkylosaurus(EntityType<? extends EntityAnkylosaurus> type, Level world) {
        super(type, world);
    }

    public EntityAnkylosaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 200));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(2, new JurassiCraftAISit(this));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(4, new JurassiCraftAIFollowFood(this, 50, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(4, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(5, new JurassiCraftAIWander(this, 45, 0.7D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 5.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(6, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
            this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.setCreatureExperiencePoints(3500);    }

    public int getNumberOfAllies() {
        return 1;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 1.05D * (double) this.getYBouningBox();
    }

    public int getTalkInterval() {
        return 350;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;

        int count = Math.round(1 + (3.0F * developmentFraction) + this.getRandom().nextInt(1 + (int) (3.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);

        if (this.isMale() && this.getRandom().nextFloat() < 0.25F)
            this.dropCreatureItem(this.getCreature().getSkin(), 1);
    }

    @Override
    public void tick() {
        super.tick();

        this.tailBuffer.calculateChainSwingBuffer(45.0F, 5, 4.0F, this);
    }
}
