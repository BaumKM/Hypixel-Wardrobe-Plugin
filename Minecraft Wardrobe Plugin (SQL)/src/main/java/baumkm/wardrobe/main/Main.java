package baumkm.wardrobe.main;

import baumkm.Database.MySQL_Connection;
import baumkm.wardrobe.SQL.Database;
import baumkm.wardrobe.cmd.OpenWardrobe;
import baumkm.wardrobe.events.PlayerJoin;
import baumkm.wardrobe.events.PlayerLeave;
import baumkm.wardrobe.wardrobe_menu.WardrobeInteractEvent;
import baumkm.wardrobe.wardrobe_menu.WardrobeMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JavaPlugin {


    private static Main instance;

    public boolean isSQLConnected;
    private MySQL_Connection mySQL_connection;


    @Override
    public void onEnable() {


        getLogger().info("-------------------------------");
        getLogger().info("Plugin: " + this.getName());
        instance = this;



        //register Events
        register_events();

        //register commands
        register_commands();

        //connect to MySQl Database
        mySQL_connection= new MySQL_Connection();
        try {

            mySQL_connection.connect();
        } catch (SQLException | ClassNotFoundException ex) {
            getLogger().info("couldn't load MySQL Database");

        }
        if(mySQL_connection.isConnected()){
            isSQLConnected = true;
            getLogger().info("MySQL Database is connected");
            Database.createTable();
        }
        else {
            getLogger().info("MYSQL Database isn't connected ");
            isSQLConnected = false;
        }


        getLogger().info("Plugin activated");
        getLogger().info("-------------------------------");

    }

    @Override
    public void onDisable() {
        getLogger().info("-------------------------------");
        instance = null;
        getServer().getServicesManager().unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        if(mySQL_connection.isConnected()){
            try {
                mySQL_connection.disconnect();
                getLogger().info("MySQL Database is disconnected");
            } catch (SQLException throwable ) {
                throwable.printStackTrace();
            }
        }
        mySQL_connection = null;
        if(!WardrobeMap.getPlayerMap().isEmpty()) WardrobeMap.getPlayerMap().clear();
        getLogger().info("Plugin deactivated");
        getLogger().info("-------------------------------");
    }

    public void register_commands(){
        getCommand("wardrobe").setExecutor(new OpenWardrobe());
    }

    public void register_events(){
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerJoin(), this);
        manager.registerEvents(new WardrobeInteractEvent(), this);
        manager.registerEvents(new PlayerLeave(), this);

    }


    public static Main getInstance() {
        return instance;
    }

    public Connection getMySQL_connection() {
        return mySQL_connection.getConnection();
    }
}
