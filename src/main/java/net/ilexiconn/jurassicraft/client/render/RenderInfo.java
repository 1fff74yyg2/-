package net.ilexiconn.jurassicraft.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class RenderInfo {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    public Block baseBlock = Blocks.DIRT;
    public TextureAtlasSprite texture = null;
    public TextureAtlasSprite[] textureArray = null;
    public boolean[] renderSide = new boolean[6];
    public float light = -1f;
    public int brightness = -1;

    public RenderInfo() {
        setRenderAllSides();
    }

    public RenderInfo(Block template, TextureAtlasSprite[] texture) {
        this();
        baseBlock = template;
        textureArray = texture;
    }

    public RenderInfo(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this();
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public final void setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public final void setRenderAllSides() {
        Arrays.fill(renderSide, true);
    }

    public TextureAtlasSprite getBlockTextureFromSide(int side) {
        if (texture != null)
            return texture;

        if (textureArray == null || textureArray.length == 0)
            return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(baseBlock.defaultBlockState());
        else if (side >= textureArray.length)
            side = 0;

        return textureArray[side];
    }
}
