package net.ilexiconn.jurassicraft.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.client.model.block.ModelDnaCombiner;
import net.ilexiconn.jurassicraft.client.render.RenderUtils;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class TileDNACombinatorRenderer implements BlockEntityRenderer<TileDNACombinator> {
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/block/dna_combinator.png");
    private ModelDnaCombiner model = new ModelDnaCombiner();

    @Override
    public void render(TileDNACombinator tileEntityModel, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        int angle = 0;
        float offX = 0.0F;
        float offZ = 0.0F;

        if (tileEntityModel.getLevel() != null) {
            BlockState state = tileEntityModel.getLevel().getBlockState(tileEntityModel.getBlockPos());
            int direction = RenderUtils.getFacingMeta(state);

            switch (direction) {
                case 0:
                    angle = -180;
                    offX = 1.0F;
                    offZ = 1.0F;
                    break;
                case 1:
                    angle = -90;
                    offX = 0.0F;
                    offZ = 1.0F;
                    break;
                case 2:
                    angle = 0;
                    offX = 0.0F;
                    offZ = 0.0F;
                    break;
                case 3:
                    angle = -270;
                    offX = 1.0F;
                    offZ = 0.0F;
                    break;
                default:
                    angle = -180;
                    offX = 0.0F;
                    offZ = 0.0F;
                    break;
            }
        }

        poseStack.pushPose();

        poseStack.translate(offX, 1.5F, offZ);
        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(angle + 180));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
        this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        this.model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}
