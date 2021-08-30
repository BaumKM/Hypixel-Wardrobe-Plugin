package baumkm.wardrobe.wardrobe_menu;


import baumkm.wardrobe.utils.create_gui_items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class WardrobeGUI {

    private final Player player;
    private Object[] playerArray;
    private final int page;

    private final String wardrobeName;



    public static final String wardrobeNamePage1 = ChatColor.RED + "Wardrobe (1/2)";
    public static final String wardrobeNamePage2 = ChatColor.RED + "Wardrobe (2/2)";


    //used as a reference and to look up things
    public static final Object[] instanceDefaultArray;

    private static final List<Integer> grayDyeSlots = new ArrayList<>(Arrays.asList(36,37,38,39,40,41,42,43,44));


    //for new players
    public WardrobeGUI(Player p, int page){
        this.player = p;
        this.page = page;


        if(this.page == 1){
            this.wardrobeName = wardrobeNamePage1;
        }
        else {
            this.wardrobeName = wardrobeNamePage2;
        }

        this.playerArray = createDefaultArray(this.page);


    }

    //for players with saved wardrobe
    public WardrobeGUI(Player p, Object[] array, int page){
        this.player = p;
        this.page = page;

        if(this.page == 1){
            this.wardrobeName = wardrobeNamePage1;
        }
        else {
            this.wardrobeName = wardrobeNamePage2;
        }

        this.playerArray = array;

    }



    public void openWardrobeGui(){
        updateWardrobe();


        if (player.getOpenInventory().getTitle().equals(WardrobeGUI.wardrobeNamePage1) || player.getOpenInventory().getTitle().equals(WardrobeGUI.wardrobeNamePage2)) {

            ItemStack i = player.getItemOnCursor();
            player.getInventory().addItem(i);
            player.getItemOnCursor().setAmount(0);

        }
        else{
            player.closeInventory();
        }
        player.openInventory(createWardrobeGui());


    }

    /**
     * opens the wardrobe for another player
     * @param thirdPlayer
     */
    public void openWardrobeGui(Player thirdPlayer){
        thirdPlayer.closeInventory();
        thirdPlayer.openInventory(createWardrobeGui());
    }



    static {
        instanceDefaultArray = createDefaultArray(1);
        initializeMap();


    }


    /**
     * converts the playerArray into the corresponding inventory
     * @return the Inventory based on the given array
     */
    private Inventory createWardrobeGui(){

        Inventory i = Bukkit.createInventory(player, 6*9, wardrobeName);

        Object[] array = playerArray;

        for(int x=0; x<9*6;x++){
            Object o = array[x];
            if(o instanceof String s){
                //armor glass_panes
                if(x<36){
                    if(page == 2){
                        i.setItem(x, create_gui_items.ItemStackMap.get(s +  x/9 + "2"));
                    }
                    else{
                        i.setItem(x, create_gui_items.ItemStackMap.get(s +  x/9));
                    }

                }
                //dye
                else if(x<45){
                    if(page == 2){
                        i.setItem(x, create_gui_items.ItemStackMap.get(s +  x%9 + "2"));
                    }
                    else{
                        i.setItem(x, create_gui_items.ItemStackMap.get(s +  x%9 ));
                    }
                }


                else if(x==45){
                    if(o.equals("arr_p")){
                        i.setItem(x, create_gui_items.createArrow("p"));
                    }
                    else{
                        i.setItem(x, create_gui_items.ItemStackMap.get(s));
                    }
                }
                //arrows
                else if(x==53){
                    if(o.equals("arr_n")){
                        i.setItem(x, create_gui_items.createArrow("n"));
                    }
                    else{
                        i.setItem(x, create_gui_items.ItemStackMap.get(s));
                    }
                }

                //gray glass_pane
                else {
                    i.setItem(x, create_gui_items.ItemStackMap.get(s));
                }



            }
            else if(o instanceof ItemStack itemStack){
                i.setItem(x, itemStack);
            }


        }
        Object active_set = array[54];

        if(active_set instanceof String) return i;
        Integer set = (Integer) active_set;
        if(page ==2){
            i.setItem(36+set, create_gui_items.ItemStackMap.get("l_d"+set+"2"));
        }
        else{
            i.setItem(36+set, create_gui_items.ItemStackMap.get("l_d"+set));
        }


        return i;

    }

    /**
     * checks if an action lead into an empty set --> yes change dye and if set was active before set active set = "none"
     * --> no change color to pink
     * @param slot
     */
    public void updateEmptySets(Integer slot){

        int i = slot%9;
        if(playerArray[i] instanceof String && playerArray[i + 9] instanceof String && playerArray[i + 18] instanceof String && playerArray[i + 27] instanceof String){
            setObjectInArray( 36+i,"gr_d");
            if(page==2){
                player.getOpenInventory().setItem(36+i,create_gui_items.ItemStackMap.get("gr_d"+i+"2"));
            }
            else{
                player.getOpenInventory().setItem(36+i,create_gui_items.ItemStackMap.get("gr_d"+i));
            }



        }
        else{
            if(!getObjectFromArray(36+i).equals("pi_d")){
                setObjectInArray(36+i, "pi_d");

                if(page==2){
                    player.getOpenInventory().setItem(36+i,create_gui_items.ItemStackMap.get("pi_d"+i+"2"));
                }
                else{

                    player.getOpenInventory().setItem(36+i,create_gui_items.ItemStackMap.get("pi_d"+i));
                }
            }


        }

    }

    //replaces the old armor with a new set
    public void changeArmor(int slot, boolean keepOldArmor) {
        int set = SlotToSet(slot);
        if (keepOldArmor) {
            ItemStack helmet = player.getInventory().getHelmet();
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggings = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();

            if (helmet != null) player.getInventory().addItem(helmet);
            if (chestplate != null) player.getInventory().addItem(chestplate);
            if (leggings != null) player.getInventory().addItem(leggings);
            if (boots != null) player.getInventory().addItem(boots);


            if (getObjectFromArray(set) instanceof ItemStack item) player.getInventory().setHelmet(item);
            else player.getInventory().setHelmet(new ItemStack(Material.AIR));

            if (getObjectFromArray(set + 9) instanceof ItemStack item) player.getInventory().setChestplate(item);
            else player.getInventory().setChestplate(new ItemStack(Material.AIR));

            if (getObjectFromArray(set + 18) instanceof ItemStack item) player.getInventory().setLeggings(item);
            else player.getInventory().setLeggings(new ItemStack(Material.AIR));

            if (getObjectFromArray(set + 27) instanceof ItemStack item) player.getInventory().setBoots(item);
            else player.getInventory().setBoots(new ItemStack(Material.AIR));
        } else {

            if (getObjectFromArray(set) instanceof ItemStack item) player.getInventory().setHelmet(item);
            else player.getInventory().setHelmet(new ItemStack(Material.AIR));

            if (getObjectFromArray(set + 9) instanceof ItemStack item) player.getInventory().setChestplate(item);
            else player.getInventory().setChestplate(new ItemStack(Material.AIR));

            if (getObjectFromArray(set + 18) instanceof ItemStack item) player.getInventory().setLeggings(item);
            else player.getInventory().setLeggings(new ItemStack(Material.AIR));

            if (getObjectFromArray(set + 27) instanceof ItemStack item) player.getInventory().setBoots(item);
            else player.getInventory().setBoots(new ItemStack(Material.AIR));

        }
    }
    /**
     * updates the wardrobe, to check if player changed his armor from vanilla inventory <br>
     */
    public void updateWardrobe(){
        if(getActiveSet() instanceof String) return;

        int set = (Integer) getActiveSet();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        if(helmet == null) setObjectInArray(set, getStringFromDefaultArray(set));
        else setObjectInArray(set, helmet);
        if(chestplate == null) setObjectInArray(set+9, getStringFromDefaultArray(set+9));
        else setObjectInArray(set+9, chestplate);
        if(leggings == null) setObjectInArray(set+18, getStringFromDefaultArray(set+18));
        else setObjectInArray(set+18, leggings);
        if(boots == null) setObjectInArray(set + 27, getStringFromDefaultArray(set+27));
        else setObjectInArray(set+27, boots);

        if(isSetEmpty(set)){
            setObjectInArray(36+set, "gr_d");
            setObjectInArray(54, "none");
        }
    }




    public boolean hasActiveSet(){
        Object active_set = playerArray[54];
        return !(active_set instanceof String);
    }


    public boolean isSlotInActiveSet(int slot){
        if(getActiveSet() instanceof String) return false;
        int activeSet = (Integer) getActiveSet();
        return SlotToSet(slot) == activeSet;


    }

    public Object getActiveSet(){
        Object active_set = playerArray[54];
        if(active_set instanceof String) return "none";
        return active_set;
    }

    public void setActiveSet(Object object){
        if(!(object instanceof String || object instanceof Integer)){ System.out.println("Ups an error occurred"); return;}

        playerArray[54] = object;
    }

    public Object getObjectFromArray(int slot){
        return playerArray[slot];
    }

    public void setObjectInArray(int slot, Object o){
        playerArray[slot] = o;
    }

    /**
     * check if all slots of a set are empty --> only Strings in playerArray
     * @param slot slot
     * @return
     */
    public boolean isSetEmpty(int slot){
        Object[] array = playerArray;
        int i = slot%9;
        return array[i] instanceof String && array[i + 9] instanceof String && array[i + 18] instanceof String && array[i + 27] instanceof String;
    }

    public Object[] getPlayerArray() {
        return playerArray;
    }

    /**
     * clears the wardrobe
     * !!! use it carefully
     */
    public void resetPlayerArray(){

        if(player.getOpenInventory().getTitle().equals(wardrobeName)){
            player.closeInventory();
        }
        this.playerArray = createDefaultArray(page);
        player.sendMessage(ChatColor.DARK_RED + "Your wardrobe has been cleared by an admin");
    }







    /**
     * check wheater an item is air armor or something else
     * @param item ItemStack
     * @return
     */
    public static String isItemInHandAllowed(ItemStack item){
        if(item.getType().equals(Material.AIR)) return "air";
        if(isItemArmor(item)) return "armor";
        else return "invalid_item";


    }

    /**
     * check if Itemstack is allowed in slot
     *  --> e.g boots can be only place into the boots slot
     * @param slot slot
     * @param i ItemStack
     * @return
     */
    public static boolean isArmorPieceAllowedInSlot(Integer slot, ItemStack i){
        String s = getArmorType(i);
        switch (s) {
            case "HELMET":
                if (slot < 9) return true;
                break;
            case "CHESTPLATE":
                if (slot < 18 && slot >= 9) return true;
                break;
            case "LEGGINGS":
                if (slot < 27 && slot >= 18) return true;
                break;
            case "BOOTS":
                if (slot < 36 && slot >= 27) return true;
                break;
        }
        return false;
    }

    public static int SlotToSet(int slot){
        return slot%9;
    }


    /**
     * gets String from any slot in the default_array
     * @param slot Slot
     * @return String from default array at slot slot
     */
    public static String getStringFromDefaultArray(int slot){
        return (String) instanceDefaultArray[slot];
    }


    /**
     * gets the ItemStack (glass_pane) from any slot in the default array
     * @param slot
     * @return ItemStack from requested slot
     */
    public static ItemStack getItemFromDefaultArray(int slot,int page){
        //get the String from default array
        String o = (String) instanceDefaultArray[slot];
        //get which ItemStack belongs to that String
        int b = slot/9;
        if(page==2){
            return create_gui_items.ItemStackMap.get(o + b+"2");
        }

        return create_gui_items.ItemStackMap.get(o + b);

    }





    /** default array for new players who don't have a saved wardrobe
     *
     * @return default_array, equal for every new player (nothing stored)
     */
    private static Object[] createDefaultArray(int page){
        Object[] a = new Object[55];
        for(int x=0; x<9*6;x++){
            if(x<36){
                int slot = SlotToSet(x);

                if(slot == 0) a[x] = "r"; //red
                else if(slot == 1) a[x] = "o" ; //orange glass pane o
                else if(slot == 2) a[x] = "y";  //yellow glass pane y
                else if(slot == 3) a[x] = "l";  //lime glass pane l
                else if(slot == 4) a[x] = "g";  //green glass pane g
                else if(slot == 5) a[x] = "lb"; //light blue glass pane lb
                else if(slot == 6) a[x] = "b";  //blue glass pane b
                else if(slot == 7) a[x] = "pi"; //pink glass pane pi
                else if(slot == 8) a[x] = "pu"; //purple glass pane pu

            }
            else if(grayDyeSlots.contains(x)) a[x] = "gr_d"; //gray_dye
            else a[x] = "gr"; //gray glass pane

        }
        //sets arrow next and arrow previous page
        if(page == 1){
            a[53] = "arr_n";
        }
        else{
            a[45] = "arr_p";
        }
        //which set is active
        a[54]= "none";
        return a;

    }
    /**
     * creates every ItemStack once for all recurrent items in the wardrobe_gui
     * --> iterates through the {@link WardrobeGUI#instanceDefaultArray} which is a new/default array with no ItemStacks init <br>
     * --> contains every possible ItemStack
     * --> uses {@link WardrobeGUI#loreDye(String)} and {@link WardrobeGUI#loreGlass(Integer)} method to get the matching lore
     * code: ex: r3 --> color red --> first set , 3 --> Boots (3ed slot)
     */
    private static void initializeMap(){
        //glass_panes for armor slots
        for(int x=0; x<9*4;x++){
            Object o = instanceDefaultArray[x];
            if(o instanceof String f){
                create_gui_items.createGlassPane(f, "wardrobe_glass", ChatColor.GREEN + "Slot " + (x%9 + 1) + ": " +  getArmorType(x), loreGlass(x), x/9,1);
            }

        }
        //dyes for switching armors and indicating which set is active
        for(int x =36; x<9*5; x++){
            Object o = instanceDefaultArray[x];
            if(o instanceof String){
                create_gui_items.createDye("pi_d", "wardrobe_dye", ChatColor.GRAY + "Slot " + (x%9 +1) + ": " + ChatColor.GREEN + "Ready", loreDye("pi_d"),x%9,1);
                create_gui_items.createDye("l_d", "wardrobe_dye",ChatColor.GRAY + "Slot " + (x%9 +1) + ": "+ ChatColor.GREEN + "Equipped", loreDye("l_d"), x%9,1);
                create_gui_items.createDye("gr_d", "wardrobe_dye",ChatColor.GRAY + "Slot " + (x%9 +1) + ": "+ ChatColor.RED + "Empty", loreDye("gr_d"), x%9,1);
            }
        }

        //grey glass_panes with no function
        for(int x = 45; x<9*6; x++){
            Object o = instanceDefaultArray[x];
            if(o instanceof String){
                create_gui_items.createGlassPane("gr", "wardrobe_glass", null, null, null, null);

            }
        }
        //page 2 --> add 2 at the end
        //glass_panes for armor slots

        for(int x=0; x<9*4;x++){
            Object o = instanceDefaultArray[x];
            if(o instanceof String f){

                create_gui_items.createGlassPane(f, "wardrobe_glass", ChatColor.GREEN + "Slot " + (x%9 + 1+9) + ": " +  getArmorType(x), loreGlass(x), x/9,2);
            }

        }
        //dyes for switching armors and indicating which set is active
        // +2 for page 2
        for(int x =36; x<9*5; x++){
            Object o = instanceDefaultArray[x];
            if(o instanceof String){
                create_gui_items.createDye("pi_d", "wardrobe_dye", ChatColor.GRAY + "Slot " + (x%9 +1+9) + ": " + ChatColor.GREEN + "Ready", loreDye("pi_d"),x%9,2);
                create_gui_items.createDye("l_d", "wardrobe_dye",ChatColor.GRAY + "Slot " + (x%9 +1+9) + ": "+ ChatColor.GREEN + "Equipped", loreDye("l_d"), x%9,2);
                create_gui_items.createDye("gr_d", "wardrobe_dye",ChatColor.GRAY + "Slot " + (x%9 +1+9) + ": "+ ChatColor.RED + "Empty", loreDye("gr_d"), x%9,2);
            }
        }




    }


    private static boolean isItemArmor(ItemStack i){
        String[] item_type = i.getType().toString().split("_");
        if(item_type.length != 2) return false;
        return item_type[1].equals("BOOTS") || item_type[1].equals("CHESTPLATE") || item_type[1].equals("LEGGINGS") || item_type[1].equals("HELMET");
    }



    private static String getArmorType(ItemStack i){
        String[] item_type = i.getType().toString().split("_");
        if(item_type.length != 2) return "no";
        if(item_type[1].equals("BOOTS")) return "BOOTS";
        if(item_type[1].equals("CHESTPLATE")) return "CHESTPLATE";
        if(item_type[1].equals("HELMET")) return "HELMET";
        if(item_type[1].equals("LEGGINGS")) return "LEGGINGS";
        return "no";

    }
    private static List<String> loreGlass(Integer slot){
        List<String> l = new ArrayList<>();
        if(slot<9) {
            l.add(ChatColor.GRAY + "Place a helmet here to add it");
            l.add(ChatColor.GRAY +"to the armor set");
            return l;
        }
        if(slot<18) {
            l.add(ChatColor.GRAY + "Place a chestplate here to add");
            l.add(ChatColor.GRAY +"it to the armor set");
            return l;
        }
        if(slot<27) {
            l.add(ChatColor.GRAY + "Place a pair of leggings here");
            l.add(ChatColor.GRAY +"to add them to the armor set");
            return l;
        }
        if(slot<36){
            l.add(ChatColor.GRAY + "Place a pair of boots here to");
            l.add(ChatColor.GRAY +"add them to the armor set");
            return l;
        }
        return null;

    }
    private static List<String> loreDye(String color){
        List<String> l = new ArrayList<>();
        if(color.equals("pi_d")){
            l.add(ChatColor.GRAY + "This wardrobe slot is ready to");
            l.add(ChatColor.GRAY + "be equipped.");
            l.add("");
            l.add(ChatColor.YELLOW + "Click to equip this armor set");
            return l;
        }
        if(color.equals("l_d")){
            l.add(ChatColor.GRAY + "This wardrobe slot contains your");
            l.add(ChatColor.GRAY + "current armor set.");
            l.add("");
            l.add(ChatColor.YELLOW + "Click to unequip this armor set");
            return l;
        }
        if(color.equals("gr_d")){
            l.add(ChatColor.GRAY + "This wardrobe slot contains no");
            l.add(ChatColor.GRAY + "armor.");
            return l;
        }
        return null;
    }
    private static String getArmorType(Integer slot){
        if(slot<9) return "Helmet";
        if(slot<18) return "Chestplate";
        if(slot<27) return "Leggings";
        if(slot<36) return "Boots";
        return "";
    }


}
