package net.ilexiconn.jurassicraft.common.entity;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.egg.EntityDinoEgg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class JCEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, JurassiCraft.MODID);

    public static RegistryObject<EntityType<EntityDinoEgg>> DINO_EGG;
    public static RegistryObject<EntityType<EntitySpit>> DILO_SPIT;

    // Shared placeholder type used by the legacy (Level) constructors so the old
    // reflective spawn paths still compile. Real per-creature EntityTypes are
    // registered by CreatureHandler through registerDynamic().
    public static EntityType<? extends EntityJurassiCraftCreature> sharedCreatureType;

    public void init() {
        DINO_EGG = ENTITY_TYPES.register("dino_egg",
                () -> EntityType.Builder.<EntityDinoEgg>of((type, level) -> new EntityDinoEgg(type, level), MobCategory.MISC)
                        .sized(0.5F, 0.5F).clientTrackingRange(64).updateInterval(1).build("dino_egg"));
        DILO_SPIT = ENTITY_TYPES.register("dilo_spit",
                () -> EntityType.Builder.<EntitySpit>of((type, level) -> new EntitySpit(type, level), MobCategory.MISC)
                        .sized(0.5F, 0.5F).clientTrackingRange(64).updateInterval(1).build("dilo_spit"));
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }

    // ------------------------------------------------------------------
    // Dynamic per-creature EntityType registration (replaces the 1.12.2
    // EntityRegistry.registerModEntity call in CreatureHandler).
    // ------------------------------------------------------------------

    public static class PendingEntityType {
        public final Class<? extends Entity> entityClass;
        public final String name;
        public final float width;
        public final float height;

        public PendingEntityType(Class<? extends Entity> entityClass, String name, float width, float height) {
            this.entityClass = entityClass;
            this.name = name;
            this.width = width;
            this.height = height;
        }
    }

    private static final List<PendingEntityType> PENDING_ENTITY_TYPES = new ArrayList<>();

    public static void registerDynamic(Class<? extends Entity> entityClass, String name, float width, float height) {
        PENDING_ENTITY_TYPES.add(new PendingEntityType(entityClass, name, width, height));
    }

    public static void registerEntities(RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ENTITY_TYPES)) {
            return;
        }
        // The shared creature EntityType used by the legacy (Level) constructors.
        // It must be built and registered here (during the RegisterEvent) so its
        // intrusive holder is bound before the registry freezes.
        if (sharedCreatureType == null) {
            sharedCreatureType = EntityType.Builder
                    .<EntityJurassiCraftCreature>of((type, level) -> new EntityJurassiCraftCreature(type, level), MobCategory.CREATURE)
                    .sized(0.9F, 1.2F).build("jurassicraft_shared");
        }
        event.register(ForgeRegistries.Keys.ENTITY_TYPES,
                new ResourceLocation(JurassiCraft.MODID, "jurassicraft_shared"),
                () -> sharedCreatureType);
        for (PendingEntityType pending : PENDING_ENTITY_TYPES) {
            event.register(ForgeRegistries.Keys.ENTITY_TYPES,
                    new ResourceLocation(JurassiCraft.MODID, pending.name.toLowerCase()),
                    () -> EntityType.Builder.of((type, level) -> createInstance(pending.entityClass, type, level), MobCategory.CREATURE)
                            .sized(pending.width, pending.height).clientTrackingRange(64).updateInterval(1)
                            .build(pending.name.toLowerCase()));
        }
    }

    private static Entity createInstance(Class<? extends Entity> entityClass, EntityType<?> type, Level level) {
        try {
            return entityClass.getConstructor(EntityType.class, Level.class).newInstance(type, level);
        } catch (NoSuchMethodException e) {
            try {
                return entityClass.getConstructor(Level.class).newInstance(level);
            } catch (ReflectiveOperationException e2) {
                throw new RuntimeException("Cannot instantiate " + entityClass.getName(), e2);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot instantiate " + entityClass.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityJurassiCraftCreature> EntityType<T> getSharedCreatureType() {
        return (EntityType<T>) sharedCreatureType;
    }

    public static void setSharedCreatureType(EntityType<? extends EntityJurassiCraftCreature> type) {
        sharedCreatureType = type;
    }
}
