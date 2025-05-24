package net.lymox.citybuild.commands;

import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(args.length == 0){
            player.openInventory(new GUIManager().openSkills(player.getUniqueId(), null));
        }
        return false;
    }

    public static String getBalken(long percent){
        if(percent < 10){
            return "<gray>▌▌▌▌▌▌▌▌▌";
        }else if(percent >= 10 && percent <= 20){
            return "<green>▌<gray>▌▌▌▌▌▌▌▌";
        }else if(percent >= 20 && percent < 30){
            return "<green>▌▌<gray>▌▌▌▌▌▌▌";
        }else if(percent >= 30 && percent < 40){
            return "<green>▌▌▌<gray>▌▌▌▌▌▌";
        }else if(percent >= 40 && percent < 50){
            return "<green>▌▌▌▌<gray>▌▌▌▌▌";
        }else if(percent >= 50 && percent < 60){
            return "<green>▌▌▌▌▌<gray>▌▌▌▌";
        }else if(percent >= 60 && percent < 70){
            return "<green>▌▌▌▌▌▌<gray>▌▌▌";
        }else if(percent >= 80 && percent < 90){
            return "<green>▌▌▌▌▌▌▌<gray>▌▌";
        }else if(percent >= 90 && percent < 100){
            return "<green>▌▌▌▌▌▌▌▌<gray>▌";
        }
        return "<green>▌▌▌▌▌▌▌▌▌";
    }

    public static int slot(int level){
        switch (level){
            case 1: return 0;
            case 2: return 9;
            case 3: return 18;
            case 4: return 19;
            case 5: return 20;
            case 6: return 11;
            case 7: return 2;
            case 8: return 3;
            case 9: return 4;
            case 10: return 13;
            default: return 0;
        }
    }

}
