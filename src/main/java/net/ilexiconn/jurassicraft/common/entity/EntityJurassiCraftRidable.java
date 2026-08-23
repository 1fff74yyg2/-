package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.jurassicraft.common.item.ItemGrowthSerum;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityJurassiCraftRidable extends EntityJurassiCraftSmart {
    private float mountingSpeed;

    public EntityJurassiCraftRidable(EntityType<? extends EntityJurassiCraftRidable> type, Level world) {
        super(type, world);

        this.setMountingSpeed((float) (this.getCreature().getRidingSpeed()));
    }

    public EntityJurassiCraftRidable(Level world) {
        super(world);

        this.setMountingSpeed((float) (this.getCreature().getRidingSpeed()));
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    /**
     * Called in the aiStep() when this entity is being ridden by a player that is right clicking;
     */
    public void ridingPlayerRightClick() {
    }

    public boolean isCreatureRidable() {
        return this.getCreature().isRidable();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack playerItemStack = player.getInventory().getSelected();

        // Tamed creatures: right-click toggles the sit/stand state exactly once.
        if (!this.level().isClientSide && this.isTamed() && this.isOwner(player)) {
            ItemStack heldStack = playerItemStack;
            boolean isFavoriteFood = heldStack != null && heldStack.getItem() != null && this.getCreature().isFavoriteFood(heldStack.getItem());
            boolean isGrowthSerum = heldStack != null && heldStack.getItem() instanceof ItemGrowthSerum;
            if ((!this.checkRidingItem(playerItemStack) || this.isSitting()) && !isFavoriteFood && !isGrowthSerum) {
                if (this.consumeSitToggle()) {
                    if (this.isSitting()) {
                        this.setSitting(false, player);
                    } else {
                        this.forceSitting(player);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (!this.level().isClientSide && this.checkRidingItem(playerItemStack)) {
            if (this.isCreatureRidable() && this.isTamed() && this.isCreatureAdult() && !this.isSitting() && !this.isSleeping() && !this.isAttacking() && !this.isDefending() && this.getRider() == null && player.getName().getString().equals(this.getOwnerName())) {
                this.setRidingPlayer(player);
                return InteractionResult.SUCCESS;
            } else {
                if (!this.isCreatureRidable()) {
                    if (this.hasCustomName())
                        player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.riding.notRidable")));
                    else
                        player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.riding.notRidable")));
                } else if (!this.isTamed()) {
                    if (this.hasCustomName())
                        player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.riding.notTamed")));
                    else
                        player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.riding.notTamed")));
                } else if (!this.isCreatureAdult()) {
                    if (this.hasCustomName())
                        player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.riding.notAdult")));
                    else
                        player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.riding.notAdult")));
                } else if (this.isSitting()) {
                    if (this.hasCustomName())
                        player.sendSystemMessage(Component.literal(this.getCustomName().getString() + " (" + I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + ") " + I18nCompat.get("entity.riding.sitting")));
                    else
                        player.sendSystemMessage(Component.literal(I18nCompat.get("entity." + this.getCreature().getCreatureName() + ".name") + " " + I18nCompat.get("entity.riding.sitting")));
                } else if (this.getRider() != null) {
                    player.sendSystemMessage(Component.literal(I18nCompat.get("entity.riding.isRiding")));
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    protected boolean checkRidingItem(ItemStack ridingItem) {
        return ridingItem != null && this.getCreature().isRidingItem(ridingItem.getItem());
    }

    public float getMountingSpeed() {
        return this.mountingSpeed;
    }

    public void setMountingSpeed(float speed) {
        this.mountingSpeed = speed;
    }

    /**
     * Returns the entity currently riding this entity (the rider), or null if there is none.
     */
    private Entity getRider() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void setRidingPlayer(Player player) {
        player.setYRot(this.getYRot());
        player.setXRot(this.getXRot());
        player.startRiding(this);
    }

    /**
     * Sets the mob rotation depending on where the player is looking (Horse Style). ID: 0.
     */
    protected void handleMouseControlledRiding() {
        this.yRotO = this.getYRot(); this.setYRot(this.getRider().getYRot());
        this.setXRot(this.getRider().getXRot() * 0.5F);
        
        this.yHeadRot = this.yBodyRot = this.getYRot();
        
    }

    /**
     * Sets the mob rotation depending on the item position (Pig Style). ID: 1.
     */
    protected void handleFastItemControlledRiding() {
        this.setSpeed(this.getSpeed() * 0.1F);

        float adjust = Mth.wrapDegrees(((LivingEntity) this.getRider()).getYRot() - this.getYRot()) * 0.5F;

        if (adjust > 6.0F)
            adjust = 6.0F;
        else if (adjust < -6.0F)
            adjust = -6.0F;

        this.setYRot(Mth.wrapDegrees(this.getYRot() + adjust));
        
    }

    /**
     * Sets the mob rotation depending on the item position (Pig Style). ID: 2.
     */
    protected void handleSlowItemControlledRiding() {
        this.setSpeed(this.getSpeed() * 0.1F);

        float adjust = Mth.wrapDegrees(((LivingEntity) this.getRider()).getYRot() - this.getYRot()) * 0.5F;

        if (adjust > 2.0F)
            adjust = 2.0F;
        else if (adjust < -2.0F)
            adjust = -2.0F;

        this.setYRot(Mth.wrapDegrees(this.getYRot() + adjust));
        
    }

    /**
     * Sets the mob rotation depending on the item position (Pig Style). ID: 3.
     */
    protected void handleVerySlowItemControlledRiding() {
        this.setSpeed(this.getSpeed() * 0.1F);

        float adjust = Mth.wrapDegrees(((LivingEntity) this.getRider()).getYRot() - this.getYRot()) * 0.5F;

        if (adjust > 0.8F)
            adjust = 0.8F;
        else if (adjust < -0.8F)
            adjust = -0.8F;

        this.setYRot(Mth.wrapDegrees(this.getYRot() + adjust));
        
    }

    /**
     * Sets the mob rotation depending on A and D keys.
     * 1.20.1: reads the rider's horizontal input (xxa) instead of the client-only
     * Options class so this works on both sides.
     */
    protected void handleKeyboardControlledRiding(float adjustment) {
        float adjustPitch = 0.0F;
        float adjustYaw = 0.0F;

        if (this.getRider() instanceof Player) {
            Player rider = (Player) this.getRider();
            if (rider.xxa > 0.0F)
                adjustYaw = adjustYaw + adjustment;
            if (rider.xxa < 0.0F)
                adjustYaw = adjustYaw - adjustment;
        }

        this.setYRot(Mth.wrapDegrees(this.getYRot() + adjustYaw));
        this.setXRot(Mth.wrapDegrees(this.getXRot() + adjustPitch));

        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.getRider() != null && this.getRider() instanceof Player && this.checkRidingItem(((Player) this.getRider()).getMainHandItem())) {
            Player player = (Player) this.getRider();

            switch (this.getCreature().getRidingStyle()) {
                case 0:
                    this.handleMouseControlledRiding();
                    break;
                case 1:
                    this.handleFastItemControlledRiding();
                    break;
                case 2:
                    this.handleSlowItemControlledRiding();
                    break;
                case 3:
                    this.handleVerySlowItemControlledRiding();
                    break;
                default:
                    this.handleSlowItemControlledRiding();
            }

            this.setMaxUpStep(1.0F);

            float movementStrafing = 0.25F * player.xxa * this.getMountingSpeed();
            float movementForward;

            if (this.zza < 0)
                movementForward = player.zza * 0.3F * this.getMountingSpeed();
            else
                movementForward = player.zza * this.getMountingSpeed();

            this.decreaseHeldItemDurability(1);

            if (!this.level().isClientSide)
                super.travel(new Vec3(movementStrafing, travelVector.y, movementForward));

            this.handleLimbMovement();
        } else {
            this.setMaxUpStep(0.5F);
            // 1.20.1: do NOT setSpeed(0.02F) here. In 1.12.2 setAIMoveSpeed did not
            // affect AI-driven movement (that used the moveForward input), but in 1.20.1
            // setSpeed() IS the speed source used by travel() - MoveControl sets it to
            // speedModifier * MOVEMENT_SPEED every tick, and overriding it with 0.02F
            // here made every unridden creature crawl at ~0.02 blocks/tick.
            super.travel(travelVector);
        }
    }

    public void rideJump() {
        if (this.onGround() && !this.jumping) {
            this.decreaseHeldItemDurability(20);
            this.jumpFromGround();
        }
    }

    /**
     * Makes corrections to the limbSwing.
     */
    protected void handleLimbMovement() {
        double pointX = this.getX() - this.xo;
        double pointZ = this.getZ() - this.zo;

        float distance = Mth.sqrt((float) (pointX * pointX + pointZ * pointZ)) * 4.0F;

        if (distance > 1.0F)
            distance = 1.0F;

        this.walkAnimation.setSpeed(this.walkAnimation.speed() + (distance - this.walkAnimation.speed()) * 0.4F);
    }

    /**
     * Decreases the held item durability and destroys the item if stack size is 0 or less.
     */
    protected void decreaseHeldItemDurability(int damage) {
        Player player = (Player) this.getRider();

        ItemStack heldItem = player.getMainHandItem();

        if (this.getRider() != null && heldItem.getDamageValue() + damage > heldItem.getMaxDamage()) {
            heldItem.shrink(1);

            if (heldItem.getCount() <= 0) {
                player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
            }
        } else {
            heldItem.setDamageValue(heldItem.getDamageValue() + damage);
        }
    }

    @Override
    public boolean onClimbable() {
        return false;
    }
}
