package org.infie03.asBroadcast.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.infie03.asBroadcast.AsBroadcast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AsTabCompleter implements TabCompleter {

    private final AsBroadcast plugin;
    private final List<String> messageTypes = Arrays.asList("chat", "title", "subtitle", "actionbar");

    public AsTabCompleter(AsBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("asbroadcast.use")) {
            return new ArrayList<>();
        }
        
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // First argument: target (all or player name)
            completions.add("all");
            completions.addAll(Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList()));
        } else if (args.length == 2) {
            // Second argument: message type
            completions.addAll(messageTypes);
        } else if (args.length == 3) {
            // Third argument: time
            completions.addAll(plugin.getConfigManager().getTimeSuggestions());
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