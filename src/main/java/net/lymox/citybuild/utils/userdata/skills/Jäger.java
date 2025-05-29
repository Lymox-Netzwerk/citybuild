package net.lymox.citybuild.utils.userdata.skills;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.utils.userdata.skills.interfaces.Skill;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Jäger implements Skill {

    private int xp;
    private int killedMonsters;

    public Jäger(int killedMonsters, int xp) {
        this.xp = xp;
        this.killedMonsters = killedMonsters;
    }

    @Override
    public String getName() {
        return "Jäger";
    }

    @Override
    public String getNameFormatted() {
        return "ᴊäɢᴇʀ";
    }

    @Override
    public String getColor() {
        return "§a";
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

    public void setKills(int killedMonsters) {
        this.killedMonsters = killedMonsters;
    }

    public int getKills() {
        return killedMonsters;
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

        double oldSpeed = additionalSpeed(level-1);
        double newSpeed = additionalSpeed(level);
        String oldSpeedDisplay = String.format("%.1f", oldSpeed * 1000);
        String newSpeedDisplay = String.format("%.1f", newSpeed * 1000);
        list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<dark_gray>"+oldSpeedDisplay + "% <gray>› <color:#00e1ff>"+newSpeedDisplay + "% <gray>zu Geschwindigkeit"));

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
        return list;
    }

    public int dropsExp(Entity entity){
        EntityType entityType = entity.getType();
        if(isValidMonster(entityType)){
            if(entityType.equals(EntityType.COW)
                    || entityType.equals(EntityType.PIG)
                    || entityType.equals(EntityType.CHICKEN)
                    || entityType.equals(EntityType.SHEEP)
                    || entityType.equals(EntityType.RABBIT)
                    || entityType.equals(EntityType.MOOSHROOM)
                    || entityType.equals(EntityType.HORSE)
                    || entityType.equals(EntityType.DONKEY)
                    || entityType.equals(EntityType.MULE)
                    || entityType.equals(EntityType.LLAMA)
                    || entityType.equals(EntityType.TRADER_LLAMA)
                    || entityType.equals(EntityType.CAMEL)
                    || entityType.equals(EntityType.GOAT)
                    || entityType.equals(EntityType.STRIDER)
                    || entityType.equals(EntityType.TURTLE)){
                return 90;
            }
        }
        return 0;
    }

    public boolean isValidMonster(EntityType entityType){
        return entityType.equals(EntityType.COW)
                || entityType.equals(EntityType.PIG)
                || entityType.equals(EntityType.CHICKEN)
                || entityType.equals(EntityType.SHEEP)
                || entityType.equals(EntityType.RABBIT)
                || entityType.equals(EntityType.MOOSHROOM)
                || entityType.equals(EntityType.HORSE)
                || entityType.equals(EntityType.DONKEY)
                || entityType.equals(EntityType.MULE)
                || entityType.equals(EntityType.LLAMA)
                || entityType.equals(EntityType.TRADER_LLAMA)
                || entityType.equals(EntityType.CAMEL)
                || entityType.equals(EntityType.GOAT)
                || entityType.equals(EntityType.STRIDER)
                || entityType.equals(EntityType.TURTLE);
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
        return switch (level) {
            case 1 -> 0.005;
            case 2 -> 0.01;
            case 3 -> 0.015;
            case 4 -> 0.02;
            case 5 -> 0.025;
            case 6 -> 0.03;
            case 7 -> 0.035;
            case 8 -> 0.04;
            case 9 -> 0.045;
            case 10 -> 0.05;
            default -> 0.00;
        };
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
}
