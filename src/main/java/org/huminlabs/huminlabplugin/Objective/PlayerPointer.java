package org.huminlabs.huminlabplugin.Objective;

public class PlayerPointer {
    private String uuid;
    private String objectiveID;
    private String unit;

    public PlayerPointer(String uuid) {
        this.uuid = uuid;
        this.objectiveID = "1.0";
        this.unit = "1";
    }

    public String getUUID() {
        return uuid;
    }

    public String getObjectiveID() {
        return objectiveID;
    }
    public String getUnit() {
        return unit;
    }

    public void setObjective(String unit, String objectiveID) {
        this.unit = unit;
        this.objectiveID = objectiveID;
    }

//    public void setObjectiveID(String objectiveID) {
//        this.objectiveID = objectiveID;
//    }
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }



}