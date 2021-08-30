package baumkm.wardrobe.cmd;

import baumkm.wardrobe.main.Main;
import baumkm.wardrobe.wardrobe_menu.WardrobeGUI;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OpenWardrobe implements CommandExecutor, TabCompleter {



    //<player who wants to clear [target, Instant]>
    private static final HashMap<UUID, Object[]> deleteConfirmation = new HashMap<>();



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p){

            if(args.length == 0){
                WardrobeGUI playerWardrobe = WardrobeMap.getPlayer(p,1);
                playerWardrobe.openWardrobeGui();
                return true;
            }
            //opens the wardrobe from another player
            // only admins should be able to execute this command
            // currently read only
            else if(args.length == 1){
                if(p.hasPermission("wardrobe.open")) {
                    String target = args[0];
                    try {
                        WardrobeGUI playerWardrobe = WardrobeMap.getPlayer(Main.getInstance().getServer().getPlayer(target),1);
                        playerWardrobe.openWardrobeGui(p);
                        return true;
                    } catch (NullPointerException e) {
                        p.sendMessage(ChatColor.RED + "The player " + target + " isn't online" );
                        return true;
                    }
                }
                else{
                    p.sendMessage(ChatColor.DARK_RED + "You don't have the necessary rights to execute this command" );
                    return true;
                }
            }
            //clears the wardrobe array
            // use this command carefully
            // must be confirmed by textmessage
            else if(args.length == 2){
                if(p.hasPermission("wardrobe.clear")){
                    String target = args[0];
                    String arg = args[1];
                    if(!arg.equals("clear")){ p.sendMessage(ChatColor.RED + "Use /wardrobe <player> clear"); return true;}
                    try {

                        deleteConfirmation.remove(p.getUniqueId());
                        WardrobeGUI playerWardrobe = WardrobeMap.getPlayer(Main.getInstance().getServer().getPlayer(target),1);

                        Object[] subMap = new Object[2];
                        subMap[0] = target;
                        //message is valid for 30 seconds
                        subMap[1] = Instant.now().plusSeconds(30);
                        deleteConfirmation.put(p.getUniqueId(), subMap);
                        p.spigot().sendMessage(confirm(target));

                        return true;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        p.sendMessage(ChatColor.RED + "The player " + target + " isn't online" );
                        return true;
                    }

                }
                else{
                    p.sendMessage(ChatColor.DARK_RED + "You don't have the necessary rights to execute this command" );
                    return true;
                }
            }
            //only executed by clicking on TextComponent
            else if(args.length == 3){
                if(!args[1].equals("clear") || !args[2].equals("confirm"))  return false;
                if(p.hasPermission("wardrobe.clear.confirm")){
                    if(!deleteConfirmation.containsKey(p.getUniqueId())){ p.sendMessage(ChatColor.RED + "Use /wardrobe <player> clear"); return true;}
                    String target = (String) deleteConfirmation.get(p.getUniqueId())[0];
                    Instant instant = (Instant) deleteConfirmation.get(p.getUniqueId())[1];
                    Instant now = Instant.now();
                    if(now.isBefore(instant)){
                        try {

                            WardrobeGUI playerWardrobe = WardrobeMap.getPlayer(Main.getInstance().getServer().getPlayer(target),1);
                            WardrobeGUI playerWardrobe2 = WardrobeMap.getPlayer(Main.getInstance().getServer().getPlayer(target),2);
                            playerWardrobe.resetPlayerArray();
                            playerWardrobe2.resetPlayerArray();
                            p.sendMessage(ChatColor.GREEN + "You have successfully cleared the wardrobe from " + target);
                            deleteConfirmation.remove(p.getUniqueId());
                            return true;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            p.sendMessage(ChatColor.RED + "The player " + target + " isn't online" );
                            deleteConfirmation.remove(p.getUniqueId());
                            return true;
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED + "This confirmation isn't valid anymore!");
                        deleteConfirmation.remove(p.getUniqueId());
                        return true;
                    }


                }
                else{
                    p.sendMessage(ChatColor.DARK_RED + "You don't have the necessary rights to execute this command" );
                    return true;
                }
            }
        }
        return false;
    }
    private TextComponent confirm(String playerName){

        TextComponent message = new TextComponent("Do you really want to clear the wardrobe from ");
        message.setColor(net.md_5.bungee.api.ChatColor.RED);
        TextComponent name = new TextComponent(playerName);
        name.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.addExtra(name);
        message.addExtra("?");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wardrobe " + playerName + " clear confirm"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(net.md_5.bungee.api.ChatColor.GREEN + " Click to confirm")));
        return message;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player player){
            final ArrayList<String> list = new ArrayList<>();
            if(args.length == 1){


                Bukkit.getServer().getWorld(player.getWorld().getName()).getPlayers().forEach(p -> list.add(p.getName()));
                return list;
            }
            if(args.length == 2){
                list.add("clear");
                return list;
            }
            if(args.length == 3){
                list.add("confirm");
                return list;
            }

        }
        return null;
    }
}
