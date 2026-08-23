package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.api.IDNASource;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemMeat extends Item implements IDNASource {
    private String name;

    public ItemMeat(String name) {
        super(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F).meat().build()));
        this.name = name;
    }

    public ItemDNA getCorrespondingDNA() {
        net.ilexiconn.jurassicraft.common.entity.Creature creature = net.ilexiconn.jurassicraft.common.handler.CreatureHandler.getCreatureFromName(this.getDescriptionId().substring(5, this.getDescriptionId().length() - 5));

        if (creature != null)
            return creature.getDNA();
        else
            return null;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.dino_meat.name"));
    }

    public String getDNASequence(ItemStack meat) {
        if (meat.hasTag()) {
            if (meat.getTag().contains("DNA")) {
                return meat.getTag().getString("DNA");
            }
        }

        return I18nCompat.get("item.meat.info.errorCode");
    }

    public int getQuality(ItemStack meat) {
        if (meat.hasTag()) {
            if (meat.getTag().contains("Quality")) {
                return meat.getTag().getInt("Quality");
            }
        }

        return 0;
    }

    @Override
    public void appendHoverText(ItemStack meat, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        CompoundTag compound = meat.getTag();

        if (compound == null) {
            compound = new CompoundTag();
        }

        if (!compound.contains("Quality")) {
            compound.putInt("Quality", 0); // 1.20.1: creative-mode check removed (client-only class)
        }

        if (!compound.contains("DNA")) {
            compound.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
        }

        if (compound.contains("DNA") && compound.contains("Quality")) {
            list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.dna") + ": " + compound.getString("DNA")));
            list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.quality") + ": " + compound.getInt("Quality") + "%"));
        }

        meat.setTag(compound);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack meat = player.getItemInHand(hand);

        if (player.getAbilities().instabuild && player.isShiftKeyDown()) {
            CompoundTag compound = meat.getTag();

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

            meat.setTag(compound);

            if (level.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.qualityChanged") + " " + compound.getInt("Quality") + "%"));
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.geneticCodeIs") + ": " + compound.getString("DNA")));
            }
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, meat);
    }
}
