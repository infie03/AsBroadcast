package org.infie03.asBroadcast.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.infie03.asBroadcast.AsBroadcast;
import org.infie03.asBroadcast.utils.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AsCommand implements CommandExecutor {

    private final AsBroadcast plugin;

    public AsCommand(AsBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigManager config = plugin.getConfigManager();
        
        if (!sender.hasPermission("asbroadcast.use")) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage(
                config.getErrorMessage("no-permission", "&cYou don't have permission to use this command.")));
            return true;
        }

        // Check if the command has enough arguments
        if (args.length < 4) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage(
                config.getErrorMessage("usage", "&cUsage: /as <target> <type> <time> <message>")));
            
            List<String> helpMessages = config.getHelpMessages();
            for (String helpMsg : helpMessages) {
                sender.sendMessage(plugin.getMessageUtils().formatMessage(helpMsg));
            }
            return true;
        }

        String target = args[0];
        String type = args[1];
        String time = args[2];
        
        // Combine the rest of the arguments into a message
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.toString().trim();

        // Send the message
        boolean success = plugin.getMessageUtils().sendMessage(sender, target, type, time, message);
        
        if (success) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage(
                config.getSuccessMessage("message-sent", "&aMessage sent successfully!")));
            
            // Log if debug mode is enabled
            if (config.isDebugEnabled()) {
                plugin.getLogger().info("Command executed by " + sender.getName() + ": /as " + 
                    target + " " + type + " " + time + " " + message);
            }
        }
        
        return true;
    }
} 