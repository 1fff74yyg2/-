package net.ilexiconn.jurassicraft.common.handler;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.container.ContainerCultivate;
import net.ilexiconn.jurassicraft.common.container.ContainerDNACombinator;
import net.ilexiconn.jurassicraft.common.container.ContainerDNAExtractor;
import net.ilexiconn.jurassicraft.common.container.ContainerSecurityFenceLow;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNAExtractor;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowCorner;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 1.12.2 IGuiHandler replacement: a static DeferredRegister of MenuTypes.
 *
 * The old server/client IGuiHandler interface is gone in 1.20.1. Menus are now
 * created on the server via MenuProvider + NetworkHooks.openScreen(player, provider, pos)
 * (which writes the BlockPos into the packet buffer), and on the client the
 * IContainerFactory below re-reads that BlockPos to locate the BlockEntity.
 *
 * TODO (client side, out of scope here): register the screens with
 * MenuScreens.register(GuiHandler.CULTIVATE.get(), GuiCultivate::new); etc. in
 * the client proxy. TODO (block side, out of scope here): replace the old
 * player.openGui(...) calls with NetworkHooks.openScreen((ServerPlayer) player,
 * new SimpleMenuProvider((id, inv, p) -> new ContainerX(id, inv, tile), title),
 * tile.getBlockPos()).
 */
public class GuiHandler {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JurassiCraft.MODID);

    public static RegistryObject<MenuType<ContainerCultivate>> CULTIVATE;
    public static RegistryObject<MenuType<ContainerDNAExtractor>> DNA_EXTRACTOR;
    public static RegistryObject<MenuType<ContainerDNACombinator>> DNA_COMBINATOR;
    public static RegistryObject<MenuType<ContainerSecurityFenceLow>> SECURITY_FENCE_LOW;

    public static void init() {
        CULTIVATE = MENUS.register("cultivate", () -> IForgeMenuType.create((id, inv, extraData) -> {
            BlockEntity tileEntity = inv.player.level().getBlockEntity(extraData.readBlockPos());
            if (tileEntity instanceof TileCultivate) {
                return new ContainerCultivate(id, inv, (TileCultivate) tileEntity);
            }
            throw new IllegalStateException("No TileCultivate found at the given position");
        }));

        DNA_EXTRACTOR = MENUS.register("dna_extractor", () -> IForgeMenuType.create((id, inv, extraData) -> {
            BlockEntity tileEntity = inv.player.level().getBlockEntity(extraData.readBlockPos());
            if (tileEntity instanceof TileDNAExtractor) {
                return new ContainerDNAExtractor(id, inv, (TileDNAExtractor) tileEntity);
            }
            throw new IllegalStateException("No TileDNAExtractor found at the given position");
        }));

        DNA_COMBINATOR = MENUS.register("dna_combinator", () -> IForgeMenuType.create((id, inv, extraData) -> {
            BlockEntity tileEntity = inv.player.level().getBlockEntity(extraData.readBlockPos());
            if (tileEntity instanceof TileDNACombinator) {
                return new ContainerDNACombinator(id, inv, (TileDNACombinator) tileEntity);
            }
            throw new IllegalStateException("No TileDNACombinator found at the given position");
        }));

        SECURITY_FENCE_LOW = MENUS.register("security_fence_low", () -> IForgeMenuType.create((id, inv, extraData) -> {
            BlockEntity tileEntity = inv.player.level().getBlockEntity(extraData.readBlockPos());
            if (tileEntity instanceof TileSecurityFenceLowCorner) {
                return new ContainerSecurityFenceLow(id, inv, (TileSecurityFenceLowCorner) tileEntity);
            }
            throw new IllegalStateException("No TileSecurityFenceLowCorner found at the given position");
        }));
    }

    public static void register(IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }
}
