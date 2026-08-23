package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@OnlyIn(Dist.CLIENT)
public class GuiButtonFenceSwitch extends Button {
    private int xTexPos;
    private int yTexPos;
    private boolean state;

    public GuiButtonFenceSwitch(int xPos, int yPos, int xguiPosition, int yguiPosition, int width, int height, boolean state, OnPress onPress) {
        super(xPos, yPos, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.xTexPos = xguiPosition;
        this.yTexPos = yguiPosition;
        this.state = state;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guisecurityfencewidgets.png");

            if (this.active) {
                if (this.state)
                    guiGraphics.blit(texture, this.getX(), this.getY(), this.xTexPos, this.yTexPos, this.width, this.height);
                else
                    guiGraphics.blit(texture, this.getX(), this.getY(), this.xTexPos, this.yTexPos + 1 * this.height, this.width, this.height);
            } else {
                guiGraphics.blit(texture, this.getX(), this.getY(), this.xTexPos, this.yTexPos + 2 * this.height, this.width, this.height);
            }
        }
    }

    public void setState(boolean onORoff) {
        this.state = onORoff;
    }

    public void toggleState() {
        this.state = !this.state;
    }
}
