package net.ilexiconn.jurassicraft.client.model.animation;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.util.Mth;

import java.util.HashMap;

public class Animator {
    public static final float PI = (float) Math.PI;
    private int tempTick, prevTempTick;
    private boolean currentAnimation;
    private AdvancedModelBase mainModel;
    private IAnimatedEntity animatedEntity;
    private HashMap<AdvancedModelRenderer, Transform> transformMap, prevTransformMap;

    public Animator(AdvancedModelBase model) {
        tempTick = 0;
        currentAnimation = false;
        mainModel = model;
        transformMap = new HashMap<AdvancedModelRenderer, Transform>();
        prevTransformMap = new HashMap<AdvancedModelRenderer, Transform>();
    }

    public IAnimatedEntity getEntity() {
        return animatedEntity;
    }

    public void update(IAnimatedEntity entity) {
        tempTick = prevTempTick = 0;
        currentAnimation = false;
        animatedEntity = entity;
        transformMap.clear();
        prevTransformMap.clear();

        for (AdvancedModelRenderer box : mainModel.boxList) {
            box.rotateAngleX = 0F;
            box.rotateAngleY = 0F;
            box.rotateAngleZ = 0F;
        }
    }

    public boolean setAnimation(int animationId) {
        tempTick = prevTempTick = 0;
        currentAnimation = animatedEntity.getAnimationId() == animationId;
        return currentAnimation;
    }

    public void startPhase(int duration) {
        if (!currentAnimation)
            return;

        prevTempTick = tempTick;
        tempTick += duration;
    }

    public void setStationaryPhase(int duration) {
        startPhase(duration);
        endPhase(true);
    }

    public void resetPhase(int duration) {
        startPhase(duration);
        endPhase();
    }

    public void rotate(AdvancedModelRenderer box, float x, float y, float z) {
        if (!currentAnimation)
            return;
        if (!transformMap.containsKey(box))
            transformMap.put(box, new Transform(x, y, z));
        else
            transformMap.get(box).addRot(x, y, z);
    }

    public void move(AdvancedModelRenderer box, float x, float y, float z) {
        if (!currentAnimation)
            return;

        if (!transformMap.containsKey(box))
            transformMap.put(box, new Transform(x, y, z, 0F, 0F, 0F));
        else
            transformMap.get(box).addOffset(x, y, z);
    }

    public void endPhase() {
        endPhase(false);
    }

    private void endPhase(boolean stationary) {
        if (!currentAnimation)
            return;

        int animationTick = animatedEntity.getAnimationTick();

        if (animationTick >= prevTempTick && animationTick < tempTick) {
            if (stationary) {
                for (AdvancedModelRenderer box : prevTransformMap.keySet()) {
                    Transform transform = prevTransformMap.get(box);
                    box.rotateAngleX += transform.rotX;
                    box.rotateAngleY += transform.rotY;
                    box.rotateAngleZ += transform.rotZ;
                    box.rotationPointX += transform.offsetX;
                    box.rotationPointY += transform.offsetY;
                    box.rotationPointZ += transform.offsetZ;
                }
            } else {
                float tick = (animationTick - prevTempTick + JurassiCraft.proxy.getPartialTick()) / (tempTick - prevTempTick);
                float inc = Mth.sin(tick * PI / 2f), dec = 1f - inc;

                for (AdvancedModelRenderer box : prevTransformMap.keySet()) {
                    Transform transform = prevTransformMap.get(box);
                    box.rotateAngleX += dec * transform.rotX;
                    box.rotateAngleY += dec * transform.rotY;
                    box.rotateAngleZ += dec * transform.rotZ;
                    box.rotationPointX += dec * transform.offsetX;
                    box.rotationPointY += dec * transform.offsetY;
                    box.rotationPointZ += dec * transform.offsetZ;
                }

                for (AdvancedModelRenderer box : transformMap.keySet()) {
                    Transform transform = transformMap.get(box);
                    box.rotateAngleX += inc * transform.rotX;
                    box.rotateAngleY += inc * transform.rotY;
                    box.rotateAngleZ += inc * transform.rotZ;
                    box.rotationPointX += inc * transform.offsetX;
                    box.rotationPointY += inc * transform.offsetY;
                    box.rotationPointZ += inc * transform.offsetZ;
                }
            }
        }

        if (!stationary) {
            prevTransformMap.clear();
            prevTransformMap.putAll(transformMap);
            transformMap.clear();
        }
    }
}
