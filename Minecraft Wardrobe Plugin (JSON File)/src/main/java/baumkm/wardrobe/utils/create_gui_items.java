package baumkm.wardrobe.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class create_gui_items {

    //create different ItemStacks for the different guis

    /**
     * contains the equivalent ItemStack for every color
     */
    public static final HashMap<String, ItemStack> ItemStackMap = new HashMap<>();





    public static ItemStack createGlassPane(String color, String name, @Nullable String displayName, @Nullable List<String> lore, @Nullable Integer armor_type, @Nullable Integer page){
        ItemStack i = switch (color) {
            case "gr" -> new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            case "lr" -> new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            case "b" -> new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            case "r" -> new ItemStack(Material.RED_STAINED_GLASS_PANE);
            case "y" -> new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
            case "pu" -> new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
            case "pi" -> new ItemStack(Material.PINK_STAINED_GLASS_PANE);
            case "l" -> new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            case "g" -> new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            case "lb" -> new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            case "o" -> new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            default -> new ItemStack(Material.GLASS_PANE);
        };

        ItemMeta glassMeta = i.getItemMeta();

        glassMeta.setDisplayName(displayName == null ? " " : displayName);

        if(lore != null){
            glassMeta.setLore(lore);
        }




        glassMeta.setLocalizedName(name);
        i.setItemMeta(glassMeta);

        if(name.equals("wardrobe_glass")){
            ItemStackMap.put(armor_type == null ? color : page == 2 ? color + armor_type + "2" : color + armor_type , i );
        }


        return i;


    }

    public static ItemStack createDye(String color, String name, @Nullable String display_name, @Nullable List<String> lore, @Nullable Integer slot, @Nullable Integer page){
        ItemStack i = switch (color) {
            case "gr_d" -> new ItemStack(Material.GRAY_DYE);
            case "l_d" -> new ItemStack(Material.LIME_DYE);
            case "pi_d" -> new ItemStack(Material.PINK_DYE);
            default -> new ItemStack(Material.WHITE_DYE);
        };
        ItemMeta dye_meta = i.getItemMeta();

        assert dye_meta != null;
        dye_meta.setLocalizedName(name);
        if(Objects.equals(display_name,null)){
            dye_meta.setDisplayName(" ");
        }
        else{
            dye_meta.setDisplayName(display_name);
        }
        if(!Objects.equals(lore,null)){
            dye_meta.setLore(lore);
        }
        i.setItemMeta(dye_meta);
        if(Objects.equals(slot,null)){
            if(name.equals("wardrobe_dye")) ItemStackMap.put(color, i);
        }
        else{
            if(Objects.equals(page,2)){
                if(name.equals("wardrobe_dye")) ItemStackMap.put(color + slot+"2", i);
            }
            else{
                if(name.equals("wardrobe_dye")) ItemStackMap.put(color + slot, i);
            }

        }

        return i;

    }
    public static ItemStack createArrow(String s){
        if(s.equals("n")){
            ItemStack i  =new ItemStack(Material.ARROW);
            ItemMeta meta = i.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GREEN + "Next Page");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Page 2");
            meta.setLore(lore);
            i.setItemMeta(meta);
            return i;
        }
        else if(s.equals("p")){
            ItemStack i  =new ItemStack(Material.ARROW);
            ItemMeta meta = i.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GREEN + "Previous Page");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Page 1");
            meta.setLore(lore);
            i.setItemMeta(meta);
            return i;
        }
        return null;

    }





}
