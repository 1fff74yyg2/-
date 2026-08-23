package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author RafaMv0
 */
public class ChainBuffer {
    private int yawTimer;
    private float yawVariation;
    private int pitchTimer;
    private float pitchVariation;
    private float[] yawArray;
    private float[] pitchArray;

    public ChainBuffer(int numberOfParentedBoxes) {
        yawTimer = 0;
        pitchTimer = 0;
        yawVariation = 0f;
        pitchVariation = 0f;
        yawArray = new float[numberOfParentedBoxes];
        pitchArray = new float[numberOfParentedBoxes];
    }

    public void resetRotations() {
        yawVariation = 0f;
        pitchVariation = 0f;
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divider, LivingEntity entity) {
        if (entity.yBodyRot != entity.yBodyRotO && Math.abs(yawVariation) < maxAngle)
            yawVariation += (entity.yBodyRotO - entity.yBodyRot) / divider;

        if (yawVariation > 0.7f * angleDecrement) {
            if (yawTimer > bufferTime) {
                yawVariation -= angleDecrement;
                if (Math.abs(yawVariation) < angleDecrement) {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            } else
                yawTimer++;
        } else if (yawVariation < -0.7f * angleDecrement) {
            if (yawTimer > bufferTime) {
                yawVariation += angleDecrement;
                if (Math.abs(yawVariation) < angleDecrement) {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            } else
                yawTimer++;
        }

        for (int i = 0; i < yawArray.length; i++)
            yawArray[i] = 0.01745329251f * yawVariation / pitchArray.length;
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, float divider, LivingEntity entity) {
        if (entity.getXRot() != entity.xRotO && Math.abs(pitchVariation) < maxAngle)
            pitchVariation += (entity.xRotO - entity.getXRot()) / divider;

        if (pitchVariation > 0.7f * angleDecrement) {
            if (pitchTimer > bufferTime) {
                pitchVariation -= angleDecrement;
                if (Math.abs(pitchVariation) < angleDecrement) {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            } else
                pitchTimer++;
        } else if (pitchVariation < -0.7f * angleDecrement) {
            if (pitchTimer > bufferTime) {
                pitchVariation += angleDecrement;
                if (Math.abs(pitchVariation) < angleDecrement) {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            } else
                pitchTimer++;
        }

        for (int i = 0; i < pitchArray.length; i++)
            pitchArray[i] = 0.01745329251f * pitchVariation / pitchArray.length;
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        if (entity.yBodyRot != entity.yBodyRotO && Math.abs(yawVariation) < maxAngle)
            yawVariation += (entity.yBodyRotO - entity.yBodyRot);

        if (yawVariation > 0.7f * angleDecrement) {
            if (yawTimer > bufferTime) {
                yawVariation -= angleDecrement;
                if (Math.abs(yawVariation) < angleDecrement) {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            } else
                yawTimer++;
        } else if (yawVariation < -0.7f * angleDecrement) {
            if (yawTimer > bufferTime) {
                yawVariation += angleDecrement;
                if (Math.abs(yawVariation) < angleDecrement) {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            } else
                yawTimer++;
        }

        for (int i = 0; i < yawArray.length; i++)
            yawArray[i] = 0.01745329251f * yawVariation / pitchArray.length;
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        if (entity.getXRot() != entity.xRotO && Math.abs(pitchVariation) < maxAngle)
            pitchVariation += (entity.xRotO - entity.getXRot());

        if (pitchVariation > 0.7f * angleDecrement) {
            if (pitchTimer > bufferTime) {
                pitchVariation -= angleDecrement;
                if (Math.abs(pitchVariation) < angleDecrement) {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            } else
                pitchTimer++;
        } else if (pitchVariation < -0.7f * angleDecrement) {
            if (pitchTimer > bufferTime) {
                pitchVariation += angleDecrement;
                if (Math.abs(pitchVariation) < angleDecrement) {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            } else
                pitchTimer++;
        }

        for (int i = 0; i < pitchArray.length; i++)
            pitchArray[i] = 0.01745329251f * pitchVariation / pitchArray.length;
    }

    public void applyChainSwingBuffer(AdvancedModelRenderer[] boxes) {
        if (boxes.length == yawArray.length)
            for (int i = 0; i < boxes.length; i++)
                boxes[i].rotateAngleY += yawArray[i];
        else
            System.err.println("[LLibrary] Wrong array length being used in the buffer! (Y axis)");
    }

    public void applyChainWaveBuffer(AdvancedModelRenderer[] boxes) {
        if (boxes.length == pitchArray.length)
            for (int i = 0; i < boxes.length; i++)
                boxes[i].rotateAngleX += pitchArray[i];
        else
            System.out.println("[LLibrary] Wrong array length being used in the buffer! (X axis)");
    }
}
