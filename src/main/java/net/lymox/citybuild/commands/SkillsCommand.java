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

    public static String getBalken(long percent) {
        int balkenLaenge = 20;
        int gruenAnzahl = (int) Math.round(percent / 100.0 * balkenLaenge);

        StringBuilder sb = new StringBuilder();
        sb.append("<green>");
        for (int i = 0; i < gruenAnzahl; i++) {
            sb.append("▌");
        }
        sb.append("<gray>");
        for (int i = gruenAnzahl; i < balkenLaenge; i++) {
            sb.append("▌");
        }

        return sb.toString();
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
