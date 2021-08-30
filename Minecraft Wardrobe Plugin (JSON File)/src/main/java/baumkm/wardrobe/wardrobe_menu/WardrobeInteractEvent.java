package baumkm.wardrobe.wardrobe_menu;

import baumkm.wardrobe.utils.create_gui_items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class WardrobeInteractEvent implements Listener {


    /**
     * handles all possible actions regarding the wardrobeGUI (storing items, taking items, switching sets,...)
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void wardrobe_click(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        if(event.getClickedInventory() == null) return;


        if(!event.getView().getTitle().equals(WardrobeGUI.wardrobeNamePage1) && !event.getView().getTitle().equals(WardrobeGUI.wardrobeNamePage2)) return;


        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
        int page = event.getView().getTitle().equals(WardrobeGUI.wardrobeNamePage1) ? 1:2;
        int otherPage = page==1 ? 2:1;
        //if admin opens inv of another player
        if(!event.getClickedInventory().getHolder().equals(p.getPlayer())){
            p.sendMessage(ChatColor.RED + "This view is currently reading only");
            event.setCancelled(true);
            return;
        }


        int slot = event.getSlot();
        int set  = WardrobeGUI.SlotToSet(slot);

        ItemStack itemInHand = p.getItemOnCursor();

        WardrobeGUI playerWardrobe = WardrobeMap.getPlayer(p,page);

        if(playerWardrobe == null) return;

        //handles taking and switching ItemStacks from wardrobe
        // if slot <=35 --> player clicked an armor slot
        if(slot <=35){
            if(playerWardrobe.isSlotInActiveSet(slot)){ p.sendMessage(ChatColor.RED + "You can't modify your currently equipped armor set"); event.setCancelled(true); return;}

            //just taking item out --> itemInHand is "air"
            if(WardrobeGUI.isItemInHandAllowed(itemInHand).equals("air")){
                if(playerWardrobe.getObjectFromArray(slot) instanceof ItemStack itemInWardrobe){

                    p.setItemOnCursor(itemInWardrobe);
                    p.getOpenInventory().setItem(slot, WardrobeGUI.getItemFromDefaultArray(slot,page));
                    playerWardrobe.setObjectInArray(slot, WardrobeGUI.getStringFromDefaultArray(slot));
                    playerWardrobe.updateEmptySets(slot);
                }
                event.setCancelled(true);
                return;

            }

            //switching item --> itemInHand is "armor"
            else if(WardrobeGUI.isItemInHandAllowed(itemInHand).equals("armor")){
                if(WardrobeGUI.isArmorPieceAllowedInSlot(slot, itemInHand)){
                    if(playerWardrobe.getObjectFromArray(slot) instanceof String){
                        playerWardrobe.setObjectInArray(slot, itemInHand);
                        p.getOpenInventory().setItem(slot, itemInHand);
                        p.setItemOnCursor(new ItemStack(Material.AIR));
                        playerWardrobe.updateEmptySets(slot);
                    }
                    else if(playerWardrobe.getObjectFromArray(slot) instanceof  ItemStack itemInWardrobe){
                        playerWardrobe.setObjectInArray(slot, itemInHand);
                        p.getOpenInventory().setItem(slot, itemInHand);
                        p.setItemOnCursor(itemInWardrobe);
                    }

                }
                else p.sendMessage(ChatColor.RED+ "You can't place this item into this slot ");
                event.setCancelled(true);
                return;
            }

            else if (WardrobeGUI.isItemInHandAllowed(itemInHand).equals("invalid_item")) {
                event.setCancelled(true);
                return;
            }
            else{
                event.setCancelled(true);
                return;
            }

        }

        //handles switching and despawning armor sets
        // 3 cases:
        // is a set active? no --> equip new set and add old armor pieces to the player inv
        // set active? yes --> does player want to despawn old set or equip new one?
        // --> just despawn current set
        // --> or replace current set

        else if(slot <=44){
            if(playerWardrobe.isSetEmpty(slot)){event.setCancelled(true); return;}


            //equip a new set and add old armor to player inv
            if(!playerWardrobe.hasActiveSet()&&!WardrobeMap.getPlayer(p,otherPage).hasActiveSet()){
                playerWardrobe.setActiveSet(set);
                WardrobeMap.getPlayer(p,otherPage).setActiveSet("none");
                playerWardrobe.changeArmor(slot, true);

                if(page == 2){
                    p.getOpenInventory().setItem(slot, create_gui_items.ItemStackMap.get("l_d" + set+"2"));
                }
                else{
                    p.getOpenInventory().setItem(slot, create_gui_items.ItemStackMap.get("l_d" + set));
                }
            }

            //despawn current set
            else if(playerWardrobe.getActiveSet().equals(set)){
                ItemStack air = new ItemStack(Material.AIR);
                p.getInventory().setHelmet(air);
                p.getInventory().setChestplate(air);
                p.getInventory().setLeggings(air);
                p.getInventory().setBoots(air);
                playerWardrobe.setActiveSet("none");

                if(page == 2){
                    p.getOpenInventory().setItem(slot, create_gui_items.ItemStackMap.get("pi_d" + set+"2"));
                }
                else{
                    p.getOpenInventory().setItem(slot, create_gui_items.ItemStackMap.get("pi_d" + set));
                }
            }

            //equip a new set and overwrite old set
            else {
                int oldSet;
                if(playerWardrobe.getActiveSet() instanceof String){
                    oldSet = (int) WardrobeMap.getPlayer(p,otherPage).getActiveSet();
                }
                else{
                    oldSet = (int) playerWardrobe.getActiveSet();
                }

                playerWardrobe.setActiveSet(set);
                WardrobeMap.getPlayer(p,otherPage).setActiveSet("none");
                playerWardrobe.changeArmor(slot, false);
                if(page == 2){
                    p.getOpenInventory().setItem(oldSet + 36, create_gui_items.ItemStackMap.get("pi_d"+ set+"2"));
                    p.getOpenInventory().setItem(set + 36, create_gui_items.ItemStackMap.get("l_d"+set+"2"));
                }
                else{
                    p.getOpenInventory().setItem(oldSet + 36, create_gui_items.ItemStackMap.get("pi_d"+ set));
                    p.getOpenInventory().setItem(set + 36, create_gui_items.ItemStackMap.get("l_d"+set));
                }

            }
            event.setCancelled(true);
            return;
        }

        else if(slot == 45 && page == 2){
            event.setCancelled(true);
            WardrobeMap.getPlayer(p,1).openWardrobeGui();
            return;
        }
        else if(slot == 53 && page == 1){
            event.setCancelled(true);

            WardrobeMap.getPlayer(p,2).openWardrobeGui();
            return;
        }
        else {

            event.setCancelled(true);
        }
    }
}
