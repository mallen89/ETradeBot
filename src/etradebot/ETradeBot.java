package etradebot;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import java.sql.SQLException;


public class ETradeBot {

    public static void main(String[] args) throws SQLException 
    {
        BotConfiguration config = new BotConfiguration();
        
        config.initiateBot();
        
        new MarketBot(config.getClientRequest()).start();
        
    }
    
}
