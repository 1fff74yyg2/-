package net.ilexiconn.jurassicraft.client.model.entity;

import net.ilexiconn.jurassicraft.client.model.animation.Animator;
import net.ilexiconn.jurassicraft.client.model.base.MowzieModelBase;
import net.ilexiconn.jurassicraft.client.model.base.MowzieModelRenderer;
import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.minecraft.world.entity.Entity;

public class ModelBasilosaurus extends MowzieModelBase {
   MowzieModelRenderer Body1, Body2, Neck1, Body3, Body4, Body5, Fin3, Body6, Body7, Body8, Body9;
   MowzieModelRenderer Tail4, Tail1, Tail5, Tail6, Tail2, Tail3;
   MowzieModelRenderer Neck2, Fin1, Fin2, Head, Neck3;
   MowzieModelRenderer Jaw1, Jaw5, Jaw2, Jaw3, Jaw4, Jaw6, Jaw7, Jaw8;

   private Animator animator;
   private MowzieModelRenderer[] bodyParts;
   private MowzieModelRenderer[] tailParts;

   public ModelBasilosaurus() {
      this.textureWidth = 128;
      this.textureHeight = 128;

      this.Neck3 = new MowzieModelRenderer(this, 75, 79);
      this.Neck3.setRotationPoint(0.0F, 11.4F, 0.1F);
      this.Neck3.addBox(-2.5F, -3.0F, -9.0F, 5, 3, 9, 0.0F);
      this.setRotateAngle(Neck3, -0.37070793312359557F, 0.0F, 0.0F);

      this.Body6 = new MowzieModelRenderer(this, 86, 57);
      this.Body6.setRotationPoint(0.0F, -0.1F, 13.7F);
      this.Body6.addBox(-3.0F, -4.0F, 0.0F, 6, 8, 14, 0.0F);
      this.setRotateAngle(Body6, -0.012566370614359173F, 0.0F, 0.0F);

      this.Fin3 = new MowzieModelRenderer(this, 0, 23);
      this.Fin3.setRotationPoint(0.0F, -5.0F, 5.0F);
      this.Fin3.addBox(-0.5F, 0.0F, 0.0F, 1, 2, 5, 0.0F);
      this.setRotateAngle(Fin3, 0.3769911184307752F, 0.0F, 0.0F);

      this.Body4 = new MowzieModelRenderer(this, 54, 47);
      this.Body4.setRotationPoint(0.0F, -0.2F, 16.6F);
      this.Body4.addBox(-4.5F, -5.0F, 0.0F, 9, 10, 14, 0.0F);
      this.setRotateAngle(Body4, -0.006283185307179587F, 0.0F, 0.0F);

      this.Tail4 = new MowzieModelRenderer(this, 46, 0);
      this.Tail4.setRotationPoint(-1.0F, 0.5F, 10.0F);
      this.Tail4.addBox(-6.0F, -1.0F, 0.0F, 6, 1, 5, 0.0F);
      this.setRotateAngle(Tail4, 0.0F, 0.5215043804959056F, 0.0F);

      this.Fin2 = new MowzieModelRenderer(this, 8, 8);
      this.Fin2.setRotationPoint(-4.9F, 9.0F, -5.2F);
      this.Fin2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 3, 0.0F);
      this.setRotateAngle(Fin2, 0.1884955592153876F, 0.0F, 0.9424777960769379F);

      this.Jaw6 = new MowzieModelRenderer(this, 0, 42);
      this.Jaw6.setRotationPoint(0.0F, 0.0F, -3.8F);
      this.Jaw6.addBox(-1.0F, 0.0F, -5.8F, 2, 1, 5, 0.0F);
      this.setRotateAngle(Jaw6, -0.029670597283903602F, 0.0F, 0.0F);

      this.Fin1 = new MowzieModelRenderer(this, 0, 7);
      this.Fin1.setRotationPoint(4.9F, 9.0F, -5.2F);
      this.Fin1.addBox(-1.0F, 0.0F, 0.0F, 1, 6, 3, 0.0F);
      this.setRotateAngle(Fin1, 0.1884955592153876F, 0.0F, -0.9424777960769379F);

      this.Jaw3 = new MowzieModelRenderer(this, 111, 33);
      this.Jaw3.setRotationPoint(0.0F, -4.5F, -0.1F);
      this.Jaw3.addBox(-1.5F, 0.0F, -5.0F, 3, 2, 5, 0.0F);
      this.setRotateAngle(Jaw3, 0.20106192982974677F, 0.0F, 0.0F);

      this.Body9 = new MowzieModelRenderer(this, 90, 79);
      this.Body9.setRotationPoint(0.0F, -0.3F, 15.0F);
      this.Body9.addBox(-1.5F, -2.0F, 0.0F, 3, 4, 14, 0.0F);
      this.setRotateAngle(Body9, -0.025132741228718346F, 0.0F, 0.0F);

      this.Jaw8 = new MowzieModelRenderer(this, 112, 18);
      this.Jaw8.setRotationPoint(0.0F, 0.0F, -5.0F);
      this.Jaw8.addBox(-1.0F, -1.0F, -4.6F, 2, 1, 5, 0.0F);
      this.setRotateAngle(Jaw8, -0.06911503837897544F, 0.0F, 0.0F);

      this.Tail3 = new MowzieModelRenderer(this, 9, 4);
      this.Tail3.setRotationPoint(4.0F, 0.0F, 0.2F);
      this.Tail3.addBox(0.0F, -0.96F, 0.0F, 3, 1, 3, 0.0F);
      this.setRotateAngle(Tail3, 0.0F, -0.12566370614359174F, 0.0F);

      this.Jaw1 = new MowzieModelRenderer(this, 106, 42);
      this.Jaw1.setRotationPoint(0.0F, 4.5F, -3.9F);
      this.Jaw1.addBox(-2.0F, -3.0F, -5.0F, 4, 3, 5, 0.0F);
      this.setRotateAngle(Jaw1, -0.06283185307179587F, 0.0F, 0.0F);

      this.Body8 = new MowzieModelRenderer(this, 52, 78);
      this.Body8.setRotationPoint(0.0F, 0.0F, 14.0F);
      this.Body8.addBox(-2.0F, -3.0F, 0.0F, 4, 6, 15, 0.0F);

      this.Body3 = new MowzieModelRenderer(this, 0, 35);
      this.Body3.setRotationPoint(0.0F, 0.0F, 21.0F);
      this.Body3.addBox(-5.0F, -5.5F, 0.0F, 10, 11, 17, 0.0F);

      this.Tail5 = new MowzieModelRenderer(this, 46, 6);
      this.Tail5.setRotationPoint(-5.9F, 0.0F, 0.4F);
      this.Tail5.addBox(-4.0F, -0.97F, 0.0F, 4, 1, 4, 0.0F);
      this.setRotateAngle(Tail5, 0.0F, 0.12566370614359174F, 0.0F);

      this.Jaw5 = new MowzieModelRenderer(this, 86, 47);
      this.Jaw5.setRotationPoint(0.0F, 4.5F, -3.9F);
      this.Jaw5.addBox(-1.5F, 0.0F, -5.0F, 3, 2, 6, 0.0F);
      this.setRotateAngle(Jaw5, -0.06283185307179587F, 0.0F, 0.0F);

      this.Neck2 = new MowzieModelRenderer(this, 90, 18);
      this.Neck2.setRotationPoint(0.0F, 0.4F, -5.6F);
      this.Neck2.addBox(-4.0F, 0.0F, -5.7F, 8, 9, 6, 0.0F);
      this.setRotateAngle(Neck2, 0.031415926535897934F, 0.0F, 0.0F);

      this.Neck1 = new MowzieModelRenderer(this, 90, 0);
      this.Neck1.setRotationPoint(0.0F, -6.6F, -12.8F);
      this.Neck1.addBox(-5.0F, 0.0F, -6.0F, 10, 12, 6, 0.0F);
      this.setRotateAngle(Neck1, 0.01884955592153876F, 0.0F, 0.0F);

      this.Body7 = new MowzieModelRenderer(this, 27, 71);
      this.Body7.setRotationPoint(0.0F, -0.2F, 13.7F);
      this.Body7.addBox(-2.5F, -3.5F, 0.0F, 5, 7, 15, 0.0F);
      this.setRotateAngle(Body7, -0.006283185307179587F, 0.0F, 0.0F);

      this.Head = new MowzieModelRenderer(this, 46, 11);
      this.Head.setRotationPoint(0.0F, 0.1F, -4.4F);
      this.Head.addBox(-3.0F, 0.0F, -4.0F, 6, 8, 3, 0.0F);

      this.Body1 = new MowzieModelRenderer(this, 0, 0);
      this.Body1.setRotationPoint(0.0F, 18.01F, -25.0F);
      this.Body1.addBox(-6.0F, -7.0F, -13.0F, 12, 13, 22, 0.0F);

      this.Tail2 = new MowzieModelRenderer(this, 62, 6);
      this.Tail2.setRotationPoint(5.9F, 0.0F, 0.4F);
      this.Tail2.addBox(0.0F, -0.97F, 0.0F, 4, 1, 4, 0.0F);
      this.setRotateAngle(Tail2, 0.0F, -0.12566370614359174F, 0.0F);

      this.Jaw4 = new MowzieModelRenderer(this, 0, 35);
      this.Jaw4.setRotationPoint(0.0F, 0.0F, -4.8F);
      this.Jaw4.addBox(-1.0F, 0.0F, -5.0F, 2, 2, 5, 0.0F);
      this.setRotateAngle(Jaw4, 0.10053096491487339F, 0.0F, 0.0F);

      this.Jaw7 = new MowzieModelRenderer(this, 112, 60);
      this.Jaw7.setRotationPoint(0.0F, 3.2F, -0.1F);
      this.Jaw7.addBox(-1.0F, -2.0F, -5.0F, 2, 2, 6, 0.0F);
      this.setRotateAngle(Jaw7, -0.23876104167282428F, 0.0F, 0.0F);

      this.Tail6 = new MowzieModelRenderer(this, 7, 0);
      this.Tail6.setRotationPoint(-4.0F, 0.0F, 0.2F);
      this.Tail6.addBox(-3.0F, -0.96F, 0.0F, 3, 1, 3, 0.0F);
      this.setRotateAngle(Tail6, 0.0F, 0.12566370614359174F, 0.0F);

      this.Body2 = new MowzieModelRenderer(this, 47, 14);
      this.Body2.setRotationPoint(0.0F, -0.7F, 8.7F);
      this.Body2.addBox(-5.5F, -6.0F, 0.0F, 11, 12, 21, 0.0F);
      this.setRotateAngle(Body2, -0.031415926535897934F, 0.0F, 0.0F);

      this.Tail1 = new MowzieModelRenderer(this, 68, 0);
      this.Tail1.setRotationPoint(1.0F, 0.5F, 10.0F);
      this.Tail1.addBox(0.0F, -1.0F, 0.0F, 6, 1, 5, 0.0F);
      this.setRotateAngle(Tail1, 0.0F, -0.5215043804959056F, 0.0F);

      this.Jaw2 = new MowzieModelRenderer(this, 73, 6);
      this.Jaw2.setRotationPoint(0.0F, 0.0F, -4.7F);
      this.Jaw2.addBox(-1.5F, -2.0F, -5.0F, 3, 2, 5, 0.0F);
      this.setRotateAngle(Jaw2, -0.029670597283903602F, 0.0F, 0.0F);

      this.Body5 = new MowzieModelRenderer(this, 0, 63);
      this.Body5.setRotationPoint(0.0F, -0.1F, 13.2F);
      this.Body5.addBox(-3.5F, -4.5F, 0.0F, 7, 9, 14, 0.0F);
      this.setRotateAngle(Body5, -0.006283185307179587F, 0.0F, 0.0F);

      // ============ 父子关系（逐行对照你的原版，一个不漏） ============
      this.Neck2.addChild(this.Neck3);
      this.Body5.addChild(this.Body6);
      this.Body4.addChild(this.Fin3);
      this.Body3.addChild(this.Body4);
      this.Body9.addChild(this.Tail4);
      this.Neck1.addChild(this.Fin2);
      this.Jaw5.addChild(this.Jaw6);
      this.Neck1.addChild(this.Fin1);
      this.Jaw1.addChild(this.Jaw3);
      this.Body8.addChild(this.Body9);        // ★ 之前被我弄丢的！尾巴的根！
      this.Jaw7.addChild(this.Jaw8);          // ★ 之前也被我弄丢的！
      this.Tail2.addChild(this.Tail3);
      this.Head.addChild(this.Jaw1);
      this.Body7.addChild(this.Body8);
      this.Body2.addChild(this.Body3);
      this.Tail4.addChild(this.Tail5);
      this.Head.addChild(this.Jaw5);
      this.Neck1.addChild(this.Neck2);
      this.Body1.addChild(this.Neck1);
      this.Body6.addChild(this.Body7);
      this.Neck2.addChild(this.Head);
      this.Tail1.addChild(this.Tail2);
      this.Jaw3.addChild(this.Jaw4);
      this.Jaw5.addChild(this.Jaw7);
      this.Tail5.addChild(this.Tail6);
      this.Body1.addChild(this.Body2);
      this.Body9.addChild(this.Tail1);
      this.Jaw1.addChild(this.Jaw2);
      this.Body4.addChild(this.Body5);
      // ================================================================

      // 动画系统初始化
      this.animator = new Animator(this);
      this.bodyParts = new MowzieModelRenderer[]{
              this.Body9, this.Body8, this.Body7, this.Body6, this.Body5,
              this.Body4, this.Body3, this.Body2, this.Body1,
              this.Neck1, this.Neck2, this.Neck3, this.Head
      };
      this.tailParts = new MowzieModelRenderer[]{
              this.Tail6, this.Tail5, this.Tail4,
              this.Tail3, this.Tail2, this.Tail1
      };

      this.updateAllDefaultPoses();
   }

   private void updateAllDefaultPoses() {
      Body1.updateDefaultPose(); Body2.updateDefaultPose(); Neck1.updateDefaultPose();
      Body3.updateDefaultPose(); Body4.updateDefaultPose(); Body5.updateDefaultPose();
      Fin3.updateDefaultPose(); Body6.updateDefaultPose(); Body7.updateDefaultPose();
      Body8.updateDefaultPose(); Body9.updateDefaultPose(); Tail4.updateDefaultPose();
      Tail1.updateDefaultPose(); Tail5.updateDefaultPose(); Tail2.updateDefaultPose();
      Tail3.updateDefaultPose(); Tail6.updateDefaultPose(); Neck2.updateDefaultPose();
      Neck3.updateDefaultPose(); Fin1.updateDefaultPose(); Fin2.updateDefaultPose();
      Head.updateDefaultPose(); Jaw1.updateDefaultPose(); Jaw5.updateDefaultPose();
      Jaw2.updateDefaultPose(); Jaw3.updateDefaultPose(); Jaw4.updateDefaultPose();
      Jaw6.updateDefaultPose(); Jaw7.updateDefaultPose(); Jaw8.updateDefaultPose();
   }

   @Override
   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      if (entity instanceof IAnimatedEntity) {
         this.animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
      }
      this.Body1.render(f5);
   }

   private void resetPose() {
      Body1.resetToDefaultPose(); Body2.resetToDefaultPose(); Neck1.resetToDefaultPose();
      Body3.resetToDefaultPose(); Body4.resetToDefaultPose(); Body5.resetToDefaultPose();
      Fin3.resetToDefaultPose(); Body6.resetToDefaultPose(); Body7.resetToDefaultPose();
      Body8.resetToDefaultPose(); Body9.resetToDefaultPose(); Tail4.resetToDefaultPose();
      Tail1.resetToDefaultPose(); Tail5.resetToDefaultPose(); Tail2.resetToDefaultPose();
      Tail3.resetToDefaultPose(); Tail6.resetToDefaultPose(); Neck2.resetToDefaultPose();
      Neck3.resetToDefaultPose(); Fin1.resetToDefaultPose(); Fin2.resetToDefaultPose();
      Head.resetToDefaultPose(); Jaw1.resetToDefaultPose(); Jaw5.resetToDefaultPose();
      Jaw2.resetToDefaultPose(); Jaw3.resetToDefaultPose(); Jaw4.resetToDefaultPose();
      Jaw6.resetToDefaultPose(); Jaw7.resetToDefaultPose(); Jaw8.resetToDefaultPose();
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.resetPose();

      if (entity == null) return;
      float ticks = entity.tickCount;
      boolean isMoving = f1 > 0.05F;

      float animSpeed = isMoving ? 0.08F : 0.025F;
      float animHeight = isMoving ? 0.35F : 0.06F;
      float time = isMoving ? f : ticks;
      float amplitude = isMoving ? f1 : 1.0F;

      // 身体波浪（幅度小，不抖）
      this.chainWave(bodyParts, animSpeed, animHeight * 0.15F, 2.0D, time, amplitude);
      // 尾巴波浪（独立，不影响 Body9）
      this.chainWave(tailParts, animSpeed * 1.3F, animHeight * 0.3F, 1.0D, time, amplitude);
      // 整体浮动（沉稳）
      this.bob(Body1, animSpeed * 0.5F, animHeight * 0.5F, false, time, amplitude);
      // 胸鳍
      this.walk(Fin1, animSpeed * 0.7F, 0.12F, true, 0.0F, 0.0F, time, amplitude);
      this.walk(Fin2, animSpeed * 0.7F, 0.12F, true, 0.0F, 0.0F, time, amplitude);
      this.walk(Fin3, animSpeed * 0.7F, 0.08F, false, 0.0F, 0.0F, time, amplitude);
      // 头部追踪
      this.faceTarget(f3, f4, 1.0F, Head, Neck3, Neck2);

      if (!isMoving) {
         this.walk(Jaw1, 0.015F, 0.06F, true, 0, 0, ticks, 1.0F);
         this.walk(Jaw5, 0.015F, 0.06F, true, 0, 0, ticks, 1.0F);
      }
   }

   public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.animator.update(entity);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, (Entity) entity);

      if (entity.getAnimationId() == JurassiCraftAnimationIDs.SCRATCH.animID()) {
         this.animator.setAnimation(JurassiCraftAnimationIDs.SCRATCH.animID());
         this.animator.startPhase(10);
         this.animator.rotate(Jaw1, 0.5F, 0, 0);
         this.animator.rotate(Jaw5, 0.5F, 0, 0);
         this.animator.rotate(Head, -0.2F, 0, 0);
         this.animator.endPhase();
         this.animator.resetPhase(10);
      }
   }

   public void setRotateAngle(MowzieModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }
}