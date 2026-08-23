package net.ilexiconn.jurassicraft.common.block.fossil;

import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class BlockFossilOre extends Block {
    public BlockFossilOre() {
        super(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        float rand = builder.getLevel().random.nextFloat();

        if (rand < 0.20F) {
            return List.of(new ItemStack(Blocks.STONE));
        } else if (rand < 0.5F) {
            return List.of(new ItemStack(Blocks.COBBLESTONE));
        } else if (rand < 0.70F) {
            return List.of(new ItemStack(Items.BONE));
        } else {
            return List.of(new ItemStack(JCItemRegistry.fossil));
        }
    }
}
