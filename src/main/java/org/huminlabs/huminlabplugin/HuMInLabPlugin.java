package org.huminlabs.huminlabplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.huminlabs.huminlabplugin.NPC.DialogueManager;
import org.huminlabs.huminlabplugin.NPC.NPC;
import org.huminlabs.huminlabplugin.NPC.NPCManager;
import org.huminlabs.huminlabplugin.Objective.ObjectiveStorage;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.audience.Audience;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public final class HuMInLabPlugin extends JavaPlugin {
    public static HuMInLabPlugin plugin;
    private BukkitAudiences adventure;
    public static EventListeners eventListeners;
    public static NPCManager npcManager;
    public static DialogueManager dialogueManager;
    public static ObjectiveStorage objectiveStorage;



    public static HuMInLabPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        //Set up adventure and event listeners
        this.adventure = BukkitAudiences.create(this);
        eventListeners = new EventListeners(this);
        getServer().getPluginManager().registerEvents(eventListeners, this);

        //Set up npc manager with interaction listener
        npcManager = new NPCManager(this);
        npcInteractionListener();

        //Set up dialogue manager
        try {
            dialogueManager = new DialogueManager();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Load Objectives and player pointers
        objectiveStorage = new ObjectiveStorage();

        System.out.println("HuMInLabsPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public void npcInteractionListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY){
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer packet = event.getPacket();

                        int entityID = packet.getIntegers().read(0);
                        ArrayList<NPC> npcs = npcManager.getNPCs();
                        for(int i = 0; i < npcs.size(); i++){
                            if(npcs.get(i).getID() == entityID){
                                Player player = event.getPlayer();
                                String id =  "1.0"; //ObjectiveStorageUtil.getPlayerPointer(player.getUniqueId().toString()).getObjectiveID();
                                if(id != "0.0" ) {
                                    npcs.get(i).runDialogue(player, id);
                                }
                            }
                        }
                    }
                });
    }


    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
