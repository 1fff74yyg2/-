package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.egg.EntityDinoEgg;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class GuiDinoPadEgg extends Screen {
    private EntityDinoEgg egg;
    private float renderRotation;
    private int imageWidth;
    private int imageHeight;
    private int leftPos;
    private int topPos;

    public GuiDinoPadEgg(Entity entity) {
        super(Component.translatable("container.pad.egg"));

        if (entity instanceof EntityDinoEgg) {
            this.egg = (EntityDinoEgg) entity;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.imageWidth = 256;
        this.imageHeight = 176;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.renderRotation = 0.0F;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.egg = null;
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
            this.onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.egg != null) {
            this.renderRotation++;

            if (!this.egg.isAlive()) {
                this.onClose();
            }
        } else {
            this.onClose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopadegg.png");
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.egg != null && this.egg.isAlive()) {
            this.renderEgg((float) (this.leftPos + 67), (float) (this.topPos + 108), 60.0F);

            if (this.egg.creature != null) {
                guiGraphics.drawString(this.font, I18n.get("entity." + this.egg.creature.getCreatureName() + ".name"), this.leftPos + 127 - this.font.width(I18n.get("entity." + this.egg.creature.getCreatureName() + ".name")) / 2, this.topPos + 14, 14737632);
            }

            if (this.egg.currentSpawnTime >= 0) {
                guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, this.egg.getHatchingProgressScaled(95), 5);
                guiGraphics.drawString(this.font, I18n.get("container.pad.egg.hatching") + ": " + String.valueOf(this.egg.getHatchingProgressScaled(100)) + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.hatching") + String.valueOf(this.egg.getHatchingProgressScaled(100)) + "%") / 2, this.topPos + 68, 14737632);
                guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence()) / 2, this.topPos + 92, 14737632);
                guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
            } else {
                if (this.egg.dried) {
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.notHatching"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.notHatching")) / 2, this.topPos + 68, 32000000);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dried"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dried")) / 2, this.topPos + 80, 32000000);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence()) / 2, this.topPos + 92, 32000000);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%") / 2, this.topPos + 104, 32000000);
                } else if (this.egg.froze) {
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.notHatching"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.notHatching")) / 2, this.topPos + 68, 950000);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.frozen"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.frozen")) / 2, this.topPos + 80, 950000);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence()) / 2, this.topPos + 92, 950000);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%") / 2, this.topPos + 104, 950000);
                } else {
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.notHatching"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.notHatching")) / 2, this.topPos + 68, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaCode") + ": " + this.egg.getDNASequence()) / 2, this.topPos + 80, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.egg.dnaQuality") + ": " + this.egg.getDNAQuality() + "%") / 2, this.topPos + 92, 14737632);
                }
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderEgg(float x, float y, float scale) {
        // TODO 1.20.1: entity thumbnail rendering. The old GL11/RenderHelper based approach
        // (Minecraft.getMinecraft().getRenderManager().renderEntity) is not available anymore.
        // Re-implement with EntityRenderDispatcher + PoseStack + MultiBufferSource if needed.
    }
}
