package etradebot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration 
{
    String userName = "etradeBot";
    String password = "password";
    String url = "jdbc:sqlserver://tigerlily.arvixe.com;databaseName=StockBot";
    
    Connection conn;

    public DatabaseConfiguration() throws SQLException
    {
        conn = DriverManager.getConnection(url, userName, password);
        
    }
    
    public Connection getConnection()
    {
        return conn;
    }
    
    public void AddQuoteData()
    {
        
    }
    
    
    
    
    
    
}
