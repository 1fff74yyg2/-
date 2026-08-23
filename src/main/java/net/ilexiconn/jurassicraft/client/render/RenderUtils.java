package net.ilexiconn.jurassicraft.client.render;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * @author ProPercivalalb
 */
public class RenderUtils {
    private static RenderUtils instance = new RenderUtils();

    public static RenderUtils instance() {
        return instance;
    }

    public enum Type {
        WORLD, INVENTORY
    }

    public static class RenderData {
        int x, y, z = -300000000;
        int metadata = 0;
        Block block;

        public RenderData(BlockGetter world, Block block, int x, int y, int z) {
            this(block, 0);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public RenderData(Block block, int metadata) {
            this.metadata = metadata;
            this.block = block;
        }
    }

    /**
     * Maps the horizontal facing stored in a 1.20.1 BlockState back to the old
     * 1.12.2 metadata values used by JurassiCraft's machine/fence blocks
     * (0 = south, 1 = west, 2 = north, 3 = east). Falls back to 0 when the
     * state has no HORIZONTAL_FACING property (block migration not finished).
     */
    public static int getFacingMeta(BlockState state) {
        if (state != null && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            switch (direction) {
                case SOUTH:
                    return 0;
                case WEST:
                    return 1;
                case NORTH:
                    return 2;
                default:
                    return 3; // EAST
            }
        }
        return 0;
    }

    /**
     * Converts the old-style facing metadata into the model rotation angle used
     * by the block entity renderers (same table as the 1.12.2 renderers).
     */
    public static int getRotationAngle(BlockState state) {
        switch (getFacingMeta(state)) {
            case 0:
                return -180;
            case 1:
                return -90;
            case 2:
                return 0;
            default:
                return -270;
        }
    }
}
