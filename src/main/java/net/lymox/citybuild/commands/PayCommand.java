package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(args.length != 2){
            String message = Messages.wrongCommandUsage;
            message = message.replace("%c", "/pay <Spieler> <Anzahl>");
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
        }else {
            Player target = Bukkit.getPlayer(args[0]);
            if(target==null){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red><b>" + args[0] +"</b> ist nicht auf CityBuild Online.")));
                return true;
            }
            if(target==player){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Du kannst nicht an dich selbst überweisen!")));
                return true;
            }
            try {
                Integer amount = Integer.parseInt(args[1]);
                if(new Userdata(player.getUniqueId()).getMünzen()<amount){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Du hast nicht genug Münzen um diesen Betrag zu überweisen.")));
                    return true;
                }
                new Userdata(player.getUniqueId()).removeMünzen(amount);
                CitybuildPlugin.getInstance().getManagers().getScoreboardManager().setDefaultScore(player);
                new Userdata(target.getUniqueId()).addMünzen(amount);
                CitybuildPlugin.getInstance().getManagers().getScoreboardManager().setDefaultScore(target);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Du hast <b>" + CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(target.getUniqueId()).displayColor() + target.getName() + "</b><yellow> <i>" + amount + "</i> Münzen <gray>überwiesen.")));
                target.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Du hast von <b>" + CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(target.getUniqueId()).displayColor() + target.getName() + "</b><yellow> <i>" + amount + "</i> Münzen <gray>überwiesen bekommen.")));
            }catch (NumberFormatException e){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Anzahl muss eine Zahl sein!")));
            }
        }
        return true;
    }
}
