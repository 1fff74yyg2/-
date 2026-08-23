package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class GuiDinoPad extends Screen {
    private EntityJurassiCraftSmart creature;
    private int imageWidth;
    private int imageHeight;
    private int leftPos;
    private int topPos;
    private float renderRotation;
    private int pageNumber;
    private HashMap<Integer, String[]> dinoInfo = new HashMap<Integer, String[]>();

    public GuiDinoPad(Entity entity) {
        super(Component.translatable("container.pad"));

        if (entity instanceof EntityJurassiCraftSmart) {
            this.creature = (EntityJurassiCraftSmart) entity;
            this.imageWidth = 256;
            this.imageHeight = 176;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.dinoInfo.clear();

        if (this.creature == null || !this.creature.isAlive()) {
            this.onClose();
            return;
        }

        for (int numberOfPages = 1; numberOfPages <= this.creature.getCreature().getInfoPageCount(); numberOfPages++) {
            this.dinoInfo.put(numberOfPages, this.getCreatureInformation(numberOfPages));
        }

        this.renderRotation = 0.0F;
        this.pageNumber = 0;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.addRenderableWidget(new GuiButtonDinopad(this.leftPos + (this.imageWidth - 18) / 2, this.topPos + 146, 0, 210, 18, 18, button -> this.pageNumber = 0));
        this.addRenderableWidget(new GuiButtonDinopad(this.leftPos + (this.imageWidth - 18) / 2 - 14, this.topPos + 146, 36, 210, 12, 18, button -> {
            if (this.pageNumber > 0) {
                this.pageNumber--;
            } else {
                this.pageNumber = this.creature.getCreature().getInfoPageCount();
            }
        }));
        this.addRenderableWidget(new GuiButtonDinopad(this.leftPos + (this.imageWidth - 18) / 2 + 20, this.topPos + 146, 60, 210, 12, 18, button -> {
            if (this.pageNumber < this.creature.getCreature().getInfoPageCount()) {
                this.pageNumber++;
            } else {
                this.pageNumber = 0;
            }
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.creature = null;

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

        if (this.creature == null || !this.creature.isAlive()) {
            this.onClose();
        } else {
            this.renderRotation++;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.creature != null) {
            ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopad.png");
            guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

            switch (this.pageNumber) {
                case 0:
                    this.renderEmptyBars(guiGraphics);
                    this.renderStatusBars(guiGraphics);

                    if (this.creature.getCreature().getCreatureID() >= 0 && this.creature.getCreatureLength() > this.creature.getCreatureHeight()) {
                        this.renderCreature((float) (this.leftPos + 67), (float) (this.topPos + 108), (55.0F / creature.getCreatureLength()) * (0.4F + 0.6F * this.creature.getCreatureLength() / (this.creature.getCreature().getMaxLength())) * 4);
                    } else {
                        this.renderCreature((float) (this.leftPos + 67), (float) (this.topPos + 108), (55.0F / creature.getCreatureHeight()) * (0.4F + 0.6F * this.creature.getCreatureHeight() / this.creature.getCreature().getMaxHeight()) * 4);
                    }

                    this.renderNameGenderStrings(guiGraphics);
                    this.renderStatusStrings(guiGraphics);
                    this.renderTamedStrings(guiGraphics);

                    break;
                default:
                    this.renderNameGenderStrings(guiGraphics);
                    this.renderCreatureInformation(guiGraphics, this.pageNumber);

                    break;
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderEmptyBars(GuiGraphics guiGraphics) {
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopad.png");
        guiGraphics.blit(texture, this.leftPos + 140, this.topPos + 55, 0, 202, 98, 8);
        guiGraphics.blit(texture, this.leftPos + 140, this.topPos + 80, 0, 202, 98, 8);
        guiGraphics.blit(texture, this.leftPos + 140, this.topPos + 105, 0, 202, 98, 8);
    }

    private void renderStatusBars(GuiGraphics guiGraphics) {
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopad.png");
        guiGraphics.blit(texture, this.leftPos + 141, this.topPos + 56, 1, 177, this.creature.getCreatureHealthScaled(95), 5);
        guiGraphics.blit(texture, this.leftPos + 141, this.topPos + 81, 1, 182, this.creature.getCreatureAttackScaled(95), 5);
        guiGraphics.blit(texture, this.leftPos + 141, this.topPos + 106, 1, 187, this.creature.getCreatureSpeedScaled(95), 5);
    }

    private void renderNameGenderStrings(GuiGraphics guiGraphics) {
        if (this.creature.hasCustomName()) {
            guiGraphics.drawString(this.font, this.creature.getCustomName().getString() + " (" + this.creature.getCreatureName() + ")", this.leftPos + 127 - this.font.width(this.creature.getCustomName().getString() + "(" + this.creature.getCreatureName() + ")") / 2, this.topPos + 11, 14737632);
        } else {
            guiGraphics.drawString(this.font, I18n.get("container.pad.creature") + ": " + this.creature.getCreatureName(), this.leftPos + 127 - this.font.width(I18n.get("container.pad.creature") + ": " + this.creature.getCreatureName()) / 2, this.topPos + 11, 14737632);
        }

        guiGraphics.drawString(this.font, this.creature.getCreatureAgeString() + ", " + this.creature.getCreatureGenderString(), this.leftPos + 127 - this.font.width(this.creature.getCreatureAgeString() + ", " + this.creature.getCreatureGenderString()) / 2, this.topPos + 19, 14737632);
    }

    private void renderStatusStrings(GuiGraphics guiGraphics) {
        String healthLang = I18n.get("container.pad.health");
        String attackLang = I18n.get("container.pad.attack");
        String speedLang = I18n.get("container.pad.speed");
        String heightLang = I18n.get("container.pad.height");
        String lengthLang = I18n.get("container.pad.length");

        String health = healthLang + ": " + String.valueOf(this.creature.getCreatureCurrentHealth() + "/" + this.creature.getCreatureHealth());
        String attack = attackLang + ": " + String.valueOf(this.creature.getCreatureAttack());
        String speed = speedLang + ": " + String.valueOf(this.creature.getCreatureSpeedValue());
        String height = heightLang + ": " + String.valueOf(this.creature.getCreatureHeight());
        String length = lengthLang + ": " + String.valueOf(this.creature.getCreatureLength());

        guiGraphics.drawString(this.font, health, this.leftPos + 192 - this.font.width(health) / 2, this.topPos + 45, 14737632);
        guiGraphics.drawString(this.font, attack, this.leftPos + 192 - this.font.width(attack) / 2, this.topPos + 70, 14737632);
        guiGraphics.drawString(this.font, speed, this.leftPos + 192 - this.font.width(speed) / 2, this.topPos + 95, 14737632);
        guiGraphics.drawString(this.font, height, this.leftPos + 192 - this.font.width(height) / 2, this.topPos + 116, 14737632);
        guiGraphics.drawString(this.font, length, this.leftPos + 192 - this.font.width(length) / 2, this.topPos + 126, 14737632);
    }

    private void renderTamedStrings(GuiGraphics guiGraphics) {
        if (this.creature.isTamed()) {
            if (this.creature.getCreature().isRidable() && this.creature.isCreatureAdult()) {
                guiGraphics.drawString(this.font, I18n.get("container.pad.owner") + ": " + this.creature.getOwnerName(), this.leftPos + 67 - this.font.width(I18n.get("container.pad.owner") + this.creature.getOwnerName()) / 2, this.topPos + 112, 14737632);
                guiGraphics.drawString(this.font, I18n.get("container.pad.ridable"), this.leftPos + 67 - this.font.width("Ridable") / 2, this.topPos + 122, 14737632);
            } else {
                guiGraphics.drawString(this.font, I18n.get("container.pad.owner") + ": " + this.creature.getOwnerName(), this.leftPos + 67 - this.font.width(I18n.get("container.pad.owner") + ": " + this.creature.getOwnerName()) / 2, this.topPos + 122, 14737632);
            }
        } else {
            guiGraphics.drawString(this.font, I18n.get("container.pad.owner") + ": " + I18n.get("container.pad.none"), this.leftPos + 67 - this.font.width(I18n.get("container.pad.owner") + ": " + I18n.get("container.pad.none")) / 2, this.topPos + 122, 14737632);
        }
    }

    private String[] getCreatureInformation(int page) {
        String info = I18n.get("container.pad.info." + this.creature.getCreature().getCreatureName() + ".page" + page);
        String[] pageInfo = new String[8];

        if (info != null && info != "") {
            int line = 0;
            int index = 0;

            for (int infoSize = info.length(); infoSize >= 43; line++) {
                index = 43;

                while (!String.valueOf(info.substring(0, index).charAt(index - 1)).equals(" ")) {
                    index--;
                }

                pageInfo[line] = info.substring(0, index - 1);
                info = info.substring(index, infoSize);
                infoSize = info.length();
            }

            pageInfo[line] = info;
        }

        return pageInfo;
    }

    private void renderCreatureInformation(GuiGraphics guiGraphics, int page) {
        if (this.dinoInfo.containsKey(page)) {
            for (int line = 0; line < this.dinoInfo.get(page).length; line++) {
                guiGraphics.drawString(this.font, this.dinoInfo.get(page)[line], this.leftPos + 128 - this.font.width(this.dinoInfo.get(page)[line]) / 2, this.topPos + 45 + 12 * line, 14737632);
            }
        } else {
            guiGraphics.drawString(this.font, "Page missing! This is a bug!", this.leftPos + 128 - this.font.width("Page missing! This is a bug!") / 2, this.topPos + 45, 14737632);
        }
    }

    private void renderCreature(float posX, float posY, float scale) {
        // TODO 1.20.1: entity thumbnail rendering. The old GL11/RenderHelper based approach
        // (Minecraft.getMinecraft().getRenderManager().renderEntity) is not available anymore.
        // Re-implement with EntityRenderDispatcher + PoseStack + MultiBufferSource if needed.
    }
}
