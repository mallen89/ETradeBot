package etradebot;


public class ETradeBot {

    public static void main(String[] args) 
    {
        Configuration config = new Configuration();
        
        new MarketBot(config.getClientRequest()).start();
        
        config.start();
        
        
        
    }
    
}
