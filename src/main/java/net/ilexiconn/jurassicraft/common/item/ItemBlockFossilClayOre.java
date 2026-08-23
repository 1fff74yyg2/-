package net.ilexiconn.jurassicraft.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemBlockFossilClayOre extends BlockItem {
    public static final String[] colors = { "", "brown", "orange", "red", "silver", "white", "yellow" };

    public ItemBlockFossilClayOre(Block block) {
        super(block, new Item.Properties());
    }

    @Override
    public Component getName(ItemStack itemStack) {
        String name = "";
        if (!(colors[itemStack.getDamageValue()]).equals("")) {
            for (String item : colors[itemStack.getDamageValue()].replaceAll("_", " ").split(" "))
                name = name + String.valueOf(item.charAt(0)).toUpperCase() + item.substring(1) + " ";
            return Component.literal(name + "Stained Clay Fossil Ore");
        } else {
            return Component.literal("Clay Fossil Ore");
        }
    }

    /**
     * TODO 1.20.1: this old 1.12.2 placeBlockAt(ItemStack, Player, Level, BlockPos, Direction, ...)
     * signature is no longer an override in 1.20.1 (BlockItem now places via useOn(UseOnContext) /
     * placeBlockAt(BlockPlaceContext, BlockState)), so vanilla will NOT call this method. Kept here
     * with 1.20.1 API calls for reference; block metadata (getStateFromMeta / getMetaFromState) no
     * longer exists.
     */
    public boolean placeBlockAt(ItemStack stack, Player player, Level world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState) {
        world.setBlockAndUpdate(pos, newState);
        return true;
    }
}
