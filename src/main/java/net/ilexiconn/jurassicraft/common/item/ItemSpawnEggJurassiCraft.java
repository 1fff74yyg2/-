package net.ilexiconn.jurassicraft.common.item;

import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.ilexiconn.llibrary.server.util.I18nCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ItemSpawnEggJurassiCraft extends Item {
    public ItemSpawnEggJurassiCraft() {
        super(new Item.Properties());
    }

    public static EntityJurassiCraftCreature spawnCreature(Level world, Player player, ItemStack egg, double x, double y, double z) {
        int creatureId = getCreatureId(egg);
        Creature creatureDef = CreatureHandler.getCreatureFromId(creatureId);
        if (creatureDef == null) {
            return null;
        }
        Class creatureClass = creatureDef.getCreatureClass();

        try {
            // 1.20.1: spawn through the registered per-creature EntityType so the
            // client can find the matching renderer and dimensions; fall back to the
            // legacy (Level) constructor if the type is not registered yet.
            net.minecraft.world.entity.EntityType<?> type = creatureDef.getEntityType();
            Entity creatureToSpawn = type != null
                    ? type.create(player.level())
                    : (Entity) creatureClass.getConstructor(Level.class).newInstance(player.level());

            if (creatureToSpawn instanceof EntityJurassiCraftCreature) {
                EntityJurassiCraftCreature creature = (EntityJurassiCraftCreature) creatureToSpawn;
                creature.setGenetics(100, JurassiCraftDNAHandler.createDefaultDNA());
                creature.moveTo(x, y, z);
                creature.moveTo(x, y, z, Mth.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
                creature.yHeadRot = creature.getYRot();
                creature.yBodyRot = creature.getYRot();

                if (!player.isShiftKeyDown()) {
                    creature.setFullGrowth();
                }

                return creature;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void appendHoverText(ItemStack egg, Level worldIn, List<Component> info, TooltipFlag flagIn) {
        info.add(Component.literal(I18nCompat.get("lore.baby_dino.name")));
    }

    /**
     * 1.20.1: ItemStack damage/metadata no longer exists, so the creature id is
     * stored in the stack NBT ("CreatureID"). Falls back to the damage value for
     * legacy stacks.
     */
    public static int getCreatureId(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("CreatureID")) {
            return stack.getTag().getInt("CreatureID");
        }
        return stack.getDamageValue();
    }

    @Override
    public Component getName(ItemStack itemStack) {
        Creature creature = CreatureHandler.getCreatureFromId(getCreatureId(itemStack));
        if (creature != null) {
            return Component.literal(I18nCompat.get("entity." + creature.getCreatureName() + ".name") + " " + I18nCompat.get("item.dino_spawn_egg.name"));
        }
        return Component.literal(I18nCompat.get("item.dino_spawn_egg.name"));
    }

    // TODO 1.20.1: getColorFromItemStack was a 1.12.2-only hook; in 1.20.1 item tinting is handled
    // through IClientItemExtensions (getTintIndex / color) together with a "tintindex" in the item
    // model JSON. Kept here (it no longer overrides anything) for reference only.
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        Creature creature = CreatureHandler.getCreatureFromId(getCreatureId(stack));

        return creature != null ? (renderPass == 0 ? creature.getEggPrimaryColor() : creature.getEggSecondaryColor()) : 16777215;
    }

    // TODO 1.20.1: creative-tab entries are populated via BuildCreativeModeTabContentsEvent;
    // the old getSubItems(CreativeModeTab, NonNullList) hook was removed in 1.20.1. Note that
    // ItemStack damage/metadata was also removed, so the per-creature egg "subtype" would need to
    // be stored in the stack tag instead of the damage value.

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            Player player = context.getPlayer();
            if (player == null) {
                return InteractionResult.FAIL;
            }
            ItemStack itemStack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();
            Direction facing = context.getClickedFace();
            BlockState clickedState = worldIn.getBlockState(pos);
            pos = pos.relative(facing);

            double yTranslation = 0.0D;

            if (facing == Direction.UP && clickedState.getRenderShape() == RenderShape.INVISIBLE) {
                yTranslation = 0.5D;
            } else if (facing.get2DDataValue() >= 0) {
                // Clicking a horizontal side: after relative() the target block
                // keeps the clicked block's Y (its BOTTOM height). Spawning there
                // puts the creature inside/under the terrain, its hitbox overlaps
                // solid blocks -> suffocation check triggers -> black creature.
                // Raise the spawn point to the top of that block column.
                yTranslation = 1.0D;
            }

            EntityJurassiCraftCreature creature = spawnCreature(worldIn, player, itemStack, (double) pos.getX() + 0.5D, (double) pos.getY() + yTranslation, (double) pos.getZ() + 0.5D);

            if (creature != null) {
                if (creature instanceof LivingEntity && itemStack.hasCustomHoverName()) {
                    creature.setCustomName(itemStack.getHoverName());
                }

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    if (itemStack.isEmpty()) {
                        itemStack = ItemStack.EMPTY;
                    }
                }

                worldIn.addFreshEntity(creature);
                creature.playAmbientSound();
            }

            return InteractionResult.SUCCESS;
        }
    }
}
