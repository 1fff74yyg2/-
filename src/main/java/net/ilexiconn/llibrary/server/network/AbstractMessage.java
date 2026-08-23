package net.ilexiconn.llibrary.server.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

/**
 * 1.20.1 compatible replacement for llibrary's AbstractMessage.
 * <p>
 * Messages are registered with a SimpleChannel using the classic
 * encoder/decoder/consumer pattern; the static handle() methods of the concrete
 * messages dispatch to onClientReceived/onServerReceived.
 * <p>
 * IMPORTANT (1.20.1 dist isolation): this base class and every message class are
 * loaded on BOTH sides, so their method bodies must never reference client-only
 * classes (Minecraft, LocalPlayer, ...). Client-side handling must be delegated to
 * the client proxy (ClientProxy) or a @OnlyIn(Dist.CLIENT) helper class.
 */
public abstract class AbstractMessage<T extends AbstractMessage<T>> {
    public abstract void toBytes(FriendlyByteBuf buffer);

    public abstract void fromBytes(FriendlyByteBuf buffer);

    public abstract void onClientReceived(T message, Player player, NetworkEvent.Context messageContext);

    public abstract void onServerReceived(MinecraftServer server, T message, ServerPlayer player, NetworkEvent.Context messageContext);

    public static boolean isClientSide(NetworkEvent.Context messageContext) {
        return messageContext.getDirection() == NetworkDirection.PLAY_TO_CLIENT;
    }

    /**
     * Decoder helper for SimpleChannel registration: constructs a fresh message
     * through its no-arg constructor and reads the payload into it.
     */
    public static <T extends AbstractMessage<T>> T decode(Class<T> clazz, FriendlyByteBuf buffer) {
        try {
            T message = clazz.getDeclaredConstructor().newInstance();
            message.fromBytes(buffer);
            return message;
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode message " + clazz.getName(), e);
        }
    }
}
