package baumkm.wardrobe.events;


import baumkm.wardrobe.json_file.JsonFile;
import baumkm.wardrobe.utils.convert_wardrobe_array;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();

        savePlayer(p);

    }

    private void savePlayer(Player p){
        WardrobeMap.getPlayer(p,1).updateWardrobe();
        WardrobeMap.getPlayer(p,2).updateWardrobe();

        String s1 = convert_wardrobe_array.convertArrayInString(WardrobeMap.getPlayer(p,1).getPlayerArray());
        String s2 = convert_wardrobe_array.convertArrayInString(WardrobeMap.getPlayer(p,2).getPlayerArray());
        JsonFile.savePlayerArray(p, s1,1);
        JsonFile.savePlayerArray(p, s2,2);

        WardrobeMap.removePlayer(p);

    }

}
