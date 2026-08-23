package net.ilexiconn.jurassicraft.common.tileentity;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowBase;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowCorner;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowGrid;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowPole;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JCTileEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, JurassiCraft.MODID);

    public static RegistryObject<BlockEntityType<TileDNACombinator>> DNA_COMBINATOR;
    public static RegistryObject<BlockEntityType<TileDNAExtractor>> DNA_EXTRACTOR;
    public static RegistryObject<BlockEntityType<TileCultivate>> CULTIVATOR;
    public static RegistryObject<BlockEntityType<TileDinoPad>> DINO_PAD;
    public static RegistryObject<BlockEntityType<TileSecurityFenceLowCorner>> FENCE_LOW_CORNER;
    public static RegistryObject<BlockEntityType<TileSecurityFenceLowBase>> FENCE_LOW_BASE;
    public static RegistryObject<BlockEntityType<TileSecurityFenceLowGrid>> FENCE_LOW_GRID;
    public static RegistryObject<BlockEntityType<TileSecurityFenceLowPole>> FENCE_LOW_POLE;

    public void init() {
        DNA_COMBINATOR = BLOCK_ENTITY_TYPES.register("dna_combinator",
                () -> BlockEntityType.Builder.of(TileDNACombinator::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.dnaCombinator)).build(null));
        DNA_EXTRACTOR = BLOCK_ENTITY_TYPES.register("dna_extractor",
                () -> BlockEntityType.Builder.of(TileDNAExtractor::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.dnaExtractor)).build(null));
        CULTIVATOR = BLOCK_ENTITY_TYPES.register("cultivator",
                () -> BlockEntityType.Builder.of(TileCultivate::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.cultivateBottomOff)).build(null));
        DINO_PAD = BLOCK_ENTITY_TYPES.register("dino_pad",
                () -> BlockEntityType.Builder.of(TileDinoPad::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.dinoPad)).build(null));
        FENCE_LOW_CORNER = BLOCK_ENTITY_TYPES.register("security_fence_low_corner",
                () -> BlockEntityType.Builder.of(TileSecurityFenceLowCorner::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.securityFenceLowCorner)).build(null));
        FENCE_LOW_BASE = BLOCK_ENTITY_TYPES.register("security_fence_low_base",
                () -> BlockEntityType.Builder.of(TileSecurityFenceLowBase::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.securityFenceLowBase)).build(null));
        FENCE_LOW_GRID = BLOCK_ENTITY_TYPES.register("security_fence_low_grid",
                () -> BlockEntityType.Builder.of(TileSecurityFenceLowGrid::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.securityFenceLowGrid)).build(null));
        FENCE_LOW_POLE = BLOCK_ENTITY_TYPES.register("security_fence_low_pole",
                () -> BlockEntityType.Builder.of(TileSecurityFenceLowPole::new,
                        ensureBlock(net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.securityFenceLowPole)).build(null));
    }

    /**
     * The BlockEntityType suppliers run during the RegisterEvent; make sure the
     * blocks themselves have been created first (they are created lazily from
     * JCBlockRegistry.ensureInitialized(), also during the RegisterEvent).
     */
    private static net.minecraft.world.level.block.Block ensureBlock(net.minecraft.world.level.block.Block block) {
        net.ilexiconn.jurassicraft.common.block.JCBlockRegistry.ensureInitialized();
        return block;
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
