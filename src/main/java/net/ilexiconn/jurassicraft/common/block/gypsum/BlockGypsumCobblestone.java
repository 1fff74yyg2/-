package net.ilexiconn.jurassicraft.common.block.gypsum;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockGypsumCobblestone extends Block {
    public BlockGypsumCobblestone() {
        super(BlockBehaviour.Properties.of().strength(1.2F, 2.5F).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }
}
