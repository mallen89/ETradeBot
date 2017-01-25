package etradebot;

import com.etrade.etws.market.DetailFlag;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.client.MarketClient;
import com.etrade.etws.market.QuoteResponse;
import com.etrade.etws.sdk.common.ETWSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarketBot extends Thread
{
    
    private ClientRequest clientRequest;
    private MarketClient marketClient;
    private QuoteResponse quoteResponse;
    private ArrayList<String> tickers = new ArrayList<String>();
    
    public MarketBot(ClientRequest clientRequest)
    {
        this.clientRequest = clientRequest;
        marketClient = new MarketClient(clientRequest);
        quoteResponse = new QuoteResponse();
        tickers.add("CSCO");
        
    }
    
    //MarketBot's main tasks to run.
    public void run()
    {
        for(;;)
        {
            try {
                quoteResponse = marketClient.getQuote(tickers, new Boolean(true), DetailFlag.ALL);
            } catch (IOException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ETWSException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(quoteResponse);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
    //Gets list of stocks held by the user.
    public ArrayList<String> getPortfolioSymbols()
    {
        ArrayList<String> portfolioSymbols = new ArrayList<String>();
        
        return portfolioSymbols;
    }
    
    //Gets list of stocks watched by the user.
    public ArrayList<String> getWatchListSymbols()
    {
        ArrayList<String> watchListSymbols = new ArrayList<String>();
        
        return watchListSymbols;
    }
    
    
}
