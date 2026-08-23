package net.ilexiconn.jurassicraft.common.block;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.api.ISubBlocksBlock;
import net.ilexiconn.jurassicraft.common.block.cultivate.BlockCultivateBottom;
import net.ilexiconn.jurassicraft.common.block.cultivate.BlockCultivateFluid;
import net.ilexiconn.jurassicraft.common.block.cultivate.BlockCultivateTop;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowBase;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowCorner;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowGrid;
import net.ilexiconn.jurassicraft.common.block.fence.BlockSecurityFenceLowPole;
import net.ilexiconn.jurassicraft.common.block.fossil.BlockFossilClayOre;
import net.ilexiconn.jurassicraft.common.block.fossil.BlockFossilOre;
import net.ilexiconn.jurassicraft.common.block.fossil.BlockFossilSandstoneOre;
import net.ilexiconn.jurassicraft.common.block.gypsum.BlockGypsumBlock;
import net.ilexiconn.jurassicraft.common.block.gypsum.BlockGypsumBrick;
import net.ilexiconn.jurassicraft.common.block.gypsum.BlockGypsumCobblestone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class JCBlockRegistry {
    public static Block clayFossilOre;
    public static Block cultivateLiquid;
    public static Block cultivateBottomOff;
    public static Block cultivateBottomOn;
    public static Block cultivateTopOff;
    public static Block cultivateTopOn;
    public static Block dnaExtractor;
    public static Block dnaCombinator;
    public static Block gypsumBlock;
    public static Block gypsumBrick;
    public static Block gypsumCobblestone;
    public static Block amberOre;
    public static Block fossilOre;
    public static Block sandstoneFossilOre;
    public static Block dinoPad;
    public static Block securityFenceLowCorner;
    public static Block securityFenceLowPole;
    public static Block securityFenceLowBase;
    public static Block securityFenceLowGrid;
    public static Block clearGlass;

    /**
     * Registry name -> block. 1.20.1 has no metadata / setUnlocalizedName and the
     * descriptionId is derived FROM the registry name, so the names are given
     * explicitly (they keep the old 1.12.2 unlocalized names so the lang keys and
     * the blockstate/model JSON paths stay unchanged).
     */
    private static final Map<String, Block> BLOCKS_TO_REGISTER = new LinkedHashMap<String, Block>();

    private static boolean initialized = false;

    /**
     * 1.20.1: the block registry is FROZEN during the mod constructor (CONSTRUCT
     * stage) and is only unfrozen for the RegisterEvent, so Block instances must be
     * created lazily from the RegisterEvent handler instead of the mod constructor.
     * Called at the top of registerBlocks and from tile-entity suppliers so the
     * static fields are always populated before any registration consumes them.
     */
    public static void ensureInitialized() {
        if (!initialized) {
            new JCBlockRegistry().init();
            initialized = true;
        }
    }

    public void init() {
        cultivateBottomOff = new BlockCultivateBottom(false);
        cultivateTopOff = new BlockCultivateTop(false);
        cultivateBottomOn = new BlockCultivateBottom(true);
        cultivateTopOn = new BlockCultivateTop(true);
        dnaExtractor = new BlockDNAExtractor();
        dnaCombinator = new BlockDNACombinator();
        gypsumBlock = new BlockGypsumBlock();
        gypsumBrick = new BlockGypsumBrick();
        gypsumCobblestone = new BlockGypsumCobblestone();
        amberOre = new BlockAmberOre();
        fossilOre = new BlockFossilOre();
        sandstoneFossilOre = new BlockFossilSandstoneOre();
        clayFossilOre = new BlockFossilClayOre();
        dinoPad = new BlockDinoPad();
        securityFenceLowCorner = new BlockSecurityFenceLowCorner();
        securityFenceLowPole = new BlockSecurityFenceLowPole();
        securityFenceLowBase = new BlockSecurityFenceLowBase();
        securityFenceLowGrid = new BlockSecurityFenceLowGrid();
        cultivateLiquid = new BlockCultivateFluid();
        clearGlass = new BlockClearGlass();

        BLOCKS_TO_REGISTER.clear();
        put(cultivateBottomOff, "cultivate_bottom_idle");
        put(cultivateBottomOn, "cultivate_bottom_lit");
        put(cultivateTopOff, "cultivate_top_idle");
        put(cultivateTopOn, "cultivate_top_lit");
        // 1.20.1: registry names must be all-lowercase (ResourceLocation validation);
        // the asset files (blockstates/models/lang) already use these lowercase names.
        put(dnaExtractor, "dnaextractor");
        put(dnaCombinator, "dnacombinator");
        put(gypsumBlock, "block_gypsum_block");
        put(gypsumBrick, "block_gypsum_brick");
        put(gypsumCobblestone, "block_gypsum_cobblestone");
        put(amberOre, "amber_ore");
        put(fossilOre, "fossil_ore");
        put(sandstoneFossilOre, "fossil_sandstone_ore");
        put(clayFossilOre, "fossil_clay_ore");
        put(dinoPad, "dinopad");
        put(securityFenceLowCorner, "low_security_fence_main");
        put(securityFenceLowPole, "low_security_fence_pole");
        put(securityFenceLowBase, "low_security_fence_base");
        put(securityFenceLowGrid, "low_security_fence_grid");
        put(cultivateLiquid, "culivatefluid");
        put(clearGlass, "clearglass");
    }

    private static void put(Block block, String name) {
        if (block != null) {
            BLOCKS_TO_REGISTER.put(name, block);
        }
    }

    public static void registerBlocks(RegisterEvent event) {
        ensureInitialized();
        if (event.getRegistryKey().equals(net.minecraft.core.registries.Registries.BLOCK)) {
            for (Map.Entry<String, Block> entry : BLOCKS_TO_REGISTER.entrySet()) {
                event.register(net.minecraft.core.registries.Registries.BLOCK,
                        new ResourceLocation(JurassiCraft.MODID, entry.getKey()), () -> entry.getValue());
            }
        } else if (event.getRegistryKey().equals(net.minecraft.core.registries.Registries.ITEM)) {
            for (Map.Entry<String, Block> entry : BLOCKS_TO_REGISTER.entrySet()) {
                final Block block = entry.getValue();

                // BlockDinoPad has its own standalone ItemDinoPad registered in JCItemRegistry,
                // so no automatic BlockItem is registered for it.
                if (block == dinoPad) {
                    continue;
                }

                final String name = entry.getKey();
                event.register(net.minecraft.core.registries.Registries.ITEM,
                        new ResourceLocation(JurassiCraft.MODID, name), () -> {
                            BlockItem itemBlock;
                            if (block instanceof ISubBlocksBlock) {
                                try {
                                    itemBlock = ((ISubBlocksBlock) block).getItemBlockClass().getConstructor(Block.class).newInstance(block);
                                } catch (Exception e) {
                                    itemBlock = new BlockItem(block, new Item.Properties());
                                }
                            } else {
                                itemBlock = new BlockItem(block, new Item.Properties());
                            }
                            return itemBlock;
                        });
            }
        }
    }
}
