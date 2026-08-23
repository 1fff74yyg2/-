package net.ilexiconn.jurassicraft.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;

/**
 * 1.12.2 helper for rendering fluids through GL display lists.
 * <p>
 * GL display lists were removed in 1.20.1. The API is kept as a stub that returns
 * {@code null} so existing callers compile and can simply skip the fluid overlay.
 */
@OnlyIn(Dist.CLIENT)
public class RenderHelper {
    public static int[] getFluidDisplayLists(Level world) {
        return null;
    }
}
