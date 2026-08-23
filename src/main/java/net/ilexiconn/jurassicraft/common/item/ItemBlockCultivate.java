package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.block.cultivate.BlockCultivate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemBlockCultivate extends BlockItem {
    public String[] colors = { "black", "red", "green", "brown", "blue", "purple", "cyan", "light_gray", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };

    public ItemBlockCultivate(Block block) {
        super(block, new Item.Properties().stacksTo(16));
    }

    @Override
    public Component getName(ItemStack itemStack) {
        String displayName = "";
        for (String color : colors[itemStack.getDamageValue()].split(" "))
            displayName = "tile." + color + "_cultivate" + ".name";
        return Component.translatable(displayName);
    }

    /**
     * TODO 1.20.1: this old 1.12.2 placeBlockAt(ItemStack, Player, Level, BlockPos, Direction, ...)
     * signature is no longer an override in 1.20.1 (BlockItem now places via useOn(UseOnContext) /
     * placeBlockAt(BlockPlaceContext, BlockState)), so vanilla will NOT call this method and the
     * cultivate top block placement must be re-implemented through useOn if the multiblock behaviour
     * is to be restored. Kept here with 1.20.1 API calls for reference; block metadata (getStateFromMeta
     * / getMetaFromState) no longer exists.
     */
    public boolean placeBlockAt(ItemStack stack, Player player, Level world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState) {
        if (world.getBlockState(pos.above()).canBeReplaced()) {
            world.setBlockAndUpdate(pos, newState);
            world.setBlockAndUpdate(pos.above(), JCBlockRegistry.cultivateTopOff.defaultBlockState());
            BlockCultivate.setRotation(world, pos.getX(), pos.getY(), pos.getZ(), Mth.floor((double) ((player.getYRot() * 4F) / 360F) + 0.5D) & 3);
            return true;
        } else
            return false;
    }
}
