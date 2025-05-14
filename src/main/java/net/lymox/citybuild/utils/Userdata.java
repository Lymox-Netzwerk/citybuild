package net.lymox.citybuild.utils;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Userdata {

    private UUID uuid;
    private File file;
    private YamlConfiguration configuration;

    public Userdata(UUID uuid) {
        this.uuid = uuid;

        File folder = new File(CitybuildPlugin.getInstance().getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        file = new File(CitybuildPlugin.getInstance().getDataFolder().getPath()+"/userdata", uuid.toString()+".yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public Integer getMünzen(){
        if(!configuration.contains("geld")){
            configuration.set("geld", 0);
            save();
        }
        return configuration.getInt("geld");
    }

    public void setMünzen(int amount){
        configuration.set("geld", amount);
        save();
    }

    public void addMünzen(int amount){
        configuration.set("geld", getMünzen()+amount);
        save();
    }

    public void removeMünzen(int amount){
        if(getMünzen()<amount){
            configuration.set("geld", 0);
        }else {
            configuration.set("geld", getMünzen()-amount);
        }
        save();
    }

    private void save(){
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
