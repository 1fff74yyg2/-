package net.ilexiconn.jurassicraft.client.model.base;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

/**
 * Reconstructed from the compiled classes in JurassiCraft-1.4.0-1_7_10-FIXED.jar
 * (this source snapshot was missing the file). Adapted to 1.20.1: AdvancedModelRenderer
 * is now the vendored llibrary-compatible class.
 */
public class MowzieModelRenderer extends AdvancedModelRenderer {
    public MowzieModelRenderer(AdvancedModelBase model, String boxName) {
        super(model, boxName);
    }

    public MowzieModelRenderer(AdvancedModelBase model) {
        super(model);
    }

    public MowzieModelRenderer(AdvancedModelBase model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public void setRotationAngles(float x, float y, float z) {
        this.rotateAngleX = x;
        this.rotateAngleY = y;
        this.rotateAngleZ = z;
    }
}
