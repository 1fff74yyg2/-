package net.ilexiconn.jurassicraft.common.message;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Requests the pregnancy state of a vanilla animal for the DinoPad pregnancy GUI
 * (the state lives on the server; the client has no access to it). The client
 * sends an empty request, the server replies with the actual data, and the
 * client proxy fills its local EntityPregnant* entries so the GUI renders it.
 */
public class MessagePregnancy extends AbstractMessage<MessagePregnancy> {
    private int entityId;
    private String mammalName = "noEmbryo";
    private int dnaQuality;
    private int progress;
    private int speed;

    public MessagePregnancy() {
    }

    public MessagePregnancy(int entityId, String mammalName, int quality, int progress, int speed) {
        this.entityId = entityId;
        this.mammalName = mammalName == null ? "noEmbryo" : mammalName;
        this.dnaQuality = quality;
        this.progress = progress;
        this.speed = speed;
    }

    @Override
    public void onClientReceived(MessagePregnancy message, Player player, NetworkEvent.Context messageContext) {
        // Delegated to the client proxy (this class is loaded on the server too).
        JurassiCraft.proxy.onPregnancyMessage(message.entityId, message.mammalName, message.dnaQuality, message.progress, message.speed);
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePregnancy message, ServerPlayer player, NetworkEvent.Context messageContext) {
        Entity entity = player.level().getEntity(message.entityId);
        String name = "noEmbryo";
        int q = 0;
        int p = 0;
        int s = 2048;

        if (entity instanceof net.minecraft.world.entity.animal.Cow) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow.get((net.minecraft.world.entity.animal.Cow) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.Pig) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig.get((net.minecraft.world.entity.animal.Pig) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.horse.Horse) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse.get((net.minecraft.world.entity.animal.horse.Horse) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.Sheep) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep.get((net.minecraft.world.entity.animal.Sheep) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.goat.Goat) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get((net.minecraft.world.entity.animal.goat.Goat) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.camel.Camel) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get((net.minecraft.world.entity.animal.camel.Camel) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.Fox) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get((net.minecraft.world.entity.animal.Fox) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        } else if (entity instanceof net.minecraft.world.entity.animal.Panda) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get((net.minecraft.world.entity.animal.Panda) entity);
            if (c != null) { name = c.getMammalName(); q = c.getDNAQuality(); p = c.getPregnancyProgress(); s = c.getPregnancySpeed(); }
        }

        JurassiCraft.network.send(net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
                new MessagePregnancy(message.entityId, name, q, p, s));
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUtf(mammalName);
        buffer.writeVarInt(dnaQuality);
        buffer.writeVarInt(progress);
        buffer.writeVarInt(speed);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        entityId = buffer.readVarInt();
        mammalName = buffer.readUtf();
        dnaQuality = buffer.readVarInt();
        progress = buffer.readVarInt();
        speed = buffer.readVarInt();
    }

    public static void encode(MessagePregnancy msg, FriendlyByteBuf buf) {
        msg.toBytes(buf);
    }

    public static MessagePregnancy decode(FriendlyByteBuf buf) {
        return AbstractMessage.decode(MessagePregnancy.class, buf);
    }

    public static void handle(MessagePregnancy msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        if (AbstractMessage.isClientSide(ctx)) {
            ctx.enqueueWork(() -> msg.onClientReceived(msg, null, ctx));
        } else {
            ctx.enqueueWork(() -> msg.onServerReceived(ctx.getSender().server, msg, ctx.getSender(), ctx));
        }
        ctx.setPacketHandled(true);
    }
}
