package net.lymox.citybuild.manager.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Crate {

    private int id;
    private String name;
    private List<ItemStack> items;
    private boolean sellable;
    private int price;

    public Crate(int id, String name, List<ItemStack> items, boolean sellable, int price) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.sellable = sellable;
        this.price = price;
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
    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }
    public boolean isSellable() {
        return sellable;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }
}
