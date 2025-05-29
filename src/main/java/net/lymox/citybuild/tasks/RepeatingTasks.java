package net.lymox.citybuild.tasks;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Jäger;
import net.lymox.citybuild.utils.userdata.skills.Miner;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import net.royawesome.jlibnoise.module.combiner.Min;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RepeatingTasks {

    public RepeatingTasks() {
        run();
    }

    private static final UUID SKILL_HEART_UUID = UUID.fromString("12345678-1234-1234-1234-1234567890ab");

    public void run(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CitybuildPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Userdata userdata = new Userdata(onlinePlayer.getUniqueId());
                    int additionalHearts = 0;
                    double additionalSpeed = 0;
                    Monsterjäger monsterjäger = (Monsterjäger) userdata.getSkill(SkillType.MONSTERJÄGER);
                    if(monsterjäger.additionalHearts(monsterjäger.getLevel())>0){
                        additionalHearts+= monsterjäger.additionalHearts(monsterjäger.getLevel());
                    }
                    if(monsterjäger.additionalSpeed(monsterjäger.getLevel())>0){
                        additionalSpeed+= monsterjäger.additionalSpeed(monsterjäger.getLevel());
                    }
                    Jäger jäger = (Jäger) userdata.getSkill(SkillType.JÄGER);
                    if(jäger.additionalHearts(jäger.getLevel())>0){
                        additionalHearts+= jäger.additionalHearts(jäger.getLevel());
                    }
                    if(jäger.additionalSpeed(jäger.getLevel())>0){
                        additionalSpeed+= jäger.additionalSpeed(jäger.getLevel());
                    }
                    Miner miner = (Miner)userdata.getSkill(SkillType.MINER);
                    if(miner.additionalHearts(miner.getLevel())>0){
                        additionalHearts+= miner.additionalHearts(miner.getLevel());
                    }
                    applyAdditionalHearts(onlinePlayer, additionalHearts);
                    applyAdditionalSpeed(onlinePlayer, additionalSpeed);
                }
            }
        },0, 20);
    }

    private static final UUID SKILL_HEALTH_MODIFIER_UUID = UUID.fromString("b013c3d1-3b3e-4fcb-9d10-5f5b91437998");

    public static void applyAdditionalHearts(Player player, int additionalHearts) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute == null) return;

        // Entferne alten Modifier (wenn vorhanden)
        attribute.getModifiers().stream()
                .filter(mod -> mod.getUniqueId().equals(SKILL_HEALTH_MODIFIER_UUID))
                .findFirst()
                .ifPresent(attribute::removeModifier);

        // Falls zusätzliche Herzen > 0, dann neu hinzufügen
        if (additionalHearts > 0) {
            // 1 Herz = 2 Health
            AttributeModifier modifier = new AttributeModifier(
                    SKILL_HEALTH_MODIFIER_UUID,
                    "skill_bonus_health",
                    additionalHearts,
                    AttributeModifier.Operation.ADD_NUMBER
            );
            attribute.addModifier(modifier);
        }

        // Optional: Health anpassen, falls aktuelles Leben über dem neuen Max-Wert liegt
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static final UUID SKILL_SPEED_MODIFIER_UUID = UUID.fromString("12345678-1234-1234-1234-123456789abc");

    public static void applyAdditionalSpeed(Player player, double additionalSpeed) {
        AttributeInstance attribute = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (attribute == null) return;

        // Entferne alten Modifier (wenn vorhanden)
        attribute.getModifiers().stream()
                .filter(mod -> mod.getUniqueId().equals(SKILL_SPEED_MODIFIER_UUID))
                .findFirst()
                .ifPresent(attribute::removeModifier);

        // Falls zusätzlicher Speed > 0, dann neu hinzufügen
        if (additionalSpeed > 0) {
            AttributeModifier modifier = new AttributeModifier(
                    SKILL_SPEED_MODIFIER_UUID,
                    "skill_bonus_speed",
                    additionalSpeed,
                    AttributeModifier.Operation.ADD_NUMBER
            );
            attribute.addModifier(modifier);
        }
    }


}
