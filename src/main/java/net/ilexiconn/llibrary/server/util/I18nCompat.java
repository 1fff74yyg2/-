package net.ilexiconn.llibrary.server.util;

import net.minecraft.network.chat.Component;

/**
 * Server-safe replacement for the client-only {@code net.minecraft.client.resources.language.I18n}.
 * <p>
 * The 1.12.2 code used I18n.get() all over the common package; in 1.20.1 I18n lives
 * in the client jar, so any class referencing it fails to load on a dedicated server
 * (and on the integrated server of a singleplayer world). This class forwards to
 * {@link Component#translatable(String, Object...)}, which is available on both sides.
 * Note that on the dedicated server the translations resolve to the raw key (no language
 * files are loaded server-side), which matches the original I18n behaviour there.
 */
public class I18nCompat {
    public static String get(String key) {
        return Component.translatable(key).getString();
    }

    public static String get(String key, Object... args) {
        return Component.translatable(key, args).getString();
    }
}
