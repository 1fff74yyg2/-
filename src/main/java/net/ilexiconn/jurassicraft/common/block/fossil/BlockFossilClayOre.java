package net.ilexiconn.jurassicraft.common.block.fossil;

import net.ilexiconn.jurassicraft.common.api.ISubBlocksBlock;
import net.ilexiconn.jurassicraft.common.item.ItemBlockFossilClayOre;
import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class BlockFossilClayOre extends Block implements ISubBlocksBlock {
    public BlockFossilClayOre() {
        super(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        float rand = builder.getLevel().random.nextFloat();

        if (rand < 0.5F) {
            // The old per-metadata "color" variant was computed but never used to
            // select the item, so the plain terracotta drop is preserved.
            return List.of(new ItemStack(Blocks.TERRACOTTA));
        } else if (rand < 0.75F) {
            return List.of(new ItemStack(Items.BONE));
        } else {
            return List.of(new ItemStack(JCItemRegistry.fossil));
        }
    }

    public Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockFossilClayOre.class;
    }
}
