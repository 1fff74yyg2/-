package net.ilexiconn.jurassicraft.common.api;

import net.minecraft.world.item.BlockItem;

public interface ISubBlocksBlock {
    Class<? extends BlockItem> getItemBlockClass();
}
