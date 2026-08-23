package net.ilexiconn.jurassicraft.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BlockClearGlass extends Block {
    public BlockClearGlass() {
        super(BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.GLASS).noOcclusion());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        // Original quantityDropped(Random) returned 0: the glass breaks into nothing
        // (silk-touch handled by a loot table once one is provided).
        return List.of();
    }
}
