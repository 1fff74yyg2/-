package net.ilexiconn.jurassicraft.common.events;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.item.ItemDinoPad;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JurassiCraftInteractEvent {
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof Player) {
            if (event.getTarget() != null) {
                Player player = event.getEntity();
                ItemStack heldItem = player.getMainHandItem();

                if (heldItem != null && heldItem.getItem() instanceof ItemDinoPad) {
                    // 1.20.1: opening a GUI must happen on the client thread. The
                    // EntityInteract event fires on the server thread as well, so
                    // only act on the logical client side.
                    if (event.getSide() == net.minecraftforge.fml.LogicalSide.CLIENT) {
                        // Request the server-side pregnancy state (the pregnancy data
                        // lives on the server; the client cannot read it otherwise).
                        net.minecraft.world.entity.Entity target = event.getTarget();
                        if (target instanceof net.minecraft.world.entity.animal.Cow
                                || target instanceof net.minecraft.world.entity.animal.Pig
                                || target instanceof net.minecraft.world.entity.animal.horse.Horse
                                || target instanceof net.minecraft.world.entity.animal.Sheep
                                || target instanceof net.minecraft.world.entity.animal.goat.Goat
                                || target instanceof net.minecraft.world.entity.animal.camel.Camel
                                || target instanceof net.minecraft.world.entity.animal.Fox
                                || target instanceof net.minecraft.world.entity.animal.Panda) {
                            net.ilexiconn.jurassicraft.JurassiCraft.network.sendToServer(
                                    new net.ilexiconn.jurassicraft.common.message.MessagePregnancy(target.getId(), "noEmbryo", 0, 0, 0));
                        }
                        JurassiCraft.proxy.openDinoPad(event.getTarget());
                    }
                }
            }
        }
    }
}
