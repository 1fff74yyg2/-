package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.api.IDNASample;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemDNA extends Item implements IDNASample {
    private String name;

    public ItemDNA(String name) {
        super(new Item.Properties());
        this.name = name;
        String category = CreatureHandler.getCategoryFromCreatureName(name);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.dino_dna.name"));
    }

    public Item getCorrespondingEggOrSyringe() {
        Creature creature = CreatureHandler.getCreatureFromDNA(this);

        if (creature.getEgg() != null) {
            return creature.getEgg();
        } else if (creature.getMammalSyringe() != null) {
            return creature.getMammalSyringe();
        } else {
            return null;
        }
    }

    public String getDNASequence(ItemStack dnaSample) {
        if (!dnaSample.hasTag()) {
            dnaSample.setTag(new CompoundTag());
        }

        if (!dnaSample.getTag().contains("DNA")) {
            dnaSample.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
        }

        if (!dnaSample.getTag().contains("Quality")) {
            dnaSample.getTag().putInt("Quality", 50);
        }

        if (dnaSample.hasTag()) {
            if (dnaSample.getTag().contains("DNA")) {
                return dnaSample.getTag().getString("DNA");
            }
        }

        return I18nCompat.get("item.dna.info.errorCode");
    }

    public int getQuality(ItemStack dnaSample) {
        if (!dnaSample.hasTag()) {
            dnaSample.setTag(new CompoundTag());
        }

        if (!dnaSample.getTag().contains("DNA")) {
            dnaSample.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
        }

        if (!dnaSample.getTag().contains("Quality")) {
            dnaSample.getTag().putInt("Quality", 50);
        }

        if (dnaSample.hasTag()) {
            if (dnaSample.getTag().contains("Quality")) {
                return dnaSample.getTag().getInt("Quality");
            }
        }

        return 0;
    }

    @Override
    public void appendHoverText(ItemStack dnaSample, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        if (!dnaSample.hasTag()) {
            dnaSample.setTag(new CompoundTag());
        }

        if (!dnaSample.getTag().contains("Quality")) {
            // 1.20.1: TooltipFlag.isCreative() is true while hovering items in the
            // creative inventory (TooltipFlag.Default.CREATIVE), mirroring the old
            // 1.12.2 Minecraft.getMinecraft().player.capabilities.isCreativeMode check.
            dnaSample.getTag().putInt("Quality", flagIn.isCreative() ? 100 : 0);
        }

        if (!dnaSample.getTag().contains("DNA")) {
            dnaSample.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
        }

        list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.dna") + ": " + dnaSample.getTag().getString("DNA")));

        if (flagIn.isCreative() && dnaSample.getTag().getInt("Quality") == 0) {
            dnaSample.getTag().putInt("Quality", 100);
        }

        list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.quality") + ": " + dnaSample.getTag().getInt("Quality") + "%"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack dnaSample = player.getItemInHand(hand);

        if (player.getAbilities().instabuild && player.isShiftKeyDown()) {
            CompoundTag compound = dnaSample.getTag();

            if (compound == null) {
                compound = new CompoundTag();
            }

            int quality = 0;

            if (compound.contains("Quality")) {
                quality = compound.getInt("Quality");
            }

            quality += 25;

            if (quality > 100) {
                quality = 0;
            }

            compound.putInt("Quality", quality);

            compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());

            dnaSample.setTag(compound);

            if (world.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.qualityChanged") + " " + compound.getInt("Quality") + "%"));
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.geneticCodeIs") + ": " + compound.getString("DNA")));
            }
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, dnaSample);
    }
}
