package net.lymox.citybuild.utils;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.userdata.Crate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List<Crate> getCrates(){
        List<Crate> crates = new ArrayList<>();
        if(configuration.contains("crate")) {
            for (String crate : configuration.getConfigurationSection("crate").getKeys(false)) {
                try {
                    int id = Integer.parseInt(crate);
                    if (CitybuildPlugin.getInstance().getManagers().getCratesManager().getCrate(id)!=null){
                        int amount = configuration.getInt("crate." + id + ".amount");
                        crates.add(new Crate(id, amount));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return crates;
    }

    public Crate getCrate(int id){
        for (Crate crate : getCrates()) {
            if(crate.getId()==id){
                return crate;
            }
        }
        return null;
    }

    public void addCrate(int id, int amount){
        Crate crate = getCrate(id);
        if(crate!=null){
            configuration.set("crate."+crate.getId()+".amount", crate.getAmount()+amount);
        }else {
            configuration.set("crate."+id+".amount", amount);
        }
        save();
    }

    public void addCrate(Crate crate, int amount){
        configuration.set("crate."+crate.getId()+".amount", crate.getAmount()+amount);
        save();
    }

    public void removeCrate(int id, int amount){
        Crate crate = getCrate(id);
        if(crate!=null){
            if(crate.getAmount()<amount){
                configuration.set("crate."+crate.getId(), null);
            }else {
                configuration.set("crate." + crate.getId() + ".amount", crate.getAmount() - amount);
            }
        }
        save();
    }

    public void removeCrate(Crate crate, int amount){
        if(crate.getAmount()<amount){
            configuration.set("crate."+crate.getId(), null);
        }else {
            configuration.set("crate." + crate.getId() + ".amount", crate.getAmount() - amount);
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
