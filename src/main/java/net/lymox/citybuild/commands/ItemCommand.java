package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }

        if(!player.hasPermission("lymox.citybuild.commands.item")){
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
            return true;
        }

        if(args.length == 0){
            String message = Messages.wrongCommandUsage;
            message = message.replace("%c", "/item name <ItemName>");
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
        }else if(args.length >= 2){
            if(args[0].equalsIgnoreCase("name")){
                String name = "";

                for(int i = 1; i < args.length; i++){
                    if(name.isEmpty()){
                        name = args[i];
                    }else {
                        name = name + " " + args[i];
                    }
                }

                name = name.replace("&", "§");

                if(player.getInventory().getItemInMainHand().getItemMeta()==null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Du musst ein Item in der Hand halten")));
                    return true;
                }

                ItemStack itemStack = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(name);
                itemStack.setItemMeta(itemMeta);
                player.getInventory().setItemInMainHand(itemStack);
                player.updateInventory();
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Item umbenannt")));
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player))return List.of();
        if(!player.hasPermission("lymox.citybuild.commands.item"))return List.of();
        if(args.length == 1){
            List<String> tab = new ArrayList<>();
            tab.add("name");
            return tab.stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }
}
