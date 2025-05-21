package net.lymox.citybuild.manager;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationsManager {

    private File file;
    private YamlConfiguration configuration;

    public LocationsManager() {
        if(!CitybuildPlugin.getInstance().getDataFolder().exists()){
            CitybuildPlugin.getInstance().getDataFolder().mkdir();
        }

        file = new File(CitybuildPlugin.getInstance().getDataFolder().getPath(), "locations.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public List<Location> getLocations(){
        List<Location> locations = new ArrayList<>();
        for (String key : configuration.getConfigurationSection("").getKeys(false)) {
            locations.add(configuration.getLocation(key));
        }
        return locations;
    }

    public List<String> getWarps(){
        ConfigurationSection section = configuration.getConfigurationSection("Warp");
        if (section == null) {
            return List.of();  // oder Collections.emptyList()
        }
        List<String> keys = new ArrayList<>(section.getKeys(false));
        return keys;
    }

    public void add(Location location, String name){
        configuration.set(name, location);
        save();
    }

    public void addWarp(Location location, String name){
        configuration.set("Warp."+name, location);
        save();
    }

    public void remove(String name){
        configuration.set(name, null);
        save();
    }

    public Location get(String name){
        configuration = YamlConfiguration.loadConfiguration(file);
        return configuration.getLocation(name);
    }

    public void save(){
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
