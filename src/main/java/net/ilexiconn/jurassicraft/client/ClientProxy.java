package net.ilexiconn.jurassicraft.client;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.client.gui.GuiCultivateProcess;
import net.ilexiconn.jurassicraft.client.gui.GuiDinoPad;
import net.ilexiconn.jurassicraft.client.gui.GuiDinoPadEgg;
import net.ilexiconn.jurassicraft.client.gui.GuiDinoPadPregnancy;
import net.ilexiconn.jurassicraft.client.render.JCRenderRegistry;
import net.ilexiconn.jurassicraft.client.render.RenderPlayerEventHandler;
import net.ilexiconn.jurassicraft.common.CommonProxy;
import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.ilexiconn.jurassicraft.common.entity.egg.EntityDinoEgg;
import net.ilexiconn.jurassicraft.common.message.MessageAnimation;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public float getPartialTick() {
        return Minecraft.getInstance().getPartialTick();
    }

    @Override
    public Level getWorldClient() {
        return Minecraft.getInstance().level;
    }

    @Override
    public void init() throws Exception {
        MinecraftForge.EVENT_BUS.register(new RenderPlayerEventHandler());
        JurassiCraft.entityParser.parseClientEntities();
    }

    @Override
    public void onAnimationMessage(MessageAnimation message, int entityId, byte animationId) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            Entity entity = world.getEntity(entityId);
            if (entity instanceof IAnimatedEntity) {
                IAnimatedEntity animated = (IAnimatedEntity) entity;
                animated.setAnimationId(animationId);
                if (animationId == 0) {
                    animated.setAnimationTick(0);
                }
            }
        }
    }

    @Override
    public void openCultivatorProgress(TileCultivate tile) {
        Minecraft.getInstance().setScreen(new GuiCultivateProcess(tile));
    }

    @Override
    public void onPregnancyMessage(int entityId, String mammalName, int quality, int progress, int speed) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            Entity entity = world.getEntity(entityId);
            if (entity instanceof Cow) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow.get((Cow) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof Pig) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig.get((Pig) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof Horse) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse.get((Horse) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof Sheep) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep.get((Sheep) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof net.minecraft.world.entity.animal.goat.Goat) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get((net.minecraft.world.entity.animal.goat.Goat) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof net.minecraft.world.entity.animal.camel.Camel) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get((net.minecraft.world.entity.animal.camel.Camel) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof net.minecraft.world.entity.animal.Fox) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get((net.minecraft.world.entity.animal.Fox) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            } else if (entity instanceof net.minecraft.world.entity.animal.Panda) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda c = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get((net.minecraft.world.entity.animal.Panda) entity);
                if (c != null) { c.setMammalName(mammalName); c.setDNAQuality(quality); c.setPregnancyProgress(progress); c.setPregnancySpeed(speed); }
            }
        }
    }

    @Override
    public void openDinoPad(Entity entity) {
        if (entity instanceof Cow || entity instanceof Pig || entity instanceof Horse || entity instanceof Sheep
                || entity instanceof net.minecraft.world.entity.animal.goat.Goat
                || entity instanceof net.minecraft.world.entity.animal.camel.Camel
                || entity instanceof net.minecraft.world.entity.animal.Fox
                || entity instanceof net.minecraft.world.entity.animal.Panda) {
            Minecraft.getInstance().setScreen(new GuiDinoPadPregnancy(entity));
        } else if (entity instanceof EntityDinoEgg) {
            Minecraft.getInstance().setScreen(new GuiDinoPadEgg(entity));
        } else if (entity instanceof EntityJurassiCraftSmart) {
            Minecraft.getInstance().setScreen(new GuiDinoPad(entity));
        }
    }

    @Override
    public void openEntityGui(EntityJurassiCraftSmart creature) {
        Minecraft.getInstance().setScreen(new GuiDinoPad(creature));
    }

    @SubscribeEvent
    public void onClientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.gui.screens.MenuScreens.register(net.ilexiconn.jurassicraft.common.handler.GuiHandler.CULTIVATE.get(),
                    (net.ilexiconn.jurassicraft.common.container.ContainerCultivate menu, net.minecraft.world.entity.player.Inventory inv, net.minecraft.network.chat.Component title) -> new net.ilexiconn.jurassicraft.client.gui.GuiCultivate(menu, inv, title));
            net.minecraft.client.gui.screens.MenuScreens.register(net.ilexiconn.jurassicraft.common.handler.GuiHandler.DNA_EXTRACTOR.get(),
                    (net.ilexiconn.jurassicraft.common.container.ContainerDNAExtractor menu, net.minecraft.world.entity.player.Inventory inv, net.minecraft.network.chat.Component title) -> new net.ilexiconn.jurassicraft.client.gui.GuiDNAExtractor(menu, inv, title));
            net.minecraft.client.gui.screens.MenuScreens.register(net.ilexiconn.jurassicraft.common.handler.GuiHandler.DNA_COMBINATOR.get(),
                    (net.ilexiconn.jurassicraft.common.container.ContainerDNACombinator menu, net.minecraft.world.entity.player.Inventory inv, net.minecraft.network.chat.Component title) -> new net.ilexiconn.jurassicraft.client.gui.GuiDNACombinator(menu, inv, title));
            net.minecraft.client.gui.screens.MenuScreens.register(net.ilexiconn.jurassicraft.common.handler.GuiHandler.SECURITY_FENCE_LOW.get(),
                    (net.ilexiconn.jurassicraft.common.container.ContainerSecurityFenceLow menu, net.minecraft.world.entity.player.Inventory inv, net.minecraft.network.chat.Component title) -> new net.ilexiconn.jurassicraft.client.gui.GuiSecurityFenceLow(menu, inv, title));
        });
    }

    @SubscribeEvent
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        JCRenderRegistry.registerRenderers(event);
    }
}
