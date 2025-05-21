package net.lymox.citybuild.utils.userdata.skills;

import net.lymox.citybuild.utils.userdata.skills.interfaces.Skill;

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
}
