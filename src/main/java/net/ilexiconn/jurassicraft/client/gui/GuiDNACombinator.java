package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.container.ContainerDNACombinator;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@OnlyIn(Dist.CLIENT)
public class GuiDNACombinator extends AbstractContainerScreen<ContainerDNACombinator> {
    private TileDNACombinator dnaCombinator;

        public GuiDNACombinator(ContainerDNACombinator menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.dnaCombinator = menu.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 188;
    }

public GuiDNACombinator(Inventory inventoryPlayer, TileDNACombinator tileEntity) {
        super(new ContainerDNACombinator(inventoryPlayer, tileEntity), inventoryPlayer, Component.translatable("container.dnaCombinator"));
        this.dnaCombinator = tileEntity;
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
        guiGraphics.drawString(this.font, I18n.get("container.dnaCombinator"), this.imageWidth - this.font.width("container.dnaCombinator"), 7, 4210752);
        guiGraphics.drawString(this.font, I18n.get("container.inventory"), 8, this.imageHeight - 94, 4210752);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidnacombinator.png");
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int i = this.dnaCombinator.getCombinationProgressScaled(22);
        guiGraphics.blit(texture, this.leftPos + 93, this.topPos + 36, 176, 0, 9, i);
    }
}
