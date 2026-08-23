package net.ilexiconn.jurassicraft.client.model.entity;

import net.ilexiconn.jurassicraft.client.model.animation.Animator;
import net.ilexiconn.jurassicraft.client.model.base.MowzieModelBase;
import net.ilexiconn.jurassicraft.client.model.base.MowzieModelRenderer;
import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityDeinotherium;
import net.ilexiconn.jurassicraft.common.entity.reptiles.EntityElasmosaurus;
import net.minecraft.world.entity.Entity;

public class ModelElasmosaurus extends MowzieModelBase {
    public Animator animator;
    public MowzieModelRenderer Body1;
    public MowzieModelRenderer Neck1;
    public MowzieModelRenderer Tail1;
    public MowzieModelRenderer Neck2;
    public MowzieModelRenderer Fin7;
    public MowzieModelRenderer Fin1;
    public MowzieModelRenderer Neck3;
    public MowzieModelRenderer Neck4;
    public MowzieModelRenderer Neck5;
    public MowzieModelRenderer Neck6;
    public MowzieModelRenderer Neck7;
    public MowzieModelRenderer Neck8;
    public MowzieModelRenderer MainHead;
    public MowzieModelRenderer Mouth1;
    public MowzieModelRenderer Mouth2;
    public MowzieModelRenderer Nose1;
    public MowzieModelRenderer Teeth6;
    public MowzieModelRenderer Teeth4;
    public MowzieModelRenderer Teeth1;
    public MowzieModelRenderer Nose2;
    public MowzieModelRenderer Mouth3;
    public MowzieModelRenderer Teeth5;
    public MowzieModelRenderer Teeth3;
    public MowzieModelRenderer Teeth2;
    public MowzieModelRenderer Fin8;
    public MowzieModelRenderer Fin9;
    public MowzieModelRenderer Fin2;
    public MowzieModelRenderer Fin3;
    public MowzieModelRenderer Tail2;
    public MowzieModelRenderer Fin10;
    public MowzieModelRenderer Fin4;
    public MowzieModelRenderer Tail3;
    public MowzieModelRenderer Tail4;
    public MowzieModelRenderer Tail5;
    public MowzieModelRenderer Fin11;
    public MowzieModelRenderer Fin12;
    public MowzieModelRenderer Fin5;
    public MowzieModelRenderer Fin6;

    public MowzieModelRenderer[] neckParts;
    public MowzieModelRenderer[] tailParts;

    public ModelElasmosaurus() {
        this.animator = new Animator(this);
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Body1 = new MowzieModelRenderer(this, 0, 0);
        this.Body1.setRotationPoint(0.0F, 18.01F, 9.0F);
        this.Body1.addBox(-7.5F, -6.0F, -5.0F, 15, 12, 18, 0.0F);
        this.Tail4 = new MowzieModelRenderer(this, 0, 60);
        this.Tail4.setRotationPoint(0.0F, 0.4F, 6.0F);
        this.Tail4.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 7, 0.0F);
        this.setRotateAngle(Tail4, -0.08168140899333462F, 0.0F, 0.0F);
        this.Neck7 = new MowzieModelRenderer(this, 0, 48);
        this.Neck7.setRotationPoint(0.0F, 0.0F, -14.7F);
        this.Neck7.addBox(-1.5F, -1.4F, -8.0F, 3, 4, 8, 0.0F);
        this.setRotateAngle(Neck7, 0.016406094968746697F, 0.0F, 0.0F);
        this.MainHead = new MowzieModelRenderer(this, 102, 0);
        this.MainHead.setRotationPoint(0.0F, -1.5F, -1.9F);
        this.MainHead.addBox(-2.0F, 0.0F, -5.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(MainHead, 0.07068583470577035F, 0.0F, 0.0F);
        this.Fin4 = new MowzieModelRenderer(this, 28, 30);
        this.Fin4.setRotationPoint(-6.4F, 6.9F, 4.6F);
        this.Fin4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.setRotateAngle(Fin4, 0.0F, 0.0F, 1.2747884856566583F);
        this.Fin7 = new MowzieModelRenderer(this, 0, 0);
        this.Fin7.setRotationPoint(6.4F, 3.9F, -6.4F);
        this.Fin7.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.setRotateAngle(Fin7, 0.0F, 0.0F, -1.2747884856566583F);
        this.Fin12 = new MowzieModelRenderer(this, 50, 30);
        this.Fin12.setRotationPoint(-0.03F, 5.6F, 0.3F);
        this.Fin12.addBox(-1.0F, 0.0F, 0.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(Fin12, 0.1884955592153876F, 0.0F, 0.0F);
        this.Fin6 = new MowzieModelRenderer(this, 58, 30);
        this.Fin6.setRotationPoint(0.03F, 5.6F, 0.3F);
        this.Fin6.addBox(0.0F, 0.0F, 0.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(Fin6, 0.1884955592153876F, 0.0F, 0.0F);
        this.Neck5 = new MowzieModelRenderer(this, 25, 33);
        this.Neck5.setRotationPoint(0.0F, -0.4F, -10.7F);
        this.Neck5.addBox(-2.5F, -1.5F, -15.0F, 5, 4, 15, 0.0F);
        this.setRotateAngle(Neck5, -0.046425758103049164F, 0.0F, 0.0F);
        this.Nose1 = new MowzieModelRenderer(this, 66, 18);
        this.Nose1.setRotationPoint(0.0F, -1.3F, 0.0F);
        this.Nose1.addBox(-1.0F, 0.0F, -4.91F, 2, 1, 5, 0.0F);
        this.setRotateAngle(Nose1, 0.26546457922833755F, 0.0F, 0.0F);
        this.Teeth1 = new MowzieModelRenderer(this, 0, 80);
        this.Teeth1.setRotationPoint(-1.5F, 1.0F, -4.9F);
        this.Teeth1.addBox(0.0F, 0.0F, 0.0F, 3, 1, 0, 0.0F);
        this.setRotateAngle(Teeth1, -0.25132741228718347F, 0.0F, 0.0F);
        this.Tail3 = new MowzieModelRenderer(this, 22, 52);
        this.Tail3.setRotationPoint(0.0F, 0.3F, 7.0F);
        this.Tail3.addBox(-4.0F, 0.0F, 0.0F, 8, 7, 6, 0.0F);
        this.setRotateAngle(Tail3, -0.01884955592153876F, 0.0F, 0.0F);
        this.Neck4 = new MowzieModelRenderer(this, 85, 36);
        this.Neck4.setRotationPoint(0.0F, -0.5F, -11.7F);
        this.Neck4.addBox(-3.0F, -2.2F, -11.0F, 6, 5, 11, 0.0F);
        this.setRotateAngle(Neck4, -0.046425758103049164F, 0.0F, 0.0F);
        this.Fin3 = new MowzieModelRenderer(this, 0, 30);
        this.Fin3.setRotationPoint(0.03F, 7.6F, 0.3F);
        this.Fin3.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.setRotateAngle(Fin3, 0.1884955592153876F, 0.0F, 0.0F);
        this.Fin8 = new MowzieModelRenderer(this, 115, 4);
        this.Fin8.setRotationPoint(-0.03F, 2.7F, -0.1F);
        this.Fin8.addBox(-1.0F, 0.0F, 0.0F, 1, 8, 5, 0.0F);
        this.setRotateAngle(Fin8, 0.1884955592153876F, 0.0F, -0.0879645943005142F);
        this.Fin10 = new MowzieModelRenderer(this, 119, 25);
        this.Fin10.setRotationPoint(6.4F, 6.9F, 4.6F);
        this.Fin10.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.setRotateAngle(Fin10, 0.12566370614359174F, 0.0F, -1.2747884856566583F);
        this.Fin9 = new MowzieModelRenderer(this, 100, 28);
        this.Fin9.setRotationPoint(-0.03F, 7.6F, 0.3F);
        this.Fin9.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.setRotateAngle(Fin9, 0.1884955592153876F, 0.0F, 0.0F);
        this.Teeth6 = new MowzieModelRenderer(this, 4, 116);
        this.Teeth6.setRotationPoint(-1.5F, 1.0F, 0.1F);
        this.Teeth6.addBox(0.0F, 0.0F, -5.0F, 0, 1, 5, 0.0F);
        this.setRotateAngle(Teeth6, 0.0F, 0.0F, 0.1884955592153876F);
        this.Mouth1 = new MowzieModelRenderer(this, 0, 6);
        this.Mouth1.setRotationPoint(0.0F, 1.4F, -4.9F);
        this.Mouth1.addBox(-1.5F, 0.0F, -4.9F, 3, 1, 5, 0.0F);
        this.Neck3 = new MowzieModelRenderer(this, 0, 30);
        this.Neck3.setRotationPoint(0.0F, 0.5F, -9.7F);
        this.Neck3.addBox(-4.0F, -3.0F, -12.0F, 8, 6, 12, 0.0F);
        this.setRotateAngle(Neck3, -0.010471975511965976F, 0.0F, 0.0F);
        this.Neck6 = new MowzieModelRenderer(this, 50, 47);
        this.Neck6.setRotationPoint(0.0F, -0.1F, -14.7F);
        this.Neck6.addBox(-2.0F, -1.4F, -15.0F, 4, 4, 15, 0.0F);
        this.setRotateAngle(Neck6, 0.016406094968746697F, 0.0F, 0.0F);
        this.Fin2 = new MowzieModelRenderer(this, 110, 28);
        this.Fin2.setRotationPoint(0.03F, 2.7F, -0.1F);
        this.Fin2.addBox(0.0F, 0.0F, 0.0F, 1, 8, 5, 0.0F);
        this.setRotateAngle(Fin2, 0.1884955592153876F, 0.0F, 0.0879645943005142F);
        this.Teeth5 = new MowzieModelRenderer(this, 0, 109);
        this.Teeth5.setRotationPoint(-1.5F, 0.1F, 0.0F);
        this.Teeth5.addBox(0.0F, -1.0F, -5.0F, 0, 1, 5, 0.0F);
        this.setRotateAngle(Teeth5, 0.0F, 0.0F, -0.1884955592153876F);
        this.Tail1 = new MowzieModelRenderer(this, 80, 10);
        this.Tail1.setRotationPoint(0.0F, -5.5F, 13.0F);
        this.Tail1.addBox(-6.5F, 0.0F, 0.0F, 13, 10, 8, 0.0F);
        this.setRotateAngle(Tail1, -0.06283185307179587F, 0.0F, 0.0F);
        this.Fin1 = new MowzieModelRenderer(this, 8, 0);
        this.Fin1.setRotationPoint(-6.4F, 3.9F, -6.4F);
        this.Fin1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.setRotateAngle(Fin1, 0.0F, 0.0F, 1.2747884856566583F);
        this.Teeth3 = new MowzieModelRenderer(this, 15, 100);
        this.Teeth3.setRotationPoint(1.5F, 0.1F, 0.0F);
        this.Teeth3.addBox(0.0F, -1.0F, -5.0F, 0, 1, 5, 0.0F);
        this.setRotateAngle(Teeth3, 0.0F, 0.0F, 0.1884955592153876F);
        this.Neck1 = new MowzieModelRenderer(this, 48, 0);
        this.Neck1.setRotationPoint(0.0F, -1.8F, -4.0F);
        this.Neck1.addBox(-6.5F, -4.0F, -7.0F, 13, 11, 7, 0.0F);
        this.setRotateAngle(Neck1, 0.06283185307179587F, 0.0F, 0.0F);
        this.Neck2 = new MowzieModelRenderer(this, 56, 28);
        this.Neck2.setRotationPoint(0.0F, -0.8F, -7.0F);
        this.Neck2.addBox(-5.0F, -3.0F, -10.0F, 10, 9, 10, 0.0F);
        this.Mouth2 = new MowzieModelRenderer(this, 0, 12);
        this.Mouth2.setRotationPoint(0.0F, 2.4F, -4.81F);
        this.Mouth2.addBox(-1.5F, 0.0F, -5.0F, 3, 1, 5, 0.0F);
        this.Nose2 = new MowzieModelRenderer(this, 81, 0);
        this.Nose2.setRotationPoint(0.0F, 0.7F, 0.0F);
        this.Nose2.addBox(-1.0F, 0.0F, -1.91F, 2, 1, 2, 0.0F);
        this.Tail2 = new MowzieModelRenderer(this, 88, 52);
        this.Tail2.setRotationPoint(0.0F, 0.3F, 7.7F);
        this.Tail2.addBox(-5.5F, 0.0F, 0.0F, 11, 8, 7, 0.0F);
        this.setRotateAngle(Tail2, 0.01884955592153876F, 0.0F, 0.0F);
        this.Fin11 = new MowzieModelRenderer(this, 117, 48);
        this.Fin11.setRotationPoint(-0.03F, 2.7F, -0.1F);
        this.Fin11.addBox(-1.0F, 0.0F, 0.0F, 1, 6, 4, 0.0F);
        this.setRotateAngle(Fin11, 0.1884955592153876F, 0.0F, 0.0F);
        this.Mouth3 = new MowzieModelRenderer(this, 86, 28);
        this.Mouth3.setRotationPoint(0.0F, 0.5F, 0.1F);
        this.Mouth3.addBox(-1.0F, 0.0F, -5.0F, 2, 1, 5, 0.0F);
        this.setRotateAngle(Mouth3, -0.06283185307179587F, 0.0F, 0.0F);
        this.Neck8 = new MowzieModelRenderer(this, 88, 0);
        this.Neck8.setRotationPoint(0.0F, 0.0F, -9.7F);
        this.Neck8.addBox(-1.5F, -1.4F, -2.0F, 3, 4, 4, 0.0F);
        this.setRotateAngle(Neck8, 0.016406094968746697F, 0.0F, 0.0F);
        this.Teeth4 = new MowzieModelRenderer(this, 0, 100);
        this.Teeth4.setRotationPoint(1.5F, 1.0F, 0.1F);
        this.Teeth4.addBox(0.0F, 0.0F, -5.0F, 0, 1, 5, 0.0F);
        this.setRotateAngle(Teeth4, 0.0F, 0.0F, -0.1884955592153876F);
        this.Tail5 = new MowzieModelRenderer(this, 73, 47);
        this.Tail5.setRotationPoint(0.0F, 0.4F, 7.0F);
        this.Tail5.addBox(-1.5F, 0.0F, 0.0F, 3, 4, 5, 0.0F);
        this.setRotateAngle(Tail5, -0.08168140899333462F, 0.0F, 0.0F);
        this.Teeth2 = new MowzieModelRenderer(this, 0, 90);
        this.Teeth2.setRotationPoint(-1.5F, 0.0F, -5.0F);
        this.Teeth2.addBox(0.0F, -1.0F, 0.0F, 3, 1, 0, 0.0F);
        this.setRotateAngle(Teeth2, 0.25132741228718347F, 0.0F, 0.0F);
        this.Fin5 = new MowzieModelRenderer(this, 50, 52);
        this.Fin5.setRotationPoint(0.03F, 2.7F, -0.1F);
        this.Fin5.addBox(0.0F, 0.0F, 0.0F, 1, 6, 4, 0.0F);
        this.setRotateAngle(Fin5, 0.1884955592153876F, 0.0F, 0.0F);
        this.Tail3.addChild(this.Tail4);
        this.Neck6.addChild(this.Neck7);
        this.Neck8.addChild(this.MainHead);
        this.Tail1.addChild(this.Fin4);
        this.Neck1.addChild(this.Fin7);
        this.Fin11.addChild(this.Fin12);
        this.Fin5.addChild(this.Fin6);
        this.Neck4.addChild(this.Neck5);
        this.Mouth1.addChild(this.Nose1);
        this.Mouth1.addChild(this.Teeth1);
        this.Tail2.addChild(this.Tail3);
        this.Neck3.addChild(this.Neck4);
        this.Fin2.addChild(this.Fin3);
        this.Fin7.addChild(this.Fin8);
        this.Tail1.addChild(this.Fin10);
        this.Fin8.addChild(this.Fin9);
        this.Mouth1.addChild(this.Teeth6);
        this.MainHead.addChild(this.Mouth1);
        this.Neck2.addChild(this.Neck3);
        this.Neck5.addChild(this.Neck6);
        this.Fin1.addChild(this.Fin2);
        this.Mouth2.addChild(this.Teeth5);
        this.Body1.addChild(this.Tail1);
        this.Neck1.addChild(this.Fin1);
        this.Mouth2.addChild(this.Teeth3);
        this.Body1.addChild(this.Neck1);
        this.Neck1.addChild(this.Neck2);
        this.MainHead.addChild(this.Mouth2);
        this.Nose1.addChild(this.Nose2);
        this.Tail1.addChild(this.Tail2);
        this.Fin10.addChild(this.Fin11);
        this.Mouth2.addChild(this.Mouth3);
        this.Neck7.addChild(this.Neck8);
        this.Mouth1.addChild(this.Teeth4);
        this.Tail4.addChild(this.Tail5);
        this.Mouth2.addChild(this.Teeth2);
        this.Fin4.addChild(this.Fin5);

        neckParts = new MowzieModelRenderer[]{Neck1, Neck2, Neck3, Neck4, Neck5, Neck6, Neck7, Neck8, MainHead};
        tailParts = new MowzieModelRenderer[]{Tail1, Tail2, Tail3, Tail4, Tail5};

        Body1.updateDefaultPose();
        Neck1.updateDefaultPose();
        Neck2.updateDefaultPose();
        Neck3.updateDefaultPose();
        Neck4.updateDefaultPose();
        Neck5.updateDefaultPose();
        Neck6.updateDefaultPose();
        Neck7.updateDefaultPose();
        Neck8.updateDefaultPose();
        MainHead.updateDefaultPose();
        Mouth1.updateDefaultPose();
        Mouth2.updateDefaultPose();
        Mouth3.updateDefaultPose();
        Nose1.updateDefaultPose();
        Nose2.updateDefaultPose();
        Teeth1.updateDefaultPose();
        Teeth2.updateDefaultPose();
        Teeth3.updateDefaultPose();
        Teeth4.updateDefaultPose();
        Teeth5.updateDefaultPose();
        Teeth6.updateDefaultPose();
        Tail1.updateDefaultPose();
        Tail2.updateDefaultPose();
        Tail3.updateDefaultPose();
        Tail4.updateDefaultPose();
        Tail5.updateDefaultPose();
        Fin1.updateDefaultPose();
        Fin2.updateDefaultPose();
        Fin3.updateDefaultPose();
        Fin4.updateDefaultPose();
        Fin5.updateDefaultPose();
        Fin6.updateDefaultPose();
        Fin7.updateDefaultPose();
        Fin8.updateDefaultPose();
        Fin9.updateDefaultPose();
        Fin10.updateDefaultPose();
        Fin11.updateDefaultPose();
        Fin12.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((EntityElasmosaurus) entity, f, f1, f2, f3, f4, f5);
        this.Body1.render(f5);
    }

    public void resetPose() {
        Body1.resetToDefaultPose();
        Neck1.resetToDefaultPose();
        Neck2.resetToDefaultPose();
        Neck3.resetToDefaultPose();
        Neck4.resetToDefaultPose();
        Neck5.resetToDefaultPose();
        Neck6.resetToDefaultPose();
        Neck7.resetToDefaultPose();
        Neck8.resetToDefaultPose();
        MainHead.resetToDefaultPose();
        Mouth1.resetToDefaultPose();
        Mouth2.resetToDefaultPose();
        Mouth3.resetToDefaultPose();
        Nose1.resetToDefaultPose();
        Nose2.resetToDefaultPose();
        Teeth1.resetToDefaultPose();
        Teeth2.resetToDefaultPose();
        Teeth3.resetToDefaultPose();
        Teeth4.resetToDefaultPose();
        Teeth5.resetToDefaultPose();
        Teeth6.resetToDefaultPose();
        Tail1.resetToDefaultPose();
        Tail2.resetToDefaultPose();
        Tail3.resetToDefaultPose();
        Tail4.resetToDefaultPose();
        Tail5.resetToDefaultPose();
        Fin1.resetToDefaultPose();
        Fin2.resetToDefaultPose();
        Fin3.resetToDefaultPose();
        Fin4.resetToDefaultPose();
        Fin5.resetToDefaultPose();
        Fin6.resetToDefaultPose();
        Fin7.resetToDefaultPose();
        Fin8.resetToDefaultPose();
        Fin9.resetToDefaultPose();
        Fin10.resetToDefaultPose();
        Fin11.resetToDefaultPose();
        Fin12.resetToDefaultPose();
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityElasmosaurus elasmosaurus) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, elasmosaurus);
        resetPose();

        float scaleFactor = 0.65F;
        float swimAmount = f1;
        boolean inWater = elasmosaurus.isInWater();

        walk(Body1, scaleFactor * 0.7F, 0.16F, false, 0F, 0.0F, f, swimAmount);

        if(inWater) {
            chainWave(neckParts, scaleFactor * 0.6F, 0.11F, 3, f, swimAmount);
            chainWave(tailParts, scaleFactor * 0.7F, 0.14F, 2, f, swimAmount);
        } else {
            chainWave(neckParts, scaleFactor * 0.6F, 0.11F, 3, f, 0F);
            chainWave(tailParts, scaleFactor * 0.7F, 0.14F, 2, f, 0F);
        }

        float finCycle = f * scaleFactor * 0.9F;
        walk(Fin1, scaleFactor * 0.9F, 0.26F, false, 0F, 0.15F, f, swimAmount);
        walk(Fin7, scaleFactor * 0.9F, 0.26F, true, 0F, 0.15F, f, swimAmount);
        walk(Fin4, scaleFactor * 0.9F, 0.22F, true, 0F, -0.15F, f, swimAmount);
        walk(Fin10, scaleFactor * 0.9F, 0.22F, false, 0F, -0.15F, f, swimAmount);

        Fin1.rotateAngleZ += (float) Math.sin(finCycle) * 0.20F * swimAmount;
        Fin7.rotateAngleZ -= (float) Math.sin(finCycle) * 0.20F * swimAmount;
        Fin4.rotateAngleZ -= (float) Math.sin(finCycle) * 0.17F * swimAmount;
        Fin10.rotateAngleZ += (float) Math.sin(finCycle) * 0.17F * swimAmount;

        if (swimAmount < 0.01F && inWater) {
            Fin1.rotateAngleZ += (float) Math.sin(elasmosaurus.frame * 0.12F) * 0.10F;
            Fin7.rotateAngleZ -= (float) Math.sin(elasmosaurus.frame * 0.12F) * 0.10F;
            Fin4.rotateAngleZ -= (float) Math.sin(elasmosaurus.frame * 0.12F) * 0.08F;
            Fin10.rotateAngleZ += (float) Math.sin(elasmosaurus.frame * 0.12F) * 0.08F;

            chainWave(neckParts, 0.12F, 0.05F, 4, elasmosaurus.frame, 1.0F);
            chainWave(tailParts, 0.10F, 0.04F, 3, elasmosaurus.frame, 1.0F);
        }

        elasmosaurus.tailBuffer.applyChainSwingBuffer(tailParts);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityElasmosaurus) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, (EntityElasmosaurus) entityIn);
        } else {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        }
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animator.update(entity);
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityElasmosaurus) entity);
    }

    public void setRotateAngle(MowzieModelRenderer MowzieModelRenderer, float x, float y, float z) {
        MowzieModelRenderer.rotateAngleX = x;
        MowzieModelRenderer.rotateAngleY = y;
        MowzieModelRenderer.rotateAngleZ = z;
    }
}
