package net.ilexiconn.jurassicraft.common.message;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAnimation extends AbstractMessage<MessageAnimation> {
    private byte animationId;
    private int entityId;

    public MessageAnimation() {
        this((byte) 0, 0);
    }

    public MessageAnimation(byte animation, int entity) {
        animationId = animation;
        entityId = entity;
    }

    /**
     * Client-side handling is delegated to the client proxy (this class is loaded
     * on the server too, so it must not reference client-only classes).
     */
    @Override
    public void onClientReceived(MessageAnimation message, Player player, NetworkEvent.Context messageContext) {
        JurassiCraft.proxy.onAnimationMessage(message, message.entityId, message.animationId);
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageAnimation message, ServerPlayer player, NetworkEvent.Context messageContext) {
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByte(animationId);
        buffer.writeInt(entityId);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        animationId = buffer.readByte();
        entityId = buffer.readInt();
    }

    public static void encode(MessageAnimation msg, FriendlyByteBuf buf) {
        msg.toBytes(buf);
    }

    public static MessageAnimation decode(FriendlyByteBuf buf) {
        return AbstractMessage.decode(MessageAnimation.class, buf);
    }

    public static void handle(MessageAnimation msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        if (AbstractMessage.isClientSide(ctx)) {
            ctx.enqueueWork(() -> msg.onClientReceived(msg, null, ctx));
        } else {
            ctx.enqueueWork(() -> msg.onServerReceived(ctx.getSender().server, msg, ctx.getSender(), ctx));
        }
        ctx.setPacketHandled(true);
    }
}
