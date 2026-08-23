package net.ilexiconn.jurassicraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ilexiconn.jurassicraft.common.entity.EntitySpit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSpit extends EntityRenderer<EntitySpit> {
    public RenderSpit(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntitySpit entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySpit entity) {
        return null;
    }
}
