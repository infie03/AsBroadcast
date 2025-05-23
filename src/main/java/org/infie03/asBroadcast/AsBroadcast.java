package org.infie03.asBroadcast;

import org.bukkit.plugin.java.JavaPlugin;
import org.infie03.asBroadcast.commands.AsAdminCommand;
import org.infie03.asBroadcast.commands.AsCommand;
import org.infie03.asBroadcast.commands.ReloadCommand;
import org.infie03.asBroadcast.completers.AsAdminTabCompleter;
import org.infie03.asBroadcast.completers.AsTabCompleter;
import org.infie03.asBroadcast.listeners.PlayerJoinListener;
import org.infie03.asBroadcast.utils.ConfigManager;
import org.infie03.asBroadcast.utils.MessageUtils;

import java.util.Objects;

public final class AsBroadcast extends JavaPlugin {

    private static AsBroadcast instance;
    private MessageUtils messageUtils;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Save instance for static access
        instance = this;
        
        // Initialize config manager
        configManager = new ConfigManager(this);
        
        // Initialize utilities
        messageUtils = new MessageUtils(this);
        
        // Register commands
        Objects.requireNonNull(getCommand("as")).setExecutor(new AsCommand(this));
        Objects.requireNonNull(getCommand("asadmin")).setExecutor(new AsAdminCommand(this));
        Objects.requireNonNull(getCommand("asreload")).setExecutor(new ReloadCommand(this));
        
        // Register tab completers if enabled
        if (configManager.isTabCompletionEnabled()) {
            Objects.requireNonNull(getCommand("as")).setTabCompleter(new AsTabCompleter(this));
            Objects.requireNonNull(getCommand("asadmin")).setTabCompleter(new AsAdminTabCompleter(this));
        }
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        // Register command aliases from config
        registerCommandAliases();
        
        if (configManager.isDebugEnabled()) {
            getLogger().info("Debug mode is enabled");
        }
        
        getLogger().info("AsBroadcast has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AsBroadcast has been disabled!");
    }
    
    /**
     * Register command aliases from config
     */
    private void registerCommandAliases() {
        // This would normally be done in plugin.yml, but we're showing how to access config values
        if (configManager.isDebugEnabled()) {
            getLogger().info("Command aliases would be registered here if supported dynamically");
        }
    }
    
    /**
     * Reload the plugin configuration
     */
    public void reloadPluginConfig() {
        configManager.reload();
        messageUtils.updateFromConfig();
        
        if (configManager.isDebugEnabled()) {
            getLogger().info("Plugin configuration reloaded");
        }
    }
    
    public static AsBroadcast getInstance() {
        return instance;
    }
    
    public MessageUtils getMessageUtils() {
        return messageUtils;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
