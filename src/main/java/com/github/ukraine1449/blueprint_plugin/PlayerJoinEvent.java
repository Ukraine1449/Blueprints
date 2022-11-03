package com.github.ukraine1449.blueprint_plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {
Blueprint_plugin plugin;

    public PlayerJoinEvent(Blueprint_plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        if(!event.getPlayer().hasPlayedBefore()){
            plugin.insertFirstTime(event.getPlayer().getUniqueId().toString());
        }
    }

}
