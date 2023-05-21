package org.huminlabs.huminlabplugin.NPC;

public class Dialogue {
    public String id;
    public String unit;
    public String[] dialog;
    public String[] response;
    public String[] trigger;
    public String actor;

    public Dialogue(String ID, String[] dialog, String[] response, String[] trigger, String Actor) {
        this.id = ID;
        this.dialog = dialog;
        this.response = response;
        this.trigger = trigger;
        this.actor = Actor;
    }

    public String getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public String[] getDialog() {
        return dialog;
    }

    public String[] getResponse() {
        return response;
    }

    public String[] getTrigger() {
        return trigger;
    }

    public String getActor() {
        return actor;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
