package baumkm.wardrobe.main;


import baumkm.wardrobe.cmd.OpenWardrobe;
import baumkm.wardrobe.events.PlayerJoin;
import baumkm.wardrobe.events.PlayerLeave;
import baumkm.wardrobe.json_file.JsonFile;
import baumkm.wardrobe.wardrobe_menu.WardrobeInteractEvent;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    private static Main instance;

    
    public void onEnable() {


        getLogger().info("-------------------------------");
        getLogger().info("Plugin: " + this.getName());
        instance = this;



        //create json_file
        JsonFile.create_file();

        //register Events
        register_events();

        //register commands
        register_commands();


        getLogger().info("Plugin activated");
        getLogger().info("-------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("-------------------------------");
        instance = null;
        if(!WardrobeMap.getPlayerMap().isEmpty()) WardrobeMap.getPlayerMap().clear();
        getServer().getServicesManager().unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        getLogger().info("Plugin deactivated");
        getLogger().info("-------------------------------");
    }

    public void register_commands(){
        getCommand("wardrobe").setExecutor(new OpenWardrobe());
    }

    public void register_events(){
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerJoin(), this);
        manager.registerEvents(new PlayerLeave(), this);
        manager.registerEvents(new WardrobeInteractEvent(), this);

    }


    public static Main getInstance() {
        return instance;
    }
}
