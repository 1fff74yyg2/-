package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.api.IDNASource;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.minecraft.ChatFormatting;
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

public class ItemGenericDNASource extends Item implements IDNASource {
    protected String name;
    protected String type;

    public ItemGenericDNASource(String name, String type) {
        super(new Item.Properties());
        this.name = name;
        this.type = type;
        String cat = CreatureHandler.getCategoryFromCreatureName(name);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        // 1.20.1: per-creature item display name ("Tyrannosaurus Skull" etc.) using the
        // creature name from the entity.* lang keys and the type from item.dino_<type>.name.
        return Component.literal(I18nCompat.get("entity." + name + ".name") + " " + I18nCompat.get("item.dino_" + type.toLowerCase() + ".name"));
    }

    public ItemDNA getCorrespondingDNA(String type) {
        // 1.20.1: the descriptionId now includes the modid namespace
        // (e.g. "item.jurassicraft.tyrannosaurus_skull"), so strip everything up to the last
        // '.' before removing the trailing "_<type>" suffix.
        String descriptionId = this.getDescriptionId();
        String creatureName = descriptionId.substring(descriptionId.lastIndexOf('.') + 1);
        creatureName = creatureName.substring(0, creatureName.length() - (1 + type.length()));

        Creature creature = CreatureHandler.getCreatureFromName(creatureName);

        if (creature != null)
            return creature.getDNA();
        else
            return null;
    }

    public String getDNASequence(ItemStack drop) {
        if (drop.hasTag()) {
            if (drop.getTag().contains("DNA")) {
                return drop.getTag().getString("DNA");
            }
        }

        return I18nCompat.get("item.dna.info.errorCode");
    }

    public int getQuality(ItemStack drop) {
        if (drop.hasTag()) {
            if (drop.getTag().contains("Quality")) {
                return drop.getTag().getInt("Quality");
            }
        }

        return 0;
    }

    @Override
    public void appendHoverText(ItemStack drop, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        if (drop.hasTag()) {
            if (drop.getTag().contains("DNA"))
                list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.dna") + ": " + drop.getTag().getString("DNA")));
            else
                list.add(Component.literal(I18nCompat.get("item.dna.info.none")));

            if (drop.getTag().contains("Quality"))
                list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.quality") + ": " + drop.getTag().getInt("Quality") + "%"));
        } else
            list.add(Component.literal(I18nCompat.get("item.dna.info.none")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack drop = player.getItemInHand(hand);

        if (player.getAbilities().instabuild && player.isShiftKeyDown()) {
            CompoundTag compound = drop.getTag();

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

            drop.setTag(compound);

            if (world.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.qualityChanged") + " " + compound.getInt("Quality") + "%"));
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.geneticCodeIs") + ": " + compound.getString("DNA")));
            }
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, drop);
    }
}
