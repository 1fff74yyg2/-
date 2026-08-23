package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCoward;
import net.ilexiconn.jurassicraft.common.entity.ai.*;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIHypsilophodonPlaying;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIHypsilophodonScratchHead;
import net.ilexiconn.jurassicraft.common.entity.ai.animation.AnimationAIHypsilophodonSocializing;
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
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EntityHypsilophodon extends EntityJurassiCraftCoward implements IHerbivore {
    public ControlledAnimation sittingProgress = new ControlledAnimation(30);
    public ChainBuffer tailBuffer = new ChainBuffer(3);

    public EntityHypsilophodon(EntityType<? extends EntityHypsilophodon> type, Level world) {
        super(type, world);
    }

    public EntityHypsilophodon(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.25D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAISitNatural(this, 1000, 150, 350));
            this.goalSelector.addGoal(2, new AnimationAIHypsilophodonScratchHead(this));
            this.goalSelector.addGoal(2, new AnimationAIHypsilophodonPlaying(this));
            this.goalSelector.addGoal(2, new AnimationAIHypsilophodonSocializing(this));
            this.goalSelector.addGoal(2, new JurassiCraftAIPlayfulBaby(this, 300, 3.5D, 10.0D, 0.3F));
            this.goalSelector.addGoal(3, new JurassiCraftAIAvoidEntityIfNotTamed(this, Player.class, 6.5F, 0.9D * this.getCreatureSpeed(), 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(3, new AvoidEntityGoal(this, EntityHerrerasaurus.class, 12.0F, 1.0D * this.getCreatureSpeed(), 1.2D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAIFollowFood(this, 40, 1.1D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(4, new JurassiCraftAIEatDroppedFood(this, 16.0D));
            this.goalSelector.addGoal(4, new JurassiCraftAIEating(this, 20));
            this.goalSelector.addGoal(5, new JurassiCraftAIWander(this, 30, 0.8D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 7.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    
            this.goalSelector.addGoal(6, new HerdAIFollowHerd(this, false, getCreatureSpeed()));
            this.targetSelector.addGoal(1, new JurassiCraftAIFleeOwnerIsHurtByTarget(this));
            this.targetSelector.addGoal(2, new JurassiCraftAIFleeOwnerHurtsTarget(this));
            this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
            this.setCreatureExperiencePoints(800);
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

        this.tailBuffer.calculateChainSwingBuffer(60.0F, 5, 3.8F, this);
    }

    public List<EntityHypsilophodon> getHypsilophodonsNearby(double distanceX, double distanceY, double distanceZ) {
        List<Entity> nearbyEntities = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(distanceX, distanceY, distanceZ));

        ArrayList<EntityHypsilophodon> nearbyHypsilophodon = new ArrayList<EntityHypsilophodon>();

        for (Entity entityNeighbor : nearbyEntities) {
            if (entityNeighbor instanceof EntityHypsilophodon && entityNeighbor != this) {
                nearbyHypsilophodon.add((EntityHypsilophodon) entityNeighbor);
            }
        }

        return nearbyHypsilophodon;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        float developmentFraction = this.getGrowthStage() / 120.0F;

        int count = Math.round(1 + (1.5F * developmentFraction) + this.getRandom().nextInt(1 + (int) (2.0F * developmentFraction)) + this.getRandom().nextInt(1 + lootingLevel));

        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), count);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);

        if (this.isMale() && this.getRandom().nextFloat() < 0.25F)
            this.dropCreatureItem(this.getCreature().getSkin(), 1);
    }
}
