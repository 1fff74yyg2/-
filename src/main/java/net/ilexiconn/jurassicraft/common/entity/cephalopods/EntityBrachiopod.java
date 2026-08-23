package net.ilexiconn.jurassicraft.common.entity.cephalopods;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftWaterCreature;
import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

public class EntityBrachiopod extends EntityJurassiCraftWaterCreature {
    private static final EntityDataAccessor<Integer> OPEN_MOUTH = SynchedEntityData.defineId(EntityBrachiopod.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HAS_PEARL = SynchedEntityData.defineId(EntityBrachiopod.class, EntityDataSerializers.INT);

    public int openMouthDummyTimer;

    public EntityBrachiopod(EntityType<? extends EntityBrachiopod> type, Level world) {
        super(type, world);
    }

    public EntityBrachiopod(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.applyCreatureAttributes();
    }

    private void applyCreatureAttributes() {
        if (this.getAttribute(Attributes.MAX_HEALTH) != null)
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0);
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null)
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.getEntityData().define(OPEN_MOUTH, 0);
        this.getEntityData().define(HAS_PEARL, 1);
    }

    public int getOpenMouth() {
        return this.getEntityData().get(OPEN_MOUTH);
    }

    public void setOpenMouth(int open) {
        this.getEntityData().set(OPEN_MOUTH, open);
    }

    public float getOpenMouth(float var1) {
        return this.getOpenMouth() / var1;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putInt("openMouth", this.getOpenMouth());
        nbt.putInt("pearl", this.hasPearl() ? 1 : 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.setOpenMouth(nbt.getInt("openMouth"));
        this.setHasPearl(nbt.getInt("pearl") == 1);
    }

    public void resetMouth(int plusOpenMouthTimer) {
        this.openMouthDummyTimer += this.getRandom().nextInt(250) + plusOpenMouthTimer;
        this.setOpenMouth(0);
    }

    public boolean hasPearl() {
        return this.getEntityData().get(HAS_PEARL) == 1;
    }

    public void setHasPearl(boolean pearl) {
        if (pearl) {
            this.getEntityData().set(HAS_PEARL, 1);
            return;
        }

        this.getEntityData().set(HAS_PEARL, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.openMouthDummyTimer != -1)
                --this.openMouthDummyTimer;

            if (this.openMouthDummyTimer == 0 && this.getOpenMouth() == 0) {
                this.setOpenMouth(this.getOpenMouth() - 1);
                this.openMouthDummyTimer = -1;
            }

            if (this.getOpenMouth() != 0 && this.getOpenMouth() > -65) {
                this.setOpenMouth(this.getOpenMouth() - 1);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getMainHandItem();

        if (item != null && item.getItem() == JCItemRegistry.net && this.hasPearl()) {
            if (this.getOpenMouth() == -65 && this.level().random.nextInt(3) == 0) {
                this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:brachiopod.slam")), 0.1f, this.getSoundPitch());
                this.setHasPearl(false);

                // if (!this.level().isClientSide)
                // {
                // this.entityDropItem(new ItemStack(JCItemRegistry.multiItems, 1), 0.0f);
                // }

                if (!player.getAbilities().instabuild) {
                    item.hurtAndBreak(1, player, (p) -> {
                    });
                }

                this.resetMouth(500);
            } else if (this.getOpenMouth() != 0) {
                this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:brachiopod.slam")), 0.1f, this.getSoundPitch());
                // player.hurt(CarboniferousApi.brachiopodDamage, 1.0f);
                this.resetMouth(75);
            }
        } else if (this.getOpenMouth() != 0) {
            this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:brachiopod.slam")), 0.1f, this.getSoundPitch());
            // player.hurt(CarboniferousApi.brachiopodDamage, 2.0f);
            this.resetMouth(80);
        }

        return InteractionResult.SUCCESS;
    }
}
