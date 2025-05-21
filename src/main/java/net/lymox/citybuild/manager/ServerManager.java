package net.lymox.citybuild.manager;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ServerManager {

    private File file;
    private YamlConfiguration configuration;

    public ServerManager() {
        if(!CitybuildPlugin.getInstance().getDataFolder().exists()){
            CitybuildPlugin.getInstance().getDataFolder().mkdir();
        }

        file = new File(CitybuildPlugin.getInstance().getDataFolder().getPath(), "configuration.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
        loadDefaults();
    }

    private void loadDefaults(){
        configuration.addDefault("maintenanceMode", true);
        configuration.addDefault("permissionMaintenanceJoin", "lymox.citybuild.maintenance.bypass");
        save();
    }

    public void setMaintenance(boolean value){
        configuration.set("maintenanceMode", value);
        save();
    }

    public boolean isMaintenance(){
        return configuration.getBoolean("maintenanceMode");
    }

    public String getMaintenancePermission(){
        return configuration.getString("permissionMaintenanceJoin");
    }

    public void save(){
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
