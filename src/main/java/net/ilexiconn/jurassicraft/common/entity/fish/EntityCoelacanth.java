package net.ilexiconn.jurassicraft.common.entity.fish;

import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.ControlledAnimation;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSwimming;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityCoelacanth extends EntityJurassiCraftSwimming {
    public ChainBuffer tailBuffer = new ChainBuffer(4);
    public ControlledAnimation droppingTimer = new ControlledAnimation(35);

    public EntityCoelacanth(EntityType<? extends EntityCoelacanth> type, Level world) {
        super(type, world);
    }

    public EntityCoelacanth(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.swimRadius = 8.0F;
            this.swimRadiusHeight = 4.0F;
            this.swimSpeed = 0.4F;
            this.jumpOnLand = false;
            this.setCreatureExperiencePoints(50);
    }

    @Override
    protected Entity findEntityToAttack() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround() || this.isInWater())
            this.droppingTimer.decreaseTimer();
        else
            this.droppingTimer.increaseTimer();

        this.tailBuffer.calculateChainSwingBuffer(55.0F, 3, 4.0F, this);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), 1);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.isTamed();
    }
}
