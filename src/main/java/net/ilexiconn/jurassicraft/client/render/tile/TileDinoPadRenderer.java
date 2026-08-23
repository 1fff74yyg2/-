package net.ilexiconn.jurassicraft.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.client.model.block.ModelDinoPad;
import net.ilexiconn.jurassicraft.client.render.RenderUtils;
import net.ilexiconn.jurassicraft.common.tileentity.TileDinoPad;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class TileDinoPadRenderer implements BlockEntityRenderer<TileDinoPad> {
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/block/dino_pad.png");
    private ModelDinoPad model = new ModelDinoPad();

    @Override
    public void render(TileDinoPad tileEntityModel, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
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
        this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        this.model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}
