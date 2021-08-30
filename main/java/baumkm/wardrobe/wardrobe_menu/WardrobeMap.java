package baumkm.wardrobe.wardrobe_menu;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WardrobeMap {


    private static final HashMap<UUID, ArrayList<WardrobeGUI>> playerMap = new HashMap<>();



    public static void addPlayer(Player p, WardrobeGUI playerWardrobe, int position){
        if(playerMap.containsKey(p.getUniqueId())){
            playerMap.get(p.getUniqueId()).add(position-1,playerWardrobe);

        }
        else{
            playerMap.put(p.getUniqueId(), new ArrayList<>(){{add(position-1, playerWardrobe);}});
        }
    }
    public static WardrobeGUI getPlayer(Player p, int position){
        return playerMap.get(p.getUniqueId()).get(position-1);
    }
    public static void removePlayer(Player p){
        playerMap.remove(p.getUniqueId());
    }
    public static HashMap<UUID, ArrayList<WardrobeGUI>> getPlayerMap() {
        return playerMap;
    }

}
