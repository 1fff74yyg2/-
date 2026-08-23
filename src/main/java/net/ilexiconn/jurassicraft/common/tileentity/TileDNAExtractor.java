package net.ilexiconn.jurassicraft.common.tileentity;

import net.ilexiconn.jurassicraft.common.api.IDNASource;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.ilexiconn.jurassicraft.common.item.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
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

import java.util.ArrayList;

public class TileDNAExtractor extends BlockEntity implements Container {
    private static final short extractionSpeed = 100;
    private ItemStack[] slots = new ItemStack[8];
    private short extractionTime;
    private ArrayList<ItemDNA> allDNAs = new ArrayList<ItemDNA>();

    public TileDNAExtractor() {
        this(BlockPos.ZERO, null);
    }

    public TileDNAExtractor(BlockPos pos, BlockState state) {
        super(JCTileEntityRegistry.DNA_EXTRACTOR.get(), pos, state);
        this.extractionTime = 0;

        for (Creature creature : CreatureHandler.getCreatures()) {
            ItemDNA dna = creature.getDNA();

            if (dna != null)
                this.allDNAs.add(dna);
        }
    }

    public short getExtractionTime() {
        return extractionTime;
    }

    public void setExtractionTime(short time) {
        this.extractionTime = time;
    }

    public short getExtractionSpeed() {
        return extractionSpeed;
    }

    public int getExtractionProgressScaled(int i) {
        return (this.getExtractionTime() * i) / this.getExtractionSpeed();
    }

    public boolean isExtracting() {
        return (this.getExtractionTime() > 0);
    }

    private boolean canExtract() {
        if ((this.slots[0] == null || this.slots[0].isEmpty()) && (this.slots[1] == null || this.slots[1].isEmpty()) && (this.slots[2] == null || this.slots[2].isEmpty()) && (this.slots[3] == null || this.slots[3].isEmpty()))
            return false;
        else
            return !((this.slots[4] != null && !this.slots[4].isEmpty()) && (this.slots[5] != null && !this.slots[5].isEmpty()) && (this.slots[6] != null && !this.slots[6].isEmpty()) && (this.slots[7] != null && !this.slots[7].isEmpty()));
    }

    private void extractItem() {
        for (int i = 0; i < 4; i++) {
            if (slots[i] != null && !slots[i].isEmpty() && slots[i].getItem() instanceof IDNASource) {
                ItemStack newItem = null;

                if (slots[i].getItem() instanceof ItemFossil)
                    newItem = this.getDNASampleFromFossil();
                else if (slots[i].getItem() instanceof ItemMeat)
                    newItem = this.getDNASampleFromMeat(slots[i]);
                else if (slots[i].getItem() instanceof ItemAmber)
                    newItem = this.getDNASampleFromAmber();
                else if (slots[i].getItem() instanceof ItemFur || slots[i].getItem() instanceof ItemSkin || slots[i].getItem() instanceof ItemScale || slots[i].getItem() instanceof ItemFeather || slots[i].getItem() instanceof ItemSkull || slots[i].getItem() instanceof ItemTooth || slots[i].getItem() instanceof ItemBristles)
                    newItem = this.getDNASampleFromDrop(slots[i]);
                else {
                    int output = this.level.random.nextInt(3);

                    if (output == 0)
                        newItem = new ItemStack(Blocks.SAND, 1 + this.level.random.nextInt(2));
                    else if (output == 1)
                        newItem = new ItemStack(Blocks.COBBLESTONE);
                    else if (output == 2)
                        newItem = new ItemStack(Items.BONE, 1 + this.level.random.nextInt(3));
                }

                if (newItem != null) {
                    for (int j = 4; j < 8; j++) {
                        if (this.slots[j] != null && !this.slots[j].isEmpty() && this.slots[j].getItem() == newItem.getItem() && !newItem.hasTag()) {
                            this.slots[i].shrink(1);

                            if (this.slots[i].getCount() <= 0)
                                this.slots[i] = ItemStack.EMPTY;

                            this.slots[j].grow(1);
                            this.slots[j].grow(1);
                            return;
                        }
                    }

                    for (int j = 4; j < 8; j++) {
                        if (this.slots[j] == null || this.slots[j].isEmpty()) {
                            this.slots[i].shrink(1);

                            if (this.slots[i].getCount() <= 0)
                                this.slots[i] = ItemStack.EMPTY;

                            this.slots[j] = newItem;
                            return;
                        }
                    }
                }
            }
        }
    }

    private ItemStack getDNASampleFromFossil() {
        if (this.level.random.nextFloat() >= 0.70F) {
            ItemStack dna = new ItemStack(this.getRandomDNA());

            if (!dna.hasTag()) {
                CompoundTag compound = new CompoundTag();
                float probability = this.level.random.nextFloat();

                if (probability <= 0.10F)
                    compound.putInt("Quality", 100);
                else if (probability <= 0.35F)
                    compound.putInt("Quality", 75);
                else if (probability <= 0.75F)
                    compound.putInt("Quality", 50);
                else
                    compound.putInt("Quality", 25);

                compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
                dna.setTag(compound);

                return dna;
            } else {
                if (!dna.getTag().contains("Quality")) {
                    float probability = this.level.random.nextFloat();

                    if (probability <= 0.10F)
                        dna.getTag().putInt("Quality", 100);
                    else if (probability <= 0.35F)
                        dna.getTag().putInt("Quality", 75);
                    else if (probability <= 0.75F)
                        dna.getTag().putInt("Quality", 50);
                    else
                        dna.getTag().putInt("Quality", 25);
                }

                if (!dna.getTag().contains("DNA"))
                    dna.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
            }
        } else {
            int output = this.level.random.nextInt(3);

            if (output == 0)
                return new ItemStack(Blocks.SAND, 1 + this.level.random.nextInt(2));
            else if (output == 1)
                return new ItemStack(Blocks.COBBLESTONE);
            else if (output == 2)
                return new ItemStack(Items.BONE, 1 + this.level.random.nextInt(3));
        }

        return null;
    }

    private ItemStack getDNASampleFromAmber() {
        ItemStack dna = new ItemStack(this.getRandomDNA());

        if (!dna.hasTag()) {
            CompoundTag compound = new CompoundTag();
            compound.putInt("Quality", 100);
            compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
            dna.setTag(compound);

            return dna;
        } else {
            if (!dna.getTag().contains("Quality"))
                dna.getTag().putInt("Quality", 100);

            if (!dna.getTag().contains("DNA"))
                dna.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

            return dna;
        }
    }

    private ItemStack getDNASampleFromMeat(ItemStack meat) {
        ItemStack dna = new ItemStack(this.getDNAFromMeat((ItemMeat) meat.getItem()));

        if (meat.hasTag()) {
            dna.setTag(meat.getTag());

            if (!dna.getTag().contains("Quality"))
                dna.getTag().putInt("Quality", 100);

            if (!dna.getTag().contains("DNA"))
                dna.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

            return dna;
        } else {
            if (!dna.hasTag()) {
                CompoundTag compound = new CompoundTag();

                compound.putInt("Quality", 100);
                compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

                dna.setTag(compound);

                return dna;
            } else {
                if (!dna.getTag().contains("Quality"))
                    dna.getTag().putInt("Quality", 100);

                if (!dna.getTag().contains("DNA"))
                    dna.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

                return dna;
            }
        }
    }

    private ItemStack getDNASampleFromDrop(ItemStack stack) {
        ItemStack dna = null;

        Item item = stack.getItem();

        if (item instanceof ItemFur)
            dna = new ItemStack(((ItemFur) item).getCorrespondingDNA());
        else if (item instanceof ItemSkin)
            dna = new ItemStack(((ItemSkin) item).getCorrespondingDNA());
        else if (item instanceof ItemScale)
            dna = new ItemStack(((ItemScale) item).getCorrespondingDNA());
        else if (item instanceof ItemFeather)
            dna = new ItemStack(((ItemFeather) item).getCorrespondingDNA());
        else if (item instanceof ItemSkull)
            dna = new ItemStack(((ItemSkull) item).getCorrespondingDNA());
        else if (item instanceof ItemTooth)
            dna = new ItemStack(((ItemTooth) item).getCorrespondingDNA());
        else if (item instanceof ItemBristles)
            dna = new ItemStack(((ItemBristles) item).getCorrespondingDNA());
        else
            dna = new ItemStack(this.getRandomDNA());

        if (stack.hasTag()) {
            dna.setTag(stack.getTag());

            if (!dna.getTag().contains("Quality"))
                dna.getTag().putInt("Quality", 100);

            if (!dna.getTag().contains("DNA"))
                dna.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

            return dna;
        } else {
            if (!dna.hasTag()) {
                CompoundTag compound = new CompoundTag();

                compound.putInt("Quality", 100);
                compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

                dna.setTag(compound);

                return dna;
            } else {
                if (!dna.getTag().contains("Quality"))
                    dna.getTag().putInt("Quality", 100);

                if (!dna.getTag().contains("DNA"))
                    dna.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

                return dna;
            }
        }
    }

    private Item getDNAFromMeat(ItemMeat meat) {
        return meat.getCorrespondingDNA();
    }

    private Item getRandomDNA() {
        return allDNAs.get(this.level.random.nextInt(allDNAs.size()));
    }


    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!this.level.isClientSide) {
            if (this.canExtract()) {
                this.extractionTime++;

                if (this.getExtractionTime() >= this.getExtractionSpeed()) {
                    this.setExtractionTime((short) 0);
                    this.extractItem();
                }
            } else {
                this.setExtractionTime((short) 0);
            }
        }
    }

    public boolean hasItems() {
        return !((this.slots[0] == null || this.slots[0].isEmpty()) && (this.slots[1] == null || this.slots[1].isEmpty()) && (this.slots[2] == null || this.slots[2].isEmpty()) && (this.slots[3] == null || this.slots[3].isEmpty()) && (this.slots[4] == null || this.slots[4].isEmpty()) && (this.slots[5] == null || this.slots[5].isEmpty()) && (this.slots[6] == null || this.slots[6].isEmpty()) && (this.slots[7] == null || this.slots[7].isEmpty()));
    }

    @Override
    public int getContainerSize() {
        return slots.length;
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
            ItemStack stack = this.slots[i];
            this.slots[i] = ItemStack.EMPTY;

            return stack;
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
    public void setItem(int i, ItemStack stack) {
        this.slots[i] = stack;

        if (stack != null && stack.getCount() > this.getMaxStackSize())
            if (this.getMaxStackSize() > 0) { stack.setCount(this.getMaxStackSize()); } else { stack = ItemStack.EMPTY; }
    }

    public void setInventorySlotContents(int i, ItemStack stack) {
        this.setItem(i, stack);
    }

    public String getInventoryName() {
        return "DNA Extractor";
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
    public void load(CompoundTag nbt) {
        super.load(nbt);

        ListTag list = nbt.getList("Items", 10);

        this.slots = new ItemStack[this.getContainerSize()];

        for (int i = 0; i < list.size(); i++) {
            CompoundTag compound = list.getCompound(i);
            byte slot = compound.getByte("Slot");

            if (slot >= 0 && slot < this.slots.length)
                this.slots[slot] = ItemStack.of(compound);
        }

        this.setExtractionTime(nbt.getShort("ExtractionTime"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putShort("ExtractionTime", this.getExtractionTime());
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
