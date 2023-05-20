package org.huminlabs.huminlabplugin.NPC;

import org.huminlabs.huminlabplugin.HuMInLabPlugin;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
        File file = new File(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/NPC-Dialogue/Unit_1_Dialogue.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            unit1Dialogues = gson.fromJson(reader, Dialogue[].class);
            System.out.println("====== loading Unit 1 dialogue ======");

            skimJson(unit1Dialogues);
        }
        else {
            System.out.println("Dialogue file not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/NPC-Dialogue/Unit_1_Dialogue.json");
        }

        //Load unit 2 dialogues
        Dialogue[] unit2Dialogues;
        file = new File(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/NPC-Dialogue/Unit_2_Dialogue.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            unit2Dialogues = gson.fromJson(reader, Dialogue[].class);
            System.out.println("====== loading Unit 2 dialogue ======");

            skimJson(unit2Dialogues);
        }
        else {
            System.out.println("Dialogue file not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath() + "/NPC-Dialogue/Unit_2_Dialogue.json");
        }

    }

    private void skimJson(Dialogue[] unit2Dialogues) {
        for (Dialogue dialogue : unit2Dialogues) {
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

        System.out.println("====== loading Unit 2 dialogue ======");

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
