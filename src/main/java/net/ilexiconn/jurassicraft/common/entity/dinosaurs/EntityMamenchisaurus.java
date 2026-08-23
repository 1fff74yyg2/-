package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.api.IHerbivore;
import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftProtective;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIAngry;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIEatDroppedFood;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIEating;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIFlee;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIFollowFood;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIHerdBehavior;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIOwnerHurtsTarget;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIOwnerIsHurtByTarget;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAISit;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIWander;
import net.ilexiconn.jurassicraft.common.entity.ai.herds.HerdAIFollowHerd;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntityMamenchisaurus extends EntityJurassiCraftProtective implements IHerbivore {

   public ChainBuffer tailBuffer = new ChainBuffer(5);


   public EntityMamenchisaurus(EntityType<? extends EntityMamenchisaurus> type, Level world) {
      super(type, world);
    }

   public EntityMamenchisaurus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
          this.goalSelector.addGoal(1, new JurassiCraftAIAngry(this, 200));
          this.goalSelector.addGoal(1, new JurassiCraftAIFlee(this, 60, 1.1D * this.getCreatureSpeed()));
          this.goalSelector.addGoal(2, new JurassiCraftAISit(this));
          this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.100000023841858D * this.getCreatureSpeed(), false));
          this.goalSelector.addGoal(4, new JurassiCraftAIFollowFood(this, 50, 1.1D * this.getCreatureSpeed()));
          this.goalSelector.addGoal(4, new JurassiCraftAIEatDroppedFood(this, 16.0D));
          this.goalSelector.addGoal(4, new JurassiCraftAIEating(this, 20));
          this.goalSelector.addGoal(5, new JurassiCraftAIWander(this, 45, 0.7D * this.getCreatureSpeed()));
          this.goalSelector.addGoal(5, new AvoidEntityGoal(this, EntityTyrannosaurus.class, 12.0F, this.getCreatureSpeed(), 1.2D * this.getCreatureSpeed()));
          this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
          this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
          this.goalSelector.addGoal(7, new JurassiCraftAIHerdBehavior(this, 128.0D, 2500, 24.0D, this.getCreatureSpeed()));
          this.goalSelector.addGoal(7, new HerdAIFollowHerd(this, false, this.getCreatureSpeed()));
          this.targetSelector.addGoal(1, new JurassiCraftAIOwnerIsHurtByTarget(this));
          this.targetSelector.addGoal(2, new JurassiCraftAIOwnerHurtsTarget(this));
          this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
          this.setCreatureExperiencePoints(4000);
    }

   @Override
   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
      if(passenger != null) {
         double halfLength = (double)(0.55F * this.getCreatureLength());
         double xRotation = (double)Mth.sin(3.1415927F + 0.017453292F * this.yBodyRot);
         double zRotation = (double)Mth.cos(0.017453292F * this.yBodyRot);
         double extraX = (1.0D + 0.075D * Math.sin(0.04D * (double)this.getTotalTicksLived() + 1.5D)) * halfLength * xRotation;
         double extraZ = (1.0D + 0.075D * Math.sin(0.04D * (double)this.getTotalTicksLived() + 1.5D)) * halfLength * zRotation;
         double extraY = 1.03D * (double)this.getCreatureHeight() + 0.5D * Math.cos(0.05D * (double)this.getTotalTicksLived() - 0.5D);
         moveFunction.accept(passenger, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
      } else {
         super.positionRider(passenger, moveFunction);
      }
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
      this.tailBuffer.calculateChainSwingBuffer(30.0F, 4, 1.5F, this);
   }

   @Override
   protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
      float developmentFraction = (float)this.getGrowthStage() / 120.0F;
      int count = Math.round(1.0F + 5.0F * developmentFraction + (float)this.getRandom().nextInt(1 + (int)(6.5F * developmentFraction)) + (float)this.getRandom().nextInt(1 + lootingLevel));
      if(!this.isOnFire()) {
         this.dropCreatureItem(this.getCreature().getMeat(), count);
      } else {
         this.spawnCreatureItem(this.getCreature().getSteak(), 1);
      }

      if(this.getRandom().nextFloat() < 0.1F) {
         this.dropCreatureItem(this.getCreature().getSkull(), 1);
      }

      if(this.isMale() && this.getRandom().nextFloat() < 0.25F) {
         this.dropCreatureItem(this.getCreature().getSkin(), 1);
      }

   }
}
