package net.lymox.citybuild.utils.userdata.skills.interfaces;

import net.kyori.adventure.text.Component;

import java.awt.*;
import java.util.Collection;

public interface Skill {

    String getName();
    String getNameFormatted();
    String getColor();
    int getExp();
    void setExp(int value);
    void addExp(int value);
    int getLevel();
    int requiredExp(int level);
    int münzenReward(int level);
    int additionalHearts(int level);
    double additionalSpeed(int level);
    double additionalExpRate(int level);
    Collection<Component> rewards(int level, boolean gestrichen);

}
