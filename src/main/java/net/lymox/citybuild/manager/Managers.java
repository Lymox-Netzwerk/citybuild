package net.lymox.citybuild.manager;

import org.bukkit.entity.Player;

public class Managers {

    private LocationsManager locationsManager;

    public Managers() {
        this.locationsManager = new LocationsManager();
    }

    public LocationsManager getLocationsManager(){
        return locationsManager;
    }

    public ScoreboardManger getScoreboardManager(){
        return new ScoreboardManger();
    }

}
