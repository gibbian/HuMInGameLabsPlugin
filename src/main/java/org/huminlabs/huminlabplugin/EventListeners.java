package org.huminlabs.huminlabplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListeners implements Listener {
    private HuMInLabPlugin plugin;

    public EventListeners(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println("Player joined!");
        Player player = event.getPlayer();
        plugin.npcManager.loadNPCs(player);
    }
}
