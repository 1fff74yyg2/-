package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class GuiCultivateProcess extends Screen {
    private TileCultivate cultivator;
    private int imageWidth;
    private int imageHeight;
    private int leftPos;
    private int topPos;

    public GuiCultivateProcess(TileCultivate entity) {
        super(Component.translatable("container.cultivator.progress"));
        this.cultivator = entity;
        this.imageWidth = 176;
        this.imageHeight = 107;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.cultivator.isHatching()) {
            this.onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
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
    protected void init() {
        super.init();

        this.clearWidgets();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.addRenderableWidget(Button.builder(Component.translatable("container.cultivator.stopCultivating"), button -> {
            float progress = (float) this.cultivator.getCultivateTimeProgressScaled(100) / 100.0F;
            this.cultivator.cancelHatching(progress);
            this.onClose();
        }).bounds(this.leftPos + (this.imageWidth - 100) / 2, this.topPos + 70, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guicultivateprogress.png");

        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(texture, this.leftPos + 13, this.topPos + 49, 0, 107, this.cultivator.getCultivateTimeProgressScaled(150), 9);

        Creature creature = this.cultivator.getCreature();

        String name;

        if (creature != null) {
            name = creature.getCreatureName();
        } else {
            name = "Unknown";
        }

        String cultivatingLang = I18n.get("container.cultivator.cultivating");
        String progressLang = I18n.get("container.cultivator.progress");

        String progress = progressLang + ": " + this.cultivator.getCultivateTimeProgressScaled(100) + "%";
        String cultivating = cultivatingLang + ": " + name;

        guiGraphics.drawString(this.font, cultivating, this.leftPos + (this.imageWidth - this.font.width(cultivating)) / 2, this.topPos + 10, 4210752);
        guiGraphics.drawString(this.font, progress, this.leftPos + (this.imageWidth - this.font.width(progress)) / 2, this.topPos + 30, 4210752);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
