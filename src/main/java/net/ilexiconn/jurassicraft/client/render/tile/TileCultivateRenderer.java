package net.ilexiconn.jurassicraft.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.client.model.block.ModelCultivate;
import net.ilexiconn.jurassicraft.client.model.block.ModelEmbryo;
import net.ilexiconn.jurassicraft.common.block.cultivate.BlockCultivate;
import net.ilexiconn.jurassicraft.common.block.cultivate.BlockCultivateBottom;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class TileCultivateRenderer implements BlockEntityRenderer<TileCultivate> {
    public ModelCultivate cultivate = new ModelCultivate();
    public ModelEmbryo embryo = new ModelEmbryo();
    public ResourceLocation[] cultivateTextures;
    public ResourceLocation embryoTextures;

    public TileCultivateRenderer() {
        this.embryoTextures = new ResourceLocation(JurassiCraft.getModId() + "textures/block/embryo.png");
        this.cultivateTextures = new ResourceLocation[BlockCultivateBottom.iconVariationsNames.length];

        for (int i = 0; i < BlockCultivateBottom.iconVariationsNames.length; i++)
            this.cultivateTextures[i] = new ResourceLocation(JurassiCraft.getModId() + "textures/block/cultivate_" + BlockCultivateBottom.iconVariationsNames[i] + ".png");
    }

    @Override
    public void render(TileCultivate tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        // In 1.12.2 the cultivate colour variation (0-15) was read from the block
        // metadata. Block metadata no longer exists in 1.20.1 and the cultivate
        // blocks have not been migrated to a block-state property yet, so we fall
        // back to the first variation (black).
        int metadata = 0;

        if (tile.isHatching()) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.5F, 0.5F);
            int rotation = BlockCultivate.getRotation(tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation == 0 ? 0f : rotation == 1 ? -90f : rotation == 2 ? -180f : 90f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
            VertexConsumer embryoVertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(this.embryoTextures));
            this.embryo.render(tile);
            this.embryo.renderToBuffer(poseStack, embryoVertexConsumer, light, overlay, 1f, 1f, 1f, 1f);
            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
        VertexConsumer cultivateVertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(this.cultivateTextures[metadata]));
        for (int i = 0; i < this.cultivate.shapes.length - 1; i++)
            this.cultivate.shapes[i].render(poseStack, cultivateVertexConsumer, light, overlay);
        poseStack.popPose();

        // The fluid overlay was rendered through GL display lists in 1.12.2
        // (RenderHelper.getFluidDisplayLists). Display lists were removed in
        // 1.20.1, so the fluid overlay is skipped (see MIGRATION_GUIDE_1201.md).

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
        VertexConsumer glassVertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(this.cultivateTextures[metadata]));
        this.cultivate.shapes[6].setOpacity(0.7F);
        this.cultivate.shapes[6].render(poseStack, glassVertexConsumer, light, overlay);
        this.cultivate.shapes[6].setOpacity(1.0F);
        poseStack.popPose();
    }
}
