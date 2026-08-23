package net.ilexiconn.jurassicraft.common.entity.ai.stats;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class FlyingParameters {
    public int flyHeightMin;
    public int flyHeightMax;
    public float flySpeedModifier;
    public int flyRefreshRateY;
    public int flyRefreshRateXZ;
    public int flyRefreshRateSpeed;
    public int flightTimeMin;
    public int flightTimeMax;
    public int flapRate;
    public Block[] landingMaterial;

    public FlyingParameters(int heightMin, int heightMax, float speedMod, int rateY, int rateXZ, int rateSpeed, int flightTimeMin, int flightTimeMax, int flapRate, String landingMaterial) {
        flyHeightMin = heightMin;
        flyHeightMax = heightMax;
        flySpeedModifier = speedMod;

        flyRefreshRateY = rateY;
        flyRefreshRateXZ = rateXZ;
        flyRefreshRateSpeed = rateSpeed;

        this.flightTimeMin = flightTimeMin;
        this.flightTimeMax = flightTimeMax;
        this.flapRate = flapRate / 4;

        // 1.12.2 used Material.LEAVES / Material.GRASS; those no longer exist in
        // 1.20.1, so we keep a representative block for each landing material.
        if (landingMaterial.equalsIgnoreCase("grassandleaves"))
            this.landingMaterial = new Block[] { Blocks.GRASS_BLOCK, Blocks.OAK_LEAVES };
        else if (landingMaterial.equalsIgnoreCase("leaves"))
            this.landingMaterial = new Block[] { Blocks.OAK_LEAVES };
    }

    public boolean canLandOn(Block block) {
        if (landingMaterial != null) {
            for (Block possibleMaterial : landingMaterial) {
                if (block == possibleMaterial)
                    return true;
            }
        }

        return false;
    }
}
