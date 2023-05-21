package org.huminlabs.huminlabplugin;

import com.comphenix.protocol.PacketType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.Objective.Objective;
import org.huminlabs.huminlabplugin.Objective.ObjectiveStorage;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

public final class Commands implements CommandExecutor {
    private final HuMInLabPlugin plugin;

    public Commands(HuMInLabPlugin plugin){
        this.plugin = plugin;
    }

    // === Commands ===
    //
    //    getstage
    //    setstage <unit> <lessonID>
    //    nextstage
    //    prevstage

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            PlayerPointer pointer = ObjectiveStorage.getPlayerPointer(player);
            if(pointer != null) {
                switch (command.getName()) {
                    case "getstage":
                        getStage(player, pointer);
                        break;
                    case "setstage":
                        if(args.length != 2) {
                            player.sendMessage("Error: Invalid number of arguments");
                            return false;
                        }
                        String unit = args[0];
                        String lessonID = args[1];

                        setStage(player, pointer, unit, lessonID);
                        break;
                    case "nextstage":
                        nextStage(player, pointer);
                        break;
                    case "prevstage":
                        prevStage(player, pointer);
                        break;
                }
            }
        }
        return true;
    }
    private void getStage(Player player, PlayerPointer pointer) {
        player.sendMessage("Your current stage is: " + pointer.getUnit() + " " + pointer.getObjectiveID());
    }
    private void setStage(Player player, PlayerPointer pointer, String unit, String lessonID) {
        if(ObjectiveStorage.getObjective(lessonID, unit) != null) {
            pointer.setObjective(unit, lessonID);
            player.sendMessage("Stage set: " + pointer.getUnit() + " " + pointer.getObjectiveID());
            try{
                ObjectiveStorage.savePlayerPointers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            player.sendMessage("Error: Stage not found");
        }
    }
    private void nextStage(Player player, PlayerPointer pointer) {
        ObjectiveStorage.setNextObjective(player);
        player.sendMessage("Stage set: " + pointer.getUnit() + " " + pointer.getObjectiveID());
    }
    private void prevStage(Player player, PlayerPointer pointer) {
        ObjectiveStorage.setPrevObjective(player);
        player.sendMessage("Stage set: " + pointer.getUnit() + " " + pointer.getObjectiveID());
    }






}
