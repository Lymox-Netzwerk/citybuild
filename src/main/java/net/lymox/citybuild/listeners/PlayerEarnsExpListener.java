package net.lymox.citybuild.listeners;

import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Miner;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerEarnsExpListener implements Listener {

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        Userdata userdata = new Userdata(player.getUniqueId());
        Miner miner = (Miner) userdata.getSkill(SkillType.MINER);
        double dropRate = event.getAmount();
        double newDropRate = (dropRate * (100+miner.additionalExpRate(miner.getLevel())))/100;
        if(newDropRate>dropRate){
            dropRate = dropRate+newDropRate;
            event.setAmount((int) Math.round(dropRate));
        }
    }


}
