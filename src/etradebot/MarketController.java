package etradebot;

import com.etrade.etws.market.AllQuote;
import com.etrade.etws.market.DetailFlag;
import com.etrade.etws.market.QuoteData;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.client.MarketClient;
import com.etrade.etws.market.QuoteResponse;
import com.etrade.etws.sdk.common.ETWSException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarketController implements Runnable
{ 
    private MarketClient marketClient;
    private int sleepScale = 1;
    private Connection connection;
    private ArrayList<String> testSymbol = new ArrayList<String>();
    private ArrayList<AllQuote> currentMarketData = new ArrayList<AllQuote>();
    private Timestamp stamp;

    //Constructor for independent Market Controller.
    public MarketController(ClientRequest clientRequest, int sleepScale, Connection connection)
    {
        this.sleepScale = sleepScale;
        marketClient = new MarketClient(clientRequest);
        this.sleepScale = sleepScale;
        this.connection = connection;
        testSymbol.add("MSFT");
    }
    
    //Market Bot's main tasks to run.
    public void run()
    {
        for(;;)
        {
            try {
                getMarketData(testSymbol);
            } catch (SQLException ex) {
                Logger.getLogger(MarketController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           sleepBot();
        }  
    }
    
    //Gets list of stocks held by the user.
    private ArrayList<String> getPortfolioSymbols()
    {
        ArrayList<String> portfolioSymbols = new ArrayList<String>();
        
        return portfolioSymbols;
    }
    
    //Gets list of stocks watched by the user.
    private ArrayList<String> getWatchListSymbols()
    {
        ArrayList<String> watchListSymbols = new ArrayList<String>();
        
        return watchListSymbols;
    }
    
    //Gets quote data from ETrade.
    private void getMarketData(ArrayList<String> symbols) throws SQLException
    {
        QuoteResponse quoteResponse = new QuoteResponse();
        
        try {
                quoteResponse = marketClient.getQuote(symbols, new Boolean(true), DetailFlag.ALL);
            } catch (IOException ex) {
                Logger.getLogger(MarketController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ETWSException ex) {
                Logger.getLogger(MarketController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        currentMarketData = new ArrayList<AllQuote>();
        
        for(QuoteData quoteData : quoteResponse.getQuoteData())
        {
            currentMarketData.add(quoteData.getAll());
            addToDatabase(quoteData.getAll());
        }
    }
    
    //Adds quote data to database.
    private void addToDatabase(AllQuote allQuote) throws SQLException
    {
        Statement statement = connection.createStatement();
        Date date = new Date();
        stamp = new Timestamp(date.getTime());
        
        String query = "INSERT INTO QUOTEDATA (SYMBOLDESC, LASTPRICE, BID, ASK, TIME)\n" +
                        "VALUES('" + allQuote.getSymbolDesc() + "', " + allQuote.getLastTrade() + ", " 
                + allQuote.getBid()+ ", " + allQuote.getAsk() + "," +" '" + stamp + "');"; 
        
        statement.executeUpdate(query);
    }
    
    //Sleeps bot for (1 * scale) seconds. Default 1 second.
    private void sleepBot()
    {
        try {
                Thread.sleep(1000 * sleepScale);
            } catch (InterruptedException ex) {
                Logger.getLogger(MarketController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    //Allows other parts of the bot to recieve most current data.
    //** Important for executing trades with tight constraints. **
    public ArrayList<AllQuote> getCurrentData()
    {
        return currentMarketData;
    }
    
}
