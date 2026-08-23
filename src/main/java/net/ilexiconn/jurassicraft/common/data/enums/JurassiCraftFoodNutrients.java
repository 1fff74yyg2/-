package net.ilexiconn.jurassicraft.common.data.enums;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public enum JurassiCraftFoodNutrients {
    APPLE(Items.APPLE, 0.060D, 0.065D, 0.100D, 0.010D), POTATO(Items.POTATO, 0.100D, 0.200D, 0.160D, 0.020D), BREAD(Items.BREAD, 0.300D, 0.400D, 0.430D, 0.180D), CHICKEN(Items.CHICKEN, 0.390D, 0.350D, 0.280D, 0.450D), CHICKENCOOKED(Items.COOKED_CHICKEN, 0.490D, 0.425D, 0.335D, 0.555D), PORKCHOP(Items.PORKCHOP, 0.460D, 0.310D, 0.390D, 0.380D), PORKCHOPCOOKED(Items.COOKED_PORKCHOP, 0.580D, 0.390D, 0.490D, 0.470D), BEEF(Items.BEEF, 0.460D, 0.310D, 0.390D, 0.380D), BEEFCOOKED(Items.COOKED_BEEF, 0.520D, 0.330D, 0.410D, 0.400D), FISH(Items.COD, 0.480D, 0.430D, 0.140D, 0.240D), FISHCOOKED(Items.COOKED_COD, 0.500D, 0.450D, 0.200D, 0.280D), MILK(Items.MILK_BUCKET, 0.180D, 0.260D, 0.220D, 0.600D), EGG(Items.EGG, 0.050D, 0.030D, 0.050D, 0.250D), CARROT(Items.CARROT, 0.070D, 0.170D, 0.350D, 0.010D), SUGAR(Items.SUGAR, 0.200D, 0.010D, 0.010D, 0.010D), WATERMELON(Items.MELON, 0.060D, 0.060D, 0.060D, 0.010D), WHEAT(Items.WHEAT, 0.100D, 0.220D, 0.100D, 0.030D), MUTTON(Items.MUTTON, 0.440D, 0.300D, 0.360D, 0.420D), MUTTONCOOKED(Items.COOKED_MUTTON, 0.560D, 0.380D, 0.470D, 0.520D), RABBIT(Items.RABBIT, 0.360D, 0.280D, 0.240D, 0.300D), RABBITCOOKED(Items.COOKED_RABBIT, 0.460D, 0.350D, 0.310D, 0.380D), BAKEDPOTATO(Items.BAKED_POTATO, 0.150D, 0.260D, 0.210D, 0.030D), COOKIECLASSIC(Items.COOKIE, 0.040D, 0.030D, 0.020D, 0.010D), PUMPKINPIE(Items.PUMPKIN_PIE, 0.160D, 0.120D, 0.180D, 0.100D), MUSHROOMSTEW(Items.MUSHROOM_STEW, 0.090D, 0.110D, 0.070D, 0.030D), RABBITSTEW(Items.RABBIT_STEW, 0.180D, 0.140D, 0.160D, 0.120D),
    // 1.20.1 additions (foods added after 1.12.2, plus 1.13-renamed items).
    BEETROOT(Items.BEETROOT, 0.080D, 0.150D, 0.150D, 0.010D), BEETROOT_SEEDS(Items.BEETROOT_SEEDS, 0.030D, 0.100D, 0.050D, 0.010D), DRIED_KELP(Items.DRIED_KELP, 0.080D, 0.120D, 0.060D, 0.010D), SWEET_BERRIES(Items.SWEET_BERRIES, 0.050D, 0.060D, 0.150D, 0.010D), GLOW_BERRIES(Items.GLOW_BERRIES, 0.050D, 0.060D, 0.150D, 0.010D), HONEY_BOTTLE(Items.HONEY_BOTTLE, 0.050D, 0.100D, 0.300D, 0.020D), MELON_SLICE(Items.MELON_SLICE, 0.060D, 0.060D, 0.060D, 0.010D), WHEAT_SEEDS(Items.WHEAT_SEEDS, 0.030D, 0.100D, 0.050D, 0.010D), MELON_SEEDS(Items.MELON_SEEDS, 0.030D, 0.100D, 0.050D, 0.010D), PUMPKIN_SEEDS(Items.PUMPKIN_SEEDS, 0.030D, 0.100D, 0.050D, 0.010D), SALMON(Items.SALMON, 0.480D, 0.430D, 0.140D, 0.240D), SALMONCOOKED(Items.COOKED_SALMON, 0.500D, 0.450D, 0.200D, 0.280D), TROPICAL_FISH(Items.TROPICAL_FISH, 0.480D, 0.430D, 0.140D, 0.240D), PUFFERFISH(Items.PUFFERFISH, 0.480D, 0.430D, 0.140D, 0.240D);

    /**
     * Sets a list of all food. Used to check if some food is valid [.FOODLIST.containsKey(item)].
     */
    public static final Map<Item, Integer> FOODLIST = new HashMap<Item, Integer>();

    static {
        for (int i = 0; i < values().length; i++) {
            FOODLIST.put(JurassiCraftFoodNutrients.values()[i].getFoodItem(), i);
        }
    }

    private final double proximate;
    private final double minerals;
    private final double vitamins;
    private final double lipids;
    private final Item food;

    JurassiCraftFoodNutrients(Item foodID, double foodProximates, double foodMinerals, double foodVitamins, double foodLipids) {
        this.food = foodID;
        this.proximate = foodProximates;
        this.minerals = foodMinerals;
        this.vitamins = foodVitamins;
        this.lipids = foodLipids;
    }

    /**
     * Returns the food (Item).
     */
    public Item getFoodItem() {
        return food;
    }

    /**
     * Returns the value of proximate from certain food.
     */
    public double getProximate() {
        return proximate;
    }

    /**
     * Returns the mineral value from certain food.
     */
    public double getMinerals() {
        return minerals;
    }

    /**
     * Returns the vitamin value from certain food.
     */
    public double getVitamins() {
        return vitamins;
    }

    /**
     * Returns the lipid value from certain food.
     */
    public double getLipids() {
        return lipids;
    }

    /**
     * 1.20.1: whether the item is accepted by the cultivate machine. Any food in
     * the table is valid; as a fallback any item with FoodProperties (i.e. any
     * edible item, including foods added after 1.12.2) is accepted too.
     */
    public static boolean isValidFood(Item item) {
        if (item == null)
            return false;
        if (FOODLIST.containsKey(item))
            return true;
        return item.isEdible();
    }

    /**
     * 1.20.1: nutrient value (0=proximate, 1=minerals, 2=vitamins, 3=lipids) for
     * the given item. Uses the table when present, otherwise estimates from the
     * item's FoodProperties so post-1.12.2 foods still work in the cultivate.
     */
    public static double getNutrient(Item item, int nutrientType) {
        if (item != null) {
            Integer id = FOODLIST.get(item);
            if (id != null) {
                JurassiCraftFoodNutrients n = values()[id];
                switch (nutrientType) {
                    case 0: return n.getProximate();
                    case 1: return n.getMinerals();
                    case 2: return n.getVitamins();
                    default: return n.getLipids();
                }
            }
        }
        FoodProperties props = item != null ? item.getFoodProperties() : null;
        double base = props != null ? props.getNutrition() : 2.0D;
        switch (nutrientType) {
            case 0: return base * 0.05D;
            case 1: return base * 0.05D;
            case 2: return base * 0.05D;
            default: return base * 0.04D;
        }
    }
}
