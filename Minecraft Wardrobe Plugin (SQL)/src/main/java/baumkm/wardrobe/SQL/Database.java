package baumkm.wardrobe.SQL;


import baumkm.Database.DataSource;
import baumkm.wardrobe.main.Main;
import org.bukkit.entity.Player;

public class Database {
    private static final DataSource dataSource = new DataSource(Main.getInstance().getMySQL_connection());

    public static void createTable(){
        dataSource.createTable("wardrobe", "(ID INT Primary Key Auto_Increment, UUID VARCHAR(50) not null, player_name VARCHAR(50) not null, saved_array_1 TEXT, saved_array_2 TEXT)");

    }
    public static void addPlayer(Player p){
        dataSource.add("wardrobe", "(UUID, player_name)", "(" +'"' + p.getUniqueId() +'"' + ", " + '"' +p.getName() + '"' +" )" );
    }

    public static void addPlayer(Player p, String array){
        dataSource.add("wardrobe", "(UUID, player_name, saved_array)", "("+ '"' + p.getUniqueId()+ '"' + ","+ '"' + p.getName()+ '"' + ", " + '"'+ array+ '"' + " )" );

    }

    public static void updateArray(Player p, String array1, String array2){
        dataSource.update("wardrobe", p.getUniqueId(), "saved_array_1", array1);
        dataSource.update("wardrobe", p.getUniqueId(), "saved_array_2", array2);
    }

    public static void removeSavedArray(Player p){
        dataSource.update("wardrobe", p.getUniqueId(), "saved_array_1", "null");
        dataSource.update("wardrobe", p.getUniqueId(), "saved_array_2", "null");
    }

    public static void removePlayer(Player p ){
        dataSource.remove("wardrobe", p.getUniqueId());
    }

    public static String getSavedArray(Player p, int page){
        return dataSource.getStringResult("wardrobe", p.getUniqueId(), "saved_array_"+page );
    }

    public static boolean isPlayerInTable(Player p){
        return dataSource.isInTable("wardrobe", p.getUniqueId());
    }

    public static boolean hasSavedArray(Player p){
        return dataSource.isInTable("wardrobe", p.getUniqueId(), "saved_array_1")&&dataSource.isInTable("wardrobe", p.getUniqueId(), "saved_array_2") ;
    }


    @Deprecated
    public static void deleteTable(){
        dataSource.delete("wardrobe");
    }





}


