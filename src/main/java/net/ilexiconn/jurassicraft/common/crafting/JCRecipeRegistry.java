package net.ilexiconn.jurassicraft.common.crafting;

/**
 * Recipe registration.
 * <p>
 * 1.20.1 migration: the 1.12.2 GameRegistry recipe API no longer exists and Forge
 * does not offer a supported runtime recipe-registration hook. All static recipes
 * have been converted to data-driven JSON files under
 * src/main/resources/data/jurassicraft/recipes/ (they are loaded automatically as
 * part of the mod's datapack).
 * <p>
 * TODO 1.20.1: the original also generated a growth-serum recipe per dynamically
 * registered creature meat item. That requires runtime recipe injection (e.g. a
 * custom reload listener) and is not implemented yet; static meat recipes
 * (beef/pork/chicken/fish + cooked variants) are provided in the data pack instead.
 */
public class JCRecipeRegistry {
    public void init() {
        // Recipes are data-driven now; nothing to register at runtime.
    }
}
