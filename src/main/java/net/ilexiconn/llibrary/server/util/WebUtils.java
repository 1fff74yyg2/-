package net.ilexiconn.llibrary.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 1.20.1 compatible replacement for llibrary's WebUtils (pure JDK, no Guava/IOUtils).
 */
public class WebUtils {
    public static final String PASTEBIN_URL_PREFIX = "https://pastebin.com/raw/";

    public static String readPastebin(String pasteID) {
        return readURL(PASTEBIN_URL_PREFIX + pasteID);
    }

    public static List<String> readPastebinAsList(String pasteID) {
        return readURLAsList(PASTEBIN_URL_PREFIX + pasteID);
    }

    public static String readURL(String url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(), StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            return builder.toString();
        } catch (IOException e) {
            System.err.println("Failed to receive data from URL: " + url);
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> readURLAsList(String url) {
        String content = readURL(url);
        if (content == null) {
            return null;
        }
        return content.lines().collect(Collectors.toList());
    }
}
