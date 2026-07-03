package com.testforge.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework configuration with a clear precedence order:
 * 1. System property (-Dbrowser=firefox)   — highest
 * 2. Environment variable (BROWSER=firefox)
 * 3. config.properties file                — lowest
 *
 * This lets the same code run locally, in Docker, and in CI without edits.
 */
public final class ConfigManager {

    private static final Properties PROPS = new Properties();

    static {
        String configFile = System.getProperty("config.file", "config/config.properties");
        try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(configFile)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found on classpath: " + configFile);
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config: " + configFile, e);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }
        String envVar = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }
        String fileValue = PROPS.getProperty(key);
        if (fileValue == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return fileValue.trim();
    }

    public static String get(String key, String defaultValue) {
        try {
            return get(key);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }
}
