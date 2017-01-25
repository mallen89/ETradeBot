package etradebot;

import com.etrade.etws.market.AllQuote;
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
    
    public synchronized void run()
    {
        System.out.println("Before Wait");
        
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("After Wait");
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
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }  
    }
}
