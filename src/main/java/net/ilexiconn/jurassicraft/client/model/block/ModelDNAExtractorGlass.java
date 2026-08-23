package net.ilexiconn.jurassicraft.client.model.block;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.world.entity.Entity;

public class ModelDNAExtractorGlass extends AdvancedModelBase {
    AdvancedModelRenderer glass1;
    AdvancedModelRenderer glass2;
    AdvancedModelRenderer glass3;

    public ModelDNAExtractorGlass() {
        textureWidth = 64;
        textureHeight = 64;

        glass1 = new AdvancedModelRenderer(this, 32, 20);
        glass1.addBox(-1F, -14.5F, -6F, 7, 1, 7);
        glass1.setRotationPoint(0F, 24F, 0F);
        glass1.setTextureSize(64, 32);
        glass1.mirror = true;

        glass2 = new AdvancedModelRenderer(this, 28, 42);
        glass2.addBox(5.5F, -14F, -6F, 1, 11, 7);
        glass2.setRotationPoint(0F, 24F, 0F);
        glass2.setTextureSize(64, 32);
        glass2.mirror = true;

        glass3 = new AdvancedModelRenderer(this, 28, 42);
        glass3.addBox(5.5F, -14F, -1F, 1, 11, 7);
        glass3.setRotationPoint(0F, 24F, 0F);
        glass3.setTextureSize(64, 32);
        glass3.mirror = true;
        setRotation(glass3, 0F, 1.570796F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        glass1.render(f5);
        glass2.render(f5);
        glass3.render(f5);
    }

    private void setRotation(AdvancedModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
