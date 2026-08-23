package net.ilexiconn.jurassicraft.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class EntityJurassiCraftWaterCreature extends EntityJurassiCraftSmart {
    public EntityJurassiCraftWaterCreature(EntityType<? extends EntityJurassiCraftWaterCreature> type, Level world) {
        super(type, world);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftWaterCreature(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.noCollision(this.getBoundingBox());
    }

    public int getTalkInterval() {
        return 120;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return true;
    }

    @Override
    public int getExperienceReward() {
        return 1 + this.level().random.nextInt(3);
    }

    @Override
    public void tick() {
        super.tick();
        int air = this.getAirSupply();

        if (this.isAlive() && !this.isInWater()) {
            --air;

            this.setAirSupply(air);

            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.level().damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }
}
