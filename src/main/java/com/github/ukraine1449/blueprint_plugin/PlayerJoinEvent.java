package com.github.ukraine1449.blueprint_plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinEvent implements Listener {
Blueprint_plugin plugin;

    public PlayerJoinEvent(Blueprint_plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!player.hasPlayedBefore()){
            plugin.insertFirstTime(player.getUniqueId().toString());
        }
        plugin.addToList(player);
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        plugin.playerTierList.remove(event.getPlayer());
    }

}
