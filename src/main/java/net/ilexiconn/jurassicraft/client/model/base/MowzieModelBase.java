package net.ilexiconn.jurassicraft.client.model.base;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.util.Mth;

/**
 * Reconstructed from the compiled classes in JurassiCraft-1.4.0-1_7_10-FIXED.jar
 * (this source snapshot was missing the file). Adapted to 1.20.1: AdvancedModelRenderer is
 * now the vendored AdvancedModelRenderer.
 */
public class MowzieModelBase extends AdvancedModelBase {
    public MowzieModelBase() {
        super();
    }

    protected void addChildTo(AdvancedModelRenderer parent, AdvancedModelRenderer child) {
        float distance = (float) Math.sqrt(Math.pow(parent.rotationPointZ - child.rotationPointZ, 2.0) + Math.pow(parent.rotationPointY - child.rotationPointY, 2.0));
        float prevRotX = child.rotateAngleX;
        float rotation = (float) Math.atan((parent.rotationPointZ - child.rotationPointZ) / (parent.rotationPointY - child.rotationPointY));
        float rotation2 = rotation - child.rotateAngleX;
        float distX = (float) (distance * Math.cos(rotation2));
        float distY = (float) (distance * Math.sin(rotation2));
        child.rotateAngleX = 0.0F;
        parent.setRotationPoint(parent.rotationPointX - child.rotationPointX, distX, distY);
        child.addChild(parent);
        child.rotateAngleX = prevRotX;
        parent.rotateAngleX -= child.rotateAngleX;
        parent.rotateAngleY -= child.rotateAngleY;
        parent.rotateAngleZ -= child.rotateAngleZ;
    }

    public float rotateBox(float x, float y, boolean bool, float f1, float f2, float f3, float f4) {
        if (bool) {
            return -(Mth.cos(f3 * x + f1) * y * f4) + f2 * f4;
        } else {
            return Mth.cos(f3 * x + f1) * y * f4 + f2 * f4;
        }
    }
}
