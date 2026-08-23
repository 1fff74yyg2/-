package net.ilexiconn.jurassicraft.common.handler;

import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.entity.JCEntityRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

/**
 * 1.20.1: entity attributes must be registered via EntityAttributeCreationEvent,
 * otherwise the entity has no attributes ("has no attributes" warning) and
 * getAttribute() returns null, which breaks health/AI and makes spawned
 * creatures die instantly.
 */
public class AttributeHandler {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        AttributeSupplier supplier = createAttributes();
        for (Creature creature : CreatureHandler.getCreatures()) {
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(
                    new ResourceLocation(JurassiCraft.MODID, creature.getCreatureName().toLowerCase(Locale.ROOT)));
            if (type == null) {
                type = BuiltInRegistries.ENTITY_TYPE.get(
                        new ResourceLocation(JurassiCraft.MODID, creature.getCreatureName().toLowerCase(Locale.ROOT)));
            }
            if (type != null) {
                // All registered creature entity classes extend LivingEntity.
                @SuppressWarnings("unchecked")
                EntityType<? extends LivingEntity> livingType = (EntityType<? extends LivingEntity>) type;
                event.put(livingType, supplier);
            }
        }
        if (JCEntityRegistry.getSharedCreatureType() != null) {
            event.put(JCEntityRegistry.getSharedCreatureType(), supplier);
        }
        // DINO_EGG (EntityDinoEgg) and DILO_SPIT (EntitySpit) are not LivingEntity
        // subclasses, so they have no attributes to register.
    }

    private static AttributeSupplier createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .build();
    }
}
