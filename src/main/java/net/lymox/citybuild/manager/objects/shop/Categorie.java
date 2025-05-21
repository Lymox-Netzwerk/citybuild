package net.lymox.citybuild.manager.objects.shop;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Material;

import java.util.List;

public class Categorie {

    private int id;
    private String name;
    private List<ShopItem> items;
    private Material material;

    public Categorie(int id, String name, Material material, List<ShopItem> items) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.items = items;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
    public List<ShopItem> getItems() {
        return items;
    }
    public void save(){
        CitybuildPlugin.getInstance().getManagers().getShopManager().saveCategorie(this);
    }
}
