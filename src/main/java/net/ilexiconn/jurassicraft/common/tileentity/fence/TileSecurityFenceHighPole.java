package net.ilexiconn.jurassicraft.common.tileentity.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSecurityFenceHighPole extends BlockEntity {
    private boolean[] builtFences = new boolean[4];

    public TileSecurityFenceHighPole() {
        this(BlockPos.ZERO, null);
    }

    public TileSecurityFenceHighPole(BlockPos pos, BlockState state) {
        super(null, pos, state);
        for (int i = 0; i < builtFences.length; i++)
            builtFences[i] = false;
    }

    public void setGridAtSide(int side, boolean flag) {
        this.builtFences[side] = flag;
        if (this.level != null)
            this.setChanged();
    }

    public boolean getGridAtSide(int side) {
        return this.builtFences[side];
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        for (int i = 0; i < builtFences.length; i++)
            compound.putBoolean("GridAtSide" + i, this.getGridAtSide(i));
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        for (int i = 0; i < builtFences.length; i++)
            this.setGridAtSide(i, compound.getBoolean("GridAtSide" + i));
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
