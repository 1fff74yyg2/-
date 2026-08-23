package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.container.ContainerCultivate;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@OnlyIn(Dist.CLIENT)
public class GuiCultivate extends AbstractContainerScreen<ContainerCultivate> {
    private TileCultivate cultivator;

        public GuiCultivate(ContainerCultivate menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.cultivator = menu.getTileEntity();
        this.imageWidth = 352;
        this.imageHeight = 188;
    }

public GuiCultivate(Inventory inventoryPlayer, TileCultivate entity) {
        super(new ContainerCultivate(inventoryPlayer, entity), inventoryPlayer, Component.translatable("container.cultivate"));
        this.cultivator = entity;
        this.imageWidth = 352;
        this.imageHeight = 188;
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        if (this.cultivator.isHatching()) {
            this.minecraft.player.closeContainer();
            this.minecraft.setScreen(new GuiCultivateProcess(cultivator));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, I18n.get("Cultivate"), this.imageWidth * 3 / 8 - this.font.width("Cultivate") / 2 - 1, 20, 4210752);
        guiGraphics.drawString(this.font, I18n.get("Proximates"), 200, 48, 4210752);
        guiGraphics.drawString(this.font, I18n.get("Minerals"), 200, 74, 4210752);
        guiGraphics.drawString(this.font, I18n.get("Vitamins"), 200, 100, 4210752);
        guiGraphics.drawString(this.font, I18n.get("Lipids"), 200, 126, 4210752);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation leftTexture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guicultivateleft.png");
        ResourceLocation rightTexture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guicultivateright.png");

        guiGraphics.blit(leftTexture, this.leftPos, this.topPos, 0, 0, 176, 188);

        int water = this.cultivator.getWaterStoredProgressScaled(67);
        guiGraphics.blit(leftTexture, this.leftPos + 48, this.topPos + 18, 0, 188, 42, 67 - water);

        guiGraphics.blit(rightTexture, this.leftPos + 177, this.topPos, 0, 0, 176, 166);

        int proximates = this.cultivator.getProximateBarScaled(150);
        guiGraphics.blit(rightTexture, this.leftPos + 190, this.topPos + 56, 0, 166, proximates, 9);

        int minerals = this.cultivator.getMineralBarScaled(150);
        guiGraphics.blit(rightTexture, this.leftPos + 190, this.topPos + 82, 0, 175, minerals, 9);

        int vitamins = this.cultivator.getVitaminBarScaled(150);
        guiGraphics.blit(rightTexture, this.leftPos + 190, this.topPos + 108, 0, 184, vitamins, 9);

        int lipids = this.cultivator.getLipidBarScaled(150);
        guiGraphics.blit(rightTexture, this.leftPos + 190, this.topPos + 134, 0, 193, lipids, 9);
    }
}
