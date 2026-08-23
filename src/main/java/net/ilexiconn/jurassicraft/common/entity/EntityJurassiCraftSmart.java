package net.ilexiconn.jurassicraft.common.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.common.entity.ai.JCPathNavigate;
import net.ilexiconn.jurassicraft.common.entity.ai.States;
import net.ilexiconn.jurassicraft.common.item.ItemDinoPad;
import net.ilexiconn.jurassicraft.common.item.ItemGrowthSerum;
import net.ilexiconn.jurassicraft.common.item.ItemOnAStick;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.scores.Team;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class holds many states of the creature, such as sitting, taming, sleeping, and other behaviors. Every state is set by using bit flags.
 *
 * @author RafaMv
 */
public class EntityJurassiCraftSmart extends EntityJurassiCraftCreature implements OwnableEntity {
    private static final EntityDataAccessor<Integer> STATUS = SynchedEntityData.defineId(EntityJurassiCraftSmart.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(EntityJurassiCraftSmart.class, EntityDataSerializers.STRING);

    public LivingEntity creatureToAttack;
    protected int angryTicks;
    protected int numberOfAllies;
    protected int fleeingTick;

    private long lastSitToggleTimeMillis = -1L;
    private int lastSitToggleTick = -2;

    public EntityJurassiCraftSmart(EntityType<? extends EntityJurassiCraftSmart> type, Level world) {
        super(type, world);
        this.numberOfAllies = 0;
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityJurassiCraftSmart(Level world) {
        this(JCEntityRegistry.getSharedCreatureType(), world);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new JCPathNavigate(this, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(STATUS, Integer.valueOf(0));
        this.getEntityData().define(OWNER, String.valueOf(""));
    }

    /**
     * In Minecraft 1.12.2 a single right-click on an entity makes the client send
     * TWO packets (INTERACT_AT and INTERACT), so the server calls processInteract
     * twice for one click. Without a guard, every click would toggle the sit/stand
     * state twice ("is not sitting anymore" + "is sitting" messages together).
     * This method returns true only for the first call of the same click and false
     * for the duplicate one (same tick, or within a 60 ms window).
     */
    protected boolean consumeSitToggle() {
        long now = System.currentTimeMillis();
        if (this.tickCount == this.lastSitToggleTick || (this.lastSitToggleTimeMillis != -1L && now - this.lastSitToggleTimeMillis < 60L)) {
            return false;
        }
        this.lastSitToggleTick = this.tickCount;
        this.lastSitToggleTimeMillis = now;
        return true;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getMainHandItem();

        if (heldItem != null) {
            if (!(heldItem.getItem() instanceof ItemGrowthSerum) && !(heldItem.getItem() instanceof ItemDinoPad) && !(heldItem.getItem() instanceof ItemOnAStick)) {
                if (this.getCreature().isFavoriteFood(heldItem.getItem())) {
                    boolean shouldDecreaceItemStack = false;

                    if ((double) (this.getHealth() + 3.0F) <= this.getCreatureHealth()) {
                        shouldDecreaceItemStack = true;
                        this.heal(3.0F);
                    }

                    if (!this.isTamed() && !this.level().isClientSide) {
                        shouldDecreaceItemStack = true;

                        if (this.getRandom().nextInt(4) == 0)
                            this.setTamed(true, player);
                        else
                            this.level().broadcastEntityEvent(this, (byte) 6);
                    }

                    if (shouldDecreaceItemStack) {
                        if (!player.getAbilities().instabuild) {
                            heldItem.shrink(1);

                            if (heldItem.getCount() <= 0)
                                player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                        }
                    }
                } else if (this.isTamed() && this.isOwner(player) && !this.level().isClientSide) {
                    if (this.consumeSitToggle()) {
                        if (this.isSitting())
                            this.setSitting(false, player);
                        else
                            this.forceSitting(player);
                    }
                }
            }
        } else {
            if (this.isTamed() && this.isOwner(player) && !this.level().isClientSide) {
                if (this.consumeSitToggle()) {
                    if (this.isSitting())
                        this.setSitting(false, player);
                    else
                        this.forceSitting(player);
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Returns the states of the creature. It uses bitwise language.
     */
    public int getStatus() {
        return this.getEntityData().get(STATUS);
    }

    /**
     * Sets the states of the creature. It uses bitwise language.
     *
     * @param states is an integer representing one or more states that are true.
     */
    public void setStatus(int states) {
        this.getEntityData().set(STATUS, Integer.valueOf(states));
    }

    /**
     * Returns true if the creature is swimming.
     */
    public boolean isTakingOff() {
        return (this.getStatus() & States.SWIMMING) == States.SWIMMING;
    }

    /**
     * Sets if the creature is taking off.
     */
    public void setTakingOff(boolean takingOff) {
        if (takingOff && !this.isSitting() && !this.isSleeping() && !this.isEating() && !this.isDrinking() && !this.isPlaying() && !this.isBreeding() && !this.isFlying()) {
            this.setStatus(this.getStatus() | States.SWIMMING);
        } else {
            this.setStatus(this.getStatus() & ~States.SWIMMING);
        }
    }

    /**
     * Sets incompatible states false to set swimming state.
     */
    public void forceTakingOff(boolean takingOff) {
        this.setStatus(this.getStatus() & ~States.SITTING);
        this.setStatus(this.getStatus() & ~States.SLEEPING);
        this.setStatus(this.getStatus() & ~States.EATING);
        this.setStatus(this.getStatus() & ~States.DRINKING);
        this.setStatus(this.getStatus() & ~States.PLAYING);
        this.setStatus(this.getStatus() & ~States.BREEDING);
        this.setStatus(this.getStatus() & ~States.FLYING);
        this.setStatus(this.getStatus() | States.SWIMMING);
    }

    /**
     * Returns true if the creature is flying.
     */
    public boolean isFlying() {
        return (this.getStatus() & States.FLYING) == States.FLYING;
    }

    /**
     * Sets if the creature is flying.
     */
    public void setFlying(boolean flying) {
        if (flying && !this.isSitting() && !this.isSleeping() && !this.isEating() && !this.isDrinking() && !this.isPlaying() && !this.isBreeding() && this.isTakingOff()) {
            this.setStatus(this.getStatus() | States.FLYING);
        } else {
            this.setStatus(this.getStatus() & ~States.FLYING);
        }
    }

    /**
     * Sets incompatible states false to set flying state.
     */
    public void forceFlying(boolean flying) {
        this.setStatus(this.getStatus() & ~States.SITTING);
        this.setStatus(this.getStatus() & ~States.SLEEPING);
        this.setStatus(this.getStatus() & ~States.EATING);
        this.setStatus(this.getStatus() & ~States.DRINKING);
        this.setStatus(this.getStatus() & ~States.PLAYING);
        this.setStatus(this.getStatus() & ~States.BREEDING);
        this.setStatus(this.getStatus() & ~States.SWIMMING);
        this.setStatus(this.getStatus() | States.FLYING);
    }

    /**
     * Returns true if the creature is tamed.
     */
    public boolean isTamed() {
        return (this.getStatus() & States.TAMED) == States.TAMED;
    }

    /**
     * Sets if the creature is tamed.
     */
    public void setTamed(boolean tamed, Player player) {
        if (tamed) {
            this.setStatus(this.getStatus() | States.TAMED);
            this.setTarget(null);
            this.getNavigation().stop();
            this.forceSitting(player);
            this.setOwner(player.getName().getString());
            this.level().broadcastEntityEvent(this, (byte) 7);

            if (this.hasCustomName())
                player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.interaction.tamed")));
            else
                player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.interaction.tamed")));
        } else {
            this.setStatus(this.getStatus() & ~States.TAMED);
        }
    }

    /**
     * Returns true if the creature is sitting.
     */
    public boolean isSitting() {
        return (this.getStatus() & States.SITTING) == States.SITTING;
    }

    /**
     * Sets if the creature is sitting.
     *
     * @param sitting is the next state that the creature will be (true/false).
     * @param player is the Player that will receive the sitting text. Set null to not send the message.
     */
    public void setSitting(boolean sitting, Player player) {
        if (sitting == this.isSitting())
            return;

        if (sitting && !this.isDefending() && !this.isEating() && !this.isAttacking() && !this.isDrinking() && !this.isPlaying() && !this.isBreeding() && !this.isTakingOff() && !this.isFlying()) {
            this.setStatus(this.getStatus() | States.SITTING);
            this.setJumping(false);
            this.getNavigation().stop();
            this.setTarget(null);
            this.handleSittingText(player);
        } else if (sitting) {
            // Creature is busy (defending/attacking/eating/...): do not flip the state,
            // just tell the player the creature cannot sit right now.
            if (player != null) {
                if (this.hasCustomName())
                    player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.interaction.cannotSit")));
                else
                    player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.interaction.cannotSit")));
            }
        } else {
            this.setStatus(this.getStatus() & ~States.SITTING);
            this.setStatus(this.getStatus() & ~States.DEFENDING);
            this.setStatus(this.getStatus() & ~States.ATTACKING);
            this.setTarget(null);
            this.handleSittingText(player);
        }
    }

    /**
     * Sets true for the sitting state and false for the stressed and defending states.
     */
    public void forceSitting(Player player) {
        this.setStatus(this.getStatus() & ~States.DEFENDING);
        this.setStatus(this.getStatus() & ~States.ATTACKING);
        this.setStatus(this.getStatus() & ~States.EATING);
        this.setStatus(this.getStatus() & ~States.DRINKING);
        this.setStatus(this.getStatus() & ~States.PLAYING);
        this.setStatus(this.getStatus() & ~States.BREEDING);
        this.setStatus(this.getStatus() & ~States.SWIMMING);
        this.setStatus(this.getStatus() & ~States.FLYING);
        this.setStatus(this.getStatus() | States.SITTING);
        this.setJumping(false);
        this.getNavigation().stop();
        this.setTarget(null);
        this.handleSittingText(player);
    }

    /**
     * Shows a text about the sitting state of the creature.
     */
    public void handleSittingText(Player player) {
        if (player != null) {
            if (this.isSitting()) {
                if (this.hasCustomName())
                    player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.interaction.isSitting")));
                else
                    player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.interaction.isSitting")));
            } else {
                if (this.hasCustomName())
                    player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.interaction.isNotSitting")));
                else
                    player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.interaction.isNotSitting")));
            }
        }
    }

    /**
     * Returns true if the creature is sleeping.
     */
    public boolean isSleeping() {
        return (this.getStatus() & States.SLEEPING) == States.SLEEPING;
    }

    /**
     * Sets if the creature is sleeping.
     */
    public void setSleeping(boolean sleeping) {
        if (sleeping && this.isSitting() && !this.isDefending() && !this.isAttacking() && !this.isEating() && !this.isDrinking() && !this.isPlaying() && !this.isBreeding() && !this.isTakingOff() && !this.isFlying())
            this.setStatus(this.getStatus() | States.SLEEPING);
        else
            this.setStatus(this.getStatus() & ~States.SLEEPING);
    }

    /**
     * Returns true if the creature is hungry.
     */
    public boolean isHungry() {
        return (this.getStatus() & States.HUNGRY) == States.HUNGRY;
    }

    /**
     * Sets if the creature is hungry.
     */
    public void setHunger(boolean hungry) {
        if (hungry)
            this.setStatus(this.getStatus() | States.HUNGRY);
        else
            this.setStatus(this.getStatus() & ~States.HUNGRY);
    }

    /**
     * Returns true if the creature is eating.
     */
    public boolean isEating() {
        return (this.getStatus() & States.EATING) == States.EATING;
    }

    /**
     * Sets if the creature is eating.
     */
    public void setEating(boolean eating) {
        if (eating && !this.isSleeping() && !this.isDefending() && !this.isAttacking() && !this.isSitting() && !this.isDrinking() && !this.isPlaying() && !this.isBreeding() && !this.isTakingOff() && !this.isFlying())
            this.setStatus(this.getStatus() | States.EATING);
        else
            this.setStatus(this.getStatus() & ~States.EATING);
    }

    /**
     * Returns true if the creature is thirsty.
     */
    public boolean isThirsty() {
        return (this.getStatus() & States.THIRSTY) == States.THIRSTY;
    }

    /**
     * Sets if the creature is thirsty.
     */
    public void setThirsty(boolean thirsty) {
        if (thirsty)
            this.setStatus(this.getStatus() | States.THIRSTY);
        else
            this.setStatus(this.getStatus() & ~States.THIRSTY);
    }

    /**
     * Returns true if the creature is drinking.
     */
    public boolean isDrinking() {
        return (this.getStatus() & States.DRINKING) == States.DRINKING;
    }

    /**
     * Sets if the creature is drinking.
     */
    public void setDrinking(boolean drinking) {
        if (drinking && !this.isSleeping() && !this.isSitting() && !this.isDefending() && !this.isAttacking() && !this.isEating() && !this.isPlaying() && !this.isBreeding() && !this.isTakingOff() && !this.isFlying())
            this.setStatus(this.getStatus() | States.DRINKING);
        else
            this.setStatus(this.getStatus() & ~States.DRINKING);
    }

    /**
     * Returns true if the creature is injured.
     */
    public boolean isInjured() {
        return (this.getStatus() & States.INJURED) == States.INJURED;
    }

    /**
     * Sets if the creature is injured.
     */
    public void setInjured(boolean injured) {
        if (injured)
            this.setStatus(this.getStatus() | States.INJURED);
        else
            this.setStatus(this.getStatus() & ~States.INJURED);
    }

    /**
     * Returns true if the creature is socializing.
     */
    public boolean isSocializing() {
        return (this.getStatus() & States.SOCIALIZING) == States.SOCIALIZING;
    }

    /**
     * Sets if the creature is socializing.
     */
    public void setSocializing(boolean socializing) {
        if (socializing && !this.isSleeping() && !this.isDefending() && !this.isAttacking() && !this.isBreeding())
            this.setStatus(this.getStatus() | States.SOCIALIZING);
        else
            this.setStatus(this.getStatus() & ~States.SOCIALIZING);
    }

    /**
     * Returns true if the creature is defending itself from some threat.
     */
    public boolean isDefending() {
        return (this.getStatus() & States.DEFENDING) == States.DEFENDING;
    }

    /**
     * Sets if the creature is defending itself from some threat.
     */
    public void setDefending(boolean defending) {
        if (defending && !this.isSleeping())
            this.setStatus(this.getStatus() | States.DEFENDING);
        else
            this.setStatus(this.getStatus() & ~States.DEFENDING);
    }

    /**
     * Returns the required number of creature of the same type to attack as group.
     */
    public int getNumberOfAllies() {
        return this.numberOfAllies;
    }

    /**
     * Sets the required number of creature of the same type to attack as group.
     */
    public void setNumberOfAllies(int numberOfAllies) {
        this.numberOfAllies = numberOfAllies;
    }

    /**
     * Returns true if the creature is attacking.
     */
    public boolean isAttacking() {
        return (this.getStatus() & States.ATTACKING) == States.ATTACKING;
    }

    /**
     * Sets if the creature is attacking.
     */
    public void setAttacking(boolean attacking) {
        if (attacking && !this.isSleeping())
            this.setStatus(this.getStatus() | States.ATTACKING);
        else
            this.setStatus(this.getStatus() & ~States.ATTACKING);
    }

    /**
     * Returns true if the creature is attacking.
     */
    public boolean isAngry() {
        return (this.getStatus() & States.ANGRY) == States.ANGRY;
    }

    /**
     * Sets if the creature is attacking.
     */
    public void setAngry(boolean angry) {
        if (angry && !this.isSleeping() && !this.isFleeing())
            this.setStatus(this.getStatus() | States.ANGRY);
        else
            this.setStatus(this.getStatus() & ~States.ANGRY);
    }

    /**
     * Returns the angry ticks of the creature. Higher than zero means that the creature is attacking.
     */
    public int getAngerLevel() {
        return this.angryTicks;
    }

    /**
     * Sets the angry ticks of the creature. When it is positive, it can be reduced each tick using some AI.
     */
    public void setAngerLevel(int angryTicks) {
        this.angryTicks = angryTicks;
    }

    /**
     * Sets the angry level of this creature.
     */
    protected void setCreatureAngry(EntityJurassiCraftAggressive creature, Entity attacker) {
        if (attacker instanceof LivingEntity)
            creature.becomeAngry((LivingEntity) attacker, 0.0F);
    }

    /**
     * Sets this creature to attack a target if it has a proper age. If it is also tamed, this will check if the target is tamed by the owner of this creature.
     */
    protected void becomeAngry(LivingEntity target, float age) {
        if (this.isCreatureOlderThan(age)) {
            if (this.isTamed()) {
                if (this.checkTargetBeforeAttacking(target)) {
                    if (this.isSitting())
                        this.setSitting(false, null);
                    this.setTarget(target);
                    this.setAngry(true);
                }
            } else {
                if (this.isSitting())
                    this.setSitting(false, null);

                this.setTarget(target);
                this.setAngry(true);
            }
        }
    }

    /**
     * Returns true if the creature is defending itself from some threat.
     */
    public boolean isFleeing() {
        return (this.getStatus() & States.FLEEING) == States.FLEEING;
    }

    /**
     * Sets if the creature is defending itself from some threat.
     */
    public void setFleeing(boolean fleeing) {
        if (fleeing && !this.isSleeping())
            this.setStatus(this.getStatus() | States.FLEEING);
        else
            this.setStatus(this.getStatus() & ~States.FLEEING);
    }

    /**
     * Returns the fleeing ticks of the creature. Higher than zero means that the creature was attacked and it is fleeing.
     */
    public int getFleeingTick() {
        return fleeingTick;
    }

    /**
     * Sets the fleeing tick value of the creature. When it is positive, it can be reduced each tick using some AI.
     */
    public void setFleeingTick(int value) {
        this.fleeingTick = value;
    }

    /**
     * Sets the creature to flee.
     */
    protected void startFleeing() {
        if (this.isSitting())
            this.setSitting(false, null);

        this.setTarget(null);
        this.setFleeing(true);
    }

    /**
     * Returns true if the creature was damaged recently.
     */
    public boolean hasBeenHurt() {
        return this.hurtTime > 0;
    }

    /**
     * Returns true if the creature is playing.
     */
    public boolean isPlaying() {
        return (this.getStatus() & States.PLAYING) == States.PLAYING;
    }

    /**
     * Sets if the creature is playing.
     */
    public void setPlaying(boolean playing) {
        if (playing && !this.isSleeping() && !this.isEating() && !this.isDrinking() && !this.isDefending() && !this.isAttacking() && !this.isTakingOff())
            this.setStatus(this.getStatus() | States.PLAYING);
        else
            this.setStatus(this.getStatus() & ~States.PLAYING);
    }

    /**
     * Returns true if the creature is stalking.
     */
    public boolean isStalking() {
        return (this.getStatus() & States.STALKING) == States.STALKING;
    }

    /**
     * Sets if the creature is stalking.
     */
    public void setStalking(boolean stalking) {
        if (stalking && !this.isSitting() && !this.isSleeping() && !this.isEating() && !this.isDrinking() && !this.isDefending() && !this.isTakingOff() && !this.isFlying())
            this.setStatus(this.getStatus() | States.STALKING);
        else
            this.setStatus(this.getStatus() & ~States.STALKING);
    }

    /**
     * Returns true if the creature is breeding.
     */
    public boolean isBreeding() {
        return (this.getStatus() & States.BREEDING) == States.BREEDING;
    }

    /**
     * Sets if the creature is breeding.
     */
    public void setBreeding(boolean breeding) {
        if (breeding && !this.isSitting() && !this.isSleeping() && !this.isDefending() && !this.isAttacking() && !this.isEating() && !this.isDrinking() && !this.isTakingOff() && !this.isFlying())
            this.setStatus(this.getStatus() | States.BREEDING);
        else
            this.setStatus(this.getStatus() & ~States.BREEDING);
    }

    /**
     * Returns true if the creature is in love.
     */
    public boolean isInLove() {
        return (this.getStatus() & States.INLOVE) == States.INLOVE;
    }

    /**
     * Sets if the creature is in love.
     */
    public void setInLove(boolean inLove) {
        if (inLove && !this.isSleeping())
            this.setStatus(this.getStatus() | States.INLOVE);
        else
            this.setStatus(this.getStatus() & ~States.INLOVE);
    }

    /**
     * Returns true if the creature can be tamed when spawning from an egg. You must call this method to set the nearest player as the owner. This value is set in the assets.jurassicraft.json file.
     */
    public boolean canBeTamedUponSpawning() {
        return this.getCreature().canBeTamedUponSpawning();
    }

    /**
     * Clear all states from this creature, except for the injury and tamed state.
     */
    public void clearStatus() {
        this.setStatus(this.getStatus() & ~States.SITTING);
        this.setStatus(this.getStatus() & ~States.SLEEPING);
        this.setStatus(this.getStatus() & ~States.HUNGRY);
        this.setStatus(this.getStatus() & ~States.THIRSTY);
        this.setStatus(this.getStatus() & ~States.EATING);
        this.setStatus(this.getStatus() & ~States.DRINKING);
        this.setStatus(this.getStatus() & ~States.SOCIALIZING);
        this.setStatus(this.getStatus() & ~States.DEFENDING);
        this.setStatus(this.getStatus() & ~States.ATTACKING);
        this.setStatus(this.getStatus() & ~States.FLEEING);
        this.setStatus(this.getStatus() & ~States.PLAYING);
        this.setStatus(this.getStatus() & ~States.STALKING);
        this.setStatus(this.getStatus() & ~States.INLOVE);
    }

    public EntityJurassiCraftAggressive getClosestEntityAggressive(LivingEntity creature, double x, double y, double z) {
        List<Entity> nearbyEntities = creature.level().getEntities(creature, creature.getBoundingBox().inflate(x, y, z));

        ArrayList<EntityJurassiCraftAggressive> listOfTargets = new ArrayList<EntityJurassiCraftAggressive>();

        for (Entity entity : nearbyEntities) {
            if (entity instanceof EntityJurassiCraftAggressive) {
                listOfTargets.add((EntityJurassiCraftAggressive) entity);
            }
        }

        if (!listOfTargets.isEmpty()) {
            EntityJurassiCraftAggressive closestAggressive = null;
            double distanceSq = x * x + y * y + z * z;

            for (EntityJurassiCraftAggressive closeTarget : listOfTargets) {
                double nextDistance = creature.distanceToSqr(closeTarget);

                if (nextDistance < distanceSq) {
                    distanceSq = nextDistance;
                    closestAggressive = closeTarget;
                }
            }

            return closestAggressive;
        }

        return null;
    }

    public ArrayList<EntityJurassiCraftAggressive> getClosestEntityAggressiveList(LivingEntity creature, double x, double y, double z) {
        List<Entity> closest = creature.level().getEntities(creature, creature.getBoundingBox().inflate(x, y, z));
        ArrayList<EntityJurassiCraftAggressive> listOfTargets = new ArrayList<EntityJurassiCraftAggressive>();

        for (Entity entity : closest) {
            if (entity instanceof EntityJurassiCraftAggressive)
                listOfTargets.add((EntityJurassiCraftAggressive) entity);
        }

        return listOfTargets;
    }

    public EntityJurassiCraftProtective getClosestEntityProtective(LivingEntity creature, double x, double y, double z) {
        List<Entity> list = creature.level().getEntities(creature, creature.getBoundingBox().inflate(x, y, z));

        ArrayList<EntityJurassiCraftProtective> targets = new ArrayList<EntityJurassiCraftProtective>();

        for (Entity entity : list) {
            if (entity instanceof EntityJurassiCraftProtective) {
                targets.add((EntityJurassiCraftProtective) entity);
            }
        }

        if (!targets.isEmpty()) {
            EntityJurassiCraftProtective closestProtective = null;
            double distanceSq = x * x + y * y + z * z;

            for (EntityJurassiCraftProtective closeTarget : targets) {
                double nextDistance = creature.distanceToSqr(closeTarget);

                if (nextDistance < distanceSq) {
                    distanceSq = nextDistance;
                    closestProtective = closeTarget;
                }
            }

            return closestProtective;
        }

        return null;
    }

    public ArrayList<EntityJurassiCraftProtective> getClosestEntityProtectiveList(LivingEntity creature, double x, double y, double z) {
        List<Entity> nearby = creature.level().getEntities(creature, creature.getBoundingBox().inflate(x, y, z));

        ArrayList<EntityJurassiCraftProtective> targets = new ArrayList<EntityJurassiCraftProtective>();

        for (Entity entity : nearby) {
            if (entity instanceof EntityJurassiCraftProtective)
                targets.add((EntityJurassiCraftProtective) entity);
        }

        return targets;
    }

    public EntityJurassiCraftCoward getClosestEntityCoward(LivingEntity creature, double x, double y, double z) {
        List<Entity> list = creature.level().getEntities(creature, creature.getBoundingBox().inflate(x, y, z));

        ArrayList<EntityJurassiCraftCoward> targets = new ArrayList<EntityJurassiCraftCoward>();

        for (Entity entity : list) {
            if (entity instanceof EntityJurassiCraftCoward)
                targets.add((EntityJurassiCraftCoward) entity);
        }

        if (!targets.isEmpty()) {
            EntityJurassiCraftCoward closestCoward = null;
            double distanceSq = x * x + y * y + z * z;

            for (EntityJurassiCraftCoward closeTarget : targets) {
                double nextDistance = creature.distanceToSqr(closeTarget);

                if (nextDistance < distanceSq) {
                    distanceSq = nextDistance;
                    closestCoward = closeTarget;
                }
            }

            return closestCoward;
        }

        return null;
    }

    public ArrayList<EntityJurassiCraftCoward> getClosestEntityCowardList(LivingEntity creature, double x, double y, double z) {
        List<Entity> nearby = creature.level().getEntities(creature, creature.getBoundingBox().inflate(x, y, z));
        ArrayList<EntityJurassiCraftCoward> targets = new ArrayList<EntityJurassiCraftCoward>();

        for (Entity entity : nearby) {
            if (entity instanceof EntityJurassiCraftCoward)
                targets.add((EntityJurassiCraftCoward) entity);
        }

        return targets;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte flag) {
        if (flag == 7) {
            this.playTameEffect(true);
        } else if (flag == 6) {
            this.playTameEffect(false);
        } else {
            super.handleEntityEvent(flag);
        }
    }

    /**
     * Spawns particles depending on the flag. It is used in vanilla creatures when they are being tamed.
     *
     * @param heart true spawns heart particles, whereas false spawns smoke particles.
     */
    protected void playTameEffect(boolean heart) {
        for (int i = 0; i < 7; i++) {
            double d0 = this.getRandom().nextGaussian() * 0.02D;
            double d1 = this.getRandom().nextGaussian() * 0.02D;
            double d2 = this.getRandom().nextGaussian() * 0.02D;
            this.level().addParticle(heart ? ParticleTypes.HEART : ParticleTypes.SMOKE, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (double) (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
        }
    }

    public String getOwnerName() {
        return this.getEntityData().get(OWNER);
    }

    /**
     * Returns true if the entity is the creature owner.
     */
    public boolean isOwner(Entity possibleOwner) {
        return possibleOwner == this.getOwner();
    }

    public boolean allowLeashing() {
        return !this.isLeashed() && this.isTamed() && !this.isTakingOff() && !this.isFlying();
    }

    @Override
    public LivingEntity getOwner() {
        String ownerName = this.getOwnerName();

        if (ownerName != null && !ownerName.isEmpty()) {
            for (Player player : this.level().players()) {
                if (player.getName().getString().equals(ownerName))
                    return player;
            }
        }

        return null;
    }

    public void setOwner(String owner) {
        this.getEntityData().set(OWNER, owner);
    }

    @Override
    public Team getTeam() {
        if (this.isTamed()) {
            LivingEntity owner = this.getOwner();

            if (owner != null)
                return owner.getTeam();
        }

        return super.getTeam();
    }

    @Override
    public boolean isAlliedTo(Entity creature) {
        if (this.isTamed()) {
            LivingEntity owner = this.getOwner();

            if (creature == owner)
                return true;

            if (owner != null)
                return owner.isAlliedTo(creature);
        }

        return super.isAlliedTo(creature);
    }

    @Override
    public UUID getOwnerUUID() {
        return null;
    }

    /**
     * Returns true if the target of this creature is not the owner or other creature from the same owner or same species or riding or being ridden by this creature.
     */
    public boolean checkTargetBeforeAttacking(LivingEntity target) {
        if (target.getPassengers().contains(this) || this.getPassengers().contains(target) || target.getVehicle() != null)
            return false;

        if (target == null || target == this || target == this.getOwner()) {
            if (target instanceof EntityJurassiCraftSmart) {
                return !this.isOwner(((EntityJurassiCraftSmart) target).getOwner());
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * This is a separated entity living base that can be set in order to add attacks or animations
     */
    public LivingEntity getCreatureToAttack() {
        return this.creatureToAttack;
    }

    /**
     * This is a separated entity living base that can be set in order to add attacks or animations.
     *
     * @param creature is the target;
     */
    public void setCreatureToAttack(LivingEntity creature) {
        if (creature != this)
            this.creatureToAttack = creature;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putInt("Status", this.getStatus());
        compound.putInt("FleeingTick", this.getFleeingTick());
        compound.putInt("AngerLevel", this.getAngerLevel());

        compound.putString("Owner", this.getOwnerName() != null ? this.getOwnerName() : "");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("Status"))
            this.setStatus(compound.getInt("Status"));
        if (compound.contains("FleeingTick"))
            this.setFleeingTick(compound.getInt("FleeingTick"));
        if (compound.contains("AngerLevel"))
            this.setAngerLevel(compound.getInt("AngerLevel"));

        if (compound.contains("Owner")) {
            String ownerName = compound.getString("Owner");

            if (ownerName != null && ownerName.length() > 0)
                this.setOwner(ownerName);
        } else {
            this.setOwner("");
        }
    }
}
