package net.ilexiconn.jurassicraft.common.world;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class WorldGenFossilOre {
    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("jurassicraft", "fossil_ore"));
    public static final ResourceKey<PlacedFeature> PLACED = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("jurassicraft", "fossil_ore"));

    public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        OreConfiguration.TargetBlockState target = OreConfiguration.target(
                new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), JCBlockRegistry.fossilOre.defaultBlockState());
        context.register(CONFIGURED, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(target), 6)));
    }

    public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
        context.register(PLACED, new PlacedFeature(
                context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CONFIGURED), placement()));
    }

    public static List<PlacementModifier> placement() {
        return List.of(
                CountPlacement.of(20),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(60)),
                BiomeFilter.biome());
    }
}
