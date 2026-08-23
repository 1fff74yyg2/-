package net.ilexiconn.jurassicraft.common.block.gypsum;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BlockGypsumBlock extends Block {
    public BlockGypsumBlock() {
        super(BlockBehaviour.Properties.of().strength(1.5F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return List.of(new ItemStack(JCBlockRegistry.gypsumCobblestone));
    }
}
