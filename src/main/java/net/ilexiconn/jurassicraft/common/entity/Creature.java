package net.ilexiconn.jurassicraft.common.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.ilexiconn.jurassicraft.common.data.CreatureContainer;
import net.ilexiconn.jurassicraft.common.item.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Creature {
    private Class<LivingEntity> clazz;

    private CreatureContainer container;

    private String creatureCategory;

    private byte creatureID;
    private byte addItemTypes;

    private String creatureName;

    private double minHealth;
    private double minStrength;
    private double minSpeed;
    private double minKnockback;
    private double minProximate;
    private double minMinerals;
    private double minVitamins;
    private double minLipids;
    private double maxHealth;
    private double maxStrength;
    private double maxSpeed;
    private double maxKnockback;
    private double ridingSpeed;

    private float adultAge;
    private float minLength;
    private float minHeight;
    private float maxLength;
    private float maxHeight;
    private float xzBoxMin;
    private float yBoxMin;
    private float xzBoxDelta;
    private float yBoxDelta;
    private float scaleAdjustment;
    private float shadowSize;

    private int ticksToAdulthood;
    private int cultivateSpeed;
    private int textureCount;
    private int ridingStyle;
    private int numberOfInfoPages;

    private ArrayList<String> favoriteFoodList;
    private ArrayList<String> ridingItemList;

    private boolean isRidable;
    private boolean canBeTamedUponSpawning;
    private boolean waterCreature;
    private boolean flyingCreature;

    private ItemDNA dna;
    private ItemEgg egg;
    private ItemMammalSyringe syringe;
    private ItemMeat meat;
    private ItemSteak steak;
    private ItemFur fur;
    private ItemSkin skin;
    private ItemScale scale;
    private ItemFeather feather;
    private ItemBristles bristles;
    private ItemSkull skull;
    private ItemTooth tooth;

    private int eggPrimaryColor;
    private int eggSecondaryColor;

    public Creature(String creatureCategory, CreatureContainer def, Class<LivingEntity> clazz) {
        this.clazz = clazz;
        this.container = def;

        this.creatureCategory = creatureCategory;

        this.adultAge = def.adultAge;
        this.canBeTamedUponSpawning = def.canBeTamedUponSpawning;
        this.creatureID = def.creatureID;
        this.creatureName = def.creatureName;
        this.cultivateSpeed = def.cultivateSpeed;
        this.favoriteFoodList = def.favoriteFoodList;
        this.isRidable = def.isRidable;
        this.maxHealth = def.maxHealth;
        this.maxHeight = def.maxHeight;
        this.maxKnockback = def.maxKnockback;
        this.maxLength = def.maxLength;
        this.maxSpeed = def.maxSpeed;
        this.maxStrength = def.maxStrength;
        this.minHealth = def.minHealth;
        this.minHeight = def.minHeight;
        this.minKnockback = def.minKnockback;
        this.minLength = def.minLength;
        this.minLipids = def.minLipids;
        this.minMinerals = def.minMinerals;
        this.minProximate = def.minProximate;
        this.minSpeed = def.minSpeed;
        this.minStrength = def.minStrength;
        this.minVitamins = def.minVitamins;
        this.numberOfInfoPages = def.numberOfInfoPages;
        this.textureCount = def.numberOfTextures;
        this.ridingItemList = def.ridingItemList;
        this.ridingSpeed = def.ridingSpeed;
        this.ridingStyle = def.ridingStyle;
        this.scaleAdjustment = def.scaleAdjustment;
        this.shadowSize = def.shadowSize;
        this.ticksToAdulthood = def.ticksToAdulthood;
        this.waterCreature = def.waterCreature;
        this.flyingCreature = def.flyingCreature;
        this.xzBoxDelta = def.xzBoxDelta;
        this.xzBoxMin = def.xzBoxMin;
        this.yBoxDelta = def.yBoxDelta;
        this.yBoxMin = def.yBoxMin;
        this.addItemTypes = def.addItemTypes;
        this.eggPrimaryColor = def.eggPrimaryColor;
        this.eggSecondaryColor = def.eggSecondaryColor;
    }

    private void registerItem(Item item, String type) {
        // 1.20.1: registry names are derived from the creature name + item type
        // (e.g. "tyrannosaurus_dna") so they match the item model files under
        // assets/jurassicraft/models/item/ (tyrannosaurus_dna.json, ...).
        net.ilexiconn.jurassicraft.common.item.JCItemRegistry.registerDynamicItem(
                this.creatureName.toLowerCase(Locale.ROOT) + "_" + type, item);
    }

    public void addDNA() {
        this.dna = new ItemDNA(this.creatureName);
        registerItem(this.dna, "dna");
    }

    public Class<LivingEntity> getCreatureClass() {
        return clazz;
    }

    public void addEgg() {
        this.egg = new ItemEgg(this.creatureName);
        registerItem(this.egg, "egg");
    }

    public void addSyringe() {
        this.syringe = new ItemMammalSyringe(this.creatureName);
        registerItem(this.syringe, "syringe");
    }

    public void addMeat() {
        this.meat = new ItemMeat(this.creatureName);
        this.steak = new ItemSteak(this.creatureName);
        registerItem(this.meat, "meat");
        registerItem(this.steak, "steak");
        // smelting recipe (meat -> steak) moved to datapack
    }

    public void addFur() {
        this.fur = new ItemFur(this.creatureName);
        registerItem(this.fur, "fur");
        // Leptictidium fur armor recipes moved to datapack
        if (false && this.creatureName.equals("Leptictidium")) {
        }
    }

    public void addSkin() {
        this.skin = new ItemSkin(this.creatureName);
        registerItem(this.skin, "skin");
    }

    public void addScale() {
        this.scale = new ItemScale(this.creatureName);
        registerItem(this.scale, "scale");
    }

    public void addFeather() {
        this.feather = new ItemFeather(this.creatureName);
        registerItem(this.feather, "feather");
    }

    public void addBristles() {
        this.bristles = new ItemBristles(this.creatureName);
        registerItem(this.bristles, "bristles");
    }

    public void addSkull() {
        this.skull = new ItemSkull(this.creatureName);
        registerItem(this.skull, "skull");
    }

    public void addTooth() {
        this.tooth = new ItemTooth(this.creatureName);
        registerItem(this.tooth, "tooth");
    }

    public String getCreatureName() {
        return this.creatureName;
    }

    public CreatureContainer getCreatureContainer() {
        return this.container;
    }

    /**
     * The per-creature EntityType registered by JCEntityRegistry (registry name is
     * the lowercase creature name, e.g. "tyrannosaurus").
     */
    public net.minecraft.world.entity.EntityType<?> getEntityType() {
        // 1.20.1: mod entities live in the Forge registry; BuiltInRegistries may not
        // see them, which previously caused spawning to fall back to the shared type.
        net.minecraft.resources.ResourceLocation id =
                new net.minecraft.resources.ResourceLocation(net.ilexiconn.jurassicraft.JurassiCraft.MODID,
                        this.creatureName.toLowerCase(java.util.Locale.ROOT));
        net.minecraft.world.entity.EntityType<?> type = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getValue(id);
        if (type == null) {
            type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(id);
        }
        return type;
    }

    public String getCreatureCategory() {
        return this.creatureCategory;
    }

    public byte getCreatureID() {
        return this.creatureID;
    }

    public boolean isWaterCreature() {
        return this.waterCreature;
    }

    public boolean isFlyingCreature() {
        return this.flyingCreature;
    }

    public ItemDNA getDNA() {
        return this.dna;
    }

    public ItemMeat getMeat() {
        return this.meat;
    }

    public ItemSteak getSteak() {
        return this.steak;
    }

    public ItemFur getFur() {
        return this.fur;
    }

    public ItemSkin getSkin() {
        return this.skin;
    }

    public ItemScale getScale() {
        return this.scale;
    }

    public ItemFeather getFeather() {
        return this.feather;
    }

    public ItemBristles getBristles() {
        return this.bristles;
    }

    public ItemSkull getSkull() {
        return this.skull;
    }

    public ItemTooth getTooth() {
        return this.tooth;
    }

    public boolean isRidingItem(Item item) {
        if (this.ridingItemList != null && item != null) {
            String oldName = toOldUnlocalizedName(item.getDescriptionId());
            if (oldName != null && this.ridingItemList.contains(oldName)) {
                return true;
            }
            // The 1.7.10->1.12.2 remap for riding items.
            String mapped = OLD_UNLOCALIZED_TO_NEW.get(oldName);
            return mapped != null && this.ridingItemList.contains(mapped);
        }

        return false;
    }

    public boolean isRidable() {
        return this.isRidable;
    }

    public int getRidingStyle() {
        return this.ridingStyle;
    }

    public double getRidingSpeed() {
        return this.ridingSpeed;
    }

    /**
     * Some 1.12.2 environments keep the 1.7.10-style unlocalized names
     * (e.g. "item.beefRaw" instead of "item.beef"). The JSON food lists use the
     * modern names, so map the old ones onto the modern ones before matching.
     */
    private static final Map<String, String> OLD_UNLOCALIZED_TO_NEW = new HashMap<String, String>();

    static {
        OLD_UNLOCALIZED_TO_NEW.put("item.beefRaw", "item.beef");
        OLD_UNLOCALIZED_TO_NEW.put("item.beefCooked", "item.cooked_beef");
        OLD_UNLOCALIZED_TO_NEW.put("item.porkchopRaw", "item.porkchop");
        OLD_UNLOCALIZED_TO_NEW.put("item.porkchopCooked", "item.cooked_porkchop");
        OLD_UNLOCALIZED_TO_NEW.put("item.chickenRaw", "item.chicken");
        OLD_UNLOCALIZED_TO_NEW.put("item.chickenCooked", "item.cooked_chicken");
        OLD_UNLOCALIZED_TO_NEW.put("item.muttonRaw", "item.mutton");
        OLD_UNLOCALIZED_TO_NEW.put("item.muttonCooked", "item.cooked_mutton");
        OLD_UNLOCALIZED_TO_NEW.put("item.rabbitRaw", "item.rabbit");
        OLD_UNLOCALIZED_TO_NEW.put("item.rabbitCooked", "item.cooked_rabbit");
        OLD_UNLOCALIZED_TO_NEW.put("item.fish.cod.raw", "item.fish");
        OLD_UNLOCALIZED_TO_NEW.put("item.fish.cod.cooked", "item.cooked_fish");
        OLD_UNLOCALIZED_TO_NEW.put("item.fish.salmon.raw", "item.fish");
        OLD_UNLOCALIZED_TO_NEW.put("item.fish.salmon.cooked", "item.cooked_fish");
        OLD_UNLOCALIZED_TO_NEW.put("item.carrots", "item.carrot");
        OLD_UNLOCALIZED_TO_NEW.put("item.potatoBaked", "item.baked_potato");
        OLD_UNLOCALIZED_TO_NEW.put("item.leaves", "tile.leaves");
        OLD_UNLOCALIZED_TO_NEW.put("item.dyePowder.brown", "item.dye");
    }

    /**
     * 1.12.2 JSON food lists store the old-style unlocalized names
     * (e.g. "item.apple", "tile.leaves"). 1.20.1 getDescriptionId() returns
     * namespaced keys ("item.minecraft.apple", "block.minecraft.leaves"),
     * so convert them back before matching.
     */
    private static String toOldUnlocalizedName(String descriptionId) {
        if (descriptionId == null) {
            return null;
        }
        String s = descriptionId;
        // "item.minecraft.apple" -> "item.apple", "block.minecraft.leaves" -> "tile.leaves"
        if (s.startsWith("item.minecraft.")) {
            return "item." + s.substring("item.minecraft.".length());
        }
        if (s.startsWith("block.minecraft.")) {
            return "tile." + s.substring("block.minecraft.".length());
        }
        // Fall back to stripping a leading "minecraft:" if present in other forms.
        if (s.startsWith("minecraft:")) {
            return s.substring("minecraft:".length());
        }
        return s;
    }

    public boolean isFavoriteFood(Item item) {
        if (this.favoriteFoodList != null && item != null) {
            String descriptionId = item.getDescriptionId();
            String oldName = toOldUnlocalizedName(descriptionId);
            if (oldName != null && this.favoriteFoodList.contains(oldName)) {
                return true;
            }
            // The 1.7.10->1.12.2 remap: some food lists still use the older names.
            String mapped = OLD_UNLOCALIZED_TO_NEW.get(oldName);
            if (mapped != null && this.favoriteFoodList.contains(mapped)) {
                return true;
            }
        }

        return false;
    }

    public boolean canBeTamedUponSpawning() {
        return this.canBeTamedUponSpawning;
    }

    public int getInfoPageCount() {
        return this.numberOfInfoPages;
    }

    public float getMaxLength() {
        return this.maxLength;
    }

    public float getMaxHeight() {
        return this.maxHeight;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public double getMinHealth() {
        return this.minHealth;
    }

    public double getTicksToAdulthood() {
        return this.ticksToAdulthood;
    }

    public double getMaxStrength() {
        return this.maxStrength;
    }

    public double getMinStrength() {
        return this.minStrength;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public double getMinSpeed() {
        return this.minSpeed;
    }

    public double getMaxKnockback() {
        return this.maxKnockback;
    }

    public double getMinKnockback() {
        return this.minKnockback;
    }

    public double getYBoxMin() {
        return this.yBoxMin;
    }

    public double getYBoxDelta() {
        return this.yBoxDelta;
    }

    public double getXzBoxMin() {
        return this.xzBoxMin;
    }

    public double getXzBoxDelta() {
        return this.xzBoxDelta;
    }

    public float getScaleAdjustment() {
        return this.scaleAdjustment;
    }

    public float getShadowSize() {
        return this.shadowSize;
    }

    public float getMinHeight() {
        return this.minHeight;
    }

    public float getMinLength() {
        return this.minLength;
    }

    public float getAdultAge() {
        return this.adultAge;
    }

    public int getTextureCount() {
        return this.textureCount;
    }

    public ItemEgg getEgg() {
        return this.egg;
    }

    public ItemMammalSyringe getMammalSyringe() {
        return this.syringe;
    }

    public double getMinProximate() {
        return this.minProximate;
    }

    public double getMinVitamins() {
        return this.minVitamins;
    }

    public double getMinLipids() {
        return this.minLipids;
    }

    public double getMinMinerals() {
        return this.minMinerals;
    }

    public int getCultivateSpeed() {
        return this.cultivateSpeed;
    }

    public byte getAddedItemTypes() {
        return this.addItemTypes;
    }

    public int getEggPrimaryColor() {
        return eggPrimaryColor;
    }

    public int getEggSecondaryColor() {
        return eggSecondaryColor;
    }
}
