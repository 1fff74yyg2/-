package net.ilexiconn.jurassicraft.common.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftRidable;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCow;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantHorse;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPig;
import net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantSheep;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

public class JurassiCraftLivingEvent {
    @SubscribeEvent
    public void onEntityConstructing(EntityConstructing event) {
        Entity entity = event.getEntity();

        if (entity instanceof Cow && EntityPregnantCow.get((Cow) entity) == null)
            EntityPregnantCow.register((Cow) entity);
        else if (entity instanceof Pig && EntityPregnantPig.get((Pig) entity) == null)
            EntityPregnantPig.register((Pig) entity);
        else if (entity instanceof Horse && EntityPregnantHorse.get((Horse) entity) == null)
            EntityPregnantHorse.register((Horse) entity);
        else if (entity instanceof Sheep && EntityPregnantSheep.get((Sheep) entity) == null)
            EntityPregnantSheep.register((Sheep) entity);
        else if (entity instanceof net.minecraft.world.entity.animal.goat.Goat && net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get((net.minecraft.world.entity.animal.goat.Goat) entity) == null)
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.register((net.minecraft.world.entity.animal.goat.Goat) entity);
        else if (entity instanceof net.minecraft.world.entity.animal.camel.Camel && net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get((net.minecraft.world.entity.animal.camel.Camel) entity) == null)
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.register((net.minecraft.world.entity.animal.camel.Camel) entity);
        else if (entity instanceof net.minecraft.world.entity.animal.Fox && net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get((net.minecraft.world.entity.animal.Fox) entity) == null)
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.register((net.minecraft.world.entity.animal.Fox) entity);
        else if (entity instanceof net.minecraft.world.entity.animal.Panda && net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get((net.minecraft.world.entity.animal.Panda) entity) == null)
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.register((net.minecraft.world.entity.animal.Panda) entity);
    }

    @SubscribeEvent
    public void onEntityJump(LivingJumpEvent event) {
        Entity ridingEntity = event.getEntity().getVehicle();

        if (ridingEntity instanceof EntityJurassiCraftRidable)
            ((EntityJurassiCraftRidable) ridingEntity).rideJump();
    }

    @SubscribeEvent
    public void onEntityLiving(LivingTickEvent event) {
        // 1.20.1: only the server runs pregnancy. The client also fires
        // LivingTickEvent, and without this guard the client would "give birth"
        // on its own and create throwaway entities that never join the world.
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (event.getEntity() instanceof Cow) {
            EntityPregnantCow cow = EntityPregnantCow.get((Cow) event.getEntity());

            if (cow != null && !cow.getMammalName().equals("noEmbryo")) {
                if (cow.getPregnancyProgress() < cow.getPregnancySpeed()) {
                    cow.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    cow.saveToNbt((Cow) event.getEntity());
                } else {
                    spawnMammalBaby(cow.getMammalName(), cow.getDNAQuality(), cow.getDNASequence(), event);
                    cow.setMammalName("noEmbryo");
                    cow.setDNAQuality(0);
                    cow.setDNASequence("");
                    cow.setPregnancyProgress(0);
                    cow.setPregnancySpeed(0);
                    cow.saveToNbt((Cow) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof Pig) {
            EntityPregnantPig pig = EntityPregnantPig.get((Pig) event.getEntity());

            if (pig != null && !pig.getMammalName().equals("noEmbryo")) {
                if (pig.getPregnancyProgress() < pig.getPregnancySpeed()) {
                    pig.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    pig.saveToNbt((Pig) event.getEntity());
                } else {
                    spawnMammalBaby(pig.getMammalName(), pig.getDNAQuality(), pig.getDNASequence(), event);
                    pig.setMammalName("noEmbryo");
                    pig.setDNAQuality(0);
                    pig.setDNASequence("");
                    pig.setPregnancyProgress(0);
                    pig.setPregnancySpeed(0);
                    pig.saveToNbt((Pig) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof Horse) {
            EntityPregnantHorse horse = EntityPregnantHorse.get((Horse) event.getEntity());

            if (horse != null && !horse.getMammalName().equals("noEmbryo")) {
                if (horse.getPregnancyProgress() < horse.getPregnancySpeed()) {
                    horse.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    horse.saveToNbt((Horse) event.getEntity());
                } else {
                    spawnMammalBaby(horse.getMammalName(), horse.getDNAQuality(), horse.getDNASequence(), event);
                    horse.setMammalName("noEmbryo");
                    horse.setDNAQuality(0);
                    horse.setDNASequence("");
                    horse.setPregnancyProgress(0);
                    horse.setPregnancySpeed(0);
                    horse.saveToNbt((Horse) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof Sheep) {
            EntityPregnantSheep sheep = EntityPregnantSheep.get((Sheep) event.getEntity());

            if (sheep != null && !sheep.getMammalName().equals("noEmbryo")) {
                if (sheep.getPregnancyProgress() < sheep.getPregnancySpeed()) {
                    sheep.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    sheep.saveToNbt((Sheep) event.getEntity());
                } else {
                    spawnMammalBaby(sheep.getMammalName(), sheep.getDNAQuality(), sheep.getDNASequence(), event);
                    sheep.setMammalName("noEmbryo");
                    sheep.setDNAQuality(0);
                    sheep.setDNASequence("");
                    sheep.setPregnancyProgress(0);
                    sheep.setPregnancySpeed(0);
                    sheep.saveToNbt((Sheep) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof net.minecraft.world.entity.animal.goat.Goat) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat goat = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantGoat.get((net.minecraft.world.entity.animal.goat.Goat) event.getEntity());

            if (goat != null && !goat.getMammalName().equals("noEmbryo")) {
                if (goat.getPregnancyProgress() < goat.getPregnancySpeed()) {
                    goat.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    goat.saveToNbt((net.minecraft.world.entity.animal.goat.Goat) event.getEntity());
                } else {
                    spawnMammalBaby(goat.getMammalName(), goat.getDNAQuality(), goat.getDNASequence(), event);
                    goat.setMammalName("noEmbryo");
                    goat.setDNAQuality(0);
                    goat.setDNASequence("");
                    goat.setPregnancyProgress(0);
                    goat.setPregnancySpeed(0);
                    goat.saveToNbt((net.minecraft.world.entity.animal.goat.Goat) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof net.minecraft.world.entity.animal.camel.Camel) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel camel = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantCamel.get((net.minecraft.world.entity.animal.camel.Camel) event.getEntity());

            if (camel != null && !camel.getMammalName().equals("noEmbryo")) {
                if (camel.getPregnancyProgress() < camel.getPregnancySpeed()) {
                    camel.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    camel.saveToNbt((net.minecraft.world.entity.animal.camel.Camel) event.getEntity());
                } else {
                    spawnMammalBaby(camel.getMammalName(), camel.getDNAQuality(), camel.getDNASequence(), event);
                    camel.setMammalName("noEmbryo");
                    camel.setDNAQuality(0);
                    camel.setDNASequence("");
                    camel.setPregnancyProgress(0);
                    camel.setPregnancySpeed(0);
                    camel.saveToNbt((net.minecraft.world.entity.animal.camel.Camel) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof net.minecraft.world.entity.animal.Fox) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox fox = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantFox.get((net.minecraft.world.entity.animal.Fox) event.getEntity());

            if (fox != null && !fox.getMammalName().equals("noEmbryo")) {
                if (fox.getPregnancyProgress() < fox.getPregnancySpeed()) {
                    fox.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    fox.saveToNbt((net.minecraft.world.entity.animal.Fox) event.getEntity());
                } else {
                    spawnMammalBaby(fox.getMammalName(), fox.getDNAQuality(), fox.getDNASequence(), event);
                    fox.setMammalName("noEmbryo");
                    fox.setDNAQuality(0);
                    fox.setDNASequence("");
                    fox.setPregnancyProgress(0);
                    fox.setPregnancySpeed(0);
                    fox.saveToNbt((net.minecraft.world.entity.animal.Fox) event.getEntity());
                }
            }
        } else if (event.getEntity() instanceof net.minecraft.world.entity.animal.Panda) {
            net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda panda = net.ilexiconn.jurassicraft.common.entity.mammals.EntityPregnantPanda.get((net.minecraft.world.entity.animal.Panda) event.getEntity());

            if (panda != null && !panda.getMammalName().equals("noEmbryo")) {
                if (panda.getPregnancyProgress() < panda.getPregnancySpeed()) {
                    panda.increasePregnancyProgress();
                    event.getEntity().aiStep();
                    panda.saveToNbt((net.minecraft.world.entity.animal.Panda) event.getEntity());
                } else {
                    spawnMammalBaby(panda.getMammalName(), panda.getDNAQuality(), panda.getDNASequence(), event);
                    panda.setMammalName("noEmbryo");
                    panda.setDNAQuality(0);
                    panda.setDNASequence("");
                    panda.setPregnancyProgress(0);
                    panda.setPregnancySpeed(0);
                    panda.saveToNbt((net.minecraft.world.entity.animal.Panda) event.getEntity());
                }
            }
        }
    }

    public void spawnMammalBaby(String mammalName, int quality, String dnaSequence, LivingTickEvent event) {
        // Server only: the client must not create throwaway babies.
        if (event.getEntity().level().isClientSide) {
            return;
        }
        Class mammalToSpawnClass = CreatureHandler.getCreatureFromName(mammalName).getCreatureClass();

        if (mammalToSpawnClass != null) {
            try {
                // 1.20.1: entities must be created through their per-creature
                // EntityType. The legacy (Level) constructor falls back to the
                // shared creature type, which has no renderer registered, so the
                // baby would spawn invisible ("tamed" message but no visible mob).
                Entity mammalToSpawn = null;
                net.minecraft.world.entity.EntityType<?> type = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getValue(
                        new net.minecraft.resources.ResourceLocation(net.ilexiconn.jurassicraft.JurassiCraft.MODID, mammalName.toLowerCase()));
                if (type != null) {
                    mammalToSpawn = type.create(event.getEntity().level());
                }
                if (mammalToSpawn == null) {
                    mammalToSpawn = (Entity) mammalToSpawnClass.getConstructor(Level.class).newInstance(event.getEntity().level());
                }


                if (mammalToSpawn instanceof EntityJurassiCraftCreature) {
                    EntityJurassiCraftCreature baby = (EntityJurassiCraftCreature) mammalToSpawn;
                    baby.setGenetics(quality, dnaSequence);

                    if (mammalToSpawn instanceof EntityJurassiCraftSmart && ((EntityJurassiCraftSmart) baby).canBeTamedUponSpawning()) {
                        Player owner = event.getEntity().level().getNearestPlayer(event.getEntity(), 6.0D);

                        if (owner != null) {
                            ((EntityJurassiCraftSmart) baby).setTamed(true, owner);
                            ((EntityJurassiCraftSmart) baby).setOwner(owner.getName().getString());
                            event.getEntity().level().broadcastEntityEvent(baby, (byte) 7);
                        }
                    }

                    baby.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());

                    if (!event.getEntity().level().isClientSide) {
                        event.getEntity().level().addFreshEntity(baby);                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
