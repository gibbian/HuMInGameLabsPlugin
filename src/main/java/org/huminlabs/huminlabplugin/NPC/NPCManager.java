package org.huminlabs.huminlabplugin.NPC;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;
import org.huminlabs.huminlabplugin.NPC.NPC;

public class NPCManager {
    private HuMInLabPlugin plugin;

    public NPCManager(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadNPCs(Player player) {
        System.out.println("===NPC loaded! Update");

        //Serenity
        String signature = "yMUQXd/k4q1vgDKVgWTAsaGvn42gce9gETko6RSQ9vxiRVq5MemCQuDS9w0rS/6ejoFXPDtbdu4l5lFzFEnqCS9SRu1G68Fbjp64WukrzskA+/rc/rutpVt3QlLqMzJGabsMj8yoliu2MxTAT8csSuLrKoHyuDQOHxRFq9qb/C8TiGUDMzJNdxwG+QHceoi2sGlHI5GUWzW3JVmUOCQX/HcbQ60Qwt5jCE9OeWgDlJ6gWBSuUPT/1VnbNlqe1ugVxiCiZltn20B15EHqB/If4/upxPEiKB+TAwnH08D631cNBIId/cc1ytFh6UdLG47/i+H5JqIrCPeu84era08Edr85Iz3hI3vgpdk0Z4x2ehz0mhMPIYL9oEhMk20slgqrNhOgVCIsRUy4wAV1Q4ODCEDBfjYOVzyWCufR8Qexw4So1OpoK3R2k2x7xSIvslphq9ebG99C+4UjlSm1xes5Oc1VwEA+HH7x6pOy2jOLbhHzTZOD0UgjEqMWW8G1TNKBX/nbaYrYtOOuHdquJ+RsILfMVUNsHC+XOd1Tvj8Nw04pf1lYQCgLDnas+3/3KdZMAyOVvTtyAgIqPsM+KaPJKfuBo/tfwWf3C8gZx2tOcv4DLnVgtUQJZkLSBjBt6whXlGo5N99ViyZHCKt3lhEMX1hVXb/ac/TTG9n7L7VHGxQ=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3ODYzODc1NjM0OSwKICAicHJvZmlsZUlkIiA6ICIyMWUzNjdkNzI1Y2Y0ZTNiYjI2OTJjNGEzMDBhNGRlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZXlzZXJNQyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lY2MwZGE4ZTY4NDUyOTk0YjBjMjQwYjcyZmMxN2Y4M2UwNDBiNDMyYTY4NDNiNjIxOTRjYmI5NWJkMWFkNDRmIgogICAgfQogIH0KfQ==";

        NPC serenity = new NPC("Serenity", signature, texture, plugin, player);

        serenity.spawnNPC(player, 153.5, 71, -907.5);
    }
}
