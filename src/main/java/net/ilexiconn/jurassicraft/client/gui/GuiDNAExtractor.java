package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.container.ContainerDNAExtractor;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNAExtractor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@OnlyIn(Dist.CLIENT)
public class GuiDNAExtractor extends AbstractContainerScreen<ContainerDNAExtractor> {
    private TileDNAExtractor dnaExtractor;

        public GuiDNAExtractor(ContainerDNAExtractor menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.dnaExtractor = menu.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 188;
    }

public GuiDNAExtractor(Inventory inventoryPlayer, TileDNAExtractor tileEntity) {
        super(new ContainerDNAExtractor(inventoryPlayer, tileEntity), inventoryPlayer, Component.translatable("container.dnaExtractor"));
        this.dnaExtractor = tileEntity;
        this.imageWidth = 176;
        this.imageHeight = 188;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, I18n.get("container.dnaExtractor"), this.imageWidth - this.font.width("container.dnaExtractor"), 7, 4210752);
        guiGraphics.drawString(this.font, I18n.get("container.inventory"), 8, this.imageHeight - 94, 4210752);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidnaextractor.png");
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int i = this.dnaExtractor.getExtractionProgressScaled(22);
        guiGraphics.blit(texture, this.leftPos + 77, this.topPos + 38, 176, 0, i, 17);
    }
}
