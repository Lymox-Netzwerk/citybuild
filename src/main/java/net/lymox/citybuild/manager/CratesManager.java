package net.lymox.citybuild.manager;

import net.lymox.citybuild.manager.objects.crates.Crate;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Converter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CratesManager {

    private File file;
    private YamlConfiguration configuration;

    public CratesManager() {
        if(!CitybuildPlugin.getInstance().getDataFolder().exists()){
            CitybuildPlugin.getInstance().getDataFolder().mkdir();
        }

        file = new File(CitybuildPlugin.getInstance().getDataFolder().getPath(), "crates.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public List<Crate> getCrates(){
        List<Crate> crates = new ArrayList<>();
        for (String key : configuration.getConfigurationSection("").getKeys(false)) {
            try {
                int id = Integer.parseInt(key);
                String name = configuration.getString(id+".Name");
                boolean sellable = configuration.getBoolean(id+".Sellable");
                int price = configuration.getInt(id+".Price");
                List<String> itemsStr = configuration.getStringList(id+".Items");
                List<ItemStack> items = new ArrayList<>();
                for (String string : itemsStr) {
                    try {
                        items.add(Converter.itemStackFromBase64(string));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                crates.add(new Crate(id, name, items, sellable, price));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return crates;
    }

    public Crate getCrate(String name){
        for (Crate crate : getCrates()) {
            if(crate.getName().equalsIgnoreCase(name)){
                return crate;
            }
        }
        return null;
    }

    public Crate getCrate(int id){
        for (Crate crate : getCrates()) {
            if(crate.getId()==id){
                return crate;
            }
        }
        return null;
    }
    
    public void createCrate(String name){
        int id = getNextID();
        configuration.set(id+".Name", name);
        configuration.set(id+".Items", new ArrayList<>());
        configuration.set(id+".Sellable", false);
        configuration.set(id+".Price", 0);
        save();
    }

    public void deleteCrate(String name){
        Crate crate = getCrate(name);
        if(crate!=null){
            configuration.set(crate.getId()+"", null);
            save();
        }
    }

    public void saveCrate(Crate crate){
        configuration.set(crate.getId() + ".Name", crate.getName());
        configuration.set(crate.getId() + ".Sellable", crate.isSellable());
        configuration.set(crate.getId() + ".Price", crate.getPrice());
        List<String> items = new ArrayList<>();
        for (ItemStack item : crate.getItems()) {
            try {
                items.add(Converter.itemStackToBase64(item));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        configuration.set(crate.getId()+".Items", items);
        save();
    }

    private int getNextID(){
        int id = 1;
        for (String key : configuration.getConfigurationSection("").getKeys(false)) {
            int i = Integer.parseInt(key);
            if(i >= id){
                id = i + 1;
            }
        }
        return id;
    }

    public void save(){
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
