package org.infie03.asBroadcast.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.infie03.asBroadcast.AsBroadcast;
import org.infie03.asBroadcast.utils.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsAdminCommand implements CommandExecutor {

    private final AsBroadcast plugin;
    private final Pattern separatorPattern = Pattern.compile("(.+)\\s+\\|\\|\\s+(.+)");
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder().hexColors().build();

    public AsAdminCommand(AsBroadcast plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Create a Title.Times instance in a version-compatible way
     * @param fadeIn fade in time in milliseconds
     * @param stay stay time in milliseconds
     * @param fadeOut fade out time in milliseconds
     * @return Title.Times instance
     */
    private Title.Times createTitleTimes(long fadeIn, long stay, long fadeOut) {
        try {
            // First try using the times() method (most common in older versions)
            return Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
        } catch (NoSuchMethodError e1) {
            try {
                // Then try the of() method (intermediate versions)
                return Title.Times.of(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
            } catch (NoSuchMethodError e2) {
                // If all else fails, try a direct approach
                Title.Times times = null;
                try {
                    // Use reflection as a last resort
                    Class<?> timesClass = Title.Times.class;
                    java.lang.reflect.Method method = timesClass.getMethod("of", Duration.class, Duration.class, Duration.class);
                    times = (Title.Times) method.invoke(null, Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
                } catch (Exception e3) {
                    plugin.getLogger().severe("Failed to create Title.Times: " + e3.getMessage());
                }
                return times;
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigManager config = plugin.getConfigManager();
        
        if (!sender.hasPermission("asbroadcast.admin")) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage(
                config.getErrorMessage("no-permission", "&cYou don't have permission to use this command.")));
            return true;
        }

        // Check if the command has enough arguments
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage("&cUsage: /asadmin <time> <title> || <subtitle>"));
            sender.sendMessage(plugin.getMessageUtils().formatMessage("&7Example: /asadmin 10s &bWelcome to the server! || &eEnjoy your stay!"));
            return true;
        }

        // Parse time parameter
        String timeArg = args[0];
        long displayTime = plugin.getMessageUtils().parseTime(timeArg);
        
        // Combine the rest of the arguments into a message
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String fullMessage = messageBuilder.toString().trim();
        
        // Split the message into title and subtitle using the separator "||"
        Matcher matcher = separatorPattern.matcher(fullMessage);
        if (!matcher.matches()) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage("&cError: Title and subtitle must be separated by ' || '"));
            sender.sendMessage(plugin.getMessageUtils().formatMessage("&7Example: /asadmin 10s &bWelcome to the server! || &eEnjoy your stay!"));
            return true;
        }
        
        String titleText = matcher.group(1).trim();
        String subtitleText = matcher.group(2).trim();
        
        // Format the title and subtitle using legacy color codes
        Component titleComponent = legacySerializer.deserialize(ChatColor.translateAlternateColorCodes('&', titleText));
        Component subtitleComponent = legacySerializer.deserialize(ChatColor.translateAlternateColorCodes('&', subtitleText));
        
        try {
            // Create title times in a version-compatible way
            Title.Times times = createTitleTimes(
                config.getDefaultFadeIn(),
                displayTime,
                config.getDefaultFadeOut()
            );
            
            if (times == null) {
                sender.sendMessage(plugin.getMessageUtils().formatMessage("&cError: Failed to create title times. Please check server logs."));
                return true;
            }
            
            // Create the title object with both title and subtitle
            Title title = Title.title(titleComponent, subtitleComponent, times);
            
            // Send the title to all online players
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showTitle(title);
            }
            
            // Send success message
            sender.sendMessage(plugin.getMessageUtils().formatMessage(
                config.getSuccessMessage("message-sent", "&aMessage sent successfully!")));
        } catch (Exception e) {
            sender.sendMessage(plugin.getMessageUtils().formatMessage("&cError: " + e.getMessage()));
            plugin.getLogger().severe("Error sending title: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
        
        // Log if debug mode is enabled
        if (config.isDebugEnabled()) {
            plugin.getLogger().info("Admin command executed by " + sender.getName() + ": /asadmin " + 
                timeArg + " " + titleText + " || " + subtitleText);
        }
        
        return true;
    }
} 