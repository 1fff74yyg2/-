package net.ilexiconn.jurassicraft.client.model.block;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.world.entity.Entity;

public class ModelLowSecurityFencePole extends AdvancedModelBase {
    AdvancedModelRenderer side1;
    AdvancedModelRenderer luz;
    AdvancedModelRenderer luz1;

    public ModelLowSecurityFencePole() {
        side1 = new AdvancedModelRenderer(this, 2, 31);
        side1.setTextureSize(128, 64);
        side1.addBox(-3F, -8F, -3F, 6, 16, 6);
        side1.setRotationPoint(0F, 16F, 0F);

        luz = new AdvancedModelRenderer(this, 50, 17);
        luz.setTextureSize(128, 64);
        luz.addBox(-1F, -1F, -1F, 2, 2, 2);
        luz.setRotationPoint(-2F, 8F, 0F);

        luz1 = new AdvancedModelRenderer(this, 65, 9);
        luz1.setTextureSize(128, 64);
        luz1.addBox(-1F, -1F, -1F, 2, 2, 2);
        luz1.setRotationPoint(2F, 8F, 0F);
    }

    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float partialTicks) {
        side1.renderWithRotation(partialTicks);
        luz.renderWithRotation(partialTicks);
        luz1.renderWithRotation(partialTicks);
    }
}