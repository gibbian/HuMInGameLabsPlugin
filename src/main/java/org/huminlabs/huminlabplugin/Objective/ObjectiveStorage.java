package org.huminlabs.huminlabplugin.Objective;

import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;
import org.bukkit.boss.BossBar;
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

    public static void updateWorld(Player player){
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
}
