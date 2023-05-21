package org.huminlabs.huminlabplugin.NPC;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.BookItem;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class NPC {
    HuMInLabPlugin plugin;
    Player player;
    GameProfile gameProfile;
    public String name;
    public ServerPlayer npc;

    public Dialogue[] dialogue;

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

        npc = new ServerPlayer(server, world, gameProfile);


    }

    public void sendPackets(){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        //PlayerInfoPacket
        //SpawnPlayerPacket
        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        connection.send(new ClientboundAddPlayerPacket(npc));
    }

    public void spawnNPC(Player player, double x, double y, double z){
        setPos(x, y, z);
        sendPackets();
    }

    public void setPos(double x, double y, double z){
        npc.setPos(x, y, z);
    }

    public void setDialogue(Dialogue[] dialogue){
        this.dialogue = dialogue;
    }

    public int getID(){ return npc.getId(); }

    public void runDialogue(Player player, String unit, String id){
        if(id == null) id = "0.0";

        for(int i = 0; i < dialogue.length; i++){
            if(dialogue[i].getId().equals(id) && dialogue[i].getUnit().equals(unit)){

                if(i < dialogue.length){
                    //send dialogue[i + dialogueIndex] to player

                    String[] speech = dialogue[i].getDialog();
                    String[] response = dialogue[i].getResponse();


                    Component bookTitle = Component.text("");
                    Component bookAuthor = Component.text("");

                    ArrayList<Component> pages = new ArrayList<>();
                    for (int j = 0; j < speech.length-1; j++) {
                        String s = speech[j];
                        pages.add(Component.text(s));
                    }

                    TextComponent lastPage = Component.text(speech[speech.length-1]);
                    for(int j = 0; j < response.length; j++){
                        lastPage = lastPage.append(
                                Component.text("\n" + response[j])
                                        .color(NamedTextColor.DARK_GREEN)
                                        .decorate(TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.runCommand("/setstage " + unit + " " + dialogue[i].trigger[j] ))
                        );
                    }

                    pages.add(lastPage);
                    Collection<Component> bookPages = pages;
                    Book book = Book.book(bookTitle, bookAuthor, bookPages);
                    plugin.adventure().player(player).openBook(book);
                }
                break;
            }
        }
    }

}
