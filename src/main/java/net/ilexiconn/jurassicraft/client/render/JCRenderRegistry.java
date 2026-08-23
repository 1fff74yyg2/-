package net.ilexiconn.jurassicraft.client.render;

import net.ilexiconn.jurassicraft.client.render.entity.RenderDinoEgg;
import net.ilexiconn.jurassicraft.client.render.entity.RenderSpit;
import net.ilexiconn.jurassicraft.client.render.tile.TileCultivateRenderer;
import net.ilexiconn.jurassicraft.client.render.tile.TileDNACombinatorRenderer;
import net.ilexiconn.jurassicraft.client.render.tile.TileDNAExtractorRenderer;
import net.ilexiconn.jurassicraft.client.render.tile.TileDinoPadRenderer;
import net.ilexiconn.jurassicraft.client.render.tile.fence.TileSecurityFenceLowBaseRenderer;
import net.ilexiconn.jurassicraft.client.render.tile.fence.TileSecurityFenceLowGridRenderer;
import net.ilexiconn.jurassicraft.client.render.tile.fence.TileSecurityFenceLowPoleRenderer;
import net.ilexiconn.jurassicraft.common.entity.JCEntityRegistry;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public class JCRenderRegistry {
    /**
     * Called from ClientProxy.onRegisterRenderers(EntityRenderersEvent.RegisterRenderers).
     * <p>
     * Block entity renderers are registered against the BlockEntityTypes registered
     * by JCTileEntityRegistry (local build(null) instances would not match the
     * registered types). Per-creature entity renderers are not wired up yet
     * (see CreatureHandler.addCreatureRenderer TODO).
     */
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(JCTileEntityRegistry.DNA_COMBINATOR.get(), ctx -> new TileDNACombinatorRenderer());
        event.registerBlockEntityRenderer(JCTileEntityRegistry.CULTIVATOR.get(), ctx -> new TileCultivateRenderer());
        event.registerBlockEntityRenderer(JCTileEntityRegistry.DNA_EXTRACTOR.get(), ctx -> new TileDNAExtractorRenderer());
        event.registerBlockEntityRenderer(JCTileEntityRegistry.DINO_PAD.get(), ctx -> new TileDinoPadRenderer());
        event.registerBlockEntityRenderer(JCTileEntityRegistry.FENCE_LOW_BASE.get(), ctx -> new TileSecurityFenceLowBaseRenderer());
        event.registerBlockEntityRenderer(JCTileEntityRegistry.FENCE_LOW_GRID.get(), ctx -> new TileSecurityFenceLowGridRenderer());
        event.registerBlockEntityRenderer(JCTileEntityRegistry.FENCE_LOW_POLE.get(), ctx -> new TileSecurityFenceLowPoleRenderer());

        event.registerEntityRenderer(JCEntityRegistry.DINO_EGG.get(), RenderDinoEgg::new);
        event.registerEntityRenderer(JCEntityRegistry.DILO_SPIT.get(), RenderSpit::new);

        // Legacy entities saved under the shared EntityType cannot be rendered by
        // any concrete creature model (those cast the entity to their own class),
        // so register an invisible renderer that draws nothing - this prevents the
        // "entityrenderer is null" crash and the ClassCastException in model.render.
        if (JCEntityRegistry.getSharedCreatureType() != null) {
            @SuppressWarnings("unchecked")
            net.minecraft.world.entity.EntityType<? extends net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature> sharedType =
                    (net.minecraft.world.entity.EntityType<? extends net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature>) JCEntityRegistry.getSharedCreatureType();
            event.registerEntityRenderer(sharedType, ctx ->
                    new net.minecraft.client.renderer.entity.LivingEntityRenderer<net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature, net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase>(
                            ctx, new net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase(), 0.5F) {
                        @Override
                        public net.minecraft.resources.ResourceLocation getTextureLocation(net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature entity) {
                            return new net.minecraft.resources.ResourceLocation("jurassicraft:textures/misc/missing.png");
                        }
                    });
        }

        // Per-creature entity renderers, registered against the per-creature
        // EntityTypes (spawned creatures now use their own EntityType, so the
        // client can resolve the renderer and the correct dimensions).
        for (net.ilexiconn.jurassicraft.common.entity.Creature creature : net.ilexiconn.jurassicraft.common.handler.CreatureHandler.getCreatures()) {
            net.minecraft.world.entity.EntityType<?> type = creature.getEntityType();
            if (type == null) {
                continue;
            }
            net.ilexiconn.jurassicraft.common.data.CreatureContainer container = creature.getCreatureContainer();
            String name = creature.getCreatureName();
            String category = creature.getCreatureCategory();
            float shadow = container != null ? container.shadowSize : 0.5F;
            @SuppressWarnings("unchecked")
            net.minecraft.world.entity.EntityType<? extends net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature> creatureType =
                    (net.minecraft.world.entity.EntityType<? extends net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature>) type;
            event.registerEntityRenderer(creatureType, ctx -> {
                try {
                    return new net.ilexiconn.jurassicraft.client.render.entity.RenderJurassicraftCreature(ctx, container, name, category, shadow);
                } catch (Exception e) {
                    net.ilexiconn.jurassicraft.JurassiCraft.logger.error("Failed to create renderer for " + name, e);
                    return null;
                }
            });
        }
    }
}
