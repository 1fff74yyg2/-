package net.ilexiconn.jurassicraft.common.entity.fish;

import net.ilexiconn.jurassicraft.common.entity.ChainBuffer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSwimmingBase;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityOrthacanthus extends EntityJurassiCraftSwimmingBase {
    public ChainBuffer tailBuffer = new ChainBuffer(6);

    public EntityOrthacanthus(EntityType<? extends EntityOrthacanthus> type, Level world) {
        super(type, world);
    }

    public EntityOrthacanthus(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.swimSpeed = 2.2F;
            huntingInterval = 200;
            this.setHungry(huntingInterval);
    
            this.setCreatureExperiencePoints(50);
    }

    @Override
    public void tick() {
        super.tick();
        this.tailBuffer.calculateChainSwingBuffer(65.0F, 3, 4.0F, this);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        this.dropCreatureItem(this.getCreature().getMeat(), 1);
    }

    @Override
    public LivingEntity getTargetPriority(LivingEntity target, LivingEntity entity) {
        if (target != null) {
            if (entity instanceof Squid) // Goes for squid first
                return entity;
            else if (entity instanceof Player) // Then players
                return entity;
            else if (!(entity instanceof EntityCoelacanth))
                return target;
        } else if (entity instanceof Squid || entity instanceof Player || entity instanceof EntityOrthacanthus)
            return entity;

        return null;
    }
}
