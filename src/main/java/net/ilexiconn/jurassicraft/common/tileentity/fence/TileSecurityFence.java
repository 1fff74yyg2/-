package net.ilexiconn.jurassicraft.common.tileentity.fence;

import net.ilexiconn.jurassicraft.common.api.IFenceBase;
import net.ilexiconn.jurassicraft.common.api.IFenceGrid;
import net.ilexiconn.jurassicraft.common.api.IFencePole;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowCorner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class TileSecurityFence extends BlockEntity implements Container {
    public TileSecurityFence() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFence(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }

    private ItemStack[] slots = new ItemStack[2];
    private byte[] fenceSecurityLevel = new byte[4];
    private byte fenceDirection = 0;
    private short fenceGridsStored = 0;
    private short fenceBasesStored = 0;
    private short fencePolesStored = 0;

    public int getFenceGridsStored() {
        return fenceGridsStored;
    }

    public void setFenceGridsStored(short fenceGrids) {
        this.fenceGridsStored = fenceGrids;
    }

    public int getFenceBasesStored() {
        return fenceBasesStored;
    }

    public void setFenceBasesStored(short fenceBases) {
        this.fenceBasesStored = fenceBases;
    }

    public int getFencePolesStored() {
        return fencePolesStored;
    }

    public void setFencePolesStored(short fencePoles) {
        this.fencePolesStored = fencePoles;
    }

    public byte getSecurityLevel(int direction) {
        return fenceSecurityLevel[direction];
    }

    public void setSecurityLevel(byte securityLV, int direction) {
        if (securityLV > -1 && securityLV < 4)
            this.fenceSecurityLevel[direction] = securityLV;
    }

    public byte getDirection() {
        return fenceDirection;
    }

    public void setDirection(byte dir) {
        this.fenceDirection = dir;
    }

    public boolean isFenceBuilt(int direction) {
        return this.fenceSecurityLevel[direction] > 0;
    }

    public byte getHeightPlanned(int level) {
        switch (level) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 5;
        }

        return 0;
    }

    public boolean hasIronIngots() {
        return (this.slots[0] != null && this.slots[0].getItem() == Items.IRON_INGOT);
    }

    public boolean hasRedstone() {
        return (this.slots[1] != null && this.slots[1].getItem() == Items.REDSTONE);
    }

    public boolean hasFenceBases() {
        return this.fenceBasesStored > 0;
    }

    public boolean hasFenceGrids() {
        return this.fenceGridsStored > 0;
    }

    public boolean hasFencePoles() {
        return this.fencePolesStored > 0;
    }

    public boolean isSecurityLevelValid(int level) {
        return (level > -1 && level < 4);
    }

    public boolean isFenceValid(int direction, int level) {
        if (level == 0)
            return !(this.fenceSecurityLevel[direction] > 0);
        else if (level == 1 || level == 2 || level == 3)
            return (this.fenceSecurityLevel[direction] > 0);

        return false;
    }

    public boolean isDistanceValid(int distance) {
        return (distance > 0 && distance < 12);
    }

    public boolean isHeightValid(int height) {
        return (height > 0 && height < 7);
    }

    public int getRequiredFenceGrids(int securityLV, int direction) {
        return this.getHeightPlanned(securityLV) * (this.calculateDistanceToNextPole(direction) - 1);
    }

    public boolean hasEnoughFenceGrids(int securityLV, int direction) {
        BlockEntity tileEntity = null;

        switch (direction) {
            case 0:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + this.calculateDistanceToNextPole(direction)));
                break;
            case 1:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() - this.calculateDistanceToNextPole(direction), this.getBlockPos().getY(), this.getBlockPos().getZ()));
                break;
            case 2:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - this.calculateDistanceToNextPole(direction)));
                break;
            case 3:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() + this.calculateDistanceToNextPole(direction), this.getBlockPos().getY(), this.getBlockPos().getZ()));
                break;
        }

        if (tileEntity instanceof TileSecurityFence) {
            TileSecurityFence nextFence = (TileSecurityFence) tileEntity;

            if ((nextFence.hasFenceGrids() && nextFence.isSecurityLevelValid(securityLV)) || (this.hasFenceGrids() && this.isSecurityLevelValid(securityLV)))
                return (this.getFenceGridsStored() + nextFence.getFenceGridsStored() >= this.getRequiredFenceGrids(securityLV, direction));
        }

        return false;
    }

    public int getRequiredFenceBases(int direction) {
        return this.calculateDistanceToNextPole(direction) - 1;
    }

    public boolean hasEnoughFenceBases(int securityLV, int direction) {
        BlockEntity tileEntity = null;

        switch (direction) {
            case 0:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + this.calculateDistanceToNextPole(direction)));
                break;
            case 1:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() - this.calculateDistanceToNextPole(direction), this.getBlockPos().getY(), this.getBlockPos().getZ()));
                break;
            case 2:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - this.calculateDistanceToNextPole(direction)));
                break;
            case 3:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() + this.calculateDistanceToNextPole(direction), this.getBlockPos().getY(), this.getBlockPos().getZ()));
                break;
        }

        if (tileEntity instanceof TileSecurityFence) {
            TileSecurityFence nextFence = (TileSecurityFence) tileEntity;

            if ((nextFence.hasFenceBases() && nextFence.isSecurityLevelValid(securityLV)) || (this.hasFenceBases() && this.isSecurityLevelValid(securityLV)))
                return (this.getFenceBasesStored() + nextFence.getFenceBasesStored() >= this.getRequiredFenceBases(direction));
        }

        return false;
    }

    public int getRequiredFencePoles(int securityLV) {
        if (securityLV == 0 || securityLV == 2)
            return 4;
        else
            return 2;
    }

    public boolean hasEnoughFencePoles(int securityLV) {
        BlockEntity tileEntity = null;

        switch (fenceDirection) {
            case 0:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + this.calculateDistanceToNextPole(fenceDirection)));
                break;
            case 1:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() - this.calculateDistanceToNextPole(fenceDirection), this.getBlockPos().getY(), this.getBlockPos().getZ()));
                break;
            case 2:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - this.calculateDistanceToNextPole(fenceDirection)));
                break;
            case 3:
                tileEntity = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() + this.calculateDistanceToNextPole(fenceDirection), this.getBlockPos().getY(), this.getBlockPos().getZ()));
                break;
        }

        if (tileEntity instanceof TileSecurityFence) {
            TileSecurityFence nextFence = (TileSecurityFence) tileEntity;

            if ((nextFence.hasFencePoles() && nextFence.isSecurityLevelValid(securityLV)) || (this.hasFencePoles() && this.isSecurityLevelValid(securityLV)))
                return (this.getFencePolesStored() + nextFence.getFencePolesStored() >= this.getRequiredFencePoles(securityLV));
        }

        return false;
    }

    private boolean hasSecurityFenceMainBlockAt(Level world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockSecurityFenceLowCorner;
    }

    public boolean canCraftBases(int number) {
        return (number > 0 && this.slots[0] != null && this.slots[0].getCount() - 8 * number > -1 && this.getFenceBasesStored() + number <= 256);
    }

    public boolean canCraftGrids(int number) {
        return (number > 0 && this.slots[0] != null && this.slots[1] != null && this.slots[0].getCount() - 1 * number > -1 && this.slots[1].getCount() - 3 * number > -1 && this.getFenceGridsStored() + number <= 256);
    }

    public boolean canCraftPoles(int number) {
        return (number > 0 && this.slots[0] != null && this.slots[1] != null && this.slots[0].getCount() - 6 * number > -1 && this.slots[1].getCount() - 4 * number > -1 && this.getFencePolesStored() + number <= 256);
    }

    public void tryToIncreaseFenceBases(int number) {
        if (this.canCraftBases(number)) {
            this.setFenceBasesStored((short) (this.getFenceBasesStored() + number));
            this.slots[0].shrink(8 * number);

            if (this.slots[0].getCount() < 1)
                this.slots[0] = ItemStack.EMPTY;
        }
    }

    public void tryToIncreaseFenceGrids(int number) {
        if (this.canCraftGrids(number)) {
            this.setFenceGridsStored((short) (this.getFenceGridsStored() + number));
            this.slots[0].shrink(number);

            if (this.slots[0].getCount() < 1)
                this.slots[0] = ItemStack.EMPTY;

            this.slots[1].shrink(3 * number);

            if (this.slots[1].getCount() < 1)
                this.slots[1] = ItemStack.EMPTY;
        }
    }

    public void tryToIncreaseFencePoles(int number) {
        if (this.canCraftPoles(number)) {
            this.setFencePolesStored((short) (this.getFencePolesStored() + number));
            this.slots[0].shrink(6 * number);

            if (this.slots[0].getCount() < 1)
                this.slots[0] = ItemStack.EMPTY;

            this.slots[1].shrink(4 * number);

            if (this.slots[1].getCount() < 1)
                this.slots[1] = ItemStack.EMPTY;
        }
    }

    private boolean hasSecurityFencePoleAt(Level world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof IFencePole;
    }

    private boolean hasSecurityFenceGridAt(Level world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof IFenceGrid;
    }

    private boolean hasSecurityFenceBaseAt(Level world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof IFenceBase;
    }

    /**
     * Returns the BlockEntity of the next fence in a certain direction related to another fence. It will check only for a range of 11 blocks apart.
     */
    public TileSecurityFence getNextFence(TileSecurityFence fence, int direction) {
        BlockEntity tileEntity = null;
        switch (direction) {
            /** South */
            case 0:
                for (int i = 1; i < 12; i++) {
                    if (fence.hasSecurityFenceMainBlockAt(fence.level, fence.getBlockPos().getX(), fence.getBlockPos().getY(), fence.getBlockPos().getZ() + i))
                        tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX(), fence.getBlockPos().getY(), fence.getBlockPos().getZ() + i));
                }
                break;
            /** West */
            case 1:
                for (int i = 1; i < 12; i++) {
                    if (fence.hasSecurityFenceMainBlockAt(fence.level, fence.getBlockPos().getX() - i, fence.getBlockPos().getY(), fence.getBlockPos().getZ()))
                        tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX() - i, fence.getBlockPos().getY(), fence.getBlockPos().getZ()));
                }
                break;
            /** North */
            case 2:
                for (int i = 1; i < 12; i++) {
                    if (fence.hasSecurityFenceMainBlockAt(fence.level, fence.getBlockPos().getX(), fence.getBlockPos().getY(), fence.getBlockPos().getZ() - i))
                        tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX(), fence.getBlockPos().getY(), fence.getBlockPos().getZ() - i));
                }
                break;
            /** East */
            case 3:
                for (int i = 1; i < 12; i++) {
                    if (fence.hasSecurityFenceMainBlockAt(fence.level, fence.getBlockPos().getX() + i, fence.getBlockPos().getY(), fence.getBlockPos().getZ()))
                        tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX() + i, fence.getBlockPos().getY(), fence.getBlockPos().getZ()));
                }
                break;
        }

        if (tileEntity instanceof TileSecurityFence)
            return (TileSecurityFence) tileEntity;
        else
            return null;
    }

    /**
     * Returns the BlockEntity of the next fence in a certain direction related to another fence and distance.
     */
    public TileSecurityFence getNextFence(TileSecurityFence fence, int direction, int distance) {
        BlockEntity tileEntity = null;

        switch (direction) {
            case 0:
                tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX(), fence.getBlockPos().getY(), fence.getBlockPos().getZ() + distance));
                break;
            case 1:
                tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX() - distance, fence.getBlockPos().getY(), fence.getBlockPos().getZ()));
                break;
            case 2:
                tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX(), fence.getBlockPos().getY(), fence.getBlockPos().getZ() - distance));
                break;
            case 3:
                tileEntity = fence.level.getBlockEntity(new BlockPos(fence.getBlockPos().getX() + distance, fence.getBlockPos().getY(), fence.getBlockPos().getZ()));
                break;
        }

        if (tileEntity instanceof TileSecurityFence)
            return (TileSecurityFence) tileEntity;
        else
            return null;
    }

    /**
     * Returns the distance of the next fence in a certain direction.
     */
    public int calculateDistanceToNextPole(int direction) {
        switch (direction) {
            /** South */
            case 0:
                for (int i = 1; i < 12; i++) {
                    if (this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + i))
                        return i;
                }
                break;
            /** West */
            case 1:
                for (int i = 1; i < 12; i++) {
                    if (this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() - i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                        return i;
                }
                break;
            /** North */
            case 2:
                for (int i = 1; i < 12; i++) {
                    if (this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - i))
                        return i;
                }
                break;
            /** East */
            case 3:
                for (int i = 1; i < 12; i++) {
                    if (this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                        return i;
                }
                break;
        }
        return 0;
    }

    /**
     * Returns the height of the pole connected to this block.
     */
    public int calculateHeighOfThePole() {
        int i = 0;

        while (this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + i + 1, this.getBlockPos().getZ()))
            i++;

        return i;
    }

    /**
     * Returns a HashMap<Integer, int[]> with all fence blocks between an area of 22 x 22. Integer array (int[]) with the block coordinates: (0 = xCoord, 1 = yCoord, 2 = zCoord)
     */
    public HashMap<Integer, int[]> getAllMainBlocks() {
        HashMap<Integer, int[]> map = new HashMap<Integer, int[]>();

        int numberOfBlocks = 0;

        for (int i = -11; i < 12; i++) {
            for (int k = -11; k < 12; k++) {
                if (this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k) || this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k) || this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k) || this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ() + k)) {
                    map.put(numberOfBlocks, new int[] { i, k });
                    numberOfBlocks++;
                }
            }
        }

        return map;
    }

    /**
     * Returns the number of blocks missing depending on the security level and direction.
     */
    public int[] getBlocksToRepair(int security, int direction) {
        return this.getBlocksToRepair(security, direction, this.calculateDistanceToNextPole(direction));
    }

    /**
     * Returns the number of blocks missing depending on the security level, direction, and distance.
     */
    public int[] getBlocksToRepair(int security, int direction, int distance) {
        int[] brokenBlocks = new int[3];
        int height = 0;

        switch (security) {
            case 1:
                height = 2;
                break;
            case 2:
                height = 3;
                break;
            case 3:
                height = 5;
                break;
        }

        if (security > 0 && security < 4) {
            switch (direction) {
                case 0:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + i))
                            brokenBlocks[0]++;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + i))
                                brokenBlocks[1]++;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                            brokenBlocks[2]++;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + distance))
                            brokenBlocks[2]++;
                    }
                    break;
                case 1:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() - 1, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            brokenBlocks[0]++;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() - 1, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                brokenBlocks[1]++;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                            brokenBlocks[2]++;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                            brokenBlocks[2]++;
                    }
                    break;
                case 2:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - i))
                            brokenBlocks[0]++;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - i))
                                brokenBlocks[1]++;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                            brokenBlocks[2]++;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - distance))
                            brokenBlocks[2]++;
                    }
                    break;
                case 3:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            brokenBlocks[0]++;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                brokenBlocks[1]++;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                            brokenBlocks[2]++;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                            brokenBlocks[2]++;
                    }
                    break;
            }
        } else {
            brokenBlocks = new int[] { 0, 0, 0 };
        }

        return brokenBlocks;
    }

    /**
     * Returns true if the fence can be repaired. This checks for air blocks.
     */
    public boolean canRepair(int security, int direction) {
        int distance = this.calculateDistanceToNextPole(direction);
        int height = 0;

        switch (security) {
            case 1:
                height = 2;
                break;
            case 2:
                height = 3;
                break;
            case 3:
                height = 5;
                break;
        }

        if (security > 0 && security < 4) {
            switch (direction) {
                case 0:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + i) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + i)))
                            return false;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + i) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + i)))
                                return false;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                            return false;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + distance) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + distance)))
                            return false;
                    }
                    break;
                case 1:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() - 1, this.getBlockPos().getY(), this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - 1, this.getBlockPos().getY(), this.getBlockPos().getZ())))
                            return false;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() - 1, this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - 1, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                                return false;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                            return false;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                            return false;
                    }
                    break;
                case 2:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - i) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - i)))
                            return false;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - i) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - i)))
                                return false;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                            return false;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - distance) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - distance)))
                            return false;
                    }
                    break;
                case 3:
                    for (int i = 1; i < distance; i++) {
                        if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ())))
                            return false;
                    }
                    for (int i = 1; i < distance; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                                return false;
                        }
                    }
                    for (int j = 1; j <= height; j++) {
                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                            return false;

                        if (!this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ()) && !this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                            return false;
                    }
                    break;
            }
        }

        return true;
    }

    /**
     * Returns true if there is the required structure to build or upgrade the security fence.
     */
    public boolean hasRequiredStructure(int securityLevel, int direction, int distance) {
        switch (direction)
        // 3 messy 5 me TODO
        {
            /** South */
            case 0:
                switch (securityLevel) {
                    case 0:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + distance))
                            return false;

                        for (int j = 1; j <= 2; j++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                return false;

                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + distance))
                                return false;
                        }

                        for (int j = 0; j <= 2; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + k)))
                                    return false;
                            }
                        }

                        return true;
                    case 1:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + distance))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ() + distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ() + distance))
                            return false;

                        for (int k = 1; k < distance - 1; k++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + k))
                                return false;
                        }

                        for (int j = 1; j <= 2; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + k))
                                    return false;
                            }
                        }

                        for (int k = 1; k < distance - 1; k++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ() + k)))
                                return false;
                        }

                        return true;
                    case 2:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + distance))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ() + distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ() + distance))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ() + distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ() + distance))
                            return false;

                        for (int k = 1; k < distance - 1; k++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + k))
                                return false;
                        }

                        for (int j = 1; j <= 3; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + k))
                                    return false;
                            }
                        }

                        for (int j = 4; j <= 5; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() + k)))
                                    return false;
                            }
                        }

                        return true;
                    default:
                        return false;
                }
                /** West */
            case 1:
                switch (securityLevel) {
                    case 0:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        for (int j = 1; j <= 2; j++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                return false;

                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                return false;
                        }

                        for (int i = 1; i < distance - 1; i++) {
                            for (int j = 0; j <= 2; j++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - i, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                                    return false;
                            }
                        }

                        return true;
                    case 1:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - distance, this.getBlockPos().getY() + 3, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY() + 3, this.getBlockPos().getZ()))
                            return false;

                        for (int i = 1; i < distance - 1; i++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() - i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                                return false;
                        }

                        for (int i = 1; i < distance - 1; i++) {
                            for (int j = 1; j <= 2; j++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() - i, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                    return false;
                            }
                        }

                        for (int i = 1; i < distance - 1; i++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - i, this.getBlockPos().getY() + 3, this.getBlockPos().getZ())))
                                return false;
                        }

                        return true;
                    case 2:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - distance, this.getBlockPos().getY() + 4, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY() + 4, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - distance, this.getBlockPos().getY() + 5, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() - distance, this.getBlockPos().getY() + 5, this.getBlockPos().getZ()))
                            return false;

                        for (int i = 1; i < distance - 1; i++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() - i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                                return false;
                        }

                        for (int j = 1; j <= 3; j++) {
                            for (int i = 1; i < distance - 1; i++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() - i, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                    return false;
                            }
                        }

                        for (int j = 4; j <= 5; j++) {
                            for (int i = 1; i < distance - 1; i++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() - i, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                                    return false;
                            }
                        }

                        return true;
                    default:
                        return false;
                }
                /** North */
            case 2:
                switch (securityLevel) {
                    case 0:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - distance))
                            return false;

                        for (int j = 1; j <= 2; j++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                return false;

                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - distance))
                                return false;
                        }

                        for (int j = 0; j <= 2; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - k)))
                                    return false;
                            }
                        }

                        return true;
                    case 1:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - distance))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ() - distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ() - distance))
                            return false;

                        for (int k = 1; k < distance - 1; k++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - k))
                                return false;
                        }

                        for (int j = 1; j <= 2; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - k))
                                    return false;
                            }
                        }

                        for (int k = 1; k < distance - 1; k++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ() - k)))
                                return false;
                        }

                        return true;
                    case 2:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - distance))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ() - distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ() - distance))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ() - distance)) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ() - distance))
                            return false;

                        for (int k = 1; k < distance - 1; k++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - k))
                                return false;
                        }

                        for (int j = 1; j <= 3; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - k))
                                    return false;
                            }
                        }

                        for (int j = 4; j <= 5; j++) {
                            for (int k = 1; k < distance - 1; k++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ() - k)))
                                    return false;
                            }
                        }

                        return true;
                    default:
                        return false;
                }
                /** East */
            case 3:
                switch (securityLevel) {
                    case 0:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        for (int j = 1; j <= 2; j++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                return false;

                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                return false;
                        }

                        for (int i = 1; i < distance - 1; i++) {
                            for (int j = 0; j <= 2; j++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                                    return false;
                            }
                        }

                        return true;
                    case 1:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 3, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + distance, this.getBlockPos().getY() + 3, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY() + 3, this.getBlockPos().getZ()))
                            return false;

                        for (int i = 1; i < distance - 1; i++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                                return false;
                        }

                        for (int i = 1; i < distance - 1; i++) {
                            for (int j = 1; j <= 2; j++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                    return false;
                            }
                        }

                        for (int i = 1; i < distance - 1; i++) {
                            if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + i, this.getBlockPos().getY() + 3, this.getBlockPos().getZ())))
                                return false;
                        }

                        return true;
                    case 2:
                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.hasSecurityFenceMainBlockAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 4, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 5, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + distance, this.getBlockPos().getY() + 4, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY() + 4, this.getBlockPos().getZ()))
                            return false;

                        if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + distance, this.getBlockPos().getY() + 5, this.getBlockPos().getZ())) && !this.hasSecurityFencePoleAt(this.level, this.getBlockPos().getX() + distance, this.getBlockPos().getY() + 5, this.getBlockPos().getZ()))
                            return false;

                        for (int i = 1; i < distance - 1; i++) {
                            if (!this.hasSecurityFenceBaseAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY(), this.getBlockPos().getZ()))
                                return false;
                        }

                        for (int j = 1; j <= 3; j++) {
                            for (int i = 1; i < distance - 1; i++) {
                                if (!this.hasSecurityFenceGridAt(this.level, this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ()))
                                    return false;
                            }
                        }

                        for (int j = 4; j <= 5; j++) {
                            for (int i = 1; i < distance - 1; i++) {
                                if (!this.level.isEmptyBlock(new BlockPos(this.getBlockPos().getX() + i, this.getBlockPos().getY() + j, this.getBlockPos().getZ())))
                                    return false;
                            }
                        }

                        return true;
                    default:
                        return false;
                }
                /** Error! */
            default:
                return false;
        }
    }

    private void placeFenceBlocks(Level world, int security, int direction, int distance, int height, int x, int y, int z) {
        Block fenceBase = Blocks.AIR;
        Block fenceGrid = Blocks.AIR;
        Block fencePole = Blocks.AIR;
        int blockMetadata = 0;

        switch (security) {
        /*
         * case 0: fenceBase = JCBlockRegistry.securityFenceLowBase; fenceGrid = JCBlockRegistry.securityFenceLowFence; fencePole = JCBlockRegistry.securityFenceLowPole; break; case 1: fenceBase = JCBlockRegistry.securityFenceMediumBase; fenceGrid = JCBlockRegistry.securityFenceMediumFence; fencePole = JCBlockRegistry.securityFenceMediumPole; break; case 2: fenceBase = JCBlockRegistry.securityFenceHighBase; fenceGrid = JCBlockRegistry.securityFenceHighFence; fencePole = JCBlockRegistry.securityFenceHighPole; break;
         */
        }

        switch (direction) {
            /** South */
            case 0:
                blockMetadata = 3;

                for (int j = 1; j < height + 1; j++) {
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y + j, z));
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), fencePole.defaultBlockState());
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z + distance), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y + j, z + distance));
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z + distance), fencePole.defaultBlockState());
                }

                for (int k = 1; k < distance; k++) {
                    world.setBlockAndUpdate(new BlockPos(x, y, z + k), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y, z + k));
                    world.setBlockAndUpdate(new BlockPos(x, y, z + k), fenceBase.defaultBlockState());
                }

                for (int j = 1; j < height + 1; j++) {
                    for (int k = 1; k < distance; k++) {
                        world.setBlockAndUpdate(new BlockPos(x, y + j, z + k), Blocks.AIR.defaultBlockState());
                        world.removeBlockEntity(new BlockPos(x, y + j, z + k));
                        world.setBlockAndUpdate(new BlockPos(x, y + j, z + k), fenceGrid.defaultBlockState());
                    }
                }

                return;
            /** West */
            case 1:
                blockMetadata = 0;

                for (int j = 1; j < height + 1; j++) {
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y + j, z));
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), fencePole.defaultBlockState());
                    world.setBlockAndUpdate(new BlockPos(x - distance, y + j, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x - distance, y + j, z));
                    world.setBlockAndUpdate(new BlockPos(x - distance, y + j, z), fencePole.defaultBlockState());
                }

                for (int i = 1; i < distance; i++) {
                    world.setBlockAndUpdate(new BlockPos(x - i, y, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x - i, y, z));
                    world.setBlockAndUpdate(new BlockPos(x - i, y, z), fenceBase.defaultBlockState());
                }

                for (int j = 1; j < height + 1; j++) {
                    for (int i = 1; i < distance; i++) {
                        world.setBlockAndUpdate(new BlockPos(x - i, y + j, z), Blocks.AIR.defaultBlockState());
                        world.removeBlockEntity(new BlockPos(x - i, y + j, z));
                        world.setBlockAndUpdate(new BlockPos(x - i, y + j, z), fenceGrid.defaultBlockState());
                    }
                }

                return;
            /** North */
            case 2:
                blockMetadata = 1;

                for (int j = 1; j < height + 1; j++) {
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y + j, z));
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), fencePole.defaultBlockState());
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z - distance), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y + j, z - distance));
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z - distance), fencePole.defaultBlockState());
                }

                for (int k = 1; k < distance; k++) {
                    world.setBlockAndUpdate(new BlockPos(x, y, z - k), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y, z - k));
                    world.setBlockAndUpdate(new BlockPos(x, y, z - k), fenceBase.defaultBlockState());
                }

                for (int j = 1; j < height + 1; j++) {
                    for (int k = 1; k < distance; k++) {
                        world.setBlockAndUpdate(new BlockPos(x, y + j, z - k), Blocks.AIR.defaultBlockState());
                        world.removeBlockEntity(new BlockPos(x, y + j, z - k));
                        world.setBlockAndUpdate(new BlockPos(x, y + j, z - k), fenceGrid.defaultBlockState());
                    }
                }
                return;
            /** East */
            case 3:
                blockMetadata = 2;

                for (int j = 1; j < height + 1; j++) {
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x, y + j, z));
                    world.setBlockAndUpdate(new BlockPos(x, y + j, z), fencePole.defaultBlockState());
                    world.setBlockAndUpdate(new BlockPos(x + distance, y + j, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x + distance, y + j, z));
                    world.setBlockAndUpdate(new BlockPos(x + distance, y + j, z), fencePole.defaultBlockState());
                }

                for (int i = 1; i < distance; i++) {
                    world.setBlockAndUpdate(new BlockPos(x + i, y, z), Blocks.AIR.defaultBlockState());
                    world.removeBlockEntity(new BlockPos(x + i, y, z));
                    world.setBlockAndUpdate(new BlockPos(x + i, y, z), fenceBase.defaultBlockState());
                }

                for (int j = 1; j < height + 1; j++) {
                    for (int i = 1; i < distance; i++) {
                        world.setBlockAndUpdate(new BlockPos(x + i, y + j, z), Blocks.AIR.defaultBlockState());
                        world.removeBlockEntity(new BlockPos(x + i, y + j, z));
                        world.setBlockAndUpdate(new BlockPos(x + i, y + j, z), fenceGrid.defaultBlockState());
                    }
                }

                return;
        }

        return;
    }

    private void setFenceUpgrade(TileSecurityFence nextFence, int security, int direction) {
        switch (direction) {
            case 0:
                this.setSecurityLevel((byte) (security + 1), 0);
                nextFence.setSecurityLevel((byte) (security + 1), 2);
                break;
            case 1:
                this.setSecurityLevel((byte) (security + 1), 1);
                nextFence.setSecurityLevel((byte) (security + 1), 3);
                break;
            case 2:
                this.setSecurityLevel((byte) (security + 1), 2);
                nextFence.setSecurityLevel((byte) (security + 1), 0);
                break;
            case 3:
                this.setSecurityLevel((byte) (security + 1), 3);
                nextFence.setSecurityLevel((byte) (security + 1), 1);
                break;
        }

    }

    private void consumeMaterials(TileSecurityFence nextFence, int security, int direction) {
        int prevMaterial = this.getFenceBasesStored();
        int requiredMaterial = this.getRequiredFenceBases(direction);
        this.setFenceBasesStored((short) (this.getFenceBasesStored() - requiredMaterial));

        if (this.getFenceBasesStored() <= 0) {
            this.setFenceBasesStored((short) 0);
            requiredMaterial = requiredMaterial - prevMaterial;
            nextFence.setFenceBasesStored((short) (nextFence.getFenceBasesStored() - requiredMaterial));
        }

        prevMaterial = this.getFenceGridsStored();
        requiredMaterial = this.getRequiredFenceGrids(security, direction);
        this.setFenceGridsStored((short) (this.getFenceGridsStored() - requiredMaterial));

        if (this.getFenceGridsStored() <= 0) {
            this.setFenceGridsStored((short) 0);
            requiredMaterial = requiredMaterial - prevMaterial;
            nextFence.setFenceGridsStored((short) (nextFence.getFenceGridsStored() - requiredMaterial));
        }

        prevMaterial = this.getFencePolesStored();
        requiredMaterial = this.getRequiredFencePoles(security);
        this.setFencePolesStored((short) (this.getFencePolesStored() - requiredMaterial));

        if (this.getFencePolesStored() <= 0) {
            this.setFencePolesStored((short) 0);
            requiredMaterial = requiredMaterial - prevMaterial;
            nextFence.setFencePolesStored((short) (nextFence.getFencePolesStored() - requiredMaterial));
        }
    }

    private void consumeMaterials(int bases, int grids, int poles) {
        this.setFenceBasesStored((short) (this.getFenceBasesStored() - bases));

        if (this.getFenceBasesStored() < 0)
            this.setFenceBasesStored((short) 0);

        this.setFenceGridsStored((short) (this.getFenceGridsStored() - grids));

        if (this.getFenceGridsStored() < 0)
            this.setFenceGridsStored((short) 0);
        this.setFencePolesStored((short) (this.getFencePolesStored() - poles));

        if (this.getFencePolesStored() < 0)
            this.setFencePolesStored((short) 0);

    }

    public boolean canBuildFence(int securityLV, int direction, int length, int height) {
        return (this.isSecurityLevelValid(securityLV) && this.isHeightValid(height) && this.isDistanceValid(length) && this.isFenceValid(direction, securityLV) && this.hasEnoughFenceBases(securityLV, direction) && this.hasEnoughFenceGrids(securityLV, direction) && this.hasEnoughFencePoles(securityLV) && this.hasRequiredStructure(securityLV, direction, length));
    }

    public void tryToBuildFence(int security, int direction, int distance, int height) {
        if (!this.level.isClientSide) {
            if (this.canBuildFence(security, direction, distance, height)) {
                TileSecurityFence nextFence = this.getNextFence(this, direction, distance);
                this.consumeMaterials(nextFence, security, direction);
                this.setFenceUpgrade(nextFence, security, direction);
                this.placeFenceBlocks(this.level, security, direction, distance, height, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ());
            }
        }
    }

    public void tryToFixFence(int security, int direction, int distance, int height, int bases, int grids, int poles) {
        if (!this.level.isClientSide) {
            this.placeFenceBlocks(this.level, security, direction, distance, height, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ());
            this.consumeMaterials(bases, grids, poles);
        }
    }

    public boolean hasItems() {
        return this.slots[0] != null || this.slots[1] != null;
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
    public ItemStack removeItem(int i, int amount) {
        if (this.slots[i] != null) {
            ItemStack splitedStack;

            if (this.slots[i].getCount() <= amount) {
                splitedStack = this.slots[i];
                this.slots[i] = ItemStack.EMPTY;
                return splitedStack;
            } else {
                splitedStack = this.slots[i].split(amount);

                if (this.slots[i].getCount() == 0)
                    this.slots[i] = ItemStack.EMPTY;

                return splitedStack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack decrStackSize(int i, int amount) {
        return this.removeItem(i, amount);
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

        if (itemStack != null && itemStack.getCount() > this.getMaxStackSize())
            if (this.getMaxStackSize() > 0) { itemStack.setCount(this.getMaxStackSize()); } else { itemStack = ItemStack.EMPTY; }
    }

    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.setItem(i, itemStack);
    }

    public String getInventoryName() {
        return "Security Fence Base";
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
        return this.level.getBlockEntity(this.getBlockPos()) == this && player.distanceToSqr((double) this.getBlockPos().getX() + 0.5D, (double) this.getBlockPos().getY() + 0.5D, (double) this.getBlockPos().getZ() + 0.5D) <= 64.0D;
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
        compound.putByte("Direction", this.getDirection());
        compound.putByte("SecurityLevelSouth", this.getSecurityLevel(0));
        compound.putByte("SecurityLevelWest", this.getSecurityLevel(1));
        compound.putByte("SecurityLevelNorth", this.getSecurityLevel(2));
        compound.putByte("SecurityLevelEast", this.getSecurityLevel(3));
        compound.putShort("GridsStored", (short) this.getFenceGridsStored());
        compound.putShort("BasesStored", (short) this.getFenceBasesStored());
        compound.putShort("PolesStored", (short) this.getFencePolesStored());
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
        this.setDirection(compound.getByte("Direction"));
        this.setSecurityLevel(compound.getByte("SecurityLevelSouth"), 0);
        this.setSecurityLevel(compound.getByte("SecurityLevelWest"), 1);
        this.setSecurityLevel(compound.getByte("SecurityLevelNorth"), 2);
        this.setSecurityLevel(compound.getByte("SecurityLevelEast"), 3);
        this.setFenceGridsStored(compound.getShort("GridsStored"));
        this.setFenceBasesStored(compound.getShort("BasesStored"));
        this.setFencePolesStored(compound.getShort("PolesStored"));
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
