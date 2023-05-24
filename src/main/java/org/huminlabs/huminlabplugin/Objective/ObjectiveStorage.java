package org.huminlabs.huminlabplugin.Objective;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;
import org.bukkit.boss.BossBar;
import org.huminlabs.huminlabplugin.NPC.NPC;
import org.huminlabs.huminlabplugin.NPC.NPCManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ObjectiveStorage {
    private final HuMInLabPlugin plugin;
    private static BossBar bossBar;
    private static ArrayList<Objective> objectives = new ArrayList<>();
    private static ArrayList<PlayerPointer> pointers = new ArrayList<>();

    public ObjectiveStorage(HuMInLabPlugin plugin) {
        this.plugin = plugin;
        bossBar = plugin.getServer().createBossBar("HuMIn Game Labs", org.bukkit.boss.BarColor.BLUE, org.bukkit.boss.BarStyle.SOLID);
        try {
            loadObjectives();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            loadPlayerPointers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadObjectives() throws IOException {
        Gson gson = new Gson();

        File file = new File(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/Objectives/Unit_1_Objectives.json");
        if(file.exists()){
            Reader reader = new FileReader(file);
            Objective[] o = gson.fromJson(reader, Objective[].class);
            for(Objective objective : o) {
                objective.setUnit("1");
            }
            objectives = new ArrayList<>(Arrays.asList(o));
            System.out.println("Unit 1 loaded: " + o.length);

        }
        else {
            System.out.println("Unit 1 Objectives not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }


        file = new File(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/Objectives/Unit_2_Objectives.json");
        if(file.exists()){
            Reader reader = new FileReader(file);
            Objective[] o = gson.fromJson(reader, Objective[].class);
            for(Objective objective : o) {
                objective.setUnit("2");
            }
            objectives.addAll(new ArrayList<>(Arrays.asList(o)));
            System.out.println("Unit 2 loaded: " + o.length);
        }
        else {
            System.out.println("Unit 2 Objectives not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }
    }

    public static Objective getObjective(String id, String unit) {
        for(Objective objective : objectives) {
            if(objective.getId().equals(id) && objective.getUnit().equals(unit)) {
                return objective;
            }
        }
        return null;
    }

    public static void addPlayerPointer(Player player) throws IOException {

        for(PlayerPointer p : pointers){
            if(p.getUUID().equals(player.getUniqueId().toString())){
                return;
            }
        }
        PlayerPointer pointer = new PlayerPointer(player.getUniqueId().toString());
        pointers.add(pointer);



        savePlayerPointers();
    }

    public static void loadPlayerPointers() throws IOException{
        Gson gson = new Gson();
        File file = new File(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/PlayerData/PlayerPointers.json");
        if(file.exists()){
            Reader reader = new FileReader(file);
            PlayerPointer[] p = gson.fromJson(reader, PlayerPointer[].class);;
            pointers = new ArrayList<>(Arrays.asList(p));
            System.out.println("PlayerPointers loaded: " + p.length);

        }
        else {
            System.out.println("PlayerPointers file not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }
    }

    public static void savePlayerPointers() throws IOException {
        Gson gson = new Gson();
        File file = new File(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/PlayerData/PlayerPointers.json");
        file.getParentFile().mkdirs();
        file.createNewFile();

        Writer writer = new FileWriter(file, false);
        gson.toJson(pointers, writer);
        writer.flush();
        writer.close();
        System.out.println("PlayerPointers saved!");

    }

    public static PlayerPointer getPlayerPointer(Player player) {
        String uuid = player.getUniqueId().toString();
        for(PlayerPointer pointer : pointers) {
            if(pointer.getUUID().equals(uuid)) {
                return pointer;
            }
        }
        System.out.println("HuMInPlugin Error: PlayerPointer not found for " + player.getName() + " (" + uuid + ")");
        return null;
    }

    public static void setNextObjective(Player player){
        PlayerPointer pointer = getPlayerPointer(player);
        if(pointer != null){
            for(int i = 0; i < objectives.size(); i++){
                if(objectives.get(i).getId().equals(pointer.getObjectiveID()) && objectives.get(i).getUnit().equals(pointer.getUnit())){
                    if(i + 1 < objectives.size()){
                        pointer.setObjective(objectives.get(i + 1).getUnit(), objectives.get(i + 1).getId());
                        try {
                            savePlayerPointers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }

    public static void setPrevObjective(Player player) {
        PlayerPointer pointer = getPlayerPointer(player);
        if(pointer != null){
            for(int i = 0; i < objectives.size(); i++){
                if(objectives.get(i).getId().equals(pointer.getObjectiveID()) && objectives.get(i).getUnit().equals(pointer.getUnit())){
                    if(i - 1 >= 0){
                        pointer.setObjective(objectives.get(i - 1).getUnit(), objectives.get(i - 1).getId());
                        try {
                            savePlayerPointers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }

    public static void updateObjective(Player player){
        PlayerPointer playerPointer = getPlayerPointer(player);
        Objective objective = getObjective(playerPointer.getObjectiveID(), playerPointer.getUnit());
        String unit = playerPointer.getUnit();
        String ID = playerPointer.getObjectiveID();


        if(playerPointer == null){
            try{
                addPlayerPointer(player);
            }
            catch(Exception e) {
                System.out.println("Player pointer not found!");
            }
        }
        if(unit == "0" && ID == "0.0"){
            bossBar.setVisible(false);
        }
        else if(unit != null && ID != null) {
            System.out.println(objective.getObjective());
            handleBossBar(player, objective.getObjective());
            handleCompass(player, objective.getLocation());
            playSound(player);

            handleWorldChange(unit, ID);
            handleObjectiveItems(unit, ID, player);

            handleActorPositions(unit, ID);

        }
    }
    static void handleBossBar(Player player, String task){
        bossBar.setTitle(task);
        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
        if(bossBar.getPlayers().size() == 0) {
            bossBar.addPlayer(player);
        }
    }
    static void playSound(Player player){
        player.playSound(player.getLocation(), "minecraft:block.note_block.pling", 1, 1);
    }
    static void handleCompass(Player player, int[] location){
        //if player has compass
        if(!player.getInventory().contains(Material.COMPASS)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
        player.setCompassTarget(new Location(player.getWorld(), location[0], 70, location[1]));
    }

    static void handleWorldChange(String unit, String ID){
        switch(unit){
            case "1":
                switch(ID){
                    case "1.0":
                        resetWorld();
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 131 65 -872 120 62 -882 117 70 -881");
                        break;
                    case "5.0":
                        resetWorld();
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 118 50 -868 100 54 -881 113 70 -882");
                        break;
                }
                break;
            case "2":
                switch (ID){
                    case "2.0":
                        resetWorld();
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 117 48 -886 128 43 -877 119 70 -878");
                        break;
                }
                break;
        }
    }
    static void resetWorld(){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "fill 131 71 -869 111 74 -886 air");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:item]");
    }

    static void handleObjectiveItems(String unit, String ID, Player player){
        switch(unit){
            case "1":
                switch(ID){
                    case "1.2":
                        if(!player.getInventory().contains(Material.WRITTEN_BOOK)) {
                            //System.out.println("Making book");
                            ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
                            BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                            bookMeta.setTitle("Resource List");
                            bookMeta.setAuthor("Victoria");
                            bookMeta.addPage("Resource List:\n-20 Oak Planks\n-10 Sticks\n-4 Stone Axes");

                            bookItem.setItemMeta(bookMeta);
                            player.getInventory().addItem(bookItem);
                        }
                        break;
                    case "1.6.1":
                        ItemStack craft = new ItemStack(Material.CRAFTING_TABLE);
                        player.getInventory().addItem(craft);
                        break;
                    case "1.6.2":
                        craft = new ItemStack(Material.CRAFTING_TABLE);
                        player.getInventory().addItem(craft);
                        break;
                    case "1.13":
                        ItemStack sticks = new ItemStack(Material.STICK, 2);
                        ItemStack cobble = new ItemStack(Material.COBBLESTONE, 3);
                        player.getInventory().addItem(sticks);
                        player.getInventory().addItem(cobble);
                        break;
                    case "1.14.1":
                        ItemStack chests = new ItemStack(Material.CHEST, 3);
                        player.getInventory().addItem(chests);
                        break;

                    case "4.2":
                        chests = new ItemStack(Material.CHEST, 32);
                        player.getInventory().addItem(chests);
                        chests = new ItemStack(Material.ITEM_FRAME, 16);
                        player.getInventory().addItem(chests);
                        break;
                    case "4.5":
                        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                        bookMeta.setTitle("Order List");
                        bookMeta.setAuthor("Victoria");
                        bookMeta.addPage("Smelting Order:\n-20x Charcoal\n-4x Iron Ingots\n-5x Copper Ingot\n-10x Glass\n-11x Smooth Stone");
                        bookMeta.addPage("Tool Order:\n-6x Iron Axes\n-6x Iron Pixaxes\n-10x Torches\n-3x Shears");
                        bookMeta.addPage("Book Order:\n-7x Books\n-3x Bookshelves");

                        bookItem.setItemMeta(bookMeta);
                        player.getInventory().addItem(bookItem);
                        break;

                    case "5.6":
                        player.getInventory().addItem(new ItemStack(Material.CHEST, 10));
                        player.getInventory().addItem(new ItemStack(Material.OAK_SIGN, 4));
                        player.getInventory().addItem(new ItemStack(Material.ITEM_FRAME, 7));
                        player.getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK));
                        break;
                    case "5.13":
                        bookItem = new ItemStack(Material.WRITTEN_BOOK);
                        bookMeta = (BookMeta) bookItem.getItemMeta();
                        bookMeta.setTitle("Order List");
                        bookMeta.setAuthor("Victoria");
                        bookMeta.addPage("Order:\n0001 - 3\n0013 - 2\n0108 - 3\n1112 - 3\n-1110 - 2");

                        bookItem.setItemMeta(bookMeta);
                        player.getInventory().addItem(bookItem);
                        break;
                }
                break;
            case "2":
                switch(ID){
                    case "2.3":
                        ItemStack OakSigns = new ItemStack(Material.OAK_SIGN, 6);
                        ItemStack OakPlanks = new ItemStack(Material.OAK_PLANKS, 6);
                        ItemStack Sticks = new ItemStack(Material.STICK, 6);
                        ItemStack Cobbles = new ItemStack(Material.COBBLESTONE, 6);
                        ItemStack Coal = new ItemStack(Material.COAL, 6);

                        player.getInventory().addItem(OakSigns);
                        player.getInventory().addItem(OakPlanks);
                        player.getInventory().addItem(Sticks);
                        player.getInventory().addItem(Cobbles);
                        player.getInventory().addItem(Coal);
                        break;

                    case "3.3":
                        //Chest, oak planks, sticks, barrier block named "Recycler"
                        ItemStack Chest = new ItemStack(Material.CHEST, 1);
                        OakPlanks = new ItemStack(Material.OAK_PLANKS, 6);
                        Sticks = new ItemStack(Material.STICK, 6);
                        ItemStack Barrier = new ItemStack(Material.BARRIER, 1);
                        ItemMeta meta = Barrier.getItemMeta();
                        meta.setDisplayName("Recycler");
                        Barrier.setItemMeta(meta);

                        player.getInventory().addItem(Chest);
                        player.getInventory().addItem(OakPlanks);
                        player.getInventory().addItem(Sticks);
                        player.getInventory().addItem(Barrier);
                        break;

                    case "4.6":
                        ItemStack chests = new ItemStack(Material.CHEST, 10);
                        ItemStack hopper = new ItemStack(Material.HOPPER, 5);
                        ItemStack itemFrame = new ItemStack(Material.ITEM_FRAME, 5);
                        ItemStack furnace = new ItemStack(Material.FURNACE, 1);
                        ItemStack coal = new ItemStack(Material.COAL, 10);
                        ItemStack dandelion = new ItemStack(Material.DANDELION, 10);
                        ItemStack poppy = new ItemStack(Material.POPPY, 10);
                        ItemStack cornflower = new ItemStack(Material.CORNFLOWER, 10);
                        ItemStack cactus = new ItemStack(Material.CACTUS, 10);

                        player.getInventory().addItem(chests);
                        player.getInventory().addItem(hopper);
                        player.getInventory().addItem(itemFrame);
                        player.getInventory().addItem(furnace);
                        player.getInventory().addItem(coal);
                        player.getInventory().addItem(dandelion);
                        player.getInventory().addItem(poppy);
                        player.getInventory().addItem(cornflower);
                        player.getInventory().addItem(cactus);

                        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                        bookMeta.setTitle("ToDo: Dyes");
                        bookMeta.setAuthor("Serinity");
                        bookMeta.addPage("-Yellow Dye\n-Red Dye\n-Blue Dye\n-Green Dye\n-Cyan Dye\n-Purple Dye\n-Orange Dye");
                        bookItem.setItemMeta(bookMeta);
                        player.getInventory().addItem(bookItem);
                        break;
                }
                break;
        }
    }

    static void handleActorPositions(String unit, String ID){
        switch(unit) {
            case "1":
                switch (ID) {
                    case "1.0":
                        resetActorPositions(); //This function is only run for the first ID of the lesson
                        // Serenity     [156, -908]
                        // Victoria     [128, -844]
                        // Zion         [131,-822]
                        HuMInLabPlugin.npcManager.setNPCPos("Serinity", 156, 71, -908);
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 128, 71,-844);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 131, 71,-822);

                        break;
                    case "1.18":
                        // Mayor Goodway  [128, -844]
                        HuMInLabPlugin.npcManager.setNPCPos("Mayor Goodway", 128, 71,-844);
                        break;
                    case "3.0":
                        resetActorPositions();
                        // Serenity     [156, -908]
                        // Zion         [128, -844]
                        HuMInLabPlugin.npcManager.setNPCPos("Serinity", 156, 71, -908);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 128, 71,-844);
                        break;
                    case "4.0":
                        resetActorPositions();
                        // Serenity     [156, -908]
                        // Zion         [131,-822]
                        HuMInLabPlugin.npcManager.setNPCPos("Serinity", 156, 71, -908);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 131, 71,-822);
                        break;
                    case "5.0":
                        resetActorPositions();
                        // Serenity     [156, -908]
                        // Victoria     [128, -844]
                        HuMInLabPlugin.npcManager.setNPCPos("Serinity", 156, 71, -908);
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 128, 71,-844);
                        break;
                }
                break;
            case "2":
                switch (ID) {
                    case "2.0":
                        resetActorPositions();
                        // Victoria     [130,-870]
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 130, 71,-870);
                        break;
                    case "3.0":
                        resetActorPositions();
                        // Santiago     [150,-875]
                        // Zion         [131,-822]
                        HuMInLabPlugin.npcManager.setNPCPos("Santiago", 150, 71,-875);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 131, 71,-822);
                        break;
                    case "4.0":
                        resetActorPositions();
                        // Victoria     [128,-844]
                        // Zion         [131,-822]
                        // Santiago     [150,-875]
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 128, 71,-844);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 131, 71,-822);
                        HuMInLabPlugin.npcManager.setNPCPos("Santiago", 150, 71,-875);
                        break;
                }
                break;
        }
    }
    static void resetActorPositions(){
        HuMInLabPlugin.npcManager.setNPCPos("Serinity", 156, 71, -908);
        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 128, 0,-844);
        HuMInLabPlugin.npcManager.setNPCPos("Zion", 131, 0,-822);
        HuMInLabPlugin.npcManager.setNPCPos("Mayor Goodway", 128, 0,-844);
        HuMInLabPlugin.npcManager.setNPCPos("Santiago", 150, 0,-875);
    }

}
