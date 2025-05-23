package org.infie03.asBroadcast.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.infie03.asBroadcast.AsBroadcast;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final AsBroadcast plugin;

    public ReloadCommand(AsBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("asbroadcast.reload")) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage(
                plugin.getConfigManager().getErrorMessage("no-permission", "&cYou don't have permission to use this command.")));
            return true;
        }

        // Reload the configuration
        plugin.reloadPluginConfig();
        
        // Send success message
        sender.sendMessage(plugin.getMessageUtils().formatMessage(
            plugin.getConfigManager().getSuccessMessage("config-reloaded", "&aConfiguration reloaded successfully!")));
        
        return true;
    }
} 