package net.ilexiconn.jurassicraft;

import com.google.gson.Gson;
import net.ilexiconn.jurassicraft.common.CommonProxy;
import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.command.CommandSpawnDino;
import net.ilexiconn.jurassicraft.common.crafting.JCRecipeRegistry;
import net.ilexiconn.jurassicraft.common.creativetab.JCCreativeTabRegistry;
import net.ilexiconn.jurassicraft.common.data.CapeContainer;
import net.ilexiconn.jurassicraft.common.entity.JCEntityRegistry;
import net.ilexiconn.jurassicraft.common.events.JurassiCraftInteractEvent;
import net.ilexiconn.jurassicraft.common.events.JurassiCraftLivingEvent;
import net.ilexiconn.jurassicraft.common.handler.GuiHandler;
import net.ilexiconn.jurassicraft.common.handler.JCSoundRegistry;
import net.ilexiconn.jurassicraft.common.handler.JsonEntityHandler;
import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.ilexiconn.jurassicraft.common.message.MessageAnimation;
import net.ilexiconn.jurassicraft.common.message.MessageFence;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("jurassicraft")
public class JurassiCraft {
    public static final String MODID = "jurassicraft";
    public static final String MODNAME = "JurassiCraft";

    public static JurassiCraft instance;
    public static CommonProxy proxy;
    public static boolean enableDebugging;
    public static JsonEntityHandler entityParser;
    public static SimpleChannel network;
    public static int entityIndex = 0;
    public static CapeContainer capeContainer;
    public static Logger logger = LogManager.getLogger(MODID);

    public static String getModId() {
        return "jurassicraft:";
    }

    public JurassiCraft() {
        instance = this;
        proxy = DistExecutor.unsafeRunForDist(() -> () -> new net.ilexiconn.jurassicraft.client.ClientProxy(),
                () -> () -> new CommonProxy());

        var modBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onCommonSetup);
        modBus.addListener(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry::registerBlocks);
        modBus.addListener(net.ilexiconn.jurassicraft.common.item.JCItemRegistry::registerItems);
        modBus.addListener(net.ilexiconn.jurassicraft.common.handler.JCSoundRegistry::registerSounds);
        modBus.register(proxy);
        modBus.register(new net.ilexiconn.jurassicraft.common.handler.CreativeTabHandler());
        modBus.register(net.ilexiconn.jurassicraft.common.handler.AttributeHandler.class);

        entityParser = new JsonEntityHandler();
        entityParser.parseServerEntities();

        new JCCreativeTabRegistry().init();
        modBus.addListener(JCCreativeTabRegistry::registerTabs);
        JCEntityRegistry.register(modBus);
        modBus.addListener(JCEntityRegistry::registerEntities);
        new JCEntityRegistry().init();
        net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry.register(modBus);
        // 1.20.1: Block/Item instances must be created inside the RegisterEvent
        // (the registry is frozen during the mod constructor); JCBlockRegistry and
        // JCItemRegistry therefore initialise lazily from their RegisterEvent handlers.
        new JCRecipeRegistry().init();
        new JCTileEntityRegistry().init();
        JCSoundRegistry.init();
        GuiHandler.init();
        GuiHandler.register(modBus);

        try {
            proxy.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        network = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MODID, "main"),
                () -> "1", "1"::equals, "1"::equals);
        network.registerMessage(0, MessageAnimation.class, MessageAnimation::encode, MessageAnimation::decode, MessageAnimation::handle);
        network.registerMessage(1, MessageFence.class, MessageFence::encode, MessageFence::decode, MessageFence::handle);
        network.registerMessage(2, net.ilexiconn.jurassicraft.common.message.MessagePregnancy.class, net.ilexiconn.jurassicraft.common.message.MessagePregnancy::encode, net.ilexiconn.jurassicraft.common.message.MessagePregnancy::decode, net.ilexiconn.jurassicraft.common.message.MessagePregnancy::handle);

        MinecraftForge.EVENT_BUS.register(new JurassiCraftLivingEvent());
        MinecraftForge.EVENT_BUS.register(new JurassiCraftInteractEvent());
        MinecraftForge.EVENT_BUS.register(JurassiCraft.class);

        try {
            capeContainer = new Gson().fromJson(WebUtils.readPastebin("qhA18Kcq"), CapeContainer.class);
        } catch (Exception e) {
            capeContainer = new CapeContainer();
        }
    }

    public static void sendToAll(Object message) {
        network.send(PacketDistributor.ALL.noArg(), message);
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        // The shared creature EntityType used by the legacy (Level) constructors is
        // created and registered during the RegisterEvent (see
        // JCEntityRegistry.registerEntities); the registry is frozen by the time
        // COMMON_SETUP runs, so it cannot be built here.
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandSpawnDino.register(event.getDispatcher());
    }
}
