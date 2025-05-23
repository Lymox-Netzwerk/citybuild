package net.lymox.citybuild.utils.userdata.storage;

import org.bukkit.inventory.ItemStack;

public class Storage {

    private int id;
    private boolean bought;
    private ItemStack[] contents;

    public Storage(int id, boolean bought, ItemStack[] contents) {
        this.id = id;
        this.bought = bought;
        this.contents = contents;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getId() {
        return id;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }
}
