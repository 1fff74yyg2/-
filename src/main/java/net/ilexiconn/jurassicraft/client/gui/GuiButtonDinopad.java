package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@OnlyIn(Dist.CLIENT)
public class GuiButtonDinopad extends Button {
    private int xguiPos;
    private int yguiPos;

    public GuiButtonDinopad(int xPos, int yPos, int xguiPosition, int yguiPosition, int width, int height, OnPress onPress) {
        super(xPos, yPos, width, height, Component.empty(), onPress, DEFAULT_NARRATION);

        this.xguiPos = xguiPosition;
        this.yguiPos = yguiPosition;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            boolean hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

            if (hovered || !this.active) {
                guiGraphics.blit(new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopad.png"), this.getX(), this.getY(), xguiPos + this.width, yguiPos, this.width, this.height);
            } else {
                guiGraphics.blit(new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopad.png"), this.getX(), this.getY(), xguiPos, yguiPos, this.width, this.height);
            }
        }
    }
}
