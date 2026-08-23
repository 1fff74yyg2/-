package net.ilexiconn.jurassicraft.common.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers all JurassiCraft SoundEvents so that playSound(new SoundEvent(...)) actually works.
 */
public class JCSoundRegistry {
    private static final Map<String, SoundEvent> SOUNDS = new LinkedHashMap<String, SoundEvent>();

    public static void init() {
        SOUNDS.clear();
        try {
            JsonObject root = new JsonParser().parse(new InputStreamReader(JCSoundRegistry.class.getResourceAsStream("/assets/jurassicraft/sounds.json"))).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                String name = entry.getKey();
                ResourceLocation location = new ResourceLocation(JurassiCraft.MODID, name);
                SOUNDS.put(name, SoundEvent.createVariableRangeEvent(location));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerSounds(RegisterEvent event) {
        if (event.getRegistryKey().equals(net.minecraft.core.registries.Registries.SOUND_EVENT)) {
            for (Map.Entry<String, SoundEvent> entry : SOUNDS.entrySet()) {
                event.register(net.minecraft.core.registries.Registries.SOUND_EVENT,
                        new ResourceLocation(JurassiCraft.MODID, entry.getKey()), () -> entry.getValue());
            }
        }
    }
}
