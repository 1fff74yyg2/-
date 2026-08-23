package net.ilexiconn.jurassicraft.common.block.gypsum;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockGypsumBrick extends Block {
    public BlockGypsumBrick() {
        super(BlockBehaviour.Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }
}
