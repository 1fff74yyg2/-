package net.ilexiconn.jurassicraft.client.render.entity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.data.CreatureContainer;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityVelociraptor;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class RenderJurassicraftCreature extends LivingEntityRenderer<EntityJurassiCraftCreature, AdvancedModelBase> {
    private Map<String, ResourceLocation> textureCache = Maps.newHashMap();

    private CreatureContainer creature;
    private float resizableShadow;
    private String category;
    private String creatureName;

    public RenderJurassicraftCreature(EntityRendererProvider.Context context, CreatureContainer creature, String creatureName, String category, float shadow) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(context, (AdvancedModelBase) Class.forName("net.ilexiconn.jurassicraft.client.model.entity.Model" + creatureName).newInstance(), shadow);
        this.setCreature(creature);
        this.setShadow(shadow);
        this.category = category.toLowerCase();
        this.creatureName = creatureName.toLowerCase();
    }

    public float getShadow() {
        return this.resizableShadow;
    }

    private void setShadow(float shadow) {
        this.resizableShadow = shadow;
    }

    public CreatureContainer getCreature() {
        return this.creature;
    }

    private void setCreature(CreatureContainer creature) {
        this.creature = creature;
    }

    @Override
    protected void scale(EntityJurassiCraftCreature entity, PoseStack poseStack, float partialTick) {
        float scale = entity.getCreatureScale();
        this.shadowRadius = scale * this.getShadow();
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityJurassiCraftCreature entity) {
        EntityJurassiCraftCreature dino = entity;

        String texture;

        if (dino instanceof EntityVelociraptor) {
            String name = dino.getCustomName() != null ? dino.getCustomName().getString() : null;

            if (name != null) {
                if (name.equalsIgnoreCase("Blue")) {
                    return getTexture(JurassiCraft.getModId() + "textures/entity/" + category + "/" + creatureName + "/blue.png");
                } else if (name.equalsIgnoreCase("Charlie")) {
                    return getTexture(JurassiCraft.getModId() + "textures/entity/" + category + "/" + creatureName + "/charlie.png");
                } else if (name.equalsIgnoreCase("Delta")) {
                    return getTexture(JurassiCraft.getModId() + "textures/entity/" + category + "/" + creatureName + "/delta.png");
                } else if (name.equalsIgnoreCase("Echo")) {
                    return getTexture(JurassiCraft.getModId() + "textures/entity/" + category + "/" + creatureName + "/echo.png");
                } else if (name.equalsIgnoreCase("Velocibrine") || name.equalsIgnoreCase("Herobrine")) {
                    return getTexture(JurassiCraft.getModId() + "textures/entity/" + category + "/" + creatureName + "/" + "velocibrine" + dino.getCreatureGenderString() + "1.png");
                }
            }
        }

        return getTexture(JurassiCraft.getModId() + "textures/entity/" + category + "/" + creatureName + "/" + creatureName + dino.getCreatureGenderString() + "1.png");
    }

    @Override
    public void render(EntityJurassiCraftCreature entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        try {
            super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
        } catch (Throwable t) {
            // Defensive: an entity render error must never crash the render thread.
            // Log it (with a stack trace) so it can be diagnosed, then skip the frame.
            JurassiCraft.logger.error("Failed to render creature " + entity.getType(), t);
        }
    }

    public ResourceLocation getTexture(String texture) {
        if (textureCache.containsKey(texture)) {
            return textureCache.get(texture);
        } else {
            ResourceLocation value = new ResourceLocation(texture);
            textureCache.put(texture, value);

            return value;
        }
    }
}
