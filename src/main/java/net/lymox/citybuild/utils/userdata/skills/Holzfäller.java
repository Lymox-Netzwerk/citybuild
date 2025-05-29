package net.lymox.citybuild.utils.userdata.skills;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.utils.userdata.skills.interfaces.Skill;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Holzfäller implements Skill {

    private int xp;
    private int blocks;

    public Holzfäller(int killedMonsters, int xp) {
        this.xp = xp;
        this.blocks = killedMonsters;
    }

    @Override
    public String getName() {
        return "Holzfäller";
    }

    @Override
    public String getNameFormatted() {
        return "ʜᴏʟᴢғäʟʟᴇʀ";
    }

    @Override
    public String getColor() {
        return "§7";
    }

    @Override
    public int getExp() {
        return this.xp;
    }

    @Override
    public void setExp(int value) {
        this.xp = value;
    }

    @Override
    public void addExp(int value) {
        this.xp = getExp() + value;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getBlocks() {
        return blocks;
    }

    @Override
    public int getLevel() {
        if(getExp() >= 0 && getExp() <= 99){
            return 1;
        }else if(getExp() >= 100 && getExp() <= 249){
            return 2;
        }else if(getExp() >= 250 && getExp() <= 499){
            return 3;
        }else if(getExp() >= 500 && getExp() <= 899){
            return 4;
        }else if(getExp() >= 900 && getExp() <= 1399){
            return 5;
        }else if(getExp() >= 1400 && getExp() <= 2099){
            return 6;
        }else if(getExp() >= 2100 && getExp() <= 2999){
            return 7;
        }else if(getExp() >= 3000 && getExp() <= 4199){
            return 8;
        }else if(getExp() >= 4200 && getExp() <= 5499){
            return 9;
        }else if(getExp() >= 5500){
            return 10;
        }
        return 1;
    }

    @Override
    public int requiredExp(int level) {
        if(level == 1){
            return 0;
        }else if(level == 2){
            return 100;
        }else if(level == 3){
            return 250;
        }else if(level == 4){
            return 500;
        }else if(level == 5){
            return 900;
        }else if(level == 6){
            return 1400;
        }else if(level == 7){
            return 2100;
        }else if(level == 8){
            return 3000;
        }else if(level == 9){
            return 4200;
        }else if(level == 10){
            return 5500;
        }
        return 100;
    }

    public int additionalDamage(int level){
        return 0;
    }

    public List<Component> rewards(int level, boolean gestrichen){
        List<Component> list = new ArrayList<>();

        double oldDrop = doubleDropChance(level-1);
        double newDrop = doubleDropChance(level);
        String oldSpeedDisplay = String.format("%.0f", oldDrop);
        String newSpeedDisplay = String.format("%.0f", newDrop);
        list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<dark_gray>"+oldSpeedDisplay + "% <gray>› <color:#d15eff>"+newSpeedDisplay + "% <gray>Chance auf doppelten Lootdrop"));

        int oldHearts = additionalHearts(level-1);
        int newHearts = additionalHearts(level);
        if (oldHearts != newHearts) {
            String oldHeartsDisplay = String.format("%.1f", oldHearts / 2.0);
            String newHeartsDisplay = String.format("%.1f", newHearts / 2.0);
            list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<dark_gray>" + oldHeartsDisplay + " <gray>› <red>" + newHeartsDisplay + " <gray>zusätzliche Leben"));
        }

        int münzen = münzenReward(level);
        list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<gray>+<yellow>" + münzen + " Münzen"));

        if(level == 5){
            list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<green>1 <gray>zusätzlicher Storage"));
        }
        if(level == 10){
            list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<green>Bäume werden vollständig gefällt"));
        }
        return list;
    }

    public int dropsExp(Block block){
        if(isValidBlock(block)){
            if(block.getType().equals(Material.ACACIA_LOG)
                    || block.getType().equals(Material.BIRCH_LOG)
                    || block.getType().equals(Material.CHERRY_LOG)
                    || block.getType().equals(Material.DARK_OAK_LOG)
                    || block.getType().equals(Material.JUNGLE_LOG)
                    || block.getType().equals(Material.MANGROVE_LOG)
                    || block.getType().equals(Material.OAK_LOG)
                    || block.getType().equals(Material.SPRUCE_LOG)
                    || block.getType().equals(Material.STRIPPED_ACACIA_LOG)
                    || block.getType().equals(Material.STRIPPED_BIRCH_LOG)
                    || block.getType().equals(Material.STRIPPED_CHERRY_LOG)
                    || block.getType().equals(Material.STRIPPED_DARK_OAK_LOG)
                    || block.getType().equals(Material.STRIPPED_JUNGLE_LOG)
                    || block.getType().equals(Material.STRIPPED_MANGROVE_LOG)
                    || block.getType().equals(Material.STRIPPED_OAK_LOG)
                    || block.getType().equals(Material.STRIPPED_SPRUCE_LOG)
                    || block.getType().equals(Material.BAMBOO_BLOCK)
                    || block.getType().equals(Material.CRIMSON_STEM)
                    || block.getType().equals(Material.WARPED_STEM)){
                return 5;
            }
        }
        return 0;
    }

    public boolean isValidBlock(Block block){
        return block.getType().equals(Material.ACACIA_LOG)
                || block.getType().equals(Material.BIRCH_LOG)
                || block.getType().equals(Material.CHERRY_LOG)
                || block.getType().equals(Material.DARK_OAK_LOG)
                || block.getType().equals(Material.JUNGLE_LOG)
                || block.getType().equals(Material.MANGROVE_LOG)
                || block.getType().equals(Material.OAK_LOG)
                || block.getType().equals(Material.SPRUCE_LOG)
                || block.getType().equals(Material.STRIPPED_ACACIA_LOG)
                || block.getType().equals(Material.STRIPPED_BIRCH_LOG)
                || block.getType().equals(Material.STRIPPED_CHERRY_LOG)
                || block.getType().equals(Material.STRIPPED_DARK_OAK_LOG)
                || block.getType().equals(Material.STRIPPED_JUNGLE_LOG)
                || block.getType().equals(Material.STRIPPED_MANGROVE_LOG)
                || block.getType().equals(Material.STRIPPED_OAK_LOG)
                || block.getType().equals(Material.STRIPPED_SPRUCE_LOG)
                || block.getType().equals(Material.BAMBOO_BLOCK)
                || block.getType().equals(Material.CRIMSON_STEM)
                || block.getType().equals(Material.WARPED_STEM);
    }

    @Override
    public int additionalHearts(int level) {
        return switch (level) {
            case 2, 3 -> 1;
            case 4, 5 -> 2;
            case 6, 7 -> 3;
            case 8, 9 -> 4;
            case 10 -> 6;
            default -> 0;
        };
    }

    @Override
    public double additionalSpeed(int level) {
        return 0;
    }

    @Override
    public int münzenReward(int level) {
        return switch (level) {
            case 2 -> 200;
            case 3 -> 400;
            case 4 -> 600;
            case 5 -> 1000;
            case 6 -> 1300;
            case 7 -> 1600;
            case 8 -> 1900;
            case 9 -> 2500;
            case 10 -> 4000;
            default -> 0;
        };
    }

    @Override
    public double additionalExpRate(int level) {
        return 0;
    }

    public int doubleDropChance(int level) {
        return switch (level) {
            case 2 -> 5;
            case 3 -> 10;
            case 4 -> 15;
            case 5 -> 30;
            case 6 -> 35;
            case 7 -> 40;
            case 8 -> 45;
            case 9 -> 50;
            case 10 -> 60;
            default -> 0;
        };
    }
}
