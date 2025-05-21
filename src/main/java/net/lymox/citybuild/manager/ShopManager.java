package net.lymox.citybuild.manager;

import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.manager.objects.shop.ShopItem;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Converter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private File categoriesFile;
    private YamlConfiguration categoriesConfiguration;

    private File itemsFile;
    private YamlConfiguration itemsConfiguration;

    public ShopManager() {
        if(!CitybuildPlugin.getInstance().getDataFolder().exists()){
            CitybuildPlugin.getInstance().getDataFolder().mkdir();
        }

        String path = CitybuildPlugin.getInstance().getDataFolder().getPath() + "/shop";

        File folder = new File(CitybuildPlugin.getInstance().getDataFolder(), "shop");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        categoriesFile = new File(path, "categories.yml");
        if(!categoriesFile.exists()){
            try {
                categoriesFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        categoriesConfiguration = YamlConfiguration.loadConfiguration(categoriesFile);
        categoriesConfiguration.options().copyDefaults(true);

        itemsFile = new File(path, "shop.yml");
        if(!itemsFile.exists()){
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        itemsConfiguration = YamlConfiguration.loadConfiguration(itemsFile);
        itemsConfiguration.options().copyDefaults(true);

        save();
    }

    public List<ShopItem> getItems(int categorieId){
        List<ShopItem> shopItems = new ArrayList<>();
        if(itemsConfiguration.contains("" + categorieId)) {
            for (String key : itemsConfiguration.getConfigurationSection("" + categorieId).getKeys(false)) {
                try {
                    Integer itemId = Integer.parseInt(key);
                    int money = itemsConfiguration.getInt(categorieId + "." + itemId + ".Price");
                    int slot = itemsConfiguration.getInt(categorieId + "." + itemId + ".Slot");
                    ItemStack itemStack = Converter.itemStackFromBase64(itemsConfiguration.getString(categorieId + "." + itemId + ".Item"));
                    ShopItem shopItem = new ShopItem(categorieId, slot, itemStack, money);
                    shopItems.add(shopItem);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return shopItems;
    }

    public void createCategorie(String name){
        int id = getNextID();
        categoriesConfiguration.set(id+".Name", name);
        save();
    }

    public void deleteCategorie(String name){
        Categorie categorie = getCategorie(name);
        if(categorie != null){
            itemsConfiguration.set(categorie.getId()+"", null);
            categoriesConfiguration.set(categorie.getId()+"", null);
            save();
        }
    }

    public void saveCategorie(Categorie categorie){
        categoriesConfiguration.set(categorie.getId()+".Name", categorie.getName());
        if(categorie.getMaterial()!=null){
            categoriesConfiguration.set(categorie.getId()+".Material", categorie.getMaterial().toString());
        }
        itemsConfiguration.set(categorie.getId()+"", null);
        int itemid = getNextItemID(categorie.getId());
        for (ShopItem item : categorie.getItems()) {
            try {
                itemsConfiguration.set(categorie.getId()+"."+itemid+".Item", Converter.itemStackToBase64(item.getItemStack()));
                itemsConfiguration.set(categorie.getId()+"."+itemid+".Price", item.getPrice());
                itemsConfiguration.set(categorie.getId()+"."+itemid+".Slot", item.getInventorySlot());
                itemid++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        save();
    }

    public List<Categorie> getCategories(){
        List<Categorie> list = new ArrayList<>();
        for (String key : categoriesConfiguration.getConfigurationSection("").getKeys(false)) {
            int id = Integer.parseInt(key);
            String name = categoriesConfiguration.getString(id+".Name");
            Material material = null;
            if(categoriesConfiguration.getString(id+".Material")!=null){
                material = Material.valueOf(categoriesConfiguration.getString(id+".Material"));
            }
            Categorie categorie = new Categorie(id, name, material, getItems(id));
            list.add(categorie);
        }
        return list;
    }

    public Categorie getCategorie(String name){
        for (Categorie category : getCategories()) {
            if(category.getName().equalsIgnoreCase(name))return category;
        }
        return null;
    }

    public Categorie getCategorie(int id){
        for (Categorie category : getCategories()) {
            if(category.getId()==id)return category;
        }
        return null;
    }

    private int getNextID(){
        int id = 1;
        for (String key : categoriesConfiguration.getConfigurationSection("").getKeys(false)) {
            int i = Integer.parseInt(key);
            if(i >= id){
                id = i + 1;
            }
        }
        return id;
    }

    private int getNextItemID(int categorie){
        int id = 1;
        if(!itemsConfiguration.contains(categorie+"")){
            return 1;
        }
        for (String key : itemsConfiguration.getConfigurationSection(categorie+"").getKeys(false)) {
            int i = Integer.parseInt(key);
            if(i >= id){
                id = i + 1;
            }
        }
        return id;
    }

    public int getNextInventorySlotID(int categorie){
        int id = 0;

        if(!itemsConfiguration.contains(categorie+"")) {
            return id;
        }

        for (ShopItem item : getItems(categorie)) {
            if(item.getInventorySlot()==id){
                id+=1;
            }
        }
        return id;
    }

    public void save(){
        try {
            categoriesConfiguration.save(categoriesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            itemsConfiguration.save(itemsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
