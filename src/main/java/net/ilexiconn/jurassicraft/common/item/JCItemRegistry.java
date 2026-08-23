package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegisterEvent;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class JCItemRegistry {
    public static Item amber;
    public static Item fossil;
    public static Item dinoPad;
    public static Item dinoBone;
    public static Item growthSerum;
    public static Item gypsumPowder;
    public static Item appleOnAStick;
    public static Item beefOnAStick;
    public static Item carrotOnAStick;
    public static Item fishOnAStick;
    public static Item porkOnAStick;
    public static Item wheatOnAStick;
    public static Item net;
    public static Item spawnEgg;

    private static final Map<String, Item> ITEMS_TO_REGISTER = new LinkedHashMap<String, Item>();

    private static boolean initialized = false;

    /**
     * 1.20.1: the registry is FROZEN during the mod constructor (CONSTRUCT stage);
     * Item instances must be created lazily from the RegisterEvent handler.
     */
    public static void ensureInitialized() {
        if (!initialized) {
            new JCItemRegistry().init();
            initialized = true;
        }
    }

    public void init() {
        amber = new ItemAmber();
        fossil = new ItemFossil();
        dinoBone = new ItemDinoBone();
        growthSerum = new ItemGrowthSerum();
        gypsumPowder = new ItemGypsumPowder();
        dinoPad = new ItemDinoPad();
        appleOnAStick = new ItemOnAStick("Apple");
        beefOnAStick = new ItemOnAStick("Beef");
        carrotOnAStick = new ItemOnAStick("Carrot");
        fishOnAStick = new ItemOnAStick("Fish");
        porkOnAStick = new ItemOnAStick("Pork");
        wheatOnAStick = new ItemOnAStick("Wheat");
        spawnEgg = new ItemSpawnEggJurassiCraft();
        net = new ItemNet();

        // Must be cleared BEFORE the creature loop: registerDynamicItem() adds the
        // per-creature items (DNA/egg/meat/...) to this map, and a clear() after the
        // loop would drop all of them (leaving unregistered intrusive holders behind).
        ITEMS_TO_REGISTER.clear();

        for (Creature creature : CreatureHandler.getCreatures()) {
            switch (creature.getAddedItemTypes()) {
                case 0:
                    /** Creature not implemented yet */
                    break;
                case 1:
                    /** DNA + Egg */
                    creature.addDNA();
                    creature.addEgg();
                    break;
                case 2:
                    /** DNA + Syringe */
                    creature.addDNA();
                    creature.addSyringe();
                    break;
                case 3:
                    /** DNA + Egg + Meat */
                    creature.addDNA();
                    creature.addEgg();
                    creature.addMeat();
                    break;
                case 4:
                    /** DNA + Syringe + Meat */
                    creature.addDNA();
                    creature.addSyringe();
                    creature.addMeat();
                    break;
                case 5:
                    /** DNA + Egg + Meat + Skull */
                    creature.addDNA();
                    creature.addEgg();
                    creature.addMeat();
                    creature.addSkull();
                    break;
                case 6:
                    /** DNA + Syringe + Meat + Skull */
                    creature.addDNA();
                    creature.addSyringe();
                    creature.addMeat();
                    creature.addSkull();
                    break;
                case 7:
                    /** DNA + Egg + Meat + Skin */
                    creature.addDNA();
                    creature.addEgg();
                    creature.addMeat();
                    creature.addSkin();
                    break;
                case 8:
                    /** DNA + Syringe + Meat + Fur */
                    creature.addDNA();
                    creature.addSyringe();
                    creature.addMeat();
                    creature.addFur();
                    break;
                case 9:
                    /** DNA + Egg + Meat + Skull + Skin */
                    creature.addDNA();
                    creature.addEgg();
                    creature.addMeat();
                    creature.addSkull();
                    creature.addSkin();
                    break;
                case 10:
                    /** DNA + Syringe + Meat + Skin */
                    creature.addDNA();
                    creature.addSyringe();
                    creature.addMeat();
                    creature.addSkin();
                    break;
            }
        }

        // 1.20.1: an unregistered item's getDescriptionId() resolves to "item.minecraft.air", so the
        // old "derive the name from the descriptionId" trick no longer works; derive the registry name
        // from the field name instead (registry names must be lowercase in 1.20.1).
        // NOTE: no clear() here - the map was cleared at the top of init() and already
        // contains the dynamic per-creature items registered by the loop above.
        try {
            for (Field field : getClass().getFields()) {
                Object obj = field.get(this);
                if (obj != null && obj instanceof Item) {
                    Item item = (Item) obj;
                    String name = field.getName().toLowerCase(Locale.ROOT);
                    ITEMS_TO_REGISTER.put(name, item);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a dynamically created item (creature DNA/egg/meat/etc.) under the given name.
     * Called from Creature.registerItem().
     * <p>
     * TODO 1.20.1: the caller derives the name from getDescriptionId(), which resolves to
     * "item.minecraft.air" before registration, so the passed name is normally unusable; we sanitize
     * it and fall back to a name derived from the item class (e.g. "ItemTyrannosaurusDNA" ->
     * "tyrannosaurusdna", deduplicated with a numeric suffix so per-creature items stay unique).
     * The lang/model keys for these dynamic items must be aligned with the final chosen registry names.
     */
    public static void registerDynamicItem(String name, Item item) {
        String registryName = name == null ? "" : name.toLowerCase(Locale.ROOT);
        int separator = Math.max(registryName.lastIndexOf(':'), registryName.lastIndexOf('.'));
        if (separator >= 0) {
            registryName = registryName.substring(separator + 1);
        }
        if (registryName.isEmpty() || "air".equals(registryName)) {
            registryName = item.getClass().getSimpleName().toLowerCase(Locale.ROOT);
            if (registryName.startsWith("item")) {
                registryName = registryName.substring(4);
            }
            String base = registryName;
            int suffix = 2;
            while (ITEMS_TO_REGISTER.containsKey(registryName)) {
                registryName = base + "_" + suffix;
                suffix++;
            }
        }
        ITEMS_TO_REGISTER.put(registryName, item);
    }

    public static void registerItems(RegisterEvent event) {
        ensureInitialized();
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            for (Map.Entry<String, Item> entry : ITEMS_TO_REGISTER.entrySet()) {
                event.register(Registries.ITEM,
                        new ResourceLocation(JurassiCraft.MODID, entry.getKey()), () -> entry.getValue());
            }
        }
    }
}
