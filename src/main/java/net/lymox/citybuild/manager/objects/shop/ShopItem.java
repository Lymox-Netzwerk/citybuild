package net.lymox.citybuild.manager.objects.shop;

import org.bukkit.inventory.ItemStack;

public class ShopItem {

    private int categorieId;
    private ItemStack itemStack;
    private int inventorySlot;
    private int price;

    public ShopItem(int categorieId, int inventorySlot, ItemStack itemStack, int money) {
        this.inventorySlot = inventorySlot;
        this.categorieId = categorieId;
        this.itemStack = itemStack;
        this.price = money;
    }
    public int getCategorieId() {
        return categorieId;
    }
    public int getInventorySlot() {
        return inventorySlot;
    }
    public void setInventorySlot(int inventorySlot) {
        this.inventorySlot = inventorySlot;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
