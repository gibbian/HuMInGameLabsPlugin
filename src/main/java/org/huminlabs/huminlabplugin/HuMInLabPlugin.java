package org.huminlabs.huminlabplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huminlabs.huminlabplugin.NPC.NPCManager;

public final class HuMInLabPlugin extends JavaPlugin {
    public static HuMInLabPlugin plugin;
    public static EventListeners eventListeners;
    public static NPCManager npcManager;

    @Override
    public void onEnable() {
        plugin = this;
        npcManager = new NPCManager(this);
        eventListeners = new EventListeners(this);
        getServer().getPluginManager().registerEvents(eventListeners, this);

        System.out.println(Bukkit.getServer().getClass().getPackage().getName());

        System.out.println("HuMInLabsPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
