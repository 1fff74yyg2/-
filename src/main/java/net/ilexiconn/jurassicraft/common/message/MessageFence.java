package net.ilexiconn.jurassicraft.common.message;

import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowCorner;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageFence extends AbstractMessage<MessageFence> {
    private int id;
    private int xCoord;
    private int yCoord;
    private int zCoord;
    private int side;

    public MessageFence() {
    }

    public MessageFence(int id, int xCoord, int yCoord, int zCoord, int side) {
        this.id = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.side = side;
    }

    @Override
    public void onClientReceived(MessageFence message, Player player, NetworkEvent.Context messageContext) {
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageFence message, ServerPlayer player, NetworkEvent.Context messageContext) {
        Level world = player.level();
        if (!world.isClientSide) {
            if (message.id > -1 && message.id < 4) {
                BlockEntity blockEntity = world.getBlockEntity(new BlockPos(message.xCoord, message.yCoord, message.zCoord));
                switch (message.id) {
                    case 0:
                        if (blockEntity instanceof TileSecurityFenceLowCorner) {
                            if (message.side > -1 && message.side < 4)
                                ((TileSecurityFenceLowCorner) blockEntity).tryToBuildFence(message.side);
                        }
                        break;
                    case 1:
                        if (blockEntity instanceof TileSecurityFenceLowCorner) {
                            if (message.side > -1 && message.side < 4)
                                ((TileSecurityFenceLowCorner) blockEntity).tryToTurnOnTheFence(message.side);
                        }
                        break;
                    case 2:
                        if (blockEntity instanceof TileSecurityFenceLowCorner) {
                            if (message.side > -1 && message.side < 4)
                                ((TileSecurityFenceLowCorner) blockEntity).tryToFixFence(message.side);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        id = buf.readVarInt();
        xCoord = buf.readVarInt();
        yCoord = buf.readVarInt();
        zCoord = buf.readVarInt();
        side = buf.readVarInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(id);
        buf.writeVarInt(xCoord);
        buf.writeVarInt(yCoord);
        buf.writeVarInt(zCoord);
        buf.writeVarInt(side);
    }

    public static void encode(MessageFence msg, FriendlyByteBuf buf) {
        msg.toBytes(buf);
    }

    public static MessageFence decode(FriendlyByteBuf buf) {
        return AbstractMessage.decode(MessageFence.class, buf);
    }

    public static void handle(MessageFence msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        if (AbstractMessage.isClientSide(ctx)) {
            // Client has nothing to do for this message (the old implementation was empty).
            ctx.setPacketHandled(true);
            return;
        }
        ctx.enqueueWork(() -> msg.onServerReceived(ctx.getSender().server, msg, ctx.getSender(), ctx));
        ctx.setPacketHandled(true);
    }
}
