package baumkm.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL_Connection {
    private static final String host = "localhost";
    private static final String port = "3306";
    private static final String database = "kyra";
    private static final String username = "root";
    private static final String password = "";

    private Connection connection;


    public boolean isConnected(){
        return (this.connection == null ? false : true);
    }

    /**
     * connects to the database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
        }

    }

    /**
     * disconnects from the database
     * @throws SQLException
     */
    public void disconnect() throws SQLException{
        if(isConnected()) this.connection.close();

    }

    /**
     * gets the connection
     * @return
     */
    public Connection getConnection(){
        return this.connection;
    }


}
