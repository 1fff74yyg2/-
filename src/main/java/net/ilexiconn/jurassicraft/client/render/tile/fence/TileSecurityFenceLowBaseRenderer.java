package net.ilexiconn.jurassicraft.client.render.tile.fence;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.client.model.block.ModelLowSecurityFenceBase;
import net.ilexiconn.jurassicraft.client.render.RenderUtils;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class TileSecurityFenceLowBaseRenderer implements BlockEntityRenderer<TileSecurityFenceLowBase> {
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/block/fence/low_security_fence_base.png");
    private static final ModelLowSecurityFenceBase model = new ModelLowSecurityFenceBase();

    @Override
    public void render(TileSecurityFenceLowBase tileEntityModel, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        int angle = 0;

        if (tileEntityModel.getLevel() != null) {
            BlockState state = tileEntityModel.getLevel().getBlockState(tileEntityModel.getBlockPos());
            angle = RenderUtils.getRotationAngle(state);
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
        model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}
