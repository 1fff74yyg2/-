package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ItemMammalSyringe extends Item {
    public static final HashSet<String> creaturesFromCow = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromPig = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromHorse = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromSheep = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromGoat = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromCamel = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromFox = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public static final HashSet<String> creaturesFromPanda = new HashSet<String>(Arrays.asList("mammoth", "arsinoitherium", "basilosaurus", "uintatherium", "paraceratherium", "deinotherium", "leptictidium", "moeritherium"));
    public String mammalName;

    public ItemMammalSyringe(String mammal) {
        super(new Item.Properties());
        String cat = CreatureHandler.getCategoryFromCreatureName(mammal);
        this.mammalName = mammal;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal(I18nCompat.get("entity." + mammalName + ".name") + " " + I18nCompat.get("item.dino_syringe.name"));
    }

    public String getSyringeDNASequence(ItemStack syringe) {
        if (syringe.hasTag()) {
            if (syringe.getTag().contains("DNA")) {
                return syringe.getTag().getString("DNA");
            }
        }

        return JurassiCraftDNAHandler.createDefaultDNA();
    }

    public int getSyringeQuality(ItemStack syringe) {
        if (syringe.hasTag()) {
            if (syringe.getTag().contains("Quality")) {
                return syringe.getTag().getInt("Quality");
            }
        }

        return 75;
    }

    @Override
    public void appendHoverText(ItemStack syringe, Level worldIn, List<Component> list, TooltipFlag flagIn) {
        if (!syringe.hasTag()) {
            syringe.setTag(new CompoundTag());
        }

        if (!syringe.getTag().contains("DNA")) {
            syringe.getTag().putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
        }

        if (!syringe.getTag().contains("Quality")) {
            // 1.20.1: mirror the ItemDNA fix - TooltipFlag.isCreative() is true
            // while hovering items in the creative inventory, so creative-mode
            // syringes show (and carry) quality 100 instead of 0.
            syringe.getTag().putInt("Quality", flagIn.isCreative() ? 100 : 0);
        }

        list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.dna") + ": " + syringe.getTag().getString("DNA")));
        list.add(Component.literal(ChatFormatting.GREEN + I18nCompat.get("item.dna.info.quality") + ": " + syringe.getTag().getInt("Quality") + "%"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack syringe = player.getItemInHand(hand);

        if (player.getAbilities().instabuild && player.isShiftKeyDown()) {
            CompoundTag compound = syringe.getTag();

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

            syringe.setTag(compound);

            if (world.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.qualityChanged") + " " + compound.getInt("Quality") + "%"));
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.geneticCodeIs") + ": " + compound.getString("DNA")));
            }
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, syringe);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack syringe, Player player, LivingEntity creature, InteractionHand hand) {
        // 1.20.1: only the server handles the injection. The client also calls
        // this hook (Mob.isEffectiveAi() is true on the client for CREATURE
        // entities), and letting the client set the pregnancy data would make it
        // "give birth" locally and create throwaway entities.
        if (player.level().isClientSide) {
            return InteractionResult.PASS;
        }        if (!syringe.hasTag()) {
            syringe.setTag(new CompoundTag());
        }
        CompoundTag tag = syringe.getTag();

        if (player.getAbilities().instabuild) {
            if (!tag.contains("DNA")) {
                tag.putString("DNA", JurassiCraftDNAHandler.createDefaultDNA());
            }

            if (!tag.contains("Quality")) {
                // Match the client tooltip (appendHoverText): creative-mode syringes default to quality 100.
                tag.putInt("Quality", 100);
            }
        }

        if (!player.getAbilities().instabuild) {
            if (creature instanceof Animal && ((Animal) creature).getAge() >= 0) {
                if (!this.setBaby(player.level(), player, creature, syringe)) {
                    return InteractionResult.PASS;
                } else {
                    syringe.shrink(1);

                    if (syringe.getCount() <= 0) {
                        player.setItemInHand(hand, ItemStack.EMPTY);
                    }

                    return InteractionResult.sidedSuccess(player.level().isClientSide);
                }
            }
        } else if (player.getAbilities().instabuild && !player.isShiftKeyDown()) {
            if (creature instanceof Animal && ((Animal) creature).getAge() >= 0) {
                if (!this.setBaby(player.level(), player, creature, syringe)) {
                    return InteractionResult.PASS;
                } else {
                    syringe.shrink(1);

                    if (syringe.getCount() <= 0) {
                        player.setItemInHand(hand, ItemStack.EMPTY);
                    }

                    return InteractionResult.sidedSuccess(player.level().isClientSide);
                }
            }
        }

        return InteractionResult.PASS;
    }

    private boolean setBaby(Level world, Player player, LivingEntity creature, ItemStack syringe) {
        // Accept the quality stored by the cultivate machine ("SyringeQuality"), or fall back
        // to the generic "Quality" tag (creative-mode syringes / any syringe that carries it).
        // Without this, a freshly taken syringe always failed with "egg quality not determined".
        int syringeQuality = 0;
        if (syringe.hasTag() && syringe.getTag() != null) {
            CompoundTag tag = syringe.getTag();
            if (tag.contains("SyringeQuality")) {
                syringeQuality = tag.getInt("SyringeQuality");
            } else if (tag.contains("Quality")) {
                syringeQuality = tag.getInt("Quality");
            } else {
                // No stored quality (e.g. a syringe obtained without going through
                // the cultivate machine): use the same default as getSyringeQuality.
                syringeQuality = 75;
            }
        }

        if (syringeQuality >= 50) {            if (creature instanceof Cow) {
                if (!creaturesFromCow.contains(this.mammalName.toLowerCase())) {                    return false;
                } else {
                    EntityPregnantCow cow = EntityPregnantCow.get(((Cow) creature));

                    if (cow != null && cow.getMammalName().equals("noEmbryo")) {
                        cow.setMammalName(this.mammalName);
                        cow.setDNAQuality(this.getSyringeQuality(syringe));
                        cow.setDNASequence(this.getSyringeDNASequence(syringe));
                        cow.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof Pig) {
                if (!creaturesFromPig.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    EntityPregnantPig pig = EntityPregnantPig.get(((Pig) creature));

                    if (pig != null && pig.getMammalName().equals("noEmbryo")) {
                        pig.setMammalName(this.mammalName);
                        pig.setDNAQuality(this.getSyringeQuality(syringe));
                        pig.setDNASequence(this.getSyringeDNASequence(syringe));
                        pig.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof Horse) {
                if (!creaturesFromHorse.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    EntityPregnantHorse horse = EntityPregnantHorse.get(((Horse) creature));

                    if (horse != null && horse.getMammalName().equals("noEmbryo")) {
                        horse.setMammalName(this.mammalName);
                        horse.setDNAQuality(this.getSyringeQuality(syringe));
                        horse.setDNASequence(this.getSyringeDNASequence(syringe));
                        horse.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof Sheep) {
                if (!creaturesFromSheep.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    EntityPregnantSheep sheep = EntityPregnantSheep.get(((Sheep) creature));

                    if (sheep != null && sheep.getMammalName().equals("noEmbryo")) {
                        sheep.setMammalName(this.mammalName);
                        sheep.setDNAQuality(this.getSyringeQuality(syringe));
                        sheep.setDNASequence(this.getSyringeDNASequence(syringe));
                        sheep.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof net.minecraft.world.entity.animal.goat.Goat) {
                if (!creaturesFromGoat.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat goat = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get(((net.minecraft.world.entity.animal.goat.Goat) creature));

                    if (goat != null && goat.getMammalName().equals("noEmbryo")) {
                        goat.setMammalName(this.mammalName);
                        goat.setDNAQuality(this.getSyringeQuality(syringe));
                        goat.setDNASequence(this.getSyringeDNASequence(syringe));
                        goat.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof net.minecraft.world.entity.animal.camel.Camel) {
                if (!creaturesFromCamel.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel camel = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get(((net.minecraft.world.entity.animal.camel.Camel) creature));

                    if (camel != null && camel.getMammalName().equals("noEmbryo")) {
                        camel.setMammalName(this.mammalName);
                        camel.setDNAQuality(this.getSyringeQuality(syringe));
                        camel.setDNASequence(this.getSyringeDNASequence(syringe));
                        camel.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof net.minecraft.world.entity.animal.Fox) {
                if (!creaturesFromFox.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox fox = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get(((net.minecraft.world.entity.animal.Fox) creature));

                    if (fox != null && fox.getMammalName().equals("noEmbryo")) {
                        fox.setMammalName(this.mammalName);
                        fox.setDNAQuality(this.getSyringeQuality(syringe));
                        fox.setDNASequence(this.getSyringeDNASequence(syringe));
                        fox.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            } else if (creature instanceof net.minecraft.world.entity.animal.Panda) {
                if (!creaturesFromPanda.contains(this.mammalName.toLowerCase())) {
                    return false;
                } else {
                    net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda panda = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get(((net.minecraft.world.entity.animal.Panda) creature));

                    if (panda != null && panda.getMammalName().equals("noEmbryo")) {
                        panda.setMammalName(this.mammalName);
                        panda.setDNAQuality(this.getSyringeQuality(syringe));
                        panda.setDNASequence(this.getSyringeDNASequence(syringe));
                        panda.setPregnancySpeed(2048);

                        if (!world.isClientSide) {
                            player.sendSystemMessage(Component.literal(I18nCompat.get("item.syringe.info.embryoInseminated")));
                        }

                        return true;
                    }
                }
            }
        } else {
            if (!world.isClientSide) {
                player.sendSystemMessage(Component.literal(I18nCompat.get("item.dna.info.errorQuality")));
            }
        }

        return false;
    }
}
