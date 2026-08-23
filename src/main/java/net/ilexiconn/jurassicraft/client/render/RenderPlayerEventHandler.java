package net.ilexiconn.jurassicraft.client.render;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class RenderPlayerEventHandler {
    public ResourceLocation capeDeveloper = new ResourceLocation("jurassicraft", "textures/cape/developer_cape.png");
    public ResourceLocation capePatron = new ResourceLocation("jurassicraft", "textures/cape/patron_cape.png");

    @SubscribeEvent
    public void playerRender(RenderPlayerEvent.Pre event) {
        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();

        UUID uniqueID = player.getUUID();

        try {
            if (isDeveloper(uniqueID))
                setCape(player, capeDeveloper);
            else if (isPatron(uniqueID))
                setCape(player, capePatron);
        } catch (Exception e) {
            System.err.println("Failed to load capes!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void setCape(AbstractClientPlayer player, ResourceLocation cape) throws Exception {
        Method getPlayerInfo = AbstractClientPlayer.class.getDeclaredMethod("getPlayerInfo");
        getPlayerInfo.setAccessible(true);
        PlayerInfo playerInfo = (PlayerInfo) getPlayerInfo.invoke(player);
        if (playerInfo != null) {
            Field playerTexturesField = PlayerInfo.class.getDeclaredField("playerTextures");
            playerTexturesField.setAccessible(true);
            Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = (Map<MinecraftProfileTexture.Type, ResourceLocation>) playerTexturesField.get(playerInfo);
            playerTextures.put(MinecraftProfileTexture.Type.CAPE, cape);
        }
    }

    public boolean isDeveloper(UUID uuid) throws Exception {
        return JurassiCraft.capeContainer != null && JurassiCraft.capeContainer.getDevelopers().contains(uuid.toString());
    }

    public boolean isPatron(UUID uuid) throws Exception {
        return JurassiCraft.capeContainer != null && JurassiCraft.capeContainer.getPatrons().contains(uuid.toString());
    }
}
