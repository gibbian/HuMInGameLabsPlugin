package org.huminlabs.huminlabplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huminlabs.huminlabplugin.NPC.DialogueManager;
import org.huminlabs.huminlabplugin.NPC.NPCManager;

import java.io.FileNotFoundException;

public final class HuMInLabPlugin extends JavaPlugin {
    public static HuMInLabPlugin plugin;
    public static EventListeners eventListeners;
    public static NPCManager npcManager;
    public static DialogueManager dialogueManager;

    public static HuMInLabPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        npcManager = new NPCManager(this);
        eventListeners = new EventListeners(this);
        getServer().getPluginManager().registerEvents(eventListeners, this);

        try {
            dialogueManager = new DialogueManager();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("HuMInLabsPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
