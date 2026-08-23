package net.ilexiconn.jurassicraft.common.tileentity;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftFoodNutrients;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.item.ItemDNA;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileCultivate extends BlockEntity implements Container {
    public final Map<Short, Byte> growthRateList = new HashMap<Short, Byte>();
    private final short maxValue = 3000;
    public int animationTick;
    public int rotation;
    private Random random;
    private ItemStack[] slots = new ItemStack[4];
    private short cultivateTime;
    private int cultivateSpeed;
    private byte waterStored;
    private short proximateValue;
    private short mineralValue;
    private short vitaminValue;
    private short lipidValue;
    private float creatureSize;
    private boolean shouldUpdate;
    private Creature creature;

    public TileCultivate() {
        this(BlockPos.ZERO, null);
    }

    public TileCultivate(BlockPos pos, BlockState state) {
        super(JCTileEntityRegistry.CULTIVATOR.get(), pos, state);
        this.random = new Random();
        this.cultivateSpeed = 100;
        this.creature = null;
        this.waterStored = 0;
        this.cultivateTime = 0;
        this.proximateValue = 0;
        this.mineralValue = 0;
        this.vitaminValue = 0;
        this.lipidValue = 0;
        this.creatureSize = 0.0F;
    }

    public Creature getCreature() {
        return this.creature;
    }

    /**
     * Returns the current hatchery time.
     */
    public int getCultivateTime() {
        return this.cultivateTime;
    }

    /**
     * Returns the current hatchery time.
     */
    public void setCultivateTime(short time) {
        this.cultivateTime = time;
    }

    /**
     * Returns the maximum value of nutrients that can be stored.
     */
    public int getMaximumValueOfNutrients() {
        return this.maxValue;
    }

    public float getCreatureSize() {
        return this.creatureSize;
    }

    public void setCreatureSize(float time, float speed) {
        if (speed > 0) {
            this.creatureSize = time / speed;
        } else {
            this.creatureSize = 0.0F;
        }
    }

    /**
     * Returns the current water stored.
     */
    public byte getWaterStored() {
        return this.waterStored;
    }

    /**
     * Sets the water value.
     */
    public void setWaterStored(byte water) {
        this.waterStored = water;
    }

    /**
     * Returns true if there is water stored.
     */
    public boolean hasWater() {
        return (this.waterStored > 0);
    }

    /**
     * Returns the current proximate value.
     */
    public int getProximateValue() {
        return this.proximateValue;
    }

    /**
     * Sets the current value of proximate.
     */
    public void setProximateValue(short proximate) {
        this.proximateValue = proximate;
    }

    /**
     * Returns true if there is proximate stored.
     */
    public boolean hasProximate() {
        return (this.proximateValue > 0);
    }

    /**
     * Returns the current minerals value.
     */
    public int getMineralValue() {
        return this.mineralValue;
    }

    /**
     * Sets the current value of minerals.
     */
    public void setMineralValue(short mineral) {
        this.mineralValue = mineral;
    }

    /**
     * Returns true if there is mineral stored.
     */
    public boolean hasMineral() {
        return (this.mineralValue > 0);
    }

    /**
     * Returns the current vitamin value.
     */
    public int getVitaminValue() {
        return this.vitaminValue;
    }

    /**
     * Sets the current value of vitamins.
     */
    public void setVitaminValue(short vitamin) {
        this.vitaminValue = vitamin;
    }

    /**
     * Returns true if there is vitamin stored.
     */
    public boolean hasVitamin() {
        return (this.vitaminValue > 0);
    }

    /**
     * Returns the current value of lipids.
     */
    public int getLipidValue() {
        return this.lipidValue;
    }

    /**
     * Sets the current value of lipids.
     */
    public void setLipidValue(short lipids) {
        this.lipidValue = lipids;
    }

    /**
     * Returns true if there is lipids stored.
     */
    public boolean hasLipid() {
        return (this.lipidValue > 0);
    }

    /**
     * Returns a percentage of the water stored scaled for X value.
     */
    public int getWaterStoredProgressScaled(int i) {
        return (this.waterStored * i) / 3;
    }

    /**
     * Sets the cultivate process speed.
     */
    public int setCultivateSpeed(int speed) {
        return this.cultivateSpeed = speed;
    }

    /**
     * Returns the number of ticks required to cultivate the creature.
     */
    public int getCultivateSpeed() {
        return this.cultivateSpeed;
    }

    /**
     * Returns a percentage of the hatchery process scaled for X value.
     */
    public int getCultivateTimeProgressScaled(int i) {
        if (this.getCultivateSpeed() <= 0)
            this.setCultivateSpeed(100);

        return (this.getCultivateTime() * i) / this.getCultivateSpeed();
    }

    /**
     * Returns a percentage of the proximate value scaled for X value.
     */
    public int getProximateBarScaled(int i) {
        return (this.getProximateValue() * i) / this.getMaximumValueOfNutrients();
    }

    /**
     * Returns a percentage of the mineral value scaled for X value.
     */
    public int getMineralBarScaled(int i) {
        return (this.getMineralValue() * i) / this.getMaximumValueOfNutrients();
    }

    /**
     * Returns a percentage of vitamins scaled for X value.
     */
    public int getVitaminBarScaled(int i) {
        return (this.getVitaminValue() * i) / this.getMaximumValueOfNutrients();
    }

    /**
     * Returns a percentage of lipids scaled for X value.
     */
    public int getLipidBarScaled(int i) {
        return (this.getLipidValue() * i) / this.getMaximumValueOfNutrients();
    }

    /**
     * Returns true if the hatchery time is larger than 0.
     */
    public boolean isHatching() {
        return (this.cultivateTime > 0);
    }

    /**
     * Returns true if the machine can consume the water bucket at slot[0].
     */
    private boolean canCansumeWaterBucket() {
        if (this.slots[0] != null && !this.slots[0].isEmpty() && this.slots[0].getItem() == Items.WATER_BUCKET) {
            // 1.20.1: an emptied slot holds ItemStack.EMPTY, not null - accept it too.
            if (this.slots[1] == null || this.slots[1].isEmpty() || this.slots[1].getItem() == Items.BUCKET)
                return true;
        }

        return false;
    }

    /**
     * Consumes water bucket from slot[0] if there is space for more water.
     */
    private void consumeWaterBucket() {
        if (!this.level.isClientSide) {
            if ((this.getWaterStored() + 1) > 3) {
                return;
            } else {
                this.slots[0].shrink(1);

                if (this.slots[0].getCount() <= 0)
                    this.slots[0] = ItemStack.EMPTY;

                this.setWaterStored((byte) (this.getWaterStored() + 1));

                if (this.slots[1] == null || this.slots[1].isEmpty()) {
                    ItemStack waterBucket = new ItemStack(Items.BUCKET);
                    this.slots[1] = waterBucket;
                } else {
                    this.slots[1].grow(1);
                }
            }
        }
    }

    /**
     * Returns true if the machine can consume food.
     */
    private boolean canCansumeFood() {
        if (this.slots[3] == null || this.slots[3].isEmpty()) {
            return false;
        } else {
            if ((proximateValue < this.getMaximumValueOfNutrients()) || (mineralValue < this.getMaximumValueOfNutrients()) || (vitaminValue < this.getMaximumValueOfNutrients()) || (lipidValue < this.getMaximumValueOfNutrients())) {
                if (JurassiCraftFoodNutrients.isValidFood(this.slots[3].getItem()))
                    return true;
            }

            return false;
        }
    }

    /**
     * Consumes food from slot[7] if it is valid.
     */
    private void consumeFood() {
        if (!level.isClientSide) {
            Item foodItem = this.slots[3].getItem();
            double foodProximate = JurassiCraftFoodNutrients.getNutrient(foodItem, 0);
            double foodMinerals = JurassiCraftFoodNutrients.getNutrient(foodItem, 1);
            double foodVitamins = JurassiCraftFoodNutrients.getNutrient(foodItem, 2);
            double foodLipids = JurassiCraftFoodNutrients.getNutrient(foodItem, 3);

            if (foodItem == Items.MILK_BUCKET) {
                this.slots[3] = ItemStack.EMPTY;
                this.slots[3] = new ItemStack(Items.BUCKET);
            } else {
                this.slots[3].shrink(1);

                if (this.slots[3].getCount() <= 0)
                    this.slots[3] = ItemStack.EMPTY;
            }

            if (proximateValue < this.getMaximumValueOfNutrients()) {
                proximateValue = (short) (proximateValue + (800 + random.nextInt(201)) * foodProximate);

                if (proximateValue > this.getMaximumValueOfNutrients())
                    proximateValue = (short) this.getMaximumValueOfNutrients();
            }

            if (mineralValue < this.getMaximumValueOfNutrients()) {
                mineralValue = (short) (mineralValue + (900 + random.nextInt(101)) * foodMinerals);

                if (mineralValue > this.getMaximumValueOfNutrients())
                    mineralValue = (short) this.getMaximumValueOfNutrients();
            }

            if (vitaminValue < this.getMaximumValueOfNutrients()) {
                vitaminValue = (short) (vitaminValue + (900 + random.nextInt(101)) * foodVitamins);

                if (vitaminValue > this.getMaximumValueOfNutrients())
                    vitaminValue = (short) this.getMaximumValueOfNutrients();
            }

            if (lipidValue < this.getMaximumValueOfNutrients()) {
                lipidValue = (short) (lipidValue + (980 + random.nextInt(101)) * foodLipids);

                if (lipidValue > this.getMaximumValueOfNutrients())
                    lipidValue = (short) this.getMaximumValueOfNutrients();
            }
        }
    }

    /**
     * Returns true if the machine can cultivate certain creature.
     */
    private boolean canCultivate() {
        ItemStack dnaSlot = getDNASlot();

        if (dnaSlot != null) {
            if (dnaSlot.hasTag()) {
                if (dnaSlot.getTag().contains("Quality") && dnaSlot.getTag().contains("DNA")) {
                    if (dnaSlot.getTag().getInt("Quality") >= 50) {
                        creature = CreatureHandler.getCreatureFromDNA((ItemDNA) dnaSlot.getItem());

                        return !(this.getProximateValue() < creature.getMinProximate() || this.getMineralValue() < creature.getMinMinerals() || this.getVitaminValue() < creature.getMinVitamins() || this.getLipidValue() < creature.getMinLipids());
                    }
                }
            }
        }

        return false;
    }

    /**
     * Creates the specific creature for the current DNA setup.
     */
    private void cultivateCreature() {
        if (!canCultivate()) {
            return;
        } else {
            CompoundTag compound = new CompoundTag();

            ItemStack cultivateResult = new ItemStack(((ItemDNA) getDNASlot().getItem()).getCorrespondingEggOrSyringe(), 1);

            if (creature.getEgg() != null)
                compound.putInt("EggQuality", getDNASlot().getTag().getInt("Quality"));

            compound.putString("EggDNA", getDNASlot().getTag().getString("DNA"));

            if (creature.getMammalSyringe() != null)
                compound.putInt("SyringeQuality", getDNASlot().getTag().getInt("Quality"));

            compound.putString("SyringeDNA", getDNASlot().getTag().getString("DNA"));

            cultivateResult.setTag(compound);

            this.slots[2] = ItemStack.EMPTY;
            this.slots[2] = cultivateResult;
            this.setCultivateTime((short) 0);
            this.setWaterStored((byte) 0);

            this.proximateValue = (short) (proximateValue - creature.getMinProximate());
            this.mineralValue = (short) (mineralValue - creature.getMinMinerals());
            this.vitaminValue = (short) (vitaminValue - creature.getMinVitamins());
            this.lipidValue = (short) (lipidValue - creature.getMinLipids());
        }
    }

    private ItemStack getDNASlot() {
        return this.slots[2];
    }

    /**
     * Resets a list of values to update the size of the creature for rendering.
     */
    private void recalculateGrowthRate() {
        if (creature != null) {
            int speed = creature.getCultivateSpeed();

            for (byte i = 0; i <= 10 - 1; i++) {
                if (i > 0)
                    this.growthRateList.put((short) ((i * speed) / 10), i);
                else
                    this.growthRateList.put((short) 2, (byte) 0);
            }
        }
    }

    /**
     * Resets cultivateTime and creature, and updates render.
     */
    private void resetBaseValues() {
        this.cultivateTime = 0;
        this.cultivateSpeed = 100;
        this.creature = null;
        this.growthRateList.clear();
    }

    /**
     * Check for any item stacks in all slots.
     */
    public boolean hasItems() {
        return this.slots[0] != null || this.slots[1] != null || getDNASlot() != null || this.slots[3] != null;
    }

    /**
     * Check for any item stacks in the DNA slot.
     */
    private boolean hasEmptyDNASlot() {
        ItemStack dna = getDNASlot();
        return (dna == null || dna.isEmpty()) ? true : false;
    }


    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        animationTick++;

        if (animationTick == Integer.MAX_VALUE)
            animationTick = 0;

        if (!this.level.isClientSide) {
            if (!this.isHatching()) {
                if (this.canCansumeWaterBucket())
                    this.consumeWaterBucket();

                if (canCansumeFood())
                    this.consumeFood();

                if (this.getWaterStored() >= 3) {
                    if ((this.getProximateValue() > 0) && (this.getMineralValue() > 0) && (this.getVitaminValue() > 0) && (this.getLipidValue() > 0)) {
                        if (this.canCultivate()) {
                            this.cultivateSpeed = creature.getCultivateSpeed();
                            this.recalculateGrowthRate();
                            this.setCultivateTime((short) 1);
                            this.creatureSize = 0.0F;
                        }
                    }
                }
            } else {
                this.cultivateTime++;

                if (this.growthRateList.containsKey(cultivateTime)) {
                    this.setCreatureSize(this.cultivateTime, this.cultivateSpeed);
                } else if (shouldUpdate) {
                    this.setCreatureSize(this.cultivateTime, this.cultivateSpeed);
                    this.shouldUpdate = false;
                }

                if (this.cultivateTime >= this.cultivateSpeed) {
                    this.cultivateCreature();
                    this.resetBaseValues();
                } else {
                    if (this.hasEmptyDNASlot())
                        this.resetBaseValues();
                }
            }
        }
    }

    public void cancelHatching(float progress) {
        if (this.isHatching()) {
            this.setProximateValue((short) (this.getProximateValue() - (int) (progress * creature.getMinProximate())));
            this.setMineralValue((short) (this.getMineralValue() - (int) (progress * creature.getMinMinerals())));
            this.setVitaminValue((short) (this.getVitaminValue() - (int) (progress * creature.getMinVitamins())));
            this.setLipidValue((short) (this.getLipidValue() - (int) (progress * creature.getMinLipids())));

            if (progress >= 0.75F)
                this.setWaterStored((byte) 0);
            else if (progress >= 0.5F)
                this.setWaterStored((byte) 1);
            else
                this.setWaterStored((byte) 2);

            this.cultivateTime = 0;
            this.cultivateSpeed = 100;
            this.creature = null;
            this.growthRateList.clear();
        }
    }

    @Override
    public int getContainerSize() {
        return this.slots.length;
    }

    public int getSizeInventory() {
        return this.getContainerSize();
    }

    @Override
    public ItemStack getItem(int i) {
        return this.slots[i] != null ? this.slots[i] : ItemStack.EMPTY;
    }

    public ItemStack getStackInSlot(int i) {
        return this.getItem(i);
    }

    @Override
    public ItemStack removeItem(int i, int stackSize) {
        if (this.slots[i] != null) {
            ItemStack splitedStack;

            if (this.slots[i].getCount() <= stackSize) {
                splitedStack = this.slots[i];
                this.slots[i] = ItemStack.EMPTY;

                return splitedStack;
            } else {
                splitedStack = this.slots[i].split(stackSize);

                if (this.slots[i].getCount() == 0)
                    this.slots[i] = ItemStack.EMPTY;

                return splitedStack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack decrStackSize(int i, int stackSize) {
        return this.removeItem(i, stackSize);
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.slots[i] != null) {
            ItemStack itemStack = this.slots[i];
            this.slots[i] = ItemStack.EMPTY;

            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.getStackInSlotOnClosing(index);
    }

    public ItemStack removeStackFromSlot(int index) {
        return this.removeItemNoUpdate(index);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.slots[i] = itemStack;

        if (itemStack != null && itemStack.getCount() > this.getMaxStackSize())
            if (this.getMaxStackSize() > 0) { itemStack.setCount(this.getMaxStackSize()); } else { itemStack = ItemStack.EMPTY; }
    }

    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.setItem(i, itemStack);
    }

    public String getInventoryName() {
        return "Hatchery";
    }

    public boolean hasCustomInventoryName() {
        return true;
    }

    public String getName() {
        return this.getInventoryName();
    }

    public boolean hasCustomName() {
        return this.hasCustomInventoryName();
    }

    public Component getDisplayName() {
        return Component.literal(this.getName());
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public int getInventoryStackLimit() {
        return this.getMaxStackSize();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.level.getBlockEntity(this.getBlockPos()) == this && player.distanceToSqr((double) this.getBlockPos().getX() + 0.5D, (double) this.getBlockPos().getY() + 0.5D, (double) this.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }

    public boolean isUseableByPlayer(Player player) {
        return this.stillValid(player);
    }

    public boolean isUsableByPlayer(Player player) {
        return this.stillValid(player);
    }

    public void openInventory() {

    }

    public void closeInventory() {

    }

    @Override
    public void startOpen(Player player) {

    }

    @Override
    public void stopOpen(Player player) {

    }

    public void openInventory(Player player) {
        this.startOpen(player);
    }

    public void closeInventory(Player player) {
        this.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return this.canPlaceItem(i, itemStack);
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {

    }

    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.slots.length; i++)
            this.slots[i] = null;
    }

    public void clear() {
        this.clearContent();
    }

    @Override
    public boolean isEmpty() {
        return !this.hasItems();
    }

    public int[] getAccessibleSlotsFromSide(int i) {
        return new int[] { 0 };
    }

    public int[] getSlotsForFace(Direction side) {
        return this.getAccessibleSlotsFromSide(side.ordinal());
    }

    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        return false;
    }

    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return false;
    }

    public boolean canInsertItem(int index, ItemStack stack, Direction direction) {
        return this.canInsertItem(index, stack, direction.ordinal());
    }

    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return this.canExtractItem(index, stack, direction.ordinal());
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        byte creatureID = -1;

        if (creature != null)
            creatureID = creature.getCreatureID();

        nbt.putByte("CreatureID", creatureID);
        nbt.putByte("Water", waterStored);
        nbt.putShort("Proximate", proximateValue);
        nbt.putShort("Mineral", mineralValue);
        nbt.putShort("Vitamin", vitaminValue);
        nbt.putShort("Lipid", lipidValue);
        nbt.putShort("cultivateTime", cultivateTime);
        nbt.putFloat("CreatureSize", creatureSize);

        ListTag list = new ListTag();

        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                CompoundTag compound = new CompoundTag();
                compound.putByte("Slot", (byte) i);
                this.slots[i].save(compound);
                list.add(compound);
            }
        }

        nbt.putInt("rotation", rotation);
        nbt.put("Items", list);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.shouldUpdate = true;

        ListTag list = nbt.getList("Items", 10);
        this.slots = new ItemStack[this.getContainerSize()];

        for (int slotId = 0; slotId < list.size(); slotId++) {
            CompoundTag compound = list.getCompound(slotId);
            byte slot = compound.getByte("Slot");

            if (slot >= 0 && slot < this.slots.length)
                this.slots[slot] = ItemStack.of(compound);
        }

        this.creature = CreatureHandler.getCreatureFromId(nbt.getByte("CreatureID"));
        this.creatureSize = nbt.getShort("CreatureSize");
        this.waterStored = nbt.getByte("Water");
        this.cultivateTime = nbt.getShort("cultivateTime");
        this.proximateValue = nbt.getShort("Proximate");
        this.mineralValue = nbt.getShort("Mineral");
        this.vitaminValue = nbt.getShort("Vitamin");
        this.lipidValue = nbt.getShort("Lipid");
        this.rotation = nbt.getInt("rotation");

        if (creature != null) {
            this.setCultivateSpeed(creature.getCultivateSpeed());
            this.recalculateGrowthRate();
        }
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
