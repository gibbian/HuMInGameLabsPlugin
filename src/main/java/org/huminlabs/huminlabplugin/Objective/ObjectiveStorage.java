package org.huminlabs.huminlabplugin.Objective;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ObjectiveStorage {
    private static ArrayList<Objective> objectives = new ArrayList<>();
    private static ArrayList<PlayerPointer> pointers = new ArrayList<>();

    public ObjectiveStorage() {
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

    public static Objective getObjective(String id) {
        for(Objective objective : objectives) {
            if(objective.getId().equals(id)) {
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
}