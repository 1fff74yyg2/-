package net.ilexiconn.jurassicraft.common.entity;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.ilexiconn.jurassicraft.common.item.ItemGrowthSerum;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.HashSet;

public class EntityJurassiCraftCreature extends PathfinderMob implements IEntityAdditionalSpawnData, IAnimatedEntity {
    private Creature creature;

    protected final HashSet<Integer> growthStageList = new HashSet<Integer>();

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntityJurassiCraftCreature.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> GROWTH_STAGE = SynchedEntityData.defineId(EntityJurassiCraftCreature.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<String> DNA_SEQUENCE = SynchedEntityData.defineId(EntityJurassiCraftCreature.class, EntityDataSerializers.STRING);
    // 1.20.1: synced creature name so the client can resolve the creature (and thus
    // the correct hitbox / scale) even when the numeric creatureID shifted after a
    // mod update (old saves would otherwise fall back to the full adult EntityType size).
    private static final EntityDataAccessor<String> CREATURE_NAME = SynchedEntityData.defineId(EntityJurassiCraftCreature.class, EntityDataSerializers.STRING);
    // 1.20.1: genetic quality must be synced (it was a plain field before, so old
    // loaded entities had a random value on the client) - the client needs it to
    // recompute the attributes for display.
    private static final EntityDataAccessor<Float> GENETIC_QUALITY = SynchedEntityData.defineId(EntityJurassiCraftCreature.class, EntityDataSerializers.FLOAT);

    public int frame;
    public int expParameter;

    protected float geneticQuality;
    protected boolean gender;
    protected byte texture;

    protected int animID;
    protected int animTick;

    private float heightParameter, lengthParameter;

    private float bBoxXZ, bBoxY;

    public EntityJurassiCraftCreature(EntityType<? extends EntityJurassiCraftCreature> type, Level world) {
        super(type, world);

        this.setCreature(CreatureHandler.classToCreature(getClass()));
        if (this.getCreature() == null) {
            // Fallback: resolve the Creature from the EntityType registry name
            // (e.g. "tyrannosaurus") in case class-based lookup fails on a side
            // where the creature list was not populated the same way.
            net.minecraft.resources.ResourceLocation key =
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
            if (key != null) {
                this.setCreature(CreatureHandler.getCreatureFromName(key.getPath()));
            }
        }
        if (this.getCreature() == null && !CreatureHandler.getCreatures().isEmpty()) {
            // Last-resort fallback for legacy entities saved under the shared
            // EntityType (base class cannot be mapped to a specific creature):
            // use the first creature so the entity still loads without NPEs.
            this.setCreature(CreatureHandler.getCreatures().get(0));
        }

        if (this.getGeneticQuality() < 0.6F || this.getGeneticQuality() >= 1.4F) {
            this.setRandomGenetics();
        }

        this.resetGrowthStageList();
        this.setCreatureGender(JurassiCraftDNAHandler.getDefaultGenderDNAQuality(this.getDNASequence()) == 0.5F ? this.getRandom().nextBoolean() : (JurassiCraftDNAHandler.getDefaultGenderDNAQuality(this.getDNASequence()) > 0.5F));
        this.setNewCreatureTexture(JurassiCraftDNAHandler.getDefaultTextureDNAQuality(this.getDNASequence()));
        this.updateCreatureData(this.getTotalTicksLived());

        this.animTick = 0;
        this.animID = 0;

        // 1.20.1: the EntityType factory creates creatures through the
        // (EntityType, Level) constructor (the old 1.12.2 (Level) constructor is
        // no longer the spawn path). Subclasses register their AI goals here so
        // creatures actually get their behaviour.
        try {
            this.registerAI();
        } catch (Throwable t) {
            net.ilexiconn.jurassicraft.JurassiCraft.logger.error("Failed to register AI for " + this.getClass().getSimpleName(), t);
        }
    }

    /**
     * Hook for subclass AI registration. 1.12.2 subclasses registered their goals
     * in the (Level) constructor; in 1.20.1 the (EntityType, Level) constructor is
     * the spawn path, so subclasses override this method instead.
     */
    protected void registerAI() {
    }
    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftCreature(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(SCALE, Float.valueOf(0.0F));
        this.getEntityData().define(GROWTH_STAGE, Byte.valueOf((byte) (0)));
        this.getEntityData().define(DNA_SEQUENCE, String.valueOf(""));
        this.getEntityData().define(CREATURE_NAME, String.valueOf(""));
        this.getEntityData().define(GENETIC_QUALITY, Float.valueOf(0.0F));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        // 1.20.1: attributes are not synced to the client and the server-side
        // growth logic is skipped on the client, so the client's attribute
        // instances would keep their default values (e.g. attack 2.0). Recompute
        // the local attributes from the synced growth stage + genetic quality so
        // the DinoPad and other displays show correct values.
        if (this.level().isClientSide && (key.equals(GROWTH_STAGE) || key.equals(GENETIC_QUALITY))) {
            this.applyClientStats();
        }
    }

    private void applyClientStats() {
        Creature resolved = this.getResolvedCreature();
        if (resolved == null || this.getAttribute(Attributes.MAX_HEALTH) == null) {
            return;
        }
        float fraction = this.getGrowthStage() / 120.0F;
        float quality = this.getGeneticQuality();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((int) (quality * (resolved.getMinHealth() + (resolved.getMaxHealth() - resolved.getMinHealth()) * fraction)));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((float) (quality * (resolved.getMinStrength() + (resolved.getMaxStrength() - resolved.getMinStrength()) * fraction)));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((float) (resolved.getMinSpeed() + (resolved.getMaxSpeed() - resolved.getMinSpeed()) * fraction));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((float) (quality * (resolved.getMinKnockback() + (resolved.getMaxKnockback() - resolved.getMinKnockback()) * fraction)));
    }

    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    /**
     * Returns the creature.
     */
    public Creature getCreature() {
        return this.creature;
    }

    /**
     * Sets the creature.
     */
    public void setCreature(Creature creature) {
        this.creature = creature;
        if (creature != null) {
            this.getEntityData().set(CREATURE_NAME, creature.getCreatureName());
        }
    }

    /**
     * The synced creature name used by the client to resolve the creature when the
     * numeric creatureID is not resolvable (old saves with a shifted creatureID).
     */
    public String getSyncedCreatureName() {
        String synced = this.getEntityData().get(CREATURE_NAME);
        if (synced != null && !synced.isEmpty()) {
            return synced;
        }
        return this.creature != null ? this.creature.getCreatureName() : "";
    }

    /**
     * Resolves the creature, falling back to the synced name when the ID lookup
     * fails (creatureID shifted after a mod update).
     */
    public Creature getResolvedCreature() {
        if (this.creature != null) {
            return this.creature;
        }
        String name = this.getSyncedCreatureName();
        if (name != null && !name.isEmpty()) {
            return CreatureHandler.getCreatureFromName(name);
        }
        return null;
    }

    /**
     * Returns the creature genetic quality.
     */
    public float getGeneticQuality() {
        return this.getEntityData().get(GENETIC_QUALITY);
    }

    /**
     * Sets the creature genetic quality. Genetic quality is how much the creature varies in status. 1.0F is the base value.
     */
    private void setGeneticQuality(float quality) {
        this.getEntityData().set(GENETIC_QUALITY, Float.valueOf(quality));
    }

    /**
     * Returns the creature DNA sequence.
     */
    public String getDNASequence() {
        return this.getEntityData().get(DNA_SEQUENCE);
    }

    /**
     * Sets the creature DNA sequence.
     */
    public void setDNASequence(String dna) {
        this.getEntityData().set(DNA_SEQUENCE, String.valueOf(dna));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.animID != 0)
            this.animTick++;

        this.frame++;
        // 1.20.1: on the client the "dimensions" field (used by getBbWidth/getBbHeight
        // and the collision box) starts as the full adult EntityType size; refresh it
        // from getDimensions() so babies render with a small hitbox on both sides.
        if (this.level().isClientSide) {
            this.refreshDimensions();
        }

    }



    @Override
    public void aiStep() {
        // Growth/scale updates are server-authoritative: the client's tickCount is NOT
        // synced, so letting the client run updateCreatureData() would overwrite the server's
        // SCALE with stale values (e.g. a growth serum would appear to "shrink back").
        if (!this.level().isClientSide) {
            if (this.getTotalTicksLived() <= this.getCreature().getTicksToAdulthood() && this.growthStageList.contains(this.getTotalTicksLived())) {
                if (this.getGrowthStage() < 120)
                    this.setGrowthStage((byte) (this.getGrowthStage() + 1));

                if (this.getCreature() != null) {
                    this.updateCreatureData(this.getTotalTicksLived());
                    // 1.20.1: the hitbox follows the growth stage; refresh so the
                    // collision box is recomputed from getDimensions().
                    this.refreshDimensions();
                }
            }
        }

        if (getTarget() != null && getTarget().getVehicle() != null)
            setTarget(null);

        super.aiStep();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player != null && player.getMainHandItem() != null && !this.level().isClientSide) {
            if (player.getMainHandItem().getItem() instanceof ItemGrowthSerum) {
                if (this.forceCreatureGrowth(player, (byte) 10) && !player.getAbilities().instabuild) {
                    player.getMainHandItem().shrink(1);
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Updates the creature status.
     */
    protected void updateCreatureData(int ticks) {
        if (ticks > 0) {
            float previousHealth = this.getHealth();
            double ticksToAdulthood = this.getCreature().getTicksToAdulthood();
            double minHealth = this.getCreature().getMinHealth();
            double minStrength = this.getCreature().getMinStrength();
            double minSpeed = this.getCreature().getMinSpeed();
            double minKnockback = this.getCreature().getMinKnockback();

            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((int) (this.getGeneticQuality() * (ticks * this.getCreature().getMaxHealth() - minHealth) / ticksToAdulthood + minHealth));
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((float) (this.getGeneticQuality() * (ticks * (this.getCreature().getMaxStrength() - minStrength) / ticksToAdulthood + minStrength)));
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((float) (ticks * (this.getCreature().getMaxSpeed() - minSpeed) / ticksToAdulthood + minSpeed));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((float) (this.getGeneticQuality() * (ticks * (this.getCreature().getMaxKnockback() - minKnockback) / ticksToAdulthood + minKnockback)));
            this.setBoundingBox((float) (this.getGeneticQuality() * (this.getCreature().getXzBoxMin() + this.getCreature().getXzBoxDelta() * (((float) this.getGrowthStage()) / 120))), (float) (this.getGeneticQuality() * (this.getCreature().getYBoxMin() + this.getCreature().getYBoxDelta() * (((float) this.getGrowthStage()) / 120))));
            this.setCreatureLength();
            this.setCreatureHeight();
            this.setCreatureSize(this.getXZBoundingBox(), this.getYBouningBox());
            this.setCreatureScale();

            this.heal((float) (this.getCreatureHealth() - previousHealth));
        }
    }

    /**
     * Resets the growthStageList. This is a list of values (number of ticks) that represent when the creature updates its status.
     */
    private void resetGrowthStageList() {
        if (this.getCreature() == null) {
            // Creature data unavailable (e.g. creature not registered on this side);
            // use a default growth duration so the entity can still be created.
            this.growthStageList.add(1);
            for (byte i = 1; i < (byte) 120; i++) {
                this.growthStageList.add((24000 * i) / (byte) 120);
            }
            this.growthStageList.add(24000);
            return;
        }
        int ticks = (int) this.getCreature().getTicksToAdulthood();

        this.growthStageList.add(1);

        for (byte i = 1; i < (byte) 120; i++) {
            this.growthStageList.add((ticks * i) / (byte) 120);
        }

        this.growthStageList.add(ticks);
    }

    /**
     * Returns the current growth stage of the creature. In order words, how many times this creature has updated.
     */
    public byte getGrowthStage() {
        return this.getEntityData().get(GROWTH_STAGE);
    }

    /**
     * Sets what is the growth stage of the creature.
     */
    private void setGrowthStage(byte stage) {
        this.getEntityData().set(GROWTH_STAGE, Byte.valueOf(stage));
    }

    /**
     * Sets the creature xz and y hit box using genetic quality and growth stage.
     */
    public void setBoundingBox(float xz, float y) {
        this.bBoxXZ = xz;
        this.bBoxY = y;
    }

    /**
     * Sets a new bounding box for the creature depending on its status.
     */
    protected final void setCreatureSize(float xzBoundingBox, float yBouningBox) {
        this.setBoundingBox(xzBoundingBox, yBouningBox);
    }

    /**
     * The rendered size is always derived from the growth stage (a SynchedEntityData-synced value
     * that survives entity reloads), NOT from the SCALE field. This guarantees the
     * creature keeps its size after leaving/re-entering the render range or after chunk
     * reloads, and after using a growth serum.
     */
    public float getCreatureScale() {
        float stageFraction = this.getGrowthStage() / 120.0F;
        float maxHeight = this.getCreature().getMaxHeight();
        float minHeight = this.getCreature().getMinHeight();
        float maxLength = this.getCreature().getMaxLength();
        float minLength = this.getCreature().getMinLength();
        float scale = this.getGeneticQuality() * (((minLength + minHeight) / 2) + (((maxHeight + maxLength) / 2) - ((minHeight + minLength) / 2)) * stageFraction) / ((maxHeight + maxLength) / 2);
        return scale * this.getCreature().getScaleAdjustment();
    }

    /**
     * Sets the scale of the this.creature depending on the age and genetic quality.
     */
    private void setCreatureScale(float scale) {
        if (scale > 0.0F) {
            this.getEntityData().set(SCALE, Float.valueOf(scale));
        } else {
            this.getEntityData().set(SCALE, 0.0F);
        }
    }

    /**
     * Sets the scale of the this.creature depending on the growth stage and genetic quality.
     */
    private void setCreatureScale() {
        float stageFraction = this.getGrowthStage() / 120.0F;
        float maxHeight = this.getCreature().getMaxHeight();
        float minHeight = this.getCreature().getMinHeight();
        float maxLength = this.getCreature().getMaxLength();
        float minLength = this.getCreature().getMinLength();

        this.getEntityData().set(SCALE, Float.valueOf(this.getGeneticQuality() * (((minLength + minHeight) / 2) + (((maxHeight + maxLength) / 2) - ((minHeight + minLength) / 2)) * stageFraction) / ((maxHeight + maxLength) / 2)));
    }

    /**
     * Returns how many ticks this entity has lived.
     */
    public int getTotalTicksLived() {
        return this.tickCount;
    }

    /**
     * Resets the ticks that this entity has lived (Client only).
     */
    private void setTicksExisted(int ticks) {
        this.tickCount = ticks;
    }

    /**
     * Force the creature to grow a specific value if it is possible.
     */
    public boolean forceCreatureGrowth(Player player, byte growthIncrease) {
        if (this.getGrowthStage() + growthIncrease <= 120) {
            this.setGrowthStage((byte) (this.getGrowthStage() + growthIncrease));
            this.setTicksExisted((int) (this.getCreature().getTicksToAdulthood() * (double) this.getGrowthStage() / 120.0D));

            if (this.getCreature() != null && !this.level().isClientSide) {
                this.updateCreatureData(this.getTotalTicksLived());
                this.refreshDimensions();
            }

            return true;
        } else {
            if (player != null && player.level().isClientSide) {
                if (this.hasCustomName())
                    player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.interaction.fullGrown")));
                else
                    player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.interaction.fullGrown")));
            }

            return false;
        }
    }

    /**
     * Force the creature to grow to its maximum size.
     */
    public void setFullGrowth() {
        if (!this.isCreatureAdult()) {
            this.setGrowthStage((byte) (120));
            this.setTicksExisted((int) (this.getCreature().getTicksToAdulthood() * this.getGrowthStage() / 120));

            if (this.getCreature() != null) {
                this.updateCreatureData(this.getTotalTicksLived());
                if (!this.level().isClientSide) {
                    this.refreshDimensions();
                }
            }
        }
    }

    /**
     * Force the creature to grow to its minimum size.
     */
    public void setNoGrowth() {
        if (this.getGrowthStage() != 0) {
            this.setGrowthStage((byte) (0));
            this.setTicksExisted(0);

            if (this.getCreature() != null) {
                this.updateCreatureData(this.getTotalTicksLived());
                if (!this.level().isClientSide) {
                    this.refreshDimensions();
                }
            }
        }
    }

    /**
     * Returns the creature Name.
     */
    public String getCreatureName() {
        return this.getCreature().getCreatureName();
    }

    /**
     * Checks if the creature has a genetic code.
     */
    public boolean hasDNASequence() {
        return !(this.getDNASequence() == null || this.getDNASequence() == "");
    }

    /**
     * Sets the creature genetic data depending on the dna quality and code.
     */
    public void setGenetics(int dnaQuality, String dna) {
        this.setDNASequence(JurassiCraftDNAHandler.reviseDNA(dna, dnaQuality));
        this.setGeneticQuality(JurassiCraftDNAHandler.getDefaultGeneticDNAQuality(dna));
    }

    /**
     * Sets the creature genetic data randomly.
     */
    public void setRandomGenetics() {
        String dna = JurassiCraftDNAHandler.createDefaultDNA();

        this.setGeneticQuality(JurassiCraftDNAHandler.getDefaultGeneticDNAQuality(dna));
        this.setDNASequence(dna);
    }

    /**
     * Returns how many ticks this creature requires to reach adulthood.
     */
    public float getAdultAge() {
        return (float) this.getCreature().getTicksToAdulthood();
    }

    /**
     * Returns true if the creature is considered an adult.
     */
    public boolean isCreatureAdult() {
        return this.getTotalTicksLived() >= this.getCreature().getAdultAge() * this.getAdultAge();
    }

    /**
     * Returns true if the creature is older than a certain percentage of the ticks for adulthood.
     */
    public boolean isCreatureOlderThan(float percentage) {
        return this.getTotalTicksLived() >= percentage * this.getCreature().getTicksToAdulthood();
    }

    /**
     * Returns the creature hit box.
     */
    public float getXZBoundingBox() {
        return this.bBoxXZ;
    }

    /**
     * Returns the creature hit box.
     */
    public float getYBouningBox() {
        return this.bBoxY;
    }

    /**
     * Returns the current health of the creature. This is just a information for the user.
     */
    public double getCreatureCurrentHealth() {
        return (double) ((int) (100 * this.getHealth())) / 100;
    }

    /**
     * Returns the health of the creature. This is just a information for the user.
     */
    public double getCreatureHealth() {
        return (double) ((int) (100 * this.getAttribute(Attributes.MAX_HEALTH).getValue())) / 100;
    }

    public int getCreatureHealthScaled(int i) {
        return (int) ((this.getCreatureHealth() * i) / (1.2F * this.getCreature().getMaxHealth()));
    }

    /**
     * Returns the attack of the creature.
     */
    public double getCreatureAttack() {
        return (double) ((int) (100 * this.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) / 100;
    }

    public int getCreatureAttackScaled(int i) {
        return (int) ((this.getCreatureAttack() * i) / (1.2F * this.getCreature().getMaxStrength()));
    }

    /**
     * Returns the raw MOVEMENT_SPEED attribute value (blocks/tick). Only for display.
     */
    public double getCreatureSpeedValue() {
        return (double) ((int) (100 * this.getAttribute(Attributes.MOVEMENT_SPEED).getValue())) / 100;
    }

    /**
     * 1.20.1: Returns the speed MULTIPLIER used by AI goals. In 1.20.1 the
     * speed argument of PathNavigation.moveTo / MoveControl is a multiplier of
     * the MOVEMENT_SPEED attribute (1.0 = full attribute speed). The old
     * 1.12.2 code passed the raw attribute value here (e.g. 0.33), which in
     * 1.20.1 made creatures move at ~33% speed. The full attribute speed
     * (0.3-0.4 blocks/tick, faster than a sprinting player) felt too fast, so
     * this returns 0.7 to keep the relative speeds (wander 0.7, chase 1.2,
     * flee 1.1, attack 1.0-1.4) but land around player-walk/player-sprint
     * pace. Use {@link #getCreatureSpeedValue()} when the raw attribute value
     * is needed (display only).
     */
    public double getCreatureSpeed() {
        return 0.7D;
    }

    public int getCreatureSpeedScaled(int i) {
        return (int) ((this.getCreatureSpeedValue() * i) / (1.2F * this.getCreature().getMaxSpeed()));
    }

    /**
     * Returns the knockback resistance of the creature.
     */
    public double getCreatureKnockback() {
        return (double) ((int) (100 * this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue())) / 100;
    }

    /**
     * Returns the knock back of the creature.
     */
    public int getCreatureKnockbackScaled(int i) {
        return (int) ((this.getCreatureKnockback() * i) / (1.2F * this.getCreature().getMaxKnockback()));
    }

    /**
     * Returns the length of the creature.
     */
    public float getCreatureLength() {
        return (float) ((int) (100 * this.lengthParameter)) / 100;
    }

    public int getCreatureLengthScaled(int i) {
        return (int) ((this.getCreatureLength() * i) / (1.2F * this.getCreature().getMaxLength()));
    }

    /**
     * Returns the height of the creature.
     */
    public float getCreatureHeight() {
        return ((float) ((int) (this.heightParameter * 100) / 100)) - 0.25F;
    }

    public int getCreatureHeightScaled(int i) {
        return (int) ((this.getCreatureHeight() * i) / (1.2F * this.getCreature().getMaxHeight()));
    }

    /**
     * Returns true if the creature is a male.
     */
    public boolean isMale() {
        return this.getCreatureGender();
    }

    /**
     * Returns the creature gender as String.
     */
    public String getCreatureGenderString() {
        // 1.20.1: lowercase so the entity texture resource paths stay valid
        // (files are named ...male1.png/...female1.png).
        return this.getCreatureGender() ? "male" : "female";
    }

    /**
     * Returns the creature gender. False is female and true is male.
     */
    public boolean getCreatureGender() {
        return this.gender;
    }

    /**
     * Sets the creature gender. 0 is female and 1 is male.
     */
    public void setCreatureGender(boolean sex) {
        this.gender = sex;
    }

    /**
     * Sets the creature texture based on the genetics.
     */
    private void setNewCreatureTexture(float textureFromGenetics) {
        int textureCount = this.getCreature().getTextureCount();

        if (textureCount > 0) {
            float texturesInterval = 0.8F / textureCount;

            for (int i = 1; i <= textureCount; i++) {
                if (textureFromGenetics <= 0.2F + texturesInterval * i) {
                    this.texture = (byte) (i - 1);
                    return;
                }
            }
        }

        this.texture = (byte) 0;
    }

    /**
     * Returns the creature texture.
     */
    public byte getCreatureTexture() {
        return this.texture;
    }

    /**
     * Sets the creature texture.
     */
    private void setCreatureTexture(byte texture) {
        this.texture = texture;
    }

    /**
     * Sets the length of the creature.
     */
    public void setCreatureLength() {
        if (this.getTotalTicksLived() <= this.getCreature().getTicksToAdulthood())
            this.lengthParameter = (float) (this.getGeneticQuality() * (this.getCreature().getMinLength() + (this.getTotalTicksLived() * (this.getCreature().getMaxLength() - this.getCreature().getMinLength()) / this.getCreature().getTicksToAdulthood())));
        else
            this.lengthParameter = this.getGeneticQuality() * (this.getCreature().getMaxLength());
    }

    /**
     * Sets the height of the creature.
     */
    public void setCreatureHeight() {
        if (this.getTotalTicksLived() <= this.getCreature().getTicksToAdulthood())
            this.heightParameter = (float) (this.getGeneticQuality() * (this.getCreature().getMinHeight() + (this.getTotalTicksLived() * (this.getCreature().getMaxHeight() - this.getCreature().getMinHeight()) / this.getCreature().getTicksToAdulthood())));
        else
            this.heightParameter = this.getGeneticQuality() * (this.getCreature().getMaxHeight());
    }

    /**
     * Returns how many days this entity has lived.
     */
    public int getCreatureAgeInDays() {
        return this.getTotalTicksLived() / 24000;
    }

    /**
     * Returns how many months this entity has lived.
     */
    public int getCreatureAgeInMonths() {
        return this.getTotalTicksLived() / (720000);
    }

    /**
     * Returns how many years this entity has lived.
     */
    public int getCreatureAgeInYears() {
        return this.getTotalTicksLived() / (8640000);
    }

    /**
     * Returns how many days, and/or months, and/or years this entity has lived.
     */
    public String getCreatureAgeString() {
        byte years = (byte) getCreatureAgeInYears();
        byte months = (byte) (getCreatureAgeInMonths() - 12 * this.getCreatureAgeInYears());
        byte days = (byte) (getCreatureAgeInDays() - 30 * this.getCreatureAgeInMonths());

        String yearString = I18nCompat.get("container.pad.years");
        String monthString = I18nCompat.get("container.pad.months");
        String dayString = I18nCompat.get("container.pad.days");

        if (years <= 1)
            yearString = I18nCompat.get("container.pad.year");

        if (months <= 1)
            monthString = I18nCompat.get("container.pad.month");

        if (days <= 1)
            dayString = I18nCompat.get("container.pad.day");

        if (years <= 0) {
            if (months <= 0)
                return (String.valueOf(days) + " " + dayString);
            else
                return (String.valueOf(months) + " " + monthString + String.valueOf(days) + " " + dayString);
        } else {
            if (months <= 0)
                return (String.valueOf(years) + " " + yearString + String.valueOf(days) + " " + dayString);
            else
                return (String.valueOf(years) + " " + yearString + String.valueOf(months) + " " + monthString + String.valueOf(days) + " " + dayString);
        }
    }

    @Override
    public float getEyeHeight(net.minecraft.world.entity.Pose pose) {
        return this.getBbHeight() * 0.85F;
    }

    /**
     * 1.20.1: the collision box is derived from the synced growth stage instead of
     * the fixed (adult) EntityType dimensions, so babies spawn small and grow with
     * the growth stage. Mirrors the old 1.12.2 setSize() behaviour.
     */
    @Override
    public net.minecraft.world.entity.EntityDimensions getDimensions(net.minecraft.world.entity.Pose pose) {
        float stageFraction = this.getGrowthStage() / 120.0F;
        float xz;
        float y;
        Creature resolved = this.getResolvedCreature();
        if (resolved != null) {
            xz = (float) (this.getGeneticQuality() * (resolved.getXzBoxMin() + resolved.getXzBoxDelta() * stageFraction));
            y = (float) (this.getGeneticQuality() * (resolved.getYBoxMin() + resolved.getYBoxDelta() * stageFraction));
        } else {
            // No creature mapping: use a small default so entities never render with
            // the full adult EntityType hitbox when the mapping is missing.
            xz = 0.5F;
            y = 0.5F;
        }
        return net.minecraft.world.entity.EntityDimensions.scalable(Math.max(0.1F, xz), Math.max(0.1F, y));
    }

    public int getTalkInterval() {
        return 200;
    }

    protected float getSoundPitch() {
        return Float.valueOf(1.0F + 0.8F * (120 - this.getGrowthStage()) / 120);
    }

    protected float getSoundVolume() {
        return Float.valueOf(0.7F + 0.3F * this.getGrowthStage() / 120);
    }

    public int getCreatureExperiencePoints() {
        return this.expParameter;
    }

    public void setCreatureExperiencePoints(int points) {
        this.expParameter = points;
    }

    @Override
    public int getExperienceReward() {
        return (int) (this.getCreatureExperiencePoints() * this.getGeneticQuality() * this.getGrowthStage() / 120);
    }

    public boolean isWaterCreature() {
        return this.getCreature().isWaterCreature();
    }

    public boolean isFlyingCreature() {
        return this.getCreature().isFlyingCreature();
    }

    public int getAnimationId() {
        return this.animID;
    }

    public void setAnimationId(int id) {
        this.animID = id;
    }

    public int getAnimationTick() {
        return this.animTick;
    }

    public void setAnimationTick(int tick) {
        this.animTick = tick;
    }

    @Override
    public SoundEvent getAmbientSound() {
        this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:" + this.getCreatureName().toLowerCase() + ".living")), this.getSoundVolume(), this.getSoundPitch());
        return null;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource damageSource) {
        this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:" + this.getCreatureName().toLowerCase() + ".hurt")), this.getSoundVolume(), this.getSoundPitch());
        return null;
    }

    @Override
    public SoundEvent getDeathSound() {
        this.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("jurassicraft:" + this.getCreatureName().toLowerCase() + ".death")), this.getSoundVolume(), this.getSoundPitch());
        return null;
    }

    protected ItemEntity dropItemStackWithGenetics(ItemStack stack) {
        if (stack.getItem() != null) {
            CompoundTag compound = new CompoundTag();

            if (this.hasDNASequence())
                compound.putString("DNA", this.getDNASequence());
            else
                compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

            compound.putInt("Quality", 100);
            stack.setTag(compound);
        }

        return this.spawnAtLocation(stack, 0.0F);
    }

    /**
     * 1.20.1: safe item drop. The per-creature item (meat/skin/skull/fur/tooth/
     * steak) may not be registered for this species - Creature.getXxx() then
     * returns null and new ItemStack(null) would NPE. Skip the drop instead.
     */
    protected void dropCreatureItem(net.minecraft.world.item.Item item, int count) {
        if (item != null) {
            this.dropItemStackWithGenetics(new ItemStack(item, count));
        }
    }

    protected void spawnCreatureItem(net.minecraft.world.item.Item item, int count) {
        if (item != null) {
            this.spawnAtLocation(new ItemStack(item, count));
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        if (!this.isOnFire())
            this.dropCreatureItem(this.getCreature().getMeat(), 1);
        else
            this.spawnCreatureItem(this.getCreature().getSteak(), 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putByte("ID", this.getCreature().getCreatureID());
        compound.putInt("TicksExisted", this.getTotalTicksLived());
        compound.putByte("GrowthStage", this.getGrowthStage());
        compound.putString("DNASequence", this.getDNASequence());
        compound.putFloat("GeneticQuality", this.getGeneticQuality());
        compound.putBoolean("Gender", this.getCreatureGender());
        compound.putByte("Texture", this.getCreatureTexture());
        // 1.20.1: save the creature name too so old saves keep resolving after the
        // numeric creatureID counter shifts.
        compound.putString("CreatureName", this.getCreature().getCreatureName());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.creature = null;
        String creatureName = "";
        if (compound.contains("CreatureName")) {
            creatureName = compound.getString("CreatureName");
            this.creature = CreatureHandler.getCreatureFromName(creatureName);
        }
        if (this.creature == null && compound.contains("ID")) {
            this.creature = CreatureHandler.getCreatureFromId(compound.getByte("ID"));
        }
        if (this.creature != null) {
            creatureName = this.creature.getCreatureName();
        }
        this.getEntityData().set(CREATURE_NAME, creatureName);
        this.setTicksExisted(compound.getInt("TicksExisted"));
        this.setGrowthStage(compound.getByte("GrowthStage"));
        this.setDNASequence(compound.getString("DNASequence"));
        this.setGeneticQuality(compound.getFloat("GeneticQuality"));
        this.setCreatureGender(compound.getBoolean("Gender"));
        this.setCreatureTexture(compound.getByte("Texture"));
        this.resetGrowthStageList();
        this.updateCreatureData(this.getTotalTicksLived());
        if (!this.level().isClientSide) {
            this.refreshDimensions();
        }
    }

    @Override
    public void writeSpawnData(net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeByte(this.getCreature().getCreatureID());
        buf.writeUtf(this.getCreature().getCreatureName());
        buf.writeInt(this.getTotalTicksLived());
        buf.writeByte(this.getGrowthStage());
        buf.writeFloat(this.getGeneticQuality());
        buf.writeBoolean(this.getCreatureGender());
        buf.writeByte(this.getCreatureTexture());
    }

    @Override
    public void readSpawnData(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            byte id = buf.readByte();
            String name = buf.readUtf();
            this.creature = null;
            if (!name.isEmpty()) {
                this.creature = CreatureHandler.getCreatureFromName(name);
            }
            if (this.creature == null) {
                this.creature = CreatureHandler.getCreatureFromId(id);
            }
            this.getEntityData().set(CREATURE_NAME, this.creature != null ? this.creature.getCreatureName() : name);
            this.setTicksExisted(buf.readInt());
            this.setGrowthStage(buf.readByte());
            this.setGeneticQuality(buf.readFloat());
            this.setCreatureGender(buf.readBoolean());
            this.setCreatureTexture(buf.readByte());
            this.resetGrowthStageList();
            this.updateCreatureData(this.getTotalTicksLived());
            if (!this.level().isClientSide) {
                this.refreshDimensions();
            }
        } catch (Exception e) {
            System.err.println("Error while reading dino spawn data!");
            e.printStackTrace();
        }
    }
}
