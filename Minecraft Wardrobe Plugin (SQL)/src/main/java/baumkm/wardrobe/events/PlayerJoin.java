package baumkm.wardrobe.events;

import baumkm.wardrobe.SQL.Database;
import baumkm.wardrobe.main.Main;
import baumkm.wardrobe.wardrobe_menu.WardrobeGUI;
import baumkm.wardrobe.utils.convert_wardrobe_array;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();




        //check player in SQL Database
        if(Main.getInstance().isSQLConnected) checkPlayerInDatabase(p);




    }



    private void checkPlayerInDatabase(Player p){

        //check if player is registered in Database
        if(!Database.isPlayerInTable(p)) {
            Database.addPlayer(p);
        }

        //check if player has saved wardrobe array
        // yes --> load saved array
        //no --> create default array

        if(Database.hasSavedArray(p)){
            String s1 = Database.getSavedArray(p,1);
            String s2 = Database.getSavedArray(p,2);

            Object[] array1 = convert_wardrobe_array.StringToArray(s1);
            Object[] array2 = convert_wardrobe_array.StringToArray(s2);
            WardrobeMap.addPlayer(p, new WardrobeGUI(p,array1,1),1);
            WardrobeMap.addPlayer(p, new WardrobeGUI(p,array2,2),2);
        }
        else {
            WardrobeMap.addPlayer(p,new WardrobeGUI(p,1),1);
            WardrobeMap.addPlayer(p,new WardrobeGUI(p,2),2);
        }
    }


}
