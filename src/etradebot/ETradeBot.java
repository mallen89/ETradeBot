package etradebot;


public class ETradeBot {

    public static void main(String[] args) 
    {
        Configuration config = new Configuration();
        
        config.initiateBot();
        
        new MarketBot(config.getClientRequest()).start();
        
    }
    
}
