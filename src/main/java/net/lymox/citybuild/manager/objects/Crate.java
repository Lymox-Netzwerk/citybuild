package net.lymox.citybuild.manager.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Crate {

    private int id;
    private String name;
    private List<ItemStack> items;

    public Crate(int id, String name, List<ItemStack> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<ItemStack> getItems() {
        return items;
    }
}
