package org.huminlabs.huminlabplugin;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.huminlabs.huminlabplugin.NPC.NPC;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

import java.io.IOException;
import java.util.ArrayList;

public class EventListeners implements Listener {
    //TODO
    //Line 99, quickFrameEval, make sure to add itemframes to the arraylist during world editing
    private HuMInLabPlugin plugin;
    ArrayList<InventoryHolder> chests = new ArrayList<InventoryHolder>();
    private static ArrayList<Chest> lesson3Chests = new ArrayList<>();
    private static ArrayList<Chest> lesson4Chests = new ArrayList<>();
    ArrayList<GlowItemFrame> u2l1ItemFrames = new ArrayList<GlowItemFrame>();
    private static Chest u2Lesson3SantiChest;

    public EventListeners(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        System.out.println("Player joined!");
        Player player = event.getPlayer();
        HuMInLabPlugin.npcManager.loadNPCs(player);
        HuMInLabPlugin.objectiveStorage.addPlayerPointer(player);
        HuMInLabPlugin.backendRequestHandler.userBackendUpdate(player.getUniqueId().toString(), player.getName());
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

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material material = player.getInventory().getItemInMainHand().getType();
        String objectiveID = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player).getObjectiveID();
        String unit = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player).getUnit();
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);

        switch(unit){
            case "1":
                switch (objectiveID){
                    case "1.2":
                        if(material.equals(Material.WRITTEN_BOOK)){
                            Commands.setStage(player, playerPointer, "1", "1.3");
                        }
                        break;
                    case "1.13":
                        if(material.equals(Material.STONE_AXE)){
                            Commands.setStage(player, playerPointer, "1", "1.14");
                        }
                        break;
                    case "1.15":
                        if(player.getInventory().contains(Material.OAK_PLANKS, 20) && player.getInventory().contains(Material.STICK, 10) && player.getInventory().contains(Material.STONE_AXE, 4)){
                            Commands.setStage(player, playerPointer, "1", "1.16");
                        }
                }
                break;
            case "2":
                switch(objectiveID){
                    case "4.3":
                        if(material.equals(Material.WRITTEN_BOOK)){
                            Commands.setStage(player, playerPointer, "2", "4.4");
                        }
                        break;
                }
        }
    }

    private void quickFrameEval(Player player) {
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        int stick = 0;
        int plank = 0;
        for(GlowItemFrame itemFrame : u2l1ItemFrames){
            if(itemFrame.getItem().getType().equals(Material.STICK)){
                stick++;
            }
            if(itemFrame.getItem().getType().equals(Material.OAK_PLANKS)){
                plank++;
            }
        }
        if(stick == 1 && plank == 1){
            Commands.setStage(player, playerPointer, "2", "2.4");
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        InventoryView inventory = event.getView();
        if(inventory.getTitle().equals("Chest")){
            if(!chests.contains(inventory.getTopInventory().getHolder())){
                //System.out.println("Chest Opened");
                chests.add(inventory.getTopInventory().getHolder());
            }
        }
        if(inventory.getTitle().equals("Large Chest")){
            DoubleChest doubleChest = (DoubleChest) inventory.getTopInventory().getHolder();
            if(!chests.contains(doubleChest.getLeftSide()) && !chests.contains(doubleChest.getRightSide())){
                //System.out.println("Large Chest Opened");
                chests.add(doubleChest.getLeftSide());
                chests.add(doubleChest.getRightSide());
            }
        }
        evalChests((Player)event.getPlayer());
    }

    public static void getChestsL3(Player player) {
        //Shipments
        Location woolShip   = new Location(player.getWorld(), 129, 72, -874);
        Location stoneShip  = new Location(player.getWorld(), 129, 72, -876);
        Location woodShip   = new Location(player.getWorld(), 129, 72, -878);

        lesson3Chests.add((Chest) player.getWorld().getBlockAt(woodShip).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(stoneShip).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(woolShip).getState());

        //Storage
        Location woolStorage    = new Location(player.getWorld(), 125, 72, -878);
        Location stoneStorage   = new Location(player.getWorld(), 125, 72, -876);
        Location woodStorage    = new Location(player.getWorld(), 125, 72, -874);
        Location otherStorage   = new Location(player.getWorld(), 125, 72, -880);

        lesson3Chests.add((Chest) player.getWorld().getBlockAt(woodStorage).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(stoneStorage).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(woolStorage).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(otherStorage).getState());


        //Order

        Location order1 = new Location(player.getWorld(), 121, 72, -874);
        Location order2 = new Location(player.getWorld(), 121, 72, -876);
        Location order3 = new Location(player.getWorld(), 121, 72, -878);

        lesson3Chests.add((Chest) player.getWorld().getBlockAt(order1).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(order2).getState());
        lesson3Chests.add((Chest) player.getWorld().getBlockAt(order3).getState());

        evalChests(player);
    }

    public static void getChestsL4(Player player) {
        //Order

        Location order1 = new Location(player.getWorld(), 127, 72, -813);
        Location order2 = new Location(player.getWorld(), 125, 72, -813);
        Location order3 = new Location(player.getWorld(), 123, 72, -813);

        lesson4Chests.add((Chest) player.getWorld().getBlockAt(order1).getState());
        lesson4Chests.add((Chest) player.getWorld().getBlockAt(order2).getState());
        lesson4Chests.add((Chest) player.getWorld().getBlockAt(order3).getState());

        evalChests(player);
    }

    static void evalChests(Player player){
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        String unit = playerPointer.getUnit();
        String objectiveID = playerPointer.getObjectiveID();

        switch(unit){
            case "1":
                switch(objectiveID){
                    case "3.3":
                        int order = 0;
                        //Order 1
                        Chest order1 = lesson3Chests.get(7);
                        ItemStack order1Item1 = new ItemStack(Material.OAK_PLANKS, 1);
                        ItemStack order1Item2 = new ItemStack(Material.BIRCH_PLANKS, 1);
                        ItemStack order1Item3 = new ItemStack(Material.STICK, 1);

                        if(order1.getBlockInventory().containsAtLeast(order1Item1, 10) && order1.getBlockInventory().containsAtLeast(order1Item2, 12) && order1.getBlockInventory().containsAtLeast(order1Item3, 20)){
                            order++;
                            //System.out.println("3.3 Order 1 Complete");
                        }

                        //Order 2
                        Chest order2 = lesson3Chests.get(8);
                        ItemStack order2Item1 = new ItemStack(Material.WOODEN_AXE, 1);
                        ItemStack order2Item2 = new ItemStack(Material.STONE_SHOVEL, 1);
                        ItemStack order2Item3 = new ItemStack(Material.STONE_PICKAXE, 1);

                        if(order2.getBlockInventory().containsAtLeast(order2Item1, 5) && order2.getBlockInventory().containsAtLeast(order2Item2, 2) && order2.getBlockInventory().containsAtLeast(order2Item3, 3)){
                            order++;
                            //System.out.println("3.3 Order 2 Complete");
                        }

                        //Order 3
                        Chest order3 = lesson3Chests.get(9);
                        ItemStack order3Item1 = new ItemStack(Material.WHITE_BED, 1);
                        ItemStack order3Item2 = new ItemStack(Material.LIGHT_GRAY_BED, 1);
                        ItemStack order3Item3 = new ItemStack(Material.GRAY_BED, 1);

                        if(order3.getBlockInventory().containsAtLeast(order3Item1, 2) && order3.getBlockInventory().containsAtLeast(order3Item2, 1) && order3.getBlockInventory().containsAtLeast(order3Item3, 1)){
                            order++;
                            //System.out.println("3.3 Order 3 Complete");
                        }

                        if(order == 3){
                            //System.out.println("3.3 All Orders Complete");
                            player.sendMessage("All Orders Complete");
                            Commands.setStage(player, playerPointer, "1", "3.4");
                        }
                        break;
                    case "4.5":
                        order = 0;
                        //Order 1
                        order1 = lesson4Chests.get(0);
                        order1Item1 = new ItemStack(Material.CHARCOAL, 1);
                        order1Item2 = new ItemStack(Material.IRON_INGOT, 1);
                        order1Item3 = new ItemStack(Material.COPPER_INGOT, 1);
                        ItemStack order1Item4 = new ItemStack(Material.GLASS, 1);
                        ItemStack order1Item5 = new ItemStack(Material.SMOOTH_STONE, 1);

                        if(order1.getBlockInventory().containsAtLeast(order1Item1, 20) && order1.getBlockInventory().containsAtLeast(order1Item2, 4) && order1.getBlockInventory().containsAtLeast(order1Item3, 5) && order1.getBlockInventory().containsAtLeast(order1Item4, 10) && order1.getBlockInventory().containsAtLeast(order1Item5, 11)){
                            order++;
                        }

                        //Order 2
                        order2 = lesson4Chests.get(1);
                        order1Item1 = new ItemStack(Material.IRON_AXE, 1);
                        order1Item2 = new ItemStack(Material.IRON_PICKAXE, 1);
                        order1Item3 = new ItemStack(Material.TORCH, 1);
                        order1Item4 = new ItemStack(Material.SHEARS, 1);

                        if(order2.getBlockInventory().containsAtLeast(order1Item1, 6) && order2.getBlockInventory().containsAtLeast(order1Item2, 6) && order2.getBlockInventory().containsAtLeast(order1Item3, 10) && order2.getBlockInventory().containsAtLeast(order1Item4, 3)){
                            order++;
                        }

                        //Order 3
                        order3 = lesson4Chests.get(2);
                        order1Item1 = new ItemStack(Material.BOOK, 1);
                        order1Item2 = new ItemStack(Material.BOOKSHELF, 1);

                        if(order3.getBlockInventory().containsAtLeast(order1Item1, 7) && order3.getBlockInventory().containsAtLeast(order1Item2, 3)){
                            order++;
                        }

                        if (order == 3){
                            player.sendMessage("All Orders Complete");
                            Commands.setStage(player, playerPointer, "1", "4.6");
                        }
                }
            case "2":
                switch(objectiveID){
                    case "3.3":
                        //TODO
                        //get chest at 149 72 -839
                        Chest u2Lesson3SantiChest = (Chest) player.getWorld().getBlockAt(149, 72, -839).getState();
                        Inventory chestInv = u2Lesson3SantiChest.getInventory();
                        ItemStack item1 = new ItemStack(Material.BOOK, 1);
                        ItemMeta item1Meta = item1.getItemMeta();
                        item1Meta.setDisplayName("Contraption 3");
                        item1.setItemMeta(item1Meta);
                        if(chestInv.containsAtLeast(item1, 1)){
                            Commands.setStage(player, playerPointer, "2", "3.4");
                        }
                    case "2.4.6":
                        //TODO
                        //get chest at 149 72 -839
                        u2Lesson3SantiChest = (Chest) player.getWorld().getBlockAt(149, 72, -839).getState();
                        chestInv = u2Lesson3SantiChest.getInventory();

                        ItemStack blue = new ItemStack(Material.BLUE_DYE, 1);
                        ItemStack red = new ItemStack(Material.RED_DYE, 1);
                        ItemStack green = new ItemStack(Material.GREEN_DYE, 1);
                        ItemStack yellow = new ItemStack(Material.YELLOW_DYE, 1);

                        if(chestInv.containsAtLeast(blue,1) && chestInv.containsAtLeast(red,1) && chestInv.containsAtLeast(green,1) && chestInv.containsAtLeast(yellow,1)){
                            Commands.setStage(player, playerPointer, "2", "4.7");
                        }
                }
        }
        return;
    }

}
