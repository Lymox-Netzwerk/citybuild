package net.lymox.citybuild.utils.userdata.skills;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.utils.userdata.skills.interfaces.Skill;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Monsterjäger implements Skill {

    private int xp;
    private int killedMonsters;

    public Monsterjäger(int killedMonsters, int xp) {
        this.xp = xp;
        this.killedMonsters = killedMonsters;
    }

    @Override
    public String getName() {
        return "Monsterjäger";
    }

    @Override
    public String getNameFormatted() {
        return "ᴍᴏɴsᴛᴇʀᴊäɢᴇʀ";
    }

    @Override
    public String getColor() {
        return "§2";
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
        return switch (level) {
            case 2 -> 4;
            case 3 -> 8;
            case 4 -> 12;
            case 5 -> 16;
            case 6 -> 20;
            case 7 -> 24;
            case 8 -> 28;
            case 9 -> 32;
            case 10 -> 36;
            default -> 0;
        };
    }

    public List<Component> rewards(int level, boolean gestrichen){
        List<Component> list = new ArrayList<>();
        int oldDamage = additionalDamage(level-1);
        int newDamage = additionalDamage(level);
        int oldHearts = additionalHearts(level-1);
        int newHearts = additionalHearts(level);
        int münzen = münzenReward(level);
        list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<dark_gray>"+oldDamage + "% <gray>› <green>"+newDamage + "% <gray>zu Monsterschaden"));
        if (oldHearts != newHearts) {
            String oldHeartsDisplay = String.format("%.1f", oldHearts / 2.0);
            String newHeartsDisplay = String.format("%.1f", newHearts / 2.0);
            list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<dark_gray>" + oldHeartsDisplay + " <gray>› <red>" + newHeartsDisplay + " <gray>zusätzliche Lebenspunkte"));
        }
        list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<gray>+<yellow>" + münzen + " Münzen"));
        if(level == 5){
            list.add(MiniMessage.miniMessage().deserialize((gestrichen?"<st>":"")+"<green>1 <gray>zusätzlicher Storage"));
        }
        return list;
    }

    public int dropsExp(Entity entity){
        EntityType entityType = entity.getType();
        if(isValidMonster(entityType)){
            if(entityType.equals(EntityType.ZOMBIE)
                    || entityType.equals(EntityType.ZOMBIE_VILLAGER)
                    || entityType.equals(EntityType.ZOMBIE_HORSE)
                    || entityType.equals(EntityType.DROWNED)
                    || entityType.equals(EntityType.HUSK)
                    || entityType.equals(EntityType.ZOGLIN)
                    || entityType.equals(EntityType.ZOMBIFIED_PIGLIN)
                    || entityType.equals(EntityType.SKELETON)
                    || entityType.equals(EntityType.STRAY)
                    || entityType.equals(EntityType.SKELETON_HORSE)
                    || entityType.equals(EntityType.WITHER_SKELETON)
                    || entityType.equals(EntityType.SPIDER)
                    || entityType.equals(EntityType.CAVE_SPIDER)
                    || entityType.equals(EntityType.CREEPER)
                    || entityType.equals(EntityType.SLIME)
                    || entityType.equals(EntityType.MAGMA_CUBE)
                    || entityType.equals(EntityType.PHANTOM)
                    || entityType.equals(EntityType.WITCH)
                    || entityType.equals(EntityType.BLAZE)
                    || entityType.equals(EntityType.GHAST)
                    || entityType.equals(EntityType.ENDERMITE)
                    || entityType.equals(EntityType.SILVERFISH)
                    || entityType.equals(EntityType.SHULKER)
                    || entityType.equals(EntityType.EVOKER)
                    || entityType.equals(EntityType.VINDICATOR)
                    || entityType.equals(EntityType.PILLAGER)
                    || entityType.equals(EntityType.RAVAGER)
                    || entityType.equals(EntityType.VEX)
                    || entityType.equals(EntityType.GUARDIAN)
                    || entityType.equals(EntityType.ELDER_GUARDIAN)
                    || entityType.equals(EntityType.HOGLIN)
                    || entityType.equals(EntityType.PIGLIN_BRUTE)
                    || entityType.equals(EntityType.WARDEN)
                    || entityType.equals(EntityType.ENDER_DRAGON)
                    || entityType.equals(EntityType.WITHER)
                    || entityType.equals(EntityType.BOGGED)
                    || entityType.equals(EntityType.BREEZE)
                    || entityType.equals(EntityType.ENDERMAN)
                    || entityType.equals(EntityType.CREAKING)){
                return 5;
            }
        }
        return 0;
    }

    public boolean isValidMonster(EntityType entityType){
        return entityType.equals(EntityType.ZOMBIE)
                || entityType.equals(EntityType.ZOMBIE_VILLAGER)
                || entityType.equals(EntityType.ZOMBIE_HORSE)
                || entityType.equals(EntityType.DROWNED)
                || entityType.equals(EntityType.HUSK)
                || entityType.equals(EntityType.ZOGLIN)
                || entityType.equals(EntityType.ZOMBIFIED_PIGLIN)
                || entityType.equals(EntityType.SKELETON)
                || entityType.equals(EntityType.STRAY)
                || entityType.equals(EntityType.SKELETON_HORSE)
                || entityType.equals(EntityType.WITHER_SKELETON)
                || entityType.equals(EntityType.SPIDER)
                || entityType.equals(EntityType.CAVE_SPIDER)
                || entityType.equals(EntityType.CREEPER)
                || entityType.equals(EntityType.SLIME)
                || entityType.equals(EntityType.MAGMA_CUBE)
                || entityType.equals(EntityType.PHANTOM)
                || entityType.equals(EntityType.WITCH)
                || entityType.equals(EntityType.BLAZE)
                || entityType.equals(EntityType.GHAST)
                || entityType.equals(EntityType.ENDERMITE)
                || entityType.equals(EntityType.SILVERFISH)
                || entityType.equals(EntityType.SHULKER)
                || entityType.equals(EntityType.EVOKER)
                || entityType.equals(EntityType.VINDICATOR)
                || entityType.equals(EntityType.PILLAGER)
                || entityType.equals(EntityType.RAVAGER)
                || entityType.equals(EntityType.VEX)
                || entityType.equals(EntityType.GUARDIAN)
                || entityType.equals(EntityType.ELDER_GUARDIAN)
                || entityType.equals(EntityType.HOGLIN)
                || entityType.equals(EntityType.PIGLIN_BRUTE)
                || entityType.equals(EntityType.WARDEN)
                || entityType.equals(EntityType.ENDER_DRAGON)
                || entityType.equals(EntityType.WITHER)
                || entityType.equals(EntityType.BOGGED)
                || entityType.equals(EntityType.BREEZE)
                || entityType.equals(EntityType.ENDERMAN)
                || entityType.equals(EntityType.CREAKING);
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
}
