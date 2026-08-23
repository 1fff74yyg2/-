package net.ilexiconn.jurassicraft.common.tileentity.fence;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowBase;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowCorner;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowGrid;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowPole;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileSecurityFenceLowCorner extends BlockEntity implements Container {
    private static final int REDSTONEPERGRID = 2;
    private static final int IRONPERGRID = 1;
    private ItemStack[] slots = new ItemStack[6];
    private boolean[] builtFences = new boolean[4];
    private boolean[] fenceState = new boolean[4];
    private int plannedSide = 0;

    public TileSecurityFenceLowCorner() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFenceLowCorner(BlockPos pos, BlockState state) {
        super(JCTileEntityRegistry.FENCE_LOW_CORNER.get(), pos, state);
        this.plannedSide = 0;
        for (int i = 0; i < builtFences.length; i++)
            builtFences[i] = false;
    }

    public int getPlannedSide() {
        return this.plannedSide;
    }

    public void setPlannedSide(int side) {
        this.plannedSide = side;
    }

    public void setFenceAt(int side, boolean hasFenceAt) {
        this.builtFences[side] = hasFenceAt;
    }

    public boolean hasFenceAt(int side) {
        return this.builtFences[side];
    }

    public void setFenceOff(int side) {
        this.fenceState[side] = false;
    }

    public void setFenceOn(int side) {
        this.fenceState[side] = true;
    }

    public void setFenceOnOff(int side, boolean turnOnOrOff) {
        this.fenceState[side] = turnOnOrOff;
    }

    public boolean isFenceOff(int side) {
        return !this.fenceState[side];
    }

    public boolean isFenceOn(int side) {
        return this.fenceState[side];
    }

    public boolean hasIronStored() {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                if (this.slots[i].getItem() == Items.IRON_INGOT)
                    return true;
            }
        }
        return false;
    }

    public boolean hasNumberOfIronStored(int amount) {
        int ironStored = 0;
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                if (this.slots[i].getItem() == Items.IRON_INGOT)
                    ironStored += this.slots[i].getCount();
            }
        }
        return ironStored >= amount;
    }

    public void reduceIronIngots(int amount) {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                if (this.slots[i].getItem() == Items.IRON_INGOT) {
                    if (this.slots[i].getCount() >= amount) {
                        this.slots[i].shrink(amount);
                        if (this.slots[i].getCount() < 1)
                            this.slots[i] = ItemStack.EMPTY;
                    } else {
                        amount -= this.slots[i].getCount();
                        this.slots[i] = ItemStack.EMPTY;
                    }
                }
            }
        }
    }

    public boolean hasRedstoneStored() {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                if (this.slots[i].getItem() == Items.REDSTONE)
                    return true;
            }
        }
        return false;
    }

    public boolean hasNumberOfRedstoneStored(int amount) {
        int redstoneStored = 0;
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                if (this.slots[i].getItem() == Items.REDSTONE)
                    redstoneStored += this.slots[i].getCount();
            }
        }
        return redstoneStored >= amount;
    }

    public void reduceRedstone(int amount) {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                if (this.slots[i].getItem() == Items.REDSTONE) {
                    if (this.slots[i].getCount() >= amount) {
                        this.slots[i].shrink(amount);
                        if (this.slots[i].getCount() < 1)
                            this.slots[i] = ItemStack.EMPTY;
                    } else {
                        amount -= this.slots[i].getCount();
                        this.slots[i] = ItemStack.EMPTY;
                    }
                }
            }
        }
    }

    public boolean hasItems() {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null && this.slots[i].getCount() > 0)
                return true;
        }
        return false;
    }

    private boolean hasLowSecurityMainFenceBlockAt(TileSecurityFenceLowCorner mainFence, int x, int y, int z) {
        return mainFence.level.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockSecurityFenceLowCorner;
    }

    private boolean hasLowSecurityBaseFenceBlockAt(TileSecurityFenceLowCorner mainFence, int x, int y, int z) {
        return mainFence.level.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockSecurityFenceLowBase;
    }

    private boolean hasLowSecurityPoleFenceBlockAt(TileSecurityFenceLowCorner mainFence, int x, int y, int z) {
        return mainFence.level.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockSecurityFenceLowPole;
    }

    private boolean hasLowSecurityGridFenceBlockAt(TileSecurityFenceLowCorner mainFence, int x, int y, int z) {
        return mainFence.level.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockSecurityFenceLowGrid;
    }

    public TileSecurityFenceLowCorner getNextLowSecurityMainFenceBlock(TileSecurityFenceLowCorner mainFence, int side, int distance) {
        BlockEntity tileEntity = null;
        switch (side) {
            /** South */
            case 0:
                for (int i = 1; i < distance; i++) {
                    if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() + i))
                        tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() + i));
                }
                break;
            /** West */
            case 1:
                for (int i = 1; i < distance; i++) {
                    if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX() - i, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()))
                        tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX() - i, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()));
                }
                break;
            /** North */
            case 2:
                for (int i = 1; i < distance; i++) {
                    if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() - i))
                        tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() - i));
                }
                break;
            /** East */
            case 3:
                for (int i = 1; i < distance; i++) {
                    if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX() + i, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()))
                        tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX() + i, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()));
                }
                break;
        }
        if (tileEntity instanceof TileSecurityFenceLowCorner) {
            return (TileSecurityFenceLowCorner) tileEntity;
        } else {
            return null;
        }
    }

    public TileSecurityFenceLowCorner getNextLowSecurityMainFenceBlockDirectly(TileSecurityFenceLowCorner mainFence, int side, int distance) {
        BlockEntity tileEntity = null;
        switch (side) {
            /** South */
            case 0:
                if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() + distance))
                    tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() + distance));
                break;
            /** West */
            case 1:
                if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX() - distance, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()))
                    tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX() - distance, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()));
                break;
            /** North */
            case 2:
                if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() - distance))
                    tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() - distance));
                break;
            /** East */
            case 3:
                if (mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX() + distance, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()))
                    tileEntity = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX() + distance, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ()));
                break;
        }
        if (tileEntity instanceof TileSecurityFenceLowCorner) {
            return (TileSecurityFenceLowCorner) tileEntity;
        } else {
            return null;
        }
    }

    public int getFencePoleHeight(TileSecurityFenceLowCorner mainFence) {
        int i = 0;
        if (mainFence.level != null) {
            while (this.hasLowSecurityPoleFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ()))
                i++;
        }
        return i;
    }

    public int getSmallerFencePoleHeight(TileSecurityFenceLowCorner mainFence1, TileSecurityFenceLowCorner mainFence2) {
        int heigthFence1 = this.getFencePoleHeight(mainFence1);
        int heigthFence2 = this.getFencePoleHeight(mainFence2);
        return Math.min(heigthFence1, heigthFence2);
    }

    public List<TileSecurityFenceLowPole> getAllFencePoles(TileSecurityFenceLowCorner mainFence) {
        int i = this.getFencePoleHeight(mainFence);
        if (i > 0) {
            List<TileSecurityFenceLowPole> fencePoles = new ArrayList();
            for (int j = 0; j < i; j++) {
                fencePoles.add((TileSecurityFenceLowPole) mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ())));
            }
            return fencePoles;
        }
        return null;
    }

    public void setGridsToAllFencePoles(TileSecurityFenceLowCorner mainFence) {
        int i = this.getFencePoleHeight(mainFence);

        for (int height = 0; height < i; height++) {
            for (int side = 0; side < 4; side++) {
                BlockEntity fence = mainFence.level.getBlockEntity(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + height + 1, mainFence.getBlockPos().getZ()));
                if (fence instanceof TileSecurityFenceLowPole)
                    ((TileSecurityFenceLowPole) fence).setGridAtSide(side, this.hasFenceAt(side));
            }
        }
    }

    public int getFenceBaseLength(TileSecurityFenceLowCorner mainFence, int side) {
        boolean flag = true;
        int i = 0;
        switch (side) {
            /** South */
            case 0:
                while (flag) {
                    if (mainFence.hasLowSecurityBaseFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() + i + 1)) {
                        i++;
                    } else {
                        flag = false;
                    }
                }
                break;
            /** West */
            case 1:
                while (flag) {
                    if (mainFence.hasLowSecurityBaseFenceBlockAt(mainFence, mainFence.getBlockPos().getX() - i - 1, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ())) {
                        i++;
                    } else {
                        flag = false;
                    }
                }
                break;
            /** North */
            case 2:
                while (flag) {
                    if (mainFence.hasLowSecurityBaseFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() - i - 1)) {
                        i++;
                    } else {
                        flag = false;
                    }
                }
                break;
            /** East */
            case 3:
                while (flag) {
                    if (mainFence.hasLowSecurityBaseFenceBlockAt(mainFence, mainFence.getBlockPos().getX() + i + 1, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ())) {
                        i++;
                    } else {
                        flag = false;
                    }
                }
                break;
        }
        return i;
    }

    public boolean isBaseAtSideValid(TileSecurityFenceLowCorner mainFence, int side, int length) {
        length++;
        switch (side) {
            /** South */
            case 0:
                return mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() + length);
            /** West */
            case 1:
                return mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX() - length, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ());
            /** North */
            case 2:
                return mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ() - length);
            /** East */
            case 3:
                return mainFence.hasLowSecurityMainFenceBlockAt(mainFence, mainFence.getBlockPos().getX() + length, mainFence.getBlockPos().getY(), mainFence.getBlockPos().getZ());
            default:
                return false;
        }
    }

    public boolean hasEmptySpaceAt(TileSecurityFenceLowCorner mainFence, int side, int length, int height) {
        switch (side) {
            /** South */
            case 0:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.level.isEmptyBlock(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ() + j + 1)))
                            return false;
                    }
                }
                break;
            /** West */
            case 1:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.level.isEmptyBlock(new BlockPos(mainFence.getBlockPos().getX() - j - 1, mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ())))
                            return false;
                    }
                }
                break;
            /** North */
            case 2:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.level.isEmptyBlock(new BlockPos(mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ() - j - 1)))
                            return false;
                    }
                }
                break;
            /** East */
            case 3:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.level.isEmptyBlock(new BlockPos(mainFence.getBlockPos().getX() + j + 1, mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ())))
                            return false;
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean hasGridBetweenSpace(TileSecurityFenceLowCorner mainFence, int side, int length, int height) {
        switch (side) {
            /** South */
            case 0:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ() + j + 1))
                            return false;
                    }
                }
                break;
            /** West */
            case 1:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX() - j - 1, mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ()))
                            return false;
                    }
                }
                break;
            /** North */
            case 2:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ() - j - 1))
                            return false;
                    }
                }
                break;
            /** East */
            case 3:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX() + j + 1, mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ()))
                            return false;
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public int getNumberOfGridsToBuild(int lengthOfTheFence, int heightOfTheFence) {
        return lengthOfTheFence * heightOfTheFence;
    }

    public int getNumberOfGridsToFix(TileSecurityFenceLowCorner mainFence, int side, int length, int height) {
        int numberOfGridsMissing = 0;
        switch (side) {
            /** South */
            case 0:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ() + j + 1))
                            numberOfGridsMissing++;
                    }
                }
                break;
            /** West */
            case 1:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX() - j - 1, mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ()))
                            numberOfGridsMissing++;
                    }
                }
                break;
            /** North */
            case 2:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX(), mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ() - j - 1))
                            numberOfGridsMissing++;
                    }
                }
                break;
            /** East */
            case 3:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (!mainFence.hasLowSecurityGridFenceBlockAt(mainFence, mainFence.getBlockPos().getX() + j + 1, mainFence.getBlockPos().getY() + i + 1, mainFence.getBlockPos().getZ()))
                            numberOfGridsMissing++;
                    }
                }
                break;
        }
        return numberOfGridsMissing;
    }

    public int getRedstoneRequiredForGrid(int numberOfGrids) {
        return REDSTONEPERGRID * numberOfGrids;
    }

    public int getIronRequiredForGrid(int numberOfGrids) {
        return IRONPERGRID * numberOfGrids;
    }

    private void buildFenceOff(TileSecurityFenceLowCorner mainFence1, TileSecurityFenceLowCorner mainFence2, int side, int length, int height) {
        Block grid = JCBlockRegistry.securityFenceLowGrid;
        switch (side) {
            /** South */
            case 0:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() + j + 1), grid.defaultBlockState());
                    }
                }
                mainFence1.setFenceAt(0, true);
                mainFence1.setFenceOff(0);
                mainFence2.setFenceAt(2, true);
                mainFence2.setFenceOff(2);
                break;
            /** West */
            case 1:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX() - j - 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ()), grid.defaultBlockState());
                    }
                }
                mainFence1.setFenceAt(1, true);
                mainFence1.setFenceOff(1);
                mainFence2.setFenceAt(3, true);
                mainFence2.setFenceOff(3);
                break;
            /** North */
            case 2:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() - j - 1), grid.defaultBlockState());
                    }
                }
                mainFence1.setFenceAt(2, true);
                mainFence1.setFenceOff(2);
                mainFence2.setFenceAt(0, true);
                mainFence2.setFenceOff(0);
                break;
            /** East */
            case 3:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX() + j + 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ()), grid.defaultBlockState());
                    }
                }
                mainFence1.setFenceAt(3, true);
                mainFence1.setFenceOff(3);
                mainFence2.setFenceAt(1, true);
                mainFence2.setFenceOff(1);
                break;
        }
    }

    private void changeFenceState(boolean turnOnOrOff, TileSecurityFenceLowCorner mainFence1, TileSecurityFenceLowCorner mainFence2, int side, int length, int height) {
        int metadata = 0;
        if (turnOnOrOff) {
            switch (side) {
                case 0:
                    metadata = 1;
                    break;
                case 1:
                    metadata = 2;
                    break;
                case 2:
                    metadata = 3;
                    break;
                case 3:
                    metadata = 0;
                    break;
            }
        } else {
            switch (side) {
                case 0:
                    metadata = 5;
                    break;
                case 1:
                    metadata = 6;
                    break;
                case 2:
                    metadata = 7;
                    break;
                case 3:
                    metadata = 4;
                    break;
            }
        }
        switch (side) {
            /** South */
            case 0:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (mainFence1.hasLowSecurityGridFenceBlockAt(mainFence1, mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() + j + 1)) {
                            mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() + j + 1), mainFence1.level.getBlockState(new BlockPos(mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() + j + 1)));
                        }
                    }
                }
                mainFence1.setFenceOnOff(0, !turnOnOrOff);
                mainFence2.setFenceOnOff(2, !turnOnOrOff);
                break;
            /** West */
            case 1:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (mainFence1.hasLowSecurityGridFenceBlockAt(mainFence1, mainFence1.getBlockPos().getX() - j - 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ())) {
                            mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX() - j - 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ()), mainFence1.level.getBlockState(new BlockPos(mainFence1.getBlockPos().getX() - j - 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ())));
                        }
                    }
                }
                mainFence1.setFenceOnOff(1, !turnOnOrOff);
                mainFence2.setFenceOnOff(3, !turnOnOrOff);
                break;
            /** North */
            case 2:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (mainFence1.hasLowSecurityGridFenceBlockAt(mainFence1, mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() - j - 1)) {
                            mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() - j - 1), mainFence1.level.getBlockState(new BlockPos(mainFence1.getBlockPos().getX(), mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ() - j - 1)));
                        }
                    }
                }
                mainFence1.setFenceOnOff(2, !turnOnOrOff);
                mainFence2.setFenceOnOff(0, !turnOnOrOff);
                break;
            /** East */
            case 3:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < length; j++) {
                        if (mainFence1.hasLowSecurityGridFenceBlockAt(mainFence1, mainFence1.getBlockPos().getX() + j + 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ())) {
                            mainFence1.level.setBlockAndUpdate(new BlockPos(mainFence1.getBlockPos().getX() + j + 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ()), mainFence1.level.getBlockState(new BlockPos(mainFence1.getBlockPos().getX() + j + 1, mainFence1.getBlockPos().getY() + i + 1, mainFence1.getBlockPos().getZ())));
                        }
                    }
                }
                mainFence1.setFenceOnOff(3, !turnOnOrOff);
                mainFence2.setFenceOnOff(1, !turnOnOrOff);
                break;
        }
    }

    public void tryToBuildFence(int side) {
        if (this.level.isClientSide) {
            return;
        } else {
            int length = this.getFenceBaseLength(this, side);

            if (!this.isBaseAtSideValid(this, side, length)) {
                return;
            } else {
                TileSecurityFenceLowCorner otherFence = this.getNextLowSecurityMainFenceBlockDirectly(this, side, length + 1);

                int height = this.getSmallerFencePoleHeight(this, otherFence);
                int numberOfGrids = this.getNumberOfGridsToBuild(length, height);

                int ironRequired = this.getIronRequiredForGrid(numberOfGrids);

                if (!this.hasNumberOfIronStored(ironRequired)) {
                    return;
                } else {
                    int redstoneRequired = this.getRedstoneRequiredForGrid(numberOfGrids);
                    if (!this.hasNumberOfRedstoneStored(redstoneRequired)) {
                        return;
                    } else {
                        if (!this.hasEmptySpaceAt(this, side, length, height)) {
                            return;
                        } else {
                            this.buildFenceOff(this, otherFence, side, length, height);
                            this.reduceIronIngots(ironRequired);
                            this.reduceRedstone(redstoneRequired);
                        }
                    }
                }
            }
        }
    }

    public void tryToFixFence(int side) {
        if (this.level.isClientSide) {
            return;
        } else {
            int length = this.getFenceBaseLength(this, side);

            if (!this.isBaseAtSideValid(this, side, length)) {
                /** Missing other main base. */
                return;
            } else {
                TileSecurityFenceLowCorner otherFence = this.getNextLowSecurityMainFenceBlockDirectly(this, side, length + 1);

                int height = this.getSmallerFencePoleHeight(this, otherFence);

                if (this.hasGridBetweenSpace(this, side, length, height)) {
                    /** The fence does not need to be fixed. */
                    return;
                } else {
                    int numberOfGrids = this.getNumberOfGridsToFix(this, side, length, height);

                    int ironRequired = this.getIronRequiredForGrid(numberOfGrids);

                    if (!this.hasNumberOfIronStored(ironRequired)) {
                        /** More iron ingot is required. */
                        return;
                    } else {
                        int redstoneRequired = this.getRedstoneRequiredForGrid(numberOfGrids);

                        if (!this.hasNumberOfRedstoneStored(redstoneRequired)) {
                            /** More redstone is required. */
                            return;
                        } else {
                            this.buildFenceOff(this, otherFence, side, length, height);
                            this.reduceIronIngots(ironRequired);
                            this.reduceRedstone(redstoneRequired);
                        }
                    }
                }
            }
        }
    }

    public void tryToTurnOnTheFence(int side) {
        if (this.level.isClientSide) {
            return;
        } else {
            int length = this.getFenceBaseLength(this, side);

            if (!this.isBaseAtSideValid(this, side, length)) {
                return;
            } else {
                TileSecurityFenceLowCorner otherFence = this.getNextLowSecurityMainFenceBlockDirectly(this, side, length + 1);

                int height = this.getSmallerFencePoleHeight(this, otherFence);

                this.changeFenceState(this.isFenceOn(side), this, otherFence, side, length, height);
            }
        }
    }

    /**
     * Returns a HashMap<Integer, int[]> with all coordinates of every fence block between an area of 22 x 22.
     * <p/>
     * Note: 0 = xCoord, 1 = yCoord, 2 = zCoord.
     */
    public HashMap<Integer, int[]> getAllFenceBlocks() {
        HashMap<Integer, int[]> map = new HashMap<Integer, int[]>();
        int numberOfBlocks = 0;
        for (int i = -11; i < 12; i++) {
            for (int k = -11; k < 12; k++) {
                if (this.hasLowSecurityMainFenceBlockAt(this, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k) || this.hasLowSecurityBaseFenceBlockAt(this, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k) || this.hasLowSecurityPoleFenceBlockAt(this, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k) || this.hasLowSecurityGridFenceBlockAt(this, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k)) {
                    map.put(numberOfBlocks, new int[] { i, k });
                    numberOfBlocks++;
                }
            }
        }
        return map;
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
                if (this.slots[i].getCount() == 0) {
                    this.slots[i] = ItemStack.EMPTY;
                }
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
    public void setItem(int i, ItemStack itemStack) {
        this.slots[i] = itemStack;
        if (itemStack != null && itemStack.getCount() > this.getMaxStackSize()) {
            if (this.getMaxStackSize() > 0) { itemStack.setCount(this.getMaxStackSize()); } else { itemStack = ItemStack.EMPTY; }
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.setItem(i, itemStack);
    }

    public String getInventoryName() {
        return "Security_Fence_Main_Block";
    }

    public boolean hasCustomInventoryName() {
        return true;
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
        return this.level.getBlockEntity(this.getBlockPos()) == this && player.distanceToSqr((double) this.getBlockPos().getX() + 0.5D, (double) this.getBlockPos().getY() + 0.5D, (double) this.getBlockPos().getZ() + 0.5D) <= 49.0D;
    }

    public boolean isUseableByPlayer(Player player) {
        return this.stillValid(player);
    }

    public void openInventory() {

    }

    public void closeInventory() {

    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return this.canPlaceItem(i, itemStack);
    }

    public int[] getAccessibleSlotsFromSide(int i) {
        return new int[] { 0 };
    }

    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        return false;
    }

    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return false;
    }
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return this.canExtractItem(index, stack, direction.ordinal());
    }

    public boolean canInsertItem(int index, ItemStack stack, Direction direction) {
        return this.canInsertItem(index, stack, direction.ordinal());
    }

    public int[] getSlotsForFace(Direction side) {
        return this.getAccessibleSlotsFromSide(side.ordinal());
    }

    public boolean isUsableByPlayer(Player player) {
        return this.stillValid(player);
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

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.getStackInSlotOnClosing(index);
    }

    public ItemStack removeStackFromSlot(int index) {
        return this.removeItemNoUpdate(index);
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

    public void markDirty() {
        this.setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        ListTag list = new ListTag();
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) i);
                this.slots[i].save(tagCompound);
                list.add(tagCompound);
            }
        }
        compound.put("Items", list);

        for (int i = 0; i < builtFences.length; i++)
            compound.putBoolean("FenceAtSide" + i, this.hasFenceAt(i));

        for (int i = 0; i < fenceState.length; i++)
            compound.putBoolean("FenceStateAtSide" + i, this.isFenceOn(i));

        compound.putByte("PlannedSide", (byte) this.getPlannedSide());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        ListTag list = compound.getList("Items", 10);
        this.slots = new ItemStack[this.getContainerSize()];
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tagCompound = list.getCompound(i);
            byte j = tagCompound.getByte("Slot");

            if (j >= 0 && j < this.slots.length) {
                this.slots[j] = ItemStack.of(tagCompound);
            }
        }

        for (int i = 0; i < builtFences.length; i++)
            this.setFenceAt(i, compound.getBoolean("FenceAtSide" + i));

        for (int i = 0; i < fenceState.length; i++)
            this.setFenceOnOff(i, compound.getBoolean("FenceStateAtSide" + i));

        this.setPlannedSide((int) compound.getByte("PlannedSide"));
        this.setGridsToAllFencePoles(this);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
