package baumkm.wardrobe.events;

import baumkm.wardrobe.SQL.Database;
import baumkm.wardrobe.main.Main;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;
import baumkm.wardrobe.utils.convert_wardrobe_array;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();

        if(Main.getInstance().isSQLConnected) savePlayer(p);




    }

    private void savePlayer(Player p){

        WardrobeMap.getPlayer(p,1).updateWardrobe();
        WardrobeMap.getPlayer(p,2).updateWardrobe();

        String s1 = convert_wardrobe_array.ArrayToString(WardrobeMap.getPlayer(p,1).getPlayerArray());
        String s2 = convert_wardrobe_array.ArrayToString(WardrobeMap.getPlayer(p,2).getPlayerArray());
        Database.updateArray(p,s1,s2);
        WardrobeMap.removePlayer(p);
    }

}
