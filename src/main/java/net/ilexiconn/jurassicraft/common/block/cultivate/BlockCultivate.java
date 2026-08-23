package net.ilexiconn.jurassicraft.common.block.cultivate;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BlockCultivate {
    public static AABB[][] boxes = { { new AABB(0.0f, -1.0f, 0.0f, 1.0f, -0.6215f, 1.0f),

            new AABB(0.0f, -0.6215f, 0.0f, 0.062f, 0.93f, 0.062f), new AABB(0.938f, -0.6215f, 0.0f, 1.0f, 0.93f, 0.062f), new AABB(0.0f, -0.6215f, 0.938f, 0.062f, 0.93f, 1.0f), new AABB(0.938f, -0.6215f, 0.938f, 1.0f, 0.93f, 1.0f),

            new AABB(0.062f, -0.6215f, 0.062f, 0.938f, 0.93f, 0.938f),

            new AABB(0.0f, 0.93f, 0.0f, 1.0f, 1.0f, 1.0f) }, {

            new AABB(0.0f, 0.0f, 0.0f, 1.0f, 0.3785f, 1.0f),

            new AABB(0.0f, 0.3785f, 0.0f, 0.062f, 1.93f, 0.062f), new AABB(0.938f, 0.3785f, 0.0f, 1.0f, 1.93f, 0.062f), new AABB(0.0f, 0.3785f, 0.938f, 0.062f, 1.93f, 1.0f), new AABB(0.938f, 0.3785f, 0.938f, 1.0f, 1.93f, 1.0f),

            new AABB(0.062f, 0.3785f, 0.062f, 0.938f, 1.93f, 0.938f),

            new AABB(0.0f, 1.93f, 0.0f, 1.0f, 2.0f, 1.0f) } };

    public static void updateBlockStateWithBottom(Level world, int x, int y, int z, boolean lit) {
        BlockPos pos = new BlockPos(x, y, z);

        // 1.12.2 stored the rotation in metadata; 1.20.1 has no metadata.
        // The rotation is kept on the TileCultivate instance, so capture it
        // before replacing the blocks and restore it afterwards.
        BlockEntity tileEntity = world.getBlockEntity(pos);

        world.setBlock(pos, (lit ? JCBlockRegistry.cultivateBottomOn : JCBlockRegistry.cultivateBottomOff).defaultBlockState(), 3);
        world.setBlock(pos.above(), (lit ? JCBlockRegistry.cultivateTopOn : JCBlockRegistry.cultivateTopOff).defaultBlockState(), 3);

        if (tileEntity != null) {
            tileEntity.setLevel(world);
            world.setBlockEntity(tileEntity);
        }
    }

    public static int getRotation(Level world, int x, int y, int z) {
        BlockEntity tileEntity = world.getBlockEntity(new BlockPos(x, y, z));

        return tileEntity instanceof TileCultivate ? ((TileCultivate) tileEntity).rotation : 0;
    }

    public static void setRotation(Level world, int x, int y, int z, int rotation) {
        BlockEntity tileEntity = world.getBlockEntity(new BlockPos(x, y, z));

        if (tileEntity instanceof TileCultivate)
            ((TileCultivate) tileEntity).rotation = rotation;
    }
}
