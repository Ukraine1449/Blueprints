package com.github.ukraine1449.blueprint_plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftEvent implements Listener {
Blueprint_plugin plugin;

    public CraftEvent(Blueprint_plugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerCraft(CraftItemEvent event){

    }
}
