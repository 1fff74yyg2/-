package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * @author ProPercivalalb
 */
public class ItemMulipleBlock extends BlockItem {

    public ItemMulipleBlock(Block block) {
        super(block, new Item.Properties());
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        int i = stack.getDamageValue();
        return super.getDescriptionId() + "." + i;
    }
}
