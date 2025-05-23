package org.infie03.asBroadcast.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.infie03.asBroadcast.AsBroadcast;

public class PlayerJoinListener implements Listener {

    private final AsBroadcast plugin;

    public PlayerJoinListener(AsBroadcast plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Send welcome message to operators
        plugin.getMessageUtils().sendWelcomeMessage(player);
    }
} 