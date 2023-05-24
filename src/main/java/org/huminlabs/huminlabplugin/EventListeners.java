package org.huminlabs.huminlabplugin;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.huminlabs.huminlabplugin.NPC.NPC;

import java.io.IOException;

public class EventListeners implements Listener {
    private HuMInLabPlugin plugin;

    public EventListeners(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        System.out.println("Player joined!");
        Player player = event.getPlayer();
        HuMInLabPlugin.npcManager.loadNPCs(player);
        HuMInLabPlugin.objectiveStorage.addPlayerPointer(player);




    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        CraftPlayer player = (CraftPlayer)event.getPlayer();
        ServerGamePacketListenerImpl playerConnection = player.getHandle().connection;

        //NPC Head Follow
        for(NPC npc : plugin.npcManager.getNPCs()){
            Location npcLocation = npc.npc.getBukkitEntity().getLocation();
            npcLocation.setDirection(event.getPlayer().getLocation().subtract(npcLocation).toVector());
            float yaw = npcLocation.getYaw();
            float pitch = npcLocation.getPitch();

            //Rotate head - horizontal head movement
            //Move Entity - vertical head movement

            playerConnection.send(new ClientboundRotateHeadPacket(npc.npc, (byte) ((yaw%360) * 256 / 360)));
            playerConnection.send(new ClientboundMoveEntityPacket.Rot(npc.npc.getBukkitEntity().getEntityId(), (byte) ((yaw%360) * 256 / 360), (byte) ((pitch%360) * 256 / 360), npc.npc.isOnGround()));
        }
    }
}
