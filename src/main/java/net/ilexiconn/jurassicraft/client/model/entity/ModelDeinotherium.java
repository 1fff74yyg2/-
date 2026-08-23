package net.ilexiconn.jurassicraft.client.model.entity;

import net.ilexiconn.jurassicraft.client.model.animation.Animator;
import net.ilexiconn.jurassicraft.client.model.base.MowzieModelBase;
import net.ilexiconn.jurassicraft.client.model.base.MowzieModelRenderer;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityDeinotherium;
import net.minecraft.world.entity.Entity;

public class ModelDeinotherium extends MowzieModelBase {
   public MowzieModelRenderer Body2;
   public MowzieModelRenderer Leg4;
   public MowzieModelRenderer Leg3;
   public MowzieModelRenderer Body3;
   public MowzieModelRenderer WaistChild;
   public MowzieModelRenderer Body1;
   public MowzieModelRenderer WaistChildChild;
   public MowzieModelRenderer Leg1;
   public MowzieModelRenderer Leg2;
   public MowzieModelRenderer G;
   public MowzieModelRenderer WaistChildChild_1;
   public MowzieModelRenderer Left_Ear;
   public MowzieModelRenderer Left_Ear_1;
   public MowzieModelRenderer WaistChildChild_2;
   public MowzieModelRenderer Lower_Jaw_2;
   public MowzieModelRenderer WaistChildChild_3;
   public MowzieModelRenderer WaistChildChild_4;
   public MowzieModelRenderer WaistChildChild_5;
   public MowzieModelRenderer WaistChildChild_6;
   public MowzieModelRenderer Lowerjawfront;
   public MowzieModelRenderer Lowerlip;
   public MowzieModelRenderer Lowerlip_1;
   public MowzieModelRenderer topcresthorn;
   public MowzieModelRenderer topcresthorn_1;
   public MowzieModelRenderer Th1;
   public MowzieModelRenderer Th2;
   public MowzieModelRenderer Th3;
   public MowzieModelRenderer Th4;
   public MowzieModelRenderer Th5;
   public MowzieModelRenderer Th6;
   public MowzieModelRenderer WaistChild_1;
   public MowzieModelRenderer Th9;
   public MowzieModelRenderer Th10;
   public MowzieModelRenderer Th7;
   public MowzieModelRenderer Th8;

   public MowzieModelRenderer[] legFrontLeftParts;
   public MowzieModelRenderer[] legFrontRightParts;
   public MowzieModelRenderer[] legBackLeftParts;
   public MowzieModelRenderer[] legBackRightParts;

   private Animator animator;

   public ModelDeinotherium() {
      this.animator = new Animator(this);
      this.textureWidth = 256;
      this.textureHeight = 128;

      this.Th7 = new MowzieModelRenderer(this, 52, 67);
      this.Th7.setRotationPoint(-2.0F, 13.5F, 0.0F);
      this.Th7.addBox(-3.5F, -2.0F, -2.5F, 7, 14, 8, 0.0F);
      this.setRotateAngle(Th7, -0.020420352248333655F, 0.0F, 0.0F);

      this.Lower_Jaw_2 = new MowzieModelRenderer(this, 226, 21);
      this.Lower_Jaw_2.setRotationPoint(0.0F, 8.7F, -3.8F);
      this.Lower_Jaw_2.addBox(-4.0F, 0.0F, -3.0F, 8, 3, 4, 0.0F);

      this.Left_Ear = new MowzieModelRenderer(this, 45, 0);
      this.Left_Ear.setRotationPoint(4.2F, 1.0F, -7.0F);
      this.Left_Ear.addBox(-1.0F, 0.0F, 0.0F, 1, 8, 6, 0.0F);
      this.setRotateAngle(Left_Ear, 0.20943951023931953F, 0.6108652381980153F, 0.0F);

      this.Th4 = new MowzieModelRenderer(this, 0, 51);
      this.Th4.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.Th4.addBox(-3.5F, -2.0F, -4.0F, 7, 15, 8, 0.0F);
      this.setRotateAngle(Th4, -0.08482300164692443F, 0.0F, 0.0F);

      this.Th9 = new MowzieModelRenderer(this, 213, 55);
      this.Th9.setRotationPoint(2.0F, 13.5F, 0.0F);
      this.Th9.addBox(-3.5F, -2.0F, -2.5F, 7, 14, 8, 0.0F);
      this.setRotateAngle(Th9, -0.020420352248333655F, 0.0F, 0.0F);

      this.Th1 = new MowzieModelRenderer(this, 64, 44);
      this.Th1.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.Th1.addBox(-3.5F, -2.0F, -4.0F, 7, 15, 8, 0.0F);
      this.setRotateAngle(Th1, -0.08482300164692443F, 0.0F, 0.0F);

      this.WaistChild_1 = new MowzieModelRenderer(this, 203, 0);
      this.WaistChild_1.setRotationPoint(0.0F, 12.5F, 1.4F);
      this.WaistChild_1.addBox(-0.5F, 0.0F, -2.0F, 2, 3, 2, 0.0F);
      this.setRotateAngle(WaistChild_1, -0.25132741228718347F, 0.0F, 0.0F);

      this.WaistChildChild_6 = new MowzieModelRenderer(this, 0, 9);
      this.WaistChildChild_6.setRotationPoint(0.0F, 1.0F, -5.0F);
      this.WaistChildChild_6.addBox(-1.5F, 0.0F, -5.0F, 3, 3, 5, 0.0F);
      this.setRotateAngle(WaistChildChild_6, 0.3687880709464018F, 0.0F, 0.0F);

      this.Body3 = new MowzieModelRenderer(this, 138, 0);
      this.Body3.setRotationPoint(0.0F, -9.5F, -10.0F);
      this.Body3.addBox(-8.0F, 0.0F, -18.1F, 16, 8, 20, 0.0F);
      this.setRotateAngle(Body3, 0.16406094968746698F, 0.0F, 0.0F);

      this.WaistChildChild_2 = new MowzieModelRenderer(this, 100, 30);
      this.WaistChildChild_2.setRotationPoint(0.0F, 0.9F, -6.0F);
      this.WaistChildChild_2.addBox(-5.0F, 0.0F, -6.0F, 10, 8, 6, 0.0F);
      this.setRotateAngle(WaistChildChild_2, 0.11746065865921837F, 0.0F, 0.0F);

      this.Body2 = new MowzieModelRenderer(this, 0, 0);
      this.Body2.setRotationPoint(0.0F, -6.7F, 13.0F);
      this.Body2.addBox(-7.0F, -9.0F, -10.5F, 14, 7, 17, 0.0F);
      this.setRotateAngle(Body2, -0.22636920398366453F, 0.0F, 0.0F);

      this.Th10 = new MowzieModelRenderer(this, 30, 62);
      this.Th10.setRotationPoint(0.0F, 11.92F, 2.0F);
      this.Th10.addBox(-3.5F, -0.5F, -4.5F, 7, 5, 8, 0.0F);
      this.setRotateAngle(Th10, 0.06283185307179587F, 0.0F, 0.0F);

      this.WaistChildChild_3 = new MowzieModelRenderer(this, 174, 40);
      this.WaistChildChild_3.setRotationPoint(0.0F, 1.2F, -6.0F);
      this.WaistChildChild_3.addBox(-3.0F, 0.0F, -6.0F, 6, 6, 8, 0.0F);
      this.setRotateAngle(WaistChildChild_3, 0.49445177708999355F, 0.0F, 0.0F);

      this.Lowerlip_1 = new MowzieModelRenderer(this, 140, 89);
      this.Lowerlip_1.setRotationPoint(0.0F, 0.0F, -2.0F);
      this.Lowerlip_1.addBox(-3.0F, 0.0F, -3.8F, 6, 2, 4, 0.0F);
      this.setRotateAngle(Lowerlip_1, 0.7403686686959946F, 0.0F, 0.0F);

      this.Lowerjawfront = new MowzieModelRenderer(this, 174, 28);
      this.Lowerjawfront.setRotationPoint(0.0F, -0.1F, -3.1F);
      this.Lowerjawfront.addBox(-4.0F, 0.0F, -4.0F, 8, 3, 4, 0.0F);
      this.setRotateAngle(Lowerjawfront, 0.1324704902263696F, 0.0F, 0.0F);

      this.Body1 = new MowzieModelRenderer(this, 104, 28);
      this.Body1.setRotationPoint(0.0F, -1.9F, -28.3F);
      this.Body1.addBox(-9.0F, 0.0F, 0.0F, 18, 19, 34, 0.0F);
      this.setRotateAngle(Body1, 0.17034413499464657F, 0.0F, 0.0F);

      this.Leg2 = new MowzieModelRenderer(this, 0, 24);
      this.Leg2.setRotationPoint(6.5F, 10.2F, -16.5F);
      this.Leg2.addBox(-4.0F, -3.0F, -5.0F, 8, 17, 10, 0.0F);
      this.setRotateAngle(Leg2, 0.07731808586334879F, 0.0F, 0.0F);

      this.WaistChildChild_1 = new MowzieModelRenderer(this, 72, 30);
      this.WaistChildChild_1.setRotationPoint(0.0F, 0.0F, -6.0F);
      this.WaistChildChild_1.addBox(-4.5F, 0.0F, -7.0F, 9, 9, 5, 0.0F);
      this.setRotateAngle(WaistChildChild_1, 0.11746065865921837F, 0.0F, 0.0F);

      this.G = new MowzieModelRenderer(this, 36, 30);
      this.G.setRotationPoint(0.0F, 0.0F, -5.0F);
      this.G.addBox(-5.0F, 0.0F, -8.0F, 10, 13, 8, 0.0F);
      this.setRotateAngle(G, 0.11746065865921837F, 0.0F, 0.0F);

      this.WaistChildChild = new MowzieModelRenderer(this, 210, 0);
      this.WaistChildChild.setRotationPoint(0.0F, 0.0F, -16.0F);
      this.WaistChildChild.addBox(-6.0F, 0.0F, -7.0F, 12, 15, 6, 0.0F);
      this.setRotateAngle(WaistChildChild, -0.10873401239924674F, 0.0F, 0.0F);

      this.Leg1 = new MowzieModelRenderer(this, 200, 21);
      this.Leg1.setRotationPoint(-6.5F, 10.2F, -16.5F);
      this.Leg1.addBox(-4.0F, -3.0F, -5.0F, 8, 17, 10, 0.0F);
      this.setRotateAngle(Leg1, 0.07731808586334879F, 0.0F, 0.0F);

      this.Th2 = new MowzieModelRenderer(this, 229, 41);
      this.Th2.setRotationPoint(0.0F, 13.5F, 0.0F);
      this.Th2.addBox(-3.0F, -1.0F, -3.5F, 6, 7, 7, 0.0F);
      this.setRotateAngle(Th2, 0.04241150082346221F, 0.0F, 0.0F);

      this.Lowerlip = new MowzieModelRenderer(this, 53, 0);
      this.Lowerlip.setRotationPoint(0.0F, 0.2F, -3.9F);
      this.Lowerlip.addBox(-3.5F, 0.0F, -2.0F, 7, 3, 2, 0.0F);
      this.setRotateAngle(Lowerlip, 0.48904125640881113F, 0.0F, 0.0F);

      this.topcresthorn = new MowzieModelRenderer(this, 4, 0);
      this.topcresthorn.setRotationPoint(-0.8F, 0.1F, -3.4F);
      this.topcresthorn.addBox(-1.0F, -1.0F, 0.0F, 1, 1, 5, 0.0F);
      this.setRotateAngle(topcresthorn, -2.658136450787364F, 0.0F, 0.0F);

      this.Th8 = new MowzieModelRenderer(this, 82, 73);
      this.Th8.setRotationPoint(0.0F, 11.92F, 2.0F);
      this.Th8.addBox(-3.5F, -0.5F, -4.5F, 7, 5, 8, 0.0F);
      this.setRotateAngle(Th8, 0.06283185307179587F, 0.0F, 0.0F);

      this.Th5 = new MowzieModelRenderer(this, 195, 48);
      this.Th5.setRotationPoint(0.0F, 13.5F, 0.0F);
      this.Th5.addBox(-3.0F, -1.0F, -3.5F, 6, 7, 7, 0.0F);
      this.setRotateAngle(Th5, 0.04241150082346221F, 0.0F, 0.0F);

      this.Left_Ear_1 = new MowzieModelRenderer(this, 138, 0);
      this.Left_Ear_1.setRotationPoint(-4.2F, 1.0F, -7.0F);
      this.Left_Ear_1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 6, 0.0F);
      this.setRotateAngle(Left_Ear_1, 0.20943951023931953F, -0.6108652381980153F, 0.0F);

      this.Leg3 = new MowzieModelRenderer(this, 100, 0);
      this.Leg3.setRotationPoint(-3.7F, -6.0F, 13.5F);
      this.Leg3.addBox(-6.0F, -3.0F, -4.0F, 8, 19, 11, 0.0F);
      this.setRotateAngle(Leg3, -0.04241150082346221F, 0.0F, 0.0F);

      this.WaistChild = new MowzieModelRenderer(this, 0, 0);
      this.WaistChild.setRotationPoint(-0.5F, -6.0F, 5.4F);
      this.WaistChild.addBox(0.0F, 0.0F, 0.0F, 1, 13, 1, 0.0F);
      this.setRotateAngle(WaistChild, 0.5536184387326013F, 0.0F, 0.0F);

      this.WaistChildChild_4 = new MowzieModelRenderer(this, 89, 0);
      this.WaistChildChild_4.setRotationPoint(0.0F, 1.0F, -6.0F);
      this.WaistChildChild_4.addBox(-2.5F, 0.0F, -5.0F, 5, 5, 5, 0.0F);
      this.setRotateAngle(WaistChildChild_4, 0.3687880709464018F, 0.0F, 0.0F);

      this.Th6 = new MowzieModelRenderer(this, 30, 51);
      this.Th6.setRotationPoint(0.0F, 6.1F, 0.0F);
      this.Th6.addBox(-3.0F, -0.5F, -3.5F, 6, 4, 7, 0.0F);
      this.setRotateAngle(Th6, 0.028623399732707F, 0.0F, 0.0F);

      this.WaistChildChild_5 = new MowzieModelRenderer(this, 190, 0);
      this.WaistChildChild_5.setRotationPoint(0.0F, 1.0F, -5.0F);
      this.WaistChildChild_5.addBox(-2.0F, 0.0F, -5.0F, 4, 4, 5, 0.0F);
      this.setRotateAngle(WaistChildChild_5, 0.3687880709464018F, 0.0F, 0.0F);

      this.Leg4 = new MowzieModelRenderer(this, 62, 0);
      this.Leg4.setRotationPoint(3.7F, -6.0F, 13.5F);
      this.Leg4.addBox(-2.0F, -3.0F, -4.0F, 8, 19, 11, 0.0F);
      this.setRotateAngle(Leg4, -0.04241150082346221F, 0.0F, 0.0F);

      this.topcresthorn_1 = new MowzieModelRenderer(this, 146, 0);
      this.topcresthorn_1.setRotationPoint(1.8F, 0.1F, -3.4F);
      this.topcresthorn_1.addBox(-1.0F, -1.0F, 0.0F, 1, 1, 5, 0.0F);
      this.setRotateAngle(topcresthorn_1, -2.658136450787364F, 0.0F, 0.0F);

      this.Th3 = new MowzieModelRenderer(this, 94, 44);
      this.Th3.setRotationPoint(0.0F, 6.1F, 0.0F);
      this.Th3.addBox(-3.0F, -0.5F, -3.5F, 6, 4, 7, 0.0F);
      this.setRotateAngle(Th3, 0.028623399732707F, 0.0F, 0.0F);

      this.Leg3.addChild(this.Th7);
      this.WaistChildChild_1.addChild(this.Lower_Jaw_2);
      this.G.addChild(this.Left_Ear);
      this.Leg2.addChild(this.Th4);
      this.Leg4.addChild(this.Th9);
      this.Leg1.addChild(this.Th1);
      this.WaistChild.addChild(this.WaistChild_1);
      this.WaistChildChild_5.addChild(this.WaistChildChild_6);
      this.Body2.addChild(this.Body3);
      this.WaistChildChild_1.addChild(this.WaistChildChild_2);
      this.Th9.addChild(this.Th10);
      this.WaistChildChild_2.addChild(this.WaistChildChild_3);
      this.Lowerlip.addChild(this.Lowerlip_1);
      this.Lower_Jaw_2.addChild(this.Lowerjawfront);
      this.Body2.addChild(this.Body1);
      this.Body3.addChild(this.Leg2);
      this.G.addChild(this.WaistChildChild_1);
      this.WaistChildChild.addChild(this.G);
      this.Body3.addChild(this.WaistChildChild);
      this.Body3.addChild(this.Leg1);
      this.Th1.addChild(this.Th2);
      this.Lowerjawfront.addChild(this.Lowerlip);
      this.Lowerlip_1.addChild(this.topcresthorn);
      this.Th7.addChild(this.Th8);
      this.Th4.addChild(this.Th5);
      this.G.addChild(this.Left_Ear_1);
      this.Body2.addChild(this.WaistChild);
      this.WaistChildChild_3.addChild(this.WaistChildChild_4);
      this.Th5.addChild(this.Th6);
      this.WaistChildChild_4.addChild(this.WaistChildChild_5);
      this.Lowerlip_1.addChild(this.topcresthorn_1);
      this.Th2.addChild(this.Th3);

      legBackLeftParts = new MowzieModelRenderer[]{Th8, Th7, Leg3};
      legBackRightParts = new MowzieModelRenderer[]{Th10, Th9, Leg4};
      legFrontLeftParts = new MowzieModelRenderer[]{Th3, Th2, Th1, Leg1};
      legFrontRightParts = new MowzieModelRenderer[]{Th6, Th5, Th4, Leg2};

      this.updateDefaultPose();
   }

   @Override
   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.animate(f, f1, f2, f3, f4, f5, (EntityDeinotherium) entity);
      this.Body2.render(f5);
      this.Leg3.render(f5);
      this.Leg4.render(f5);
   }

   private void setRotateAngle(MowzieModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   @Override
   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      resetToDefaultPose();

      EntityDeinotherium deinotherium = (EntityDeinotherium) entityIn;

      float globalSpeed = 0.45F;
      float globalDegree = 1.0F;
      float height = 1.2F;
      float frontOffset = -2.2F;

      //修复：角度转弧度，解决WaistChildChild/G头部疯狂旋转
      float headYawRad = netHeadYaw * 0.017453292519943295F;
      float headPitchRad = headPitch * 0.017453292519943295F;

      G.rotateAngleY += headYawRad / 2.2F;
      WaistChildChild.rotateAngleY += headYawRad / 2.2F;
      G.rotateAngleX += headPitchRad * 0.45F;
      WaistChildChild.rotateAngleX += headPitchRad * 0.3F;

      this.bob(this.Body2, 2 * globalSpeed, height, false, limbSwing, limbSwingAmount);
      this.bob(this.Leg3, 2 * globalSpeed, height, false, limbSwing, limbSwingAmount);
      this.bob(this.Leg4, 2 * globalSpeed, height, false, limbSwing, limbSwingAmount);

      this.walk(this.Body2, 2 * globalSpeed, 0.11F * height, true, -1.4F, 0.05F, limbSwing, limbSwingAmount);
      this.walk(this.WaistChild, 2 * globalSpeed, 0.22F * height, false, -0.25F, 0F, limbSwing, limbSwingAmount);
      this.walk(this.WaistChildChild, 2 * globalSpeed, 0.07F * height, false, 0F, -0.11F, limbSwing, limbSwingAmount);
      this.walk(this.G, 2 * globalSpeed, 0.05F * height, false, 0F, -0.09F, limbSwing, limbSwingAmount);

      this.walk(this.Left_Ear, 2 * globalSpeed, 0.21F * height, false, 2.2F, 0F, limbSwing, limbSwingAmount);
      this.walk(this.Left_Ear_1, 2 * globalSpeed, 0.21F * height, false, 2.2F, 0F, limbSwing, limbSwingAmount);

      this.walk(this.Leg3, 1F * globalSpeed, 0.62F * globalDegree, false, 0F, 0F, limbSwing, limbSwingAmount);
      this.walk(this.Th7, 1F * globalSpeed, 0.62F * globalDegree, true, 1.1F, 0F, limbSwing, limbSwingAmount);
      this.walk(this.Th8, 1F * globalSpeed, 0.62F * globalDegree, false, -1.6F, 1.1F, limbSwing, limbSwingAmount);

      this.walk(this.Leg4, 1F * globalSpeed, 0.62F * globalDegree, true, 0F, 0F, limbSwing, limbSwingAmount);
      this.walk(this.Th9, 1F * globalSpeed, 0.62F * globalDegree, false, 1.1F, 0F, limbSwing, limbSwingAmount);
      this.walk(this.Th10, 1F * globalSpeed, 0.62F * globalDegree, true, -1.6F, 1.1F, limbSwing, limbSwingAmount);

      this.walk(this.Leg2, 1F * globalSpeed, 0.42F * globalDegree, true, frontOffset + 0F, -0.16F, limbSwing, limbSwingAmount);
      this.walk(this.Th4, 1F * globalSpeed, 0.62F * globalDegree, true, frontOffset + 1.1F, -0.21F, limbSwing, limbSwingAmount);
      this.walk(this.Th5, 1F * globalSpeed, 0.62F * globalDegree, false, frontOffset + 2.1F, 0.85F, limbSwing, limbSwingAmount);

      this.walk(this.Leg1, 1F * globalSpeed, 0.42F * globalDegree, false, frontOffset + 0F, -0.16F, limbSwing, limbSwingAmount);
      this.walk(this.Th1, 1F * globalSpeed, 0.62F * globalDegree, false, frontOffset + 1.1F, -0.21F, limbSwing, limbSwingAmount);
      this.walk(this.Th2, 1F * globalSpeed, 0.62F * globalDegree, true, frontOffset + 2.1F, 0.85F, limbSwing, limbSwingAmount);

      chainWave(legBackLeftParts, 2F * globalSpeed, -0.065F, 2, limbSwing, limbSwingAmount);
      chainWave(legBackRightParts, 2F * globalSpeed, -0.065F, 2, limbSwing, limbSwingAmount);

      this.walk(this.WaistChildChild, 0.1F, 0.042F, false, -1.1F, 0F, deinotherium.frame, 1F);
      this.walk(this.G, 0.1F, 0.072F, true, 0F, 0F, deinotherium.frame, 1F);
      this.walk(this.Body2, 0.1F, 0.027F, false, 0F, 0F, deinotherium.frame, 1F);

      float inverseKinematicsConstant = 0.31F;
      this.walk(this.Leg2, 0.1F, 0.11F * inverseKinematicsConstant, false, 0F, 0F, deinotherium.frame, 1F);
      this.walk(this.Th4, 0.1F, 0.31F * inverseKinematicsConstant, true, 0F, 0F, deinotherium.frame, 1F);
      this.walk(this.Th5, 0.1F, 0.18F * inverseKinematicsConstant, false, 0F, 0F, deinotherium.frame, 1F);
      this.walk(this.Leg1, 0.1F, 0.11F * inverseKinematicsConstant, false, 0F, 0F, deinotherium.frame, 1F);
      this.walk(this.Th1, 0.1F, 0.31F * inverseKinematicsConstant, true, 0F, 0F, deinotherium.frame, 1F);
      this.walk(this.Th2, 0.1F, 0.18F * inverseKinematicsConstant, false, 0F, 0F, deinotherium.frame, 1F);

      this.walk(this.Left_Ear, 0.1F, 0.11F, false, 2.1F, 0F, deinotherium.frame, 1F);
      this.walk(this.Left_Ear_1, 0.1F, 0.11F, false, 2.1F, 0F, deinotherium.frame, 1F);
      this.walk(this.WaistChild, 0.1F, 0.055F, false, -1.3F, 0F, deinotherium.frame, 1F);
   }

   public void animate(float f, float f1, float f2, float f3, float f4, float f5, EntityDeinotherium deinotherium) {
      this.animator.update(deinotherium);
      setRotationAngles(f, f1, f2, f3, f4, f5, deinotherium);

      if (deinotherium.getAnimationId() == JurassiCraftAnimationIDs.BITE.animID()) {
         this.animator.setAnimation(JurassiCraftAnimationIDs.BITE.animID());
         this.animator.startPhase(6);
         animator.rotate(Body2, 0.31F, 0.31F, 0);
         animator.rotate(Leg2, 0.31F, 0, 0);
         animator.rotate(Leg1, 0.31F, 0, 0);
         animator.rotate(Th4, -0.81F, 0, 0);
         animator.rotate(Th1, -0.81F, 0, 0);
         animator.rotate(Th5, 0.31F, 0, 0);
         animator.rotate(Th2, 0.31F, 0, 0);
         animator.rotate(WaistChildChild, 0.31F, 0.31F, 0);
         animator.rotate(G, 0.11F, 0, 0);
         animator.endPhase();
         animator.setStationaryPhase(2);
         this.animator.startPhase(4);
         animator.rotate(Body2, 0, -0.51F, 0);
         animator.rotate(WaistChildChild, -0.31F, -0.31F, 0);
         animator.rotate(G, -0.11F, 0, 0);
         animator.endPhase();
         animator.setStationaryPhase(4);
         this.animator.resetPhase(8);
      }
   }
}
