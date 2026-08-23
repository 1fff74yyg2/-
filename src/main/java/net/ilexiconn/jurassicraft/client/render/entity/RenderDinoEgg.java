package net.ilexiconn.jurassicraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.ilexiconn.jurassicraft.client.model.block.ModelEgg;
import net.ilexiconn.jurassicraft.client.model.entity.ModelCoelacanthEgg;
import net.ilexiconn.jurassicraft.client.model.entity.ModelMeganeuraEgg;
import net.ilexiconn.jurassicraft.common.entity.egg.EntityDinoEgg;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderDinoEgg extends EntityRenderer<EntityDinoEgg> {
    private final ModelMeganeuraEgg meganeuraEgg = new ModelMeganeuraEgg();
    private final ModelCoelacanthEgg fishEgg = new ModelCoelacanthEgg();

    public RenderDinoEgg(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDinoEgg entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        this.renderEgg(entity, yaw, poseStack, bufferSource, light);
    }

    private void renderEgg(EntityDinoEgg entity, float rotationYaw, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        AdvancedModelBase eggModel = new ModelEgg();

        // 1.20.1: the creature can be null when the egg is loaded from an old save
        // (the static creatureID counter shifted after a mod update) or spawned
        // without DNA data; fall back to the plain egg model/texture instead of crashing.
        String name = entity.getSyncedCreatureName().toLowerCase();

        if (name.contains("meganeura"))
            eggModel = meganeuraEgg;
        else if (name.contains("coelacanth") || name.contains("orthacanthus"))
            eggModel = fishEgg;

        if (!entity.isRemoved()) {
            poseStack.pushPose();

            poseStack.translate(0.0D, 1.5D, 0.0D);

            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
            float f4 = 0.75F;

            poseStack.scale(f4, f4, f4);
            poseStack.scale(1.0F / f4, 1.0F / f4, 1.0F / f4);
            poseStack.scale(-1.0F, -1.0F, 1.0F);

            // 1.12.2 tinted the egg blue when frozen and brown when dried via
            // GL11.glColor3f; the 1.20.1 vertex pipeline no longer applies a global
            // color, so the tint is skipped (texture is used as-is).
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
            eggModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDinoEgg entity) {
        return entity.getTexture();
    }}
