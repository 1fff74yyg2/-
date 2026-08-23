package net.ilexiconn.jurassicraft.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.container.ContainerSecurityFenceLow;
import net.ilexiconn.jurassicraft.common.message.MessageFence;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowCorner;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class GuiSecurityFenceLow extends AbstractContainerScreen<ContainerSecurityFenceLow> {
    private HashMap<Integer, int[]> fenceMap = new HashMap<Integer, int[]>();
    private TileSecurityFenceLowCorner fence;
    private int missingMaterials;
    private String errorMessage;

    private GuiButtonFenceGeneric buttonDir0;
    private GuiButtonFenceGeneric buttonDir1;
    private GuiButtonFenceGeneric buttonDir2;
    private GuiButtonFenceGeneric buttonDir3;
    private GuiButtonFenceGeneric buttonUpdateMap;
    private Button buttonBuildFence;
    private GuiButtonFenceSwitch buttonSwitch;
    private Button buttonFixFence;

        public GuiSecurityFenceLow(ContainerSecurityFenceLow menu, Inventory inv, Component title) {
        super(menu, inv, title);
        if (menu.getTileEntity() != null) {
            this.imageWidth = 256;
            this.imageHeight = 256;
            this.fence = menu.getTileEntity();
        }
    }

public GuiSecurityFenceLow(Inventory inventoryPlayer, TileSecurityFenceLowCorner entity) {
        super(new ContainerSecurityFenceLow(inventoryPlayer, entity), inventoryPlayer, Component.translatable("container.fence.security.low"));
        if (entity != null) {
            this.imageWidth = 256;
            this.imageHeight = 256;
            this.fence = entity;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        if (this.fence == null) {
            this.onClose();
            return;
        }

        // Direction Buttons.
        this.buttonDir0 = new GuiButtonFenceGeneric(this.leftPos + 54, this.topPos + 126, 15, 45, 15, 10, button -> this.setPlannedSide(0));
        this.buttonDir1 = new GuiButtonFenceGeneric(this.leftPos + 10, this.topPos + 78, 15, 0, 10, 15, button -> this.setPlannedSide(1));
        this.buttonDir2 = new GuiButtonFenceGeneric(this.leftPos + 54, this.topPos + 34, 0, 45, 15, 10, button -> this.setPlannedSide(2));
        this.buttonDir3 = new GuiButtonFenceGeneric(this.leftPos + 102, this.topPos + 78, 25, 0, 10, 15, button -> this.setPlannedSide(3));

        // Update Map Button.
        this.buttonUpdateMap = new GuiButtonFenceGeneric(this.leftPos + 23, this.topPos + 142, 35, 0, 15, 15, button -> {
            this.fenceMap = this.fence.getAllFenceBlocks();
            this.refreshGUI();
        });

        // Build Fence Button.
        this.buttonBuildFence = Button.builder(Component.translatable("container.fence.buildFence"), button -> this.sendFenceAction(5))
                .bounds(this.leftPos + 141, this.topPos + 63, 80, 20).build();

        // Turn On Off Button.
        this.buttonSwitch = new GuiButtonFenceSwitch(this.leftPos + 141, this.topPos + 86, 89, 0, 80, 54, this.fence.isFenceOn(this.fence.getPlannedSide()), button -> this.sendFenceAction(6));

        // Fixing Button.
        this.buttonFixFence = Button.builder(Component.translatable("container.fence.fixFence"), button -> this.sendFenceAction(7))
                .bounds(this.leftPos + 141, this.topPos + 63, 80, 20).build();

        this.addRenderableWidget(this.buttonDir0);
        this.addRenderableWidget(this.buttonDir1);
        this.addRenderableWidget(this.buttonDir2);
        this.addRenderableWidget(this.buttonDir3);
        this.addRenderableWidget(this.buttonUpdateMap);
        this.addRenderableWidget(this.buttonBuildFence);
        this.addRenderableWidget(this.buttonSwitch);
        this.addRenderableWidget(this.buttonFixFence);

        this.fenceMap = this.fence.getAllFenceBlocks();
        this.refreshGUI();
    }

    @Override
    public void onClose() {
        if (this.fence != null) {
            this.fence = null;
            this.fenceMap.clear();
        }
        super.onClose();
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        if (this.fence != null) {
            this.refreshGUI();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void setPlannedSide(int side) {
        this.fence.setPlannedSide((byte) side);
        this.fenceMap = this.fence.getAllFenceBlocks();
        this.refreshGUI();
    }

    private void sendFenceAction(int id) {
        JurassiCraft.network.sendToServer(new MessageFence(id, this.fence.getBlockPos().getX(), this.fence.getBlockPos().getY(), this.fence.getBlockPos().getZ(), this.fence.getPlannedSide()));
        this.fenceMap = this.fence.getAllFenceBlocks();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.fence == null) {
            return;
        }

        int direction = this.fence.getPlannedSide();

        switch (direction) {
            case 0:
                guiGraphics.drawString(this.font, I18n.get("container.fence.south"), 71 - this.font.width(I18n.get("container.fence.south")) / 2, 146, 4210752);
                break;
            case 1:
                guiGraphics.drawString(this.font, I18n.get("container.fence.west"), 71 - this.font.width(I18n.get("container.fence.west")) / 2, 146, 4210752);
                break;
            case 2:
                guiGraphics.drawString(this.font, I18n.get("container.fence.north"), 71 - this.font.width(I18n.get("container.fence.north")) / 2, 146, 4210752);
                break;
            case 3:
                guiGraphics.drawString(this.font, I18n.get("container.fence.east"), 71 - this.font.width(I18n.get("container.fence.east")) / 2, 146, 4210752);
                break;
            default:
                guiGraphics.drawString(this.font, I18n.get("container.fence.noDirection"), 71 - this.font.width(I18n.get("container.fence.noDirection")) / 2, 146, 4210752);
        }

        if (this.fence.hasFenceAt(direction)) {
            guiGraphics.drawString(this.font, I18n.get("container.fence.security.low"), 128 - this.font.width(I18n.get("container.fence.security.low")) / 2, 15, 4210752);
        } else {
            guiGraphics.drawString(this.font, I18n.get("container.fence.security.low"), 128 - this.font.width(I18n.get("container.fence.security.none")) / 2, 15, 4210752);
        }

        guiGraphics.drawString(this.font, I18n.get("container.inventory"), 48, 163, 4210752);

        if (this.errorMessage != null) {
            if (this.errorMessage == "container.fence.noIronIngots" || this.errorMessage == "container.fence.noRedstone") {
                guiGraphics.drawString(this.font, I18n.get(this.errorMessage) + " (" + this.missingMaterials + ")", 181 - this.font.width(I18n.get(this.errorMessage) + " (" + this.missingMaterials + ")") / 2, 146, 4210752);
            } else {
                guiGraphics.drawString(this.font, I18n.get(this.errorMessage), 181 - this.font.width(I18n.get(this.errorMessage)) / 2, 146, 4210752);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        if (this.fence == null) {
            return;
        }

        ResourceLocation mainTexture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guisecurityfence.png");
        ResourceLocation widgetsTexture = new ResourceLocation(JurassiCraft.getModId() + "textures/gui/guisecurityfencewidgets.png");
        guiGraphics.blit(mainTexture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        switch (this.fence.getPlannedSide()) {
            case 0:
                guiGraphics.blit(widgetsTexture, this.leftPos + 59, this.topPos + 83, 52, 0, 4, 37);
                break;
            case 1:
                guiGraphics.blit(widgetsTexture, this.leftPos + 26, this.topPos + 83, 52, 0, 37, 4);
                break;
            case 2:
                guiGraphics.blit(widgetsTexture, this.leftPos + 59, this.topPos + 50, 52, 0, 4, 37);
                break;
            case 3:
                guiGraphics.blit(widgetsTexture, this.leftPos + 59, this.topPos + 83, 52, 0, 37, 4);
                break;
        }

        if (!this.fenceMap.isEmpty()) {
            for (int i = 0; i < this.fenceMap.size(); i++) {
                guiGraphics.blit(widgetsTexture, this.leftPos + 60 + 3 * this.fenceMap.get(i)[0], this.topPos + 84 + 3 * this.fenceMap.get(i)[1], 50, 0, 2, 2);
            }
        }
    }

    private void refreshSwitchButton() {
        this.buttonSwitch.setState(this.fence.isFenceOn(this.fence.getPlannedSide()));
    }

    private void refreshGUI() {
        if (this.fence == null) {
            return;
        }

        switch (this.fence.getPlannedSide()) {
            case 0:
                this.buttonDir0.active = false;
                this.buttonDir1.active = true;
                this.buttonDir2.active = true;
                this.buttonDir3.active = true;
                break;
            case 1:
                this.buttonDir0.active = true;
                this.buttonDir1.active = false;
                this.buttonDir2.active = true;
                this.buttonDir3.active = true;
                break;
            case 2:
                this.buttonDir0.active = true;
                this.buttonDir1.active = true;
                this.buttonDir2.active = false;
                this.buttonDir3.active = true;
                break;
            case 3:
                this.buttonDir0.active = true;
                this.buttonDir1.active = true;
                this.buttonDir2.active = true;
                this.buttonDir3.active = false;
                break;
        }

        if (this.fence.hasFenceAt(this.fence.getPlannedSide())) {
            this.buttonBuildFence.visible = false;
            this.buttonSwitch.active = true;
            this.buttonFixFence.visible = true;

            if (!this.fence.isFenceOn(this.fence.getPlannedSide())) {
                int length = this.fence.getFenceBaseLength(this.fence, this.fence.getPlannedSide());

                if (this.fence.isBaseAtSideValid(this.fence, this.fence.getPlannedSide(), length)) {
                    TileSecurityFenceLowCorner otherFence = this.fence.getNextLowSecurityMainFenceBlockDirectly(this.fence, this.fence.getPlannedSide(), length + 1);

                    int height = this.fence.getSmallerFencePoleHeight(this.fence, otherFence);

                    int numberOfGridsToFix = this.fence.getNumberOfGridsToFix(this.fence, this.fence.getPlannedSide(), length, height);

                    if (numberOfGridsToFix > 0) {
                        int ironRequiredToFix = this.fence.getIronRequiredForGrid(numberOfGridsToFix);

                        if (this.fence.hasNumberOfIronStored(ironRequiredToFix)) {
                            int redstoneRequiredToFix = this.fence.getRedstoneRequiredForGrid(numberOfGridsToFix);

                            if (this.fence.hasNumberOfRedstoneStored(redstoneRequiredToFix)) {
                                this.buttonBuildFence.active = false;
                                this.buttonFixFence.active = true;
                                this.refreshSwitchButton();
                                this.missingMaterials = 0;
                                this.errorMessage = null;
                                return;
                            } else {
                                this.errorMessage = "container.fence.noRedstone";
                                this.missingMaterials = redstoneRequiredToFix;
                            }
                        } else {
                            this.errorMessage = "container.fence.noIronIngots";
                            this.missingMaterials = ironRequiredToFix;
                        }
                    } else {
                        this.errorMessage = "container.fence.notBroken";
                        this.missingMaterials = 0;
                    }
                } else {
                    this.errorMessage = "container.fence.baseIsWrong";
                    this.missingMaterials = 0;
                }
            } else {
                this.errorMessage = "container.fence.fenceOn";
                this.missingMaterials = 0;
            }

            this.buttonBuildFence.active = false;
            this.buttonFixFence.active = false;
            this.refreshSwitchButton();
        } else {
            this.buttonBuildFence.visible = true;
            this.buttonSwitch.active = false;
            this.buttonFixFence.visible = false;

            int length = this.fence.getFenceBaseLength(this.fence, this.fence.getPlannedSide());

            if (this.fence.isBaseAtSideValid(this.fence, this.fence.getPlannedSide(), length)) {
                TileSecurityFenceLowCorner otherFence = this.fence.getNextLowSecurityMainFenceBlockDirectly(this.fence, this.fence.getPlannedSide(), length + 1);

                int height = this.fence.getSmallerFencePoleHeight(this.fence, otherFence);

                int numberOfGridsToBuild = this.fence.getNumberOfGridsToBuild(length, height);

                int ironRequiredToBuild = this.fence.getIronRequiredForGrid(numberOfGridsToBuild);

                if (this.fence.hasNumberOfIronStored(ironRequiredToBuild)) {
                    int redstoneRequiredToBuild = this.fence.getRedstoneRequiredForGrid(numberOfGridsToBuild);

                    if (this.fence.hasNumberOfRedstoneStored(redstoneRequiredToBuild)) {
                        if (this.fence.hasEmptySpaceAt(this.fence, this.fence.getPlannedSide(), length, height)) {
                            this.buttonBuildFence.active = true;
                            this.buttonSwitch.active = false;
                            this.missingMaterials = 0;
                            this.errorMessage = null;
                            return;
                        } else {
                            this.errorMessage = "container.fence.pathBlocked";
                            this.missingMaterials = 0;
                        }
                    } else {
                        this.errorMessage = "container.fence.noRedstone";
                        this.missingMaterials = redstoneRequiredToBuild;
                    }
                } else {
                    this.errorMessage = "container.fence.noIronIngots";
                    this.missingMaterials = ironRequiredToBuild;
                }
            } else {
                this.errorMessage = "container.fence.baseIsWrong";
                this.missingMaterials = 0;
            }

            this.buttonBuildFence.active = false;
            this.buttonFixFence.active = false;
        }
    }
}
