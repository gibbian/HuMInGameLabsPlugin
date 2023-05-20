package org.huminlabs.huminlabplugin.NPC;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;
import net.minecraft.server.level.ServerEntity;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class NPC {
    HuMInLabPlugin plugin;
    Player player;
    GameProfile gameProfile;
    public String name;
    ServerPlayer npc;

    public NPC(String name, String signature, String texture, HuMInLabPlugin plugin, Player player){
        this.name = name;
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);
        this.gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
        this.plugin = plugin;
        this.player = player;

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel world = serverPlayer.getLevel();

        npc = new ServerPlayer(server, world, gameProfile, new ProfilePublicKey(null));


    }

    public void sendPackets(){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        //PlayerInfoPacket
        //SpawnPlayerPacket
        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
        connection.send(new ClientboundAddPlayerPacket(npc));
    }

    public void spawnNPC(Player player, double x, double y, double z){
        setPos(x, y, z);
        sendPackets();
    }

    public void setPos(double x, double y, double z){
        npc.setPos(x, y, z);
    }

}
