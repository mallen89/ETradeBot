package etradebot;


public class ETradeBot {

    public static void main(String[] args) 
    {
        BotConfiguration config = new BotConfiguration();
        
        config.initiateBot();
        
        new MarketBot(config.getClientRequest()).start();
        
    }
    
}
