package org.huminlabs.huminlabplugin.NPC;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import org.huminlabs.huminlabplugin.Objective.ObjectiveStorage;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

public class DialogueManager {



    Gson gson = new Gson();
    private ArrayList<Dialogue> serenityDialogues;
    private ArrayList<Dialogue> victoriaDialogues;
    private ArrayList<Dialogue> zionDialogues;
    private ArrayList<Dialogue> mayorDialogues;
    private ArrayList<Dialogue> santiagoDialogues;

    public DialogueManager() throws FileNotFoundException {
        serenityDialogues = new ArrayList<>();
        victoriaDialogues = new ArrayList<>();
        zionDialogues = new ArrayList<>();
        mayorDialogues = new ArrayList<>();
        santiagoDialogues = new ArrayList<>();

        loadDialogues();
    }

    public void loadDialogues() throws FileNotFoundException {

        Gson gson = new Gson();

        //Load unit 1 dialogues
        Dialogue[] unit1Dialogues;
        File file = new File("/static/NPC-Dialogue/Unit_1_Dialogue.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            unit1Dialogues = gson.fromJson(reader, Dialogue[].class);

            for(Dialogue dialogue : unit1Dialogues) {
                dialogue.setUnit("1");
            }
            System.out.println("Unit 1 loaded: " + unit1Dialogues.length);
            skimJson(unit1Dialogues);
        }
        else {
            System.out.println("Dialogue file not found!");
            System.out.println("/static/NPC-Dialogue/Unit_1_Dialogue.json");
        }

        //Load unit 2 dialogues
        Dialogue[] unit2Dialogues;
        file = new File("/static/NPC-Dialogue/Unit_2_Dialogue.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            unit2Dialogues = gson.fromJson(reader, Dialogue[].class);

            for(Dialogue dialogue : unit2Dialogues) {
                dialogue.setUnit("2");
            }

            skimJson(unit2Dialogues);
        }
        else {
            System.out.println("Dialogue file not found!");
            System.out.println("/static/NPC-Dialogue/Unit_2_Dialogue.json");
        }

    }

    private void skimJson(Dialogue[] dialogues) {
        for (Dialogue dialogue : dialogues) {
            switch (dialogue.getActor()) {
                case "Serenity":
                    serenityDialogues.add(dialogue);
                    break;
                case "Victoria":
                    victoriaDialogues.add(dialogue);
                    break;
                case "Zion":
                    zionDialogues.add(dialogue);
                    break;
                case "Mayor Goodway":
                    mayorDialogues.add(dialogue);
                    break;
                case "Santiago":
                    santiagoDialogues.add(dialogue);
                    break;
                default:
                    System.out.println("Dialogue actor not found: " + dialogue.getActor());
                    break;
            }
        }

        System.out.println("Serinity: " + serenityDialogues.size());
        System.out.println("Victoria: " + victoriaDialogues.size());
        System.out.println("Zion: " + zionDialogues.size());
        System.out.println("Mayor: " + mayorDialogues.size());
        System.out.println("Santiago: " + santiagoDialogues.size());
    }

    public Dialogue[] getDialogue(String actor){
        switch (actor) {
            case "Serenity":
                return serenityDialogues.toArray(new Dialogue[serenityDialogues.size()]);
            case "Victoria":
                return victoriaDialogues.toArray(new Dialogue[victoriaDialogues.size()]);
            case "Zion":
                return zionDialogues.toArray(new Dialogue[zionDialogues.size()]);
            case "Mayor Goodway":
                return mayorDialogues.toArray(new Dialogue[mayorDialogues.size()]);
            case "Santiago":
                return santiagoDialogues.toArray(new Dialogue[santiagoDialogues.size()]);
            default:
                System.out.println("Dialogue actor not found: " + actor);
                return null;
        }
    }


}
