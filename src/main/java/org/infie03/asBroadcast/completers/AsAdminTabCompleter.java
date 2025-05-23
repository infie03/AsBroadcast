package org.infie03.asBroadcast.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.infie03.asBroadcast.AsBroadcast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AsAdminTabCompleter implements TabCompleter {

    private final AsBroadcast plugin;
    private final List<String> exampleTitles = Arrays.asList(
        "&bWelcome to the server!",
        "&cImportant Announcement",
        "&6Special Event",
        "&aServer Restart"
    );
    
    private final List<String> exampleSubtitles = Arrays.asList(
        "&eEnjoy your stay!",
        "&7Please read the rules",
        "&dStarting in 5 minutes",
        "&fThank you for playing"
    );

    public AsAdminTabCompleter(AsBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("asbroadcast.admin")) {
            return new ArrayList<>();
        }
        
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // First argument: time
            completions.addAll(plugin.getConfigManager().getTimeSuggestions());
        } else if (args.length == 2) {
            // Second argument: title suggestions
            completions.addAll(exampleTitles);
        } else if (args.length == 3) {
            // Third argument: separator
            completions.add("||");
        } else if (args.length == 4) {
            // Fourth argument: subtitle suggestions
            completions.addAll(exampleSubtitles);
        }
        
        // Filter results based on current input
        if (!args[args.length - 1].isEmpty()) {
            return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return completions;
    }
} 