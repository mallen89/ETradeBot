package etradebot;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ETradeBot {

    public static void main(String[] args) throws SQLException 
    {
        BotConfiguration config = new BotConfiguration();
        //SQLConnectionPool dbConnectionPool = new SQLConnectionPool("test","jdbc:sqlserver://tigerlily.arvixe.com;databaseName=StockBot","test","etradeBot","password");
        
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://tigerlily.arvixe.com;databaseName=StockBot", "etradeBot", "password");
        
        config.initiateBot();
        
        new MarketBot(config.getClientRequest(), 3, connection).start();
        
    }
    
}

// http://www.javaworld.com/article/2076690/java-concurrency/build-your-own-objectpool-in-java-to-boost-app-speed.html