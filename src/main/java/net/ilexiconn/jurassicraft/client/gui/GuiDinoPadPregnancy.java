package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Horse;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class GuiDinoPadPregnancy extends Screen {
    private Animal creature;
    private float renderRotation;
    private int imageWidth;
    private int imageHeight;
    private int leftPos;
    private int topPos;

    public GuiDinoPadPregnancy(Entity entity) {
        super(Component.translatable("container.pad.pregnancy"));

        if (entity instanceof Animal && ((Animal) entity).getAge() >= 0) {
            if (entity instanceof Cow) {
                EntityPregnantCow cow = EntityPregnantCow.get((Cow) entity);

                if (cow != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof Pig) {
                EntityPregnantPig pig = EntityPregnantPig.get((Pig) entity);

                if (pig != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof Horse) {
                EntityPregnantHorse horse = EntityPregnantHorse.get((Horse) entity);

                if (horse != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof Sheep) {
                EntityPregnantSheep sheep = EntityPregnantSheep.get((Sheep) entity);

                if (sheep != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof net.minecraft.world.entity.animal.goat.Goat) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat goat = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get((net.minecraft.world.entity.animal.goat.Goat) entity);

                if (goat != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof net.minecraft.world.entity.animal.camel.Camel) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel camel = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get((net.minecraft.world.entity.animal.camel.Camel) entity);

                if (camel != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof net.minecraft.world.entity.animal.Fox) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox fox = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get((net.minecraft.world.entity.animal.Fox) entity);

                if (fox != null) {
                    this.creature = (Animal) entity;
                }
            } else if (entity instanceof net.minecraft.world.entity.animal.Panda) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda panda = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get((net.minecraft.world.entity.animal.Panda) entity);

                if (panda != null) {
                    this.creature = (Animal) entity;
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 256;
        this.imageHeight = 176;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.renderRotation = 0.0F;
    }

    @Override
    public void onClose() {
        this.creature = null;
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
            this.creature = null;
            this.onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.creature != null) {
            this.renderRotation++;
            if (!this.creature.isAlive()) {
                this.creature = null;
                this.onClose();
            }
        } else {
            this.creature = null;
            this.onClose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation texture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guidinopadpregnancy.png");
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.creature != null && this.creature.isAlive()) {
            this.renderCreature((float) (this.leftPos + 67), (float) (this.topPos + 108), 30.0F);

            if (this.creature instanceof Cow) {
                EntityPregnantCow cow = EntityPregnantCow.get((Cow) this.creature);

                if (cow.getPregnancyProgress() >= cow.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.cow"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.cow")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, cow.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantCow"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantCow")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, cow.getMammalName() + ": " + cow.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(cow.getMammalName() + ": " + cow.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + cow.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + cow.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + cow.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + cow.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof Pig) {
                EntityPregnantPig pig = EntityPregnantPig.get((Pig) this.creature);

                if (pig.getPregnancyProgress() >= pig.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pig"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pig")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, pig.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantPig"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantPig")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, pig.getMammalName() + ": " + pig.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(pig.getMammalName() + ": " + pig.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + pig.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + pig.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + pig.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + pig.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof Horse) {
                EntityPregnantHorse horse = EntityPregnantHorse.get((Horse) this.creature);

                if (horse.getPregnancyProgress() >= horse.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.horse"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.horse")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, horse.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantHorse"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantHorse")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, horse.getMammalName() + ": " + horse.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(horse.getMammalName() + ": " + horse.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + horse.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + horse.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + horse.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + horse.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof Sheep) {
                EntityPregnantSheep sheep = EntityPregnantSheep.get((Sheep) this.creature);

                if (sheep.getPregnancyProgress() >= sheep.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.sheep"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.sheep")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, sheep.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantSheep"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantSheep")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, sheep.getMammalName() + ": " + sheep.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(sheep.getMammalName() + ": " + sheep.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + sheep.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + sheep.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + sheep.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + sheep.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof net.minecraft.world.entity.animal.goat.Goat) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat goat = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get((net.minecraft.world.entity.animal.goat.Goat) this.creature);

                if (goat.getPregnancyProgress() >= goat.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.goat"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.goat")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, goat.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantGoat"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantGoat")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, goat.getMammalName() + ": " + goat.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(goat.getMammalName() + ": " + goat.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + goat.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + goat.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + goat.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + goat.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof net.minecraft.world.entity.animal.camel.Camel) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel camel = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get((net.minecraft.world.entity.animal.camel.Camel) this.creature);

                if (camel.getPregnancyProgress() >= camel.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.camel"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.camel")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, camel.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantCamel"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantCamel")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, camel.getMammalName() + ": " + camel.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(camel.getMammalName() + ": " + camel.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + camel.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + camel.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + camel.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + camel.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof net.minecraft.world.entity.animal.Fox) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox fox = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get((net.minecraft.world.entity.animal.Fox) this.creature);

                if (fox.getPregnancyProgress() >= fox.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.fox"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.fox")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, fox.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantFox"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantFox")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, fox.getMammalName() + ": " + fox.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(fox.getMammalName() + ": " + fox.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + fox.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + fox.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + fox.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + fox.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            } else if (this.creature instanceof net.minecraft.world.entity.animal.Panda) {
                net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda panda = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get((net.minecraft.world.entity.animal.Panda) this.creature);

                if (panda.getPregnancyProgress() >= panda.getPregnancySpeed()) {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.panda"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.panda")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.noEmbryo"), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.noEmbryo")) / 2, this.topPos + 70, 14737632);
                } else {
                    guiGraphics.blit(texture, this.leftPos + 130, this.topPos + 80, 0, 202, 98, 8);
                    guiGraphics.blit(texture, this.leftPos + 131, this.topPos + 81, 1, 182, panda.getPregnancyProgressScaled(95), 5);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.pregnantPanda"), this.leftPos + 127 - this.font.width(I18n.get("container.pad.pregnancy.pregnantPanda")) / 2, this.topPos + 14, 14737632);
                    guiGraphics.drawString(this.font, panda.getMammalName() + ": " + panda.getPregnancyProgressScaled(100) + "%", this.leftPos + 180 - this.font.width(panda.getMammalName() + ": " + panda.getPregnancyProgressScaled(100) + "%") / 2, this.topPos + 70, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaCode") + ": " + panda.getDNASequence(), this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaCode") + ": " + panda.getDNASequence()) / 2, this.topPos + 92, 14737632);
                    guiGraphics.drawString(this.font, I18n.get("container.pad.pregnancy.dnaQuality") + ": " + panda.getDNAQuality() + "%", this.leftPos + 180 - this.font.width(I18n.get("container.pad.pregnancy.dnaQuality") + ": " + panda.getDNAQuality() + "%") / 2, this.topPos + 104, 14737632);
                }
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderCreature(float x, float y, float scale) {
        // TODO 1.20.1: entity thumbnail rendering. The old GL11/RenderHelper based approach
        // (Minecraft.getMinecraft().getRenderManager().renderEntity) is not available anymore.
        // Re-implement with EntityRenderDispatcher + PoseStack + MultiBufferSource if needed.
    }
}
