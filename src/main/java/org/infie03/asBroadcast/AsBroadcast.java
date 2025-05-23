// Made by INFIE_03

package org.infie03.asBroadcast;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("AsBroadcast plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AsBroadcast plugin has been disabled!");
    }
}
