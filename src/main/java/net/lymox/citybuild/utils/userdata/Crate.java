package net.lymox.citybuild.utils.userdata;

public class Crate {

    int id;
    int amount;

    public Crate(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
