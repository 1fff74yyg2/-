package net.ilexiconn.jurassicraft.common.tileentity;

import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TileDNACombinator extends BlockEntity implements Container {
    private static final short combinationSpeed = 100;
    private ItemStack[] slots = new ItemStack[3];
    private short combinationTime;

    public TileDNACombinator() {
        this(BlockPos.ZERO, null);
    }

    public TileDNACombinator(BlockPos pos, BlockState state) {
        super(JCTileEntityRegistry.DNA_COMBINATOR.get(), pos, state);
        this.combinationTime = 0;
    }

    public short getCombinationTime() {
        return combinationTime;
    }

    public void setCombinationTime(short time) {
        this.combinationTime = time;
    }

    public short getCombinationSpeed() {
        return combinationSpeed;
    }

    public int getCombinationProgressScaled(int i) {
        return (this.getCombinationTime() * i) / this.getCombinationSpeed();
    }

    public boolean isCombining() {
        return (this.getCombinationTime() > 0);
    }

    private boolean canCombine() {
        if (this.slots[0] == null || this.slots[1] == null)
            return false;
        else if (this.slots[0].getItem() != this.slots[1].getItem())
            return false;
        else if (!slots[0].hasTag() || !slots[1].hasTag())
            return false;
        else if (!slots[0].getTag().contains("Quality") || !slots[1].getTag().contains("Quality") || !slots[0].getTag().contains("DNA") || !slots[1].getTag().contains("DNA"))
            return false;
        else if (slots[0].getTag().getInt("Quality") + slots[1].getTag().getInt("Quality") > 100)
            return false;

        return (this.slots[2] == null || (this.slots[0].getItem() == this.slots[2].getItem() && (slots[0].getTag().getInt("Quality") + slots[1].getTag().getInt("Quality") == slots[2].getTag().getInt("Quality")))) ? true : false;
    }

    private void combineDNA() {
        ItemStack combinedDNA = new ItemStack(slots[0].getItem());

        CompoundTag compound = new CompoundTag();

        compound.putInt("Quality", slots[0].getTag().getInt("Quality") + slots[1].getTag().getInt("Quality"));
        compound.putString("DNA", slots[0].getTag().getString("DNA") + "," + slots[1].getTag().getString("DNA"));

        combinedDNA.setTag(compound);

        slots[0].setCount(slots[0].getCount() - 1);

        if (slots[0].getCount() <= 0)
            slots[0] = null;

        slots[1].setCount(slots[1].getCount() - 1);

        if (slots[1].getCount() <= 0)
            slots[1] = null;

        if (slots[2] != null)
            slots[2].setCount(slots[2].getCount() + 1);
        else
            slots[2] = combinedDNA;
    }


    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!this.level.isClientSide) {
            if (this.canCombine()) {
                this.combinationTime++;

                if (this.getCombinationTime() >= this.getCombinationSpeed()) {
                    this.setCombinationTime((short) 0);
                    this.combineDNA();
                }
            } else {
                this.setCombinationTime((short) 0);
            }
        }
    }

    public boolean hasItems() {
        return (this.slots[0] != null || this.slots[1] != null || this.slots[2] != null) ? true : false;
    }

    @Override
    public int getContainerSize() {
        return slots.length;
    }

    public int getSizeInventory() {
        return this.getContainerSize();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.slots[slot] != null ? this.slots[slot] : ItemStack.EMPTY;
    }

    public ItemStack getStackInSlot(int slot) {
        return this.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slotIndex, int amount) {
        if (this.slots[slotIndex] != null) {
            ItemStack splitedStack;

            if (this.slots[slotIndex].getCount() <= amount) {
                splitedStack = this.slots[slotIndex];
                this.slots[slotIndex] = ItemStack.EMPTY;
                return splitedStack;
            } else {
                splitedStack = this.slots[slotIndex].split(amount);

                if (this.slots[slotIndex].getCount() == 0)
                    this.slots[slotIndex] = ItemStack.EMPTY;

                return splitedStack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack decrStackSize(int slotIndex, int amount) {
        return this.removeItem(slotIndex, amount);
    }

    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        if (this.slots[slotIndex] != null) {
            ItemStack stack = this.slots[slotIndex];
            this.slots[slotIndex] = null;

            return stack;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex) {
        return this.getStackInSlotOnClosing(slotIndex);
    }

    public ItemStack removeStackFromSlot(int slotIndex) {
        return this.removeItemNoUpdate(slotIndex);
    }

    @Override
    public boolean isEmpty() {
        return !this.hasItems();
    }

    @Override
    public void setItem(int soltIndex, ItemStack stack) {
        this.slots[soltIndex] = stack;

        if (stack != null && stack.getCount() > this.getMaxStackSize())
            stack.setCount(this.getMaxStackSize());
    }

    public void setInventorySlotContents(int soltIndex, ItemStack stack) {
        this.setItem(soltIndex, stack);
    }

    public String getInventoryName() {
        return "DNA Combiner";
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
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return this.canPlaceItem(slot, stack);
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

    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] { 0 };
    }

    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    public boolean canInsertItem(int slot, ItemStack stack, int j) {
        return false;
    }

    public boolean canExtractItem(int slot, ItemStack stack, int j) {
        return false;
    }

    public boolean canInsertItem(int slot, ItemStack stack, Direction direction) {
        return false;
    }

    public boolean canExtractItem(int slot, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        ListTag items = nbt.getList("Items", 10);

        this.slots = new ItemStack[this.getContainerSize()];

        for (int i = 0; i < items.size(); i++) {
            CompoundTag compound = items.getCompound(i);
            byte slot = compound.getByte("Slot");

            if (slot >= 0 && slot < this.slots.length) {
                this.slots[slot] = ItemStack.of(compound);
            }
        }

        this.setCombinationTime(nbt.getShort("CombinationTime"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putShort("CombinationTime", this.getCombinationTime());
        ListTag list = new ListTag();

        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                CompoundTag compound = new CompoundTag();
                compound.putByte("Slot", (byte) i);
                this.slots[i].save(compound);
                list.add(compound);
            }
        }

        nbt.put("Items", list);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
