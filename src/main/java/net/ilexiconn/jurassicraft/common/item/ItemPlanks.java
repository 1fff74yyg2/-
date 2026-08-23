package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * @author ProPercivalalb
 */
public class ItemPlanks extends BlockItem {

    public ItemPlanks(Block block) {
        super(block, new Item.Properties());
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        int i = stack.getDamageValue();
        return super.getDescriptionId() + "." + i;
    }
}
