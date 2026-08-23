package net.ilexiconn.jurassicraft.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.ilexiconn.jurassicraft.common.handler.JurassiCraftDNAHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class CommandSpawnDino {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawndino")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(context -> spawnCreature(context, null, true))
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(context -> spawnCreature(context, BlockPosArgument.getLoadedBlockPos(context, "pos"), true))
                                .then(Commands.argument("adult", BoolArgumentType.bool())
                                        .executes(context -> spawnCreature(context, BlockPosArgument.getLoadedBlockPos(context, "pos"), BoolArgumentType.getBool(context, "adult")))))));
    }

    private static int spawnCreature(CommandContext<CommandSourceStack> context, BlockPos pos, boolean adult) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        String dinoName = StringArgumentType.getString(context, "name");
        Level world = source.getLevel();
        if (pos == null) {
            pos = BlockPos.containing(source.getPosition());
        }

        net.ilexiconn.jurassicraft.common.entity.Creature creatureDef = CreatureHandler.getCreatureFromName(dinoName);
        if (creatureDef == null) {
            source.sendFailure(Component.literal("Unknown creature: " + dinoName));
            return 0;
        }
        Class creatureClass = creatureDef.getCreatureClass();
        try {
            // 1.20.1: spawn through the registered per-creature EntityType so the
            // client can find the matching renderer and dimensions.
            net.minecraft.world.entity.EntityType<?> type = creatureDef.getEntityType();
            Entity creatureToSpawn = type != null
                    ? type.create(world)
                    : (Entity) creatureClass.getConstructor(Level.class).newInstance(world);

            if (creatureToSpawn instanceof EntityJurassiCraftCreature) {
                EntityJurassiCraftCreature creature = (EntityJurassiCraftCreature) creatureToSpawn;
                creature.setGenetics(100, JurassiCraftDNAHandler.createDefaultDNA());
                creature.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, Mth.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
                creature.yHeadRot = creature.getYRot();
                creature.yBodyRot = creature.getYRot();

                if (adult)
                    creature.setFullGrowth();

                world.addFreshEntity(creature);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
