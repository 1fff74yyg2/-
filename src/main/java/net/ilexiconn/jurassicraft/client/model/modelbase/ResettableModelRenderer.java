package net.ilexiconn.jurassicraft.client.model.modelbase;

import net.minecraft.client.model.EntityModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

/**
 * Model renderer that can save its initial pose and reset offsets.
 * Reconstructed from the compiled class in JurassiCraft-1.4.0.jar
 * (needed by ModelBasilosaurus).
 */
public class ResettableModelRenderer extends AdvancedModelRenderer {
    public float firstRotateAngleX;
    public float firstRotateAngleY;
    public float firstRotateAngleZ;
    public float firstRotationPointX;
    public float firstRotationPointY;
    public float firstRotationPointZ;
    public float firstOffsetX;
    public float firstOffsetY;
    public float firstOffsetZ;

    public ResettableModelRenderer(AdvancedModelBase model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public void savefirstParameters() {
        this.firstRotateAngleX = this.rotateAngleX;
        this.firstRotateAngleY = this.rotateAngleY;
        this.firstRotateAngleZ = this.rotateAngleZ;
        this.firstRotationPointX = this.rotationPointX;
        this.firstRotationPointY = this.rotationPointY;
        this.firstRotationPointZ = this.rotationPointZ;
        this.firstOffsetX = this.offsetX;
        this.firstOffsetY = this.offsetY;
        this.firstOffsetZ = this.offsetZ;
    }

    public void resetYOffsets() {
        this.rotationPointY = this.firstRotationPointY;
        this.offsetY = this.firstOffsetY;
    }

    public void resetZOffsets() {
        this.rotationPointZ = this.firstRotationPointZ;
        this.offsetZ = this.firstOffsetZ;
    }
}
