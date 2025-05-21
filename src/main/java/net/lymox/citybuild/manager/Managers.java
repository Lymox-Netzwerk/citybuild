package net.lymox.citybuild.manager;

import org.bukkit.entity.Player;

public class Managers {

    private LocationsManager locationsManager;
    private CratesManager cratesManager;
    private ShopManager shopManager;
    private ServerManager serverManager;

    public Managers() {
        this.locationsManager = new LocationsManager();
        this.cratesManager = new CratesManager();
        this.shopManager = new ShopManager();
        this.serverManager = new ServerManager();
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

    public ShopManager getShopManager() {
        return shopManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
