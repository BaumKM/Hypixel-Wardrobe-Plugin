package baumkm.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DataSource {
    private Connection con;
    private PreparedStatement ps;

    public DataSource(Connection con){
        this.con = con;
    }


    /**
     *  creates a new Database Table
     * @param table Name of the table
     * @param elements Attributes you want to have in Table
     */
    public void createTable(String table, String elements){

        try {
            ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " " + elements);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inserts values into the table
     * @param table Name of the Table
     * @param elements Attributes you want to modify
     * @param values Values of the Attributes
     */
    public void add(String table, String elements, String values){
        try {
            ps = con.prepareStatement("INSERT INTO " + table + " "  + elements + " VALUES " + values);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * check if Player is in Table
     * @param table Name of the table
     * @param uuid UUID of the player
     * @return true if player is in table
     */
    public boolean isInTable(String table, UUID uuid){
        try {
            ps = con.prepareStatement("SELECT * FROM " + table + " WHERE uuid=? ");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            String s = "";
            if(rs.next()) {
                s = rs.getString("player_name");
                return true;
            }


        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * check if Player is in table and has a certain attribute saved
     * @param table name of the table
     * @param uuid UUID of the player
     * @param element Attribute you want to look for
     * @return true if player has a saved value of the attribute
     */
    public boolean isInTable(String table, UUID uuid, String element){
        try {
            ps = con.prepareStatement("SELECT " + element + " FROM " +table + " WHERE uuid=? ");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            String s = "";
            if(rs.next()){
                s = rs.getString(element);
                if(s == null) return false;
                else return true;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * updates the table dor a certain  player
     * @param table name of the table
     * @param uuid UUID of the player
     * @param element Attribute
     * @param value values for the Attribute
     */
    public void update(String table, UUID uuid, String element, String value){
        try {
            ps = con.prepareStatement("UPDATE "+ table + " SET " + element + " = ? WHERE uuid=?");
            ps.setString(1, value);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * gets a certain value from a certain Player
     * @param table name of the table
     * @param uuid UUID of the player
     * @param element attribute
     * @return null if attribute ins't in table
     */
    public String getStringResult(String table, UUID uuid, String element){
        try {
            ps = con.prepareStatement("SELECT " + element +" FROM "+ table + " WHERE uuid=? ");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            String s = "";
            if(rs.next()){
                s = rs.getString(element);
                return s;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * removes a certain player from the table
     * @param table name of the table
     * @param uuid UUID of the player
     */
    public void remove(String table, UUID uuid){
        try {
            ps = con.prepareStatement("DELETE FROM " + table +" WHERE uuid=?");
            ps.setString(1, uuid.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * deletes the complete table
     * @param table name of the table
     */
    public void delete(String table){
        try{
            ps = con.prepareStatement("DROP TABLE " + table);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
