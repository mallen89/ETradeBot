package etradebot;

import database.SQLConnectionPool;

public class ETradeBot {

    public static void main(String[] args)
    {
        BotConfiguration config = new BotConfiguration();

        SQLConnectionPool pool = new SQLConnectionPool("jdbc:sqlserver://tigerlily.arvixe.com;databaseName=StockBot", "etradeBot", "password");
        
        config.startAuthentication();
        
        new MarketController(config.getClientRequest(), 3, pool.borrowConnection()).start();
        
    }
    
}

// http://www.javaworld.com/article/2076690/java-concurrency/build-your-own-objectpool-in-java-to-boost-app-speed.html