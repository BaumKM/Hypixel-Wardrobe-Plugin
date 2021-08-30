package baumkm.wardrobe.events;


import baumkm.wardrobe.json_file.JsonFile;

import baumkm.wardrobe.wardrobe_menu.WardrobeGUI;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler

    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();





        //check if player has saved wardrobe in config
        loadSavedWardrobe(p);






    }


    private void loadSavedWardrobe(Player p){

        //check if player has saved array in json_file
        if(JsonFile.get_player(p,1) != null && JsonFile.get_player(p,2) != null) {
            Object[] array1 = JsonFile.get_player(p,1);
            Object[] array2 = JsonFile.get_player(p,2);
            WardrobeMap.addPlayer(p, new WardrobeGUI(p,array1,1),1);
            WardrobeMap.addPlayer(p, new WardrobeGUI(p,array2,2),2);

        }

        else {
            WardrobeMap.addPlayer(p, new WardrobeGUI(p,1),1);
            WardrobeMap.addPlayer(p, new WardrobeGUI(p,2),2);
        }
    }


}
