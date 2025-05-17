package net.lymox.citybuild.manager;

import org.bukkit.entity.Player;

public class Managers {

    private LocationsManager locationsManager;
    private CratesManager cratesManager;

    public Managers() {
        this.locationsManager = new LocationsManager();
        this.cratesManager = new CratesManager();
    }

    public LocationsManager getLocationsManager(){
        return locationsManager;
    }

    public ScoreboardManger getScoreboardManager(){
        return new ScoreboardManger();
    }

    public CratesManager getCratesManager() {
        return cratesManager;
    }
}
