package net.ilexiconn.jurassicraft.client.model.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.world.entity.Entity;

public class ModelCoelacanthEgg extends AdvancedModelBase {
    // fields
    AdvancedModelRenderer Shape1;
    AdvancedModelRenderer Shape2;

    public ModelCoelacanthEgg() {
        textureWidth = 32;
        textureHeight = 32;

        Shape1 = new AdvancedModelRenderer(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape1.setRotationPoint(1F, 22F, 1F);
        Shape1.setTextureSize(32, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new AdvancedModelRenderer(this, 0, 0);
        Shape2.addBox(0F, 0F, 0F, 3, 3, 3);
        Shape2.setRotationPoint(0F, 21F, 0F);
        Shape2.setTextureSize(32, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Shape1.render(f5);
        Shape2.render(f5);
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
