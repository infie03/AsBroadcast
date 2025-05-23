package org.infie03.asBroadcast.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.infie03.asBroadcast.AsBroadcast;

import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private final AsBroadcast plugin;
    private FileConfiguration config;
    
    // Default values for help messages
    private final List<String> DEFAULT_HELP_MESSAGES = Arrays.asList(
        "&7- target: all or player name",
        "&7- type: chat, title, subtitle, actionbar",
        "&7- time: time in seconds (e.g. 10s, 5m, 1h)",
        "&7- message: the message to send"
    );
    
    // Default values for welcome message
    private final List<String> DEFAULT_WELCOME_MESSAGE = Arrays.asList(
        "&8&l----------------------------------------",
        "&b&lAsBroadcast &7- &fBroadcast Plugin",
        "&7Version: &f1.0",
        "&7Commands:",
        "&f/as <target> <type> <time> <message> &7- Send a broadcast",
        "&f/asreload &7- Reload the configuration",
        "&8&l----------------------------------------"
    );

    public ConfigManager(AsBroadcast plugin) {
        this.plugin = plugin;
        // Save default config if it doesn't exist
        plugin.saveDefaultConfig();
        // Load the config
        reload();
    }

    /**
     * Reload the configuration from disk
     */
    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    /**
     * Get the prefix for plugin messages
     * @return The prefix string
     */
    public String getPrefix() {
        return getString("messages.prefix", "&8[&bAsBroadcast&8] &r");
    }

    /**
     * Get an error message from config
     * @param key The error key
     * @param defaultValue The default value if not found
     * @return The error message
     */
    public String getErrorMessage(String key, String defaultValue) {
        return getString("messages.errors." + key, defaultValue);
    }

    /**
     * Get a success message from config
     * @param key The success key
     * @param defaultValue The default value if not found
     * @return The success message
     */
    public String getSuccessMessage(String key, String defaultValue) {
        return getString("messages.success." + key, defaultValue);
    }

    /**
     * Check if welcome message is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isWelcomeMessageEnabled() {
        return getBoolean("welcome-message.enabled", true);
    }

    /**
     * Get the welcome message for ops
     * @return List of welcome message lines
     */
    public List<String> getWelcomeMessage() {
        // Welcome message is no longer in config, use default values
        return DEFAULT_WELCOME_MESSAGE;
    }

    /**
     * Get the help messages for command usage
     * @return List of help messages
     */
    public List<String> getHelpMessages() {
        // Help messages are no longer in config, use default values
        return DEFAULT_HELP_MESSAGES;
    }

    /**
     * Get the default duration for messages
     * @return Default duration in seconds
     */
    public int getDefaultDuration() {
        return getInt("defaults.duration", 5);
    }

    /**
     * Get the default fade-in time for titles
     * @return Default fade-in time in milliseconds
     */
    public int getDefaultFadeIn() {
        return getInt("defaults.fade-in", 500);
    }

    /**
     * Get the default fade-out time for titles
     * @return Default fade-out time in milliseconds
     */
    public int getDefaultFadeOut() {
        return getInt("defaults.fade-out", 500);
    }

    /**
     * Check if tab completion is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isTabCompletionEnabled() {
        return getBoolean("tab-completion.enabled", true);
    }

    /**
     * Get the time suggestions for tab completion
     * @return List of time suggestions
     */
    public List<String> getTimeSuggestions() {
        return getStringList("tab-completion.time-suggestions", Arrays.asList(
            "5s", "10s", "30s", "1m", "5m", "10m"
        ));
    }

    /**
     * Check if debug mode is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isDebugEnabled() {
        return getBoolean("debug", false);
    }

    // Helper methods for getting config values with defaults

    private String getString(String path, String defaultValue) {
        return config.getString(path, defaultValue);
    }

    private int getInt(String path, int defaultValue) {
        return config.getInt(path, defaultValue);
    }

    private boolean getBoolean(String path, boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }

    private List<String> getStringList(String path, List<String> defaultValue) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        return defaultValue;
    }
} 