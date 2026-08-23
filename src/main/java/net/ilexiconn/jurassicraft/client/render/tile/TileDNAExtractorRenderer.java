package net.ilexiconn.jurassicraft.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.client.model.block.ModelDNAExtractorBase;
import net.ilexiconn.jurassicraft.client.model.block.ModelDNAExtractorGlass;
import net.ilexiconn.jurassicraft.client.render.RenderUtils;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNAExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileDNAExtractorRenderer implements BlockEntityRenderer<TileDNAExtractor> {
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/block/dna_extractor.png");
    private ModelDNAExtractorBase modelBase = new ModelDNAExtractorBase();
    private ModelDNAExtractorGlass modelGlass = new ModelDNAExtractorGlass();
    private float animationAngle = 0.001F;

    @Override
    public void render(TileDNAExtractor tileEntityModel, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        int angle = 0;

        if (tileEntityModel.getLevel() != null) {
            BlockState state = tileEntityModel.getLevel().getBlockState(tileEntityModel.getBlockPos());
            angle = RenderUtils.getRotationAngle(state);
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(angle + 180));

        VertexConsumer baseVertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
        this.modelBase.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        this.modelBase.renderToBuffer(poseStack, baseVertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        if (tileEntityModel.hasItems()) {
            this.animationAngle += 0.1F;
            for (int i = 7; i > 3; i--) {
                ItemStack stack = tileEntityModel.getStackInSlot(i);
                if (stack != null && !stack.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.translate(0.05F * (i - 3), 1.0F + 0.1F * Mth.sin(this.animationAngle / 20.0F + i), -0.05F * (i - 3));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(this.animationAngle + i));
                    poseStack.scale(0.4F, 0.4F, 0.4F);
                    ItemEntity entityItem = new ItemEntity(tileEntityModel.getLevel(), 0.0D, 0.0D, 0.0D, stack);
                    Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, poseStack, bufferSource, light);
                    poseStack.popPose();
                }
            }
        }

        VertexConsumer glassVertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        this.modelGlass.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        this.modelGlass.renderToBuffer(poseStack, glassVertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 0.55F);
        poseStack.popPose();
    }
}
