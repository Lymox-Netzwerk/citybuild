package net.lymox.citybuild.utils;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.userdata.Crate;
import net.lymox.citybuild.utils.userdata.skills.Holzfäller;
import net.lymox.citybuild.utils.userdata.skills.Jäger;
import net.lymox.citybuild.utils.userdata.skills.Miner;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import net.lymox.citybuild.utils.userdata.skills.interfaces.Skill;
import net.lymox.citybuild.utils.userdata.storage.Storage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            if(crate.getAmount()<=amount){
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

    public Skill getSkill(SkillType skillType){
        if(skillType.equals(SkillType.MONSTERJÄGER)){
            if(!configuration.contains("skills.monster")){
                configuration.set("skills.monster.xp", 0);
                configuration.set("skills.monster.kills", 0);
                save();
            }
            int xp = configuration.getInt("skills.monster.xp");
            int kills = configuration.getInt("skills.monster.kills");
            return new Monsterjäger(kills, xp);
        }
        if(skillType.equals(SkillType.JÄGER)){
            if(!configuration.contains("skills.jager")){
                configuration.set("skills.jager.xp", 0);
                configuration.set("skills.jager.kills", 0);
                save();
            }
            int xp = configuration.getInt("skills.jager.xp");
            int kills = configuration.getInt("skills.jager.kills");
            return new Jäger(kills, xp);
        }
        if(skillType.equals(SkillType.MINER)){
            if(!configuration.contains("skills.miner")){
                configuration.set("skills.miner.xp", 0);
                configuration.set("skills.miner.blocks", 0);
                save();
            }
            int xp = configuration.getInt("skills.miner.xp");
            int kills = configuration.getInt("skills.miner.blocks");
            return new Miner(kills, xp);
        }
        if(skillType.equals(SkillType.HOLZFÄLLER)){
            if(!configuration.contains("skills.wooder")){
                configuration.set("skills.wooder.xp", 0);
                configuration.set("skills.wooder.blocks", 0);
                save();
            }
            int xp = configuration.getInt("skills.wooder.xp");
            int kills = configuration.getInt("skills.wooder.blocks");
            return new Holzfäller(kills, xp);
        }
        return null;
    }

    public void saveSkill(Skill skill){
        if(skill instanceof Monsterjäger monsterjäger){
            configuration.set("skills.monster.xp", monsterjäger.getExp());
            configuration.set("skills.monster.kills", monsterjäger.getKills());
            save();
        }
        if(skill instanceof Jäger jäger){
            configuration.set("skills.jager.xp", jäger.getExp());
            configuration.set("skills.jager.kills", jäger.getKills());
            save();
        }
        if(skill instanceof Miner miner){
            configuration.set("skills.miner.xp", miner.getExp());
            configuration.set("skills.miner.blocks", miner.getBlocks());
            save();
        }
        if(skill instanceof Holzfäller holzfäller){
            configuration.set("skills.wooder.xp", holzfäller.getExp());
            configuration.set("skills.wooder.blocks", holzfäller.getBlocks());
            save();
        }
    }

    public List<Storage> getStorages(){
        if(!configuration.contains("storage")){
            configuration.set("storage.1.bought", true);
            configuration.set("storage.1.contents", null);
            configuration.set("storage.2.bought", false);
            configuration.set("storage.2.contents", null);
            configuration.set("storage.3.bought", false);
            configuration.set("storage.3.contents", null);
            configuration.set("storage.4.bought", false);
            configuration.set("storage.4.contents", null);
            configuration.set("storage.5.bought", false);
            configuration.set("storage.5.contents", null);
            configuration.set("storage.6.bought", false);
            configuration.set("storage.6.contents", null);
            configuration.set("storage.7.bought", false);
            configuration.set("storage.7.contents", null);
            configuration.set("storage.8.bought", false);
            configuration.set("storage.8.contents", null);
            configuration.set("storage.9.bought", false);
            configuration.set("storage.9.contents", null);
            save();
        }
        List<Storage> storages = new ArrayList<>();
        for (String storage : configuration.getConfigurationSection("storage").getKeys(false)) {
            int id = Integer.parseInt(storage);
            boolean bought = configuration.getBoolean("storage."+id+".bought");
            ItemStack[] contents = null;
            if(configuration.contains("storage."+id+".contents")) {
                List<?> items = configuration.getList("storage." + id + ".contents");
                if (items != null) {
                    contents = new ItemStack[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        Object obj = items.get(i);
                        if (obj instanceof Map) {
                            contents[i] = ItemStack.deserialize((Map<String, Object>) obj);
                        } else {
                            contents[i] = null;
                        }
                    }
                }
            }
            Storage result = new Storage(id, bought, contents);
            storages.add(result);
        }
        return storages;
    }

    public Storage getStorage(int id){
        for (Storage storage : getStorages()) {
            if(storage.getId()==id){
                return storage;
            }
        }
        return null;
    }
    
    public void saveStorages(){
        for (Storage storage : getStorages()) {
            List<Map<String, Object>> items = new ArrayList<>();
            for (ItemStack item : storage.getContents()) {
                items.add(item == null ? null : item.serialize());
            }
            configuration.set("storage."+storage.getId()+".contents", items);
            configuration.set("storage."+storage.getId()+".bought", storage.isBought());
        }
        save();
    }

    public void saveStorage(Storage storage){
        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemStack item : storage.getContents()) {
            items.add(item == null ? null : item.serialize());
        }
        configuration.set("storage."+storage.getId()+".contents", items);
        configuration.set("storage."+storage.getId()+".bought", storage.isBought());
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
