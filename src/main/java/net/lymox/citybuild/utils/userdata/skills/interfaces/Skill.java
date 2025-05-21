package net.lymox.citybuild.utils.userdata.skills.interfaces;

public interface Skill {

    String getName();
    int getExp();
    void setExp(int value);
    void addExp(int value);

}
