package org.infie03.asBroadcast.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.infie03.asBroadcast.AsBroadcast;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    private final AsBroadcast plugin;
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder().hexColors().build();
    private final Pattern timePattern = Pattern.compile("(\\d+)([smh])");
    
    // Default values from config
    private int defaultDuration;
    private int fadeIn;
    private int fadeOut;

    public MessageUtils(AsBroadcast plugin) {
        this.plugin = plugin;
        updateFromConfig();
    }
    
    /**
     * Update values from config
     */
    public void updateFromConfig() {
        ConfigManager config = plugin.getConfigManager();
        this.defaultDuration = config.getDefaultDuration() * 1000; // Convert to milliseconds
        this.fadeIn = config.getDefaultFadeIn();
        this.fadeOut = config.getDefaultFadeOut();
    }

    /**
     * Parse a time string like "10s", "5m", "1h" into milliseconds
     * @param timeString The time string to parse
     * @return The time in milliseconds, or default duration if invalid
     */
    public long parseTime(String timeString) {
        Matcher matcher = timePattern.matcher(timeString);
        if (matcher.matches()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            
            return switch (unit) {
                case "s" -> value * 1000L;
                case "m" -> value * 60000L;
                case "h" -> value * 3600000L;
                default -> defaultDuration;
            };
        }
        return defaultDuration;
    }

    /**
     * Format a message with placeholders
     * @param message The message to format
     * @param placeholders The placeholders and their values
     * @return The formatted message
     */
    public Component formatMessage(String message, Object... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs");
        }
        
        for (int i = 0; i < placeholders.length; i += 2) {
            String placeholder = "%" + placeholders[i] + "%";
            String value = String.valueOf(placeholders[i + 1]);
            message = message.replace(placeholder, value);
        }
        
        // Always use legacy color codes for compatibility
        return legacySerializer.deserialize(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Send the welcome message to a player if they are an operator
     * @param player The player to send the message to
     */
    public void sendWelcomeMessage(Player player) {
        if (!player.isOp() || !plugin.getConfigManager().isWelcomeMessageEnabled()) {
            return;
        }
        
        List<String> welcomeMessages = plugin.getConfigManager().getWelcomeMessage();
        for (String line : welcomeMessages) {
            player.sendMessage(formatMessage(line));
        }
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

    /**
     * Send a message to the specified target(s)
     * @param sender The command sender
     * @param target The target (player name or "all")
     * @param type The message type (chat, title, subtitle, actionbar)
     * @param time The display time (for title/subtitle/actionbar)
     * @param message The message content
     * @return true if successful, false otherwise
     */
    public boolean sendMessage(CommandSender sender, String target, String type, String time, String message) {
        // Format the message using legacy color codes
        Component formattedMessage = legacySerializer.deserialize(ChatColor.translateAlternateColorCodes('&', message));
        long displayTime = parseTime(time);
        
        Collection<? extends Player> targets;
        if (target.equalsIgnoreCase("all")) {
            targets = Bukkit.getOnlinePlayers();
        } else {
            Player player = Bukkit.getPlayer(target);
            if (player == null) {
                ConfigManager config = plugin.getConfigManager();
                String errorMsg = config.getErrorMessage("player-not-found", "&cPlayer not found: %player%");
                sender.sendMessage(formatMessage(errorMsg, "player", target));
                return false;
            }
            targets = java.util.Collections.singletonList(player);
        }
        
        if (targets.isEmpty()) {
            ConfigManager config = plugin.getConfigManager();
            sender.sendMessage(formatMessage(config.getErrorMessage("no-players", "&cNo players to send message to.")));
            return false;
        }
        
        try {
            switch (type.toLowerCase()) {
                case "chat" -> {
                    for (Player player : targets) {
                        player.sendMessage(formattedMessage);
                    }
                }
                case "title" -> {
                    // Create title times in a version-compatible way
                    Title.Times times = createTitleTimes(fadeIn, displayTime, fadeOut);
                    
                    if (times == null) {
                        ConfigManager config = plugin.getConfigManager();
                        sender.sendMessage(formatMessage(config.getErrorMessage("title-error", "&cError creating title. Check server logs.")));
                        return false;
                    }
                    
                    // Create and show the title
                    Title title = Title.title(formattedMessage, Component.empty(), times);
                    for (Player player : targets) {
                        player.showTitle(title);
                    }
                }
                case "subtitle" -> {
                    // Create title times in a version-compatible way
                    Title.Times times = createTitleTimes(fadeIn, displayTime, fadeOut);
                    
                    if (times == null) {
                        ConfigManager config = plugin.getConfigManager();
                        sender.sendMessage(formatMessage(config.getErrorMessage("title-error", "&cError creating title. Check server logs.")));
                        return false;
                    }
                    
                    // Create and show the title with subtitle
                    Title title = Title.title(Component.empty(), formattedMessage, times);
                    for (Player player : targets) {
                        player.showTitle(title);
                    }
                }
                case "actionbar" -> {
                    for (Player player : targets) {
                        player.sendActionBar(formattedMessage);
                    }
                }
                default -> {
                    ConfigManager config = plugin.getConfigManager();
                    String errorMsg = config.getErrorMessage("invalid-type", "&cInvalid message type: %type%");
                    sender.sendMessage(formatMessage(errorMsg, "type", type));
                    return false;
                }
            }
        } catch (Exception e) {
            ConfigManager config = plugin.getConfigManager();
            sender.sendMessage(formatMessage(config.getErrorMessage("error", "&cError: %message%"), "message", e.getMessage()));
            plugin.getLogger().severe("Error sending message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        if (plugin.getConfigManager().isDebugEnabled()) {
            plugin.getLogger().info("Sent " + type + " message to " + targets.size() + " player(s)");
        }
        
        return true;
    }
} 