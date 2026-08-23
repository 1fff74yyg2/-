package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.entity.egg.EntityDinoEgg;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemEgg extends Item {
    public String name;

    public ItemEgg(String name) {
        super(new Item.Properties());
        this.name = name;
        String cat = CreatureHandler.getCategoryFromCreatureName(name);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.dino_egg.name"));
    }

    public String getEggDNASequence(ItemStack egg) {
        if (egg.hasTag()) {
            if (egg.getTag().contains("EggDNA")) {
                return egg.getTag().getString("EggDNA");
            }
        }

        return JurassiCraftDNAHandler.createDefaultDNA();
    }

    public int getEggQuality(ItemStack egg) {
        if (egg.hasTag()) {
            if (egg.getTag().contains("EggQuality")) {
                return egg.getTag().getInt("EggQuality");
            }
        }

        return 75;
    }

    @Override
    public void appendHoverText(ItemStack egg, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        if (!egg.hasTag()) {
            egg.setTag(new CompoundTag());
        }

        if (!egg.getTag().contains("EggDNA")) {
            egg.getTag().putString("EggDNA", JurassiCraftDNAHandler.createDefaultDNA());
        }

        if (!egg.getTag().contains("EggQuality")) {
            // 1.20.1: TooltipFlag.isCreative() mirrors the old creative-mode check.
            egg.getTag().putInt("EggQuality", flagIn.isCreative() ? 100 : 0);
        }

        list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.dna") + ": " + egg.getTag().getString("EggDNA")));
        list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.quality") + ": " + egg.getTag().getInt("EggQuality") + "%"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack egg = player.getItemInHand(hand);

        if (player.getAbilities().instabuild && player.isShiftKeyDown()) {
            CompoundTag compound = egg.getTag();

            if (compound == null) {
                compound = new CompoundTag();
            }

            int quality = 0;

            if (compound.contains("EggQuality")) {
                quality = compound.getInt("EggQuality");
            }

            quality += 25;

            if (quality > 100) {
                quality = 0;
            }

            compound.putInt("EggQuality", quality);

            compound.putString("EggDNA", JurassiCraftDNAHandler.createDefaultDNA());

            egg.setTag(compound);

            if (world.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.qualityChanged") + " " + compound.getInt("EggQuality") + "%"));
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.geneticCodeIs") + ": " + compound.getString("EggDNA")));
            }
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, egg);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }
        InteractionHand hand = context.getHand();
        BlockPos pos = context.getClickedPos();
        ItemStack egg = player.getItemInHand(hand);

        if (egg.hasTag() && egg.getTag().contains("EggQuality") && egg.getTag().getInt("EggQuality") >= 50) {
            net.ilexiconn.jurassicraft.common.entity.Creature creature = CreatureHandler.getCreatureFromName(name);

            if (creature == null) {
                // 1.20.1: the creature mapping could not be resolved (e.g. a shifted
                // creatureID after an update); refuse to spawn a broken egg.
                if (world.isClientSide) {
                    player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.errorQuality")));
                }
                return InteractionResult.PASS;
            }

            if (!world.isClientSide && !player.getAbilities().instabuild) {
                world.addFreshEntity(new EntityDinoEgg(world, creature, this.getEggQuality(egg), this.getEggDNASequence(egg), 2048, pos.getX(), pos.getY() + 1, pos.getZ()));
            } else if (!world.isClientSide && !player.isShiftKeyDown()) {
                world.addFreshEntity(new EntityDinoEgg(world, creature, this.getEggQuality(egg), this.getEggDNASequence(egg), 2048, pos.getX(), pos.getY() + 1, pos.getZ()));
            } else {
                this.use(world, player, hand);
            }

            egg.shrink(1);

            if (egg.getCount() <= 0)
                player.setItemInHand(hand, ItemStack.EMPTY);

            return InteractionResult.SUCCESS;
        } else {
            if (world.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.errorQuality")));
            }
        }

        return InteractionResult.PASS;
    }
}
