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

public class MarketBot extends Thread
{
    
    private ClientRequest clientRequest;
    private MarketClient marketClient;
    private QuoteResponse quoteResponse;
    private ArrayList<String> tickers = new ArrayList<String>();
    private int sleepScale = 1;
    private Connection connection;
    private ArrayList<String> testSymbol = new ArrayList<String>();
    private Timestamp stamp;

    //Constructor for independent Market Bot.
    public MarketBot(ClientRequest clientRequest, int sleepScale, Connection connection)
    {
        this.clientRequest = clientRequest;
        this.sleepScale = sleepScale;
        marketClient = new MarketClient(clientRequest);
        quoteResponse = new QuoteResponse();
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
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           sleepBot();
        }  
    }
    
    //Gets list of stocks held by the user.
    public ArrayList<String> getPortfolioSymbols()
    {
        ArrayList<String> portfolioSymbols = new ArrayList<String>();
        
        return portfolioSymbols;
    }
    
    //Gets an updated list of stocks held by the user.
    public ArrayList<String> getUpdatedPortfolioSymbols()
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
    
    //Gets an updated list of stocks watched by the user.
    public ArrayList<String> getUpdatedWatchListSymbolds()
    {
        ArrayList<String> watchListSymbols = new ArrayList<String>();
        
        return watchListSymbols;
    }
    
    //Gets quote data from ETrade.
    void getMarketData(ArrayList<String> symbols) throws SQLException
    {
        try {
                quoteResponse = marketClient.getQuote(symbols, new Boolean(true), DetailFlag.ALL);
            } catch (IOException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ETWSException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        for(QuoteData quoteData : quoteResponse.getQuoteData())
        {
            addToDatabase(quoteData.getAll());
        }
    }
    
    //Adds quote data to database.
    void addToDatabase(AllQuote allQuote) throws SQLException
    {
        Statement statement = connection.createStatement();
        Date date = new Date();
        stamp = new Timestamp(date.getTime());
        
        String query = "INSERT INTO QUOTEDATA (SYMBOLDESC, LASTPRICE, BID, ASK, TIME)\n" +
                        "VALUES('" + allQuote.getSymbolDesc() + "', " + allQuote.getLastTrade() + ", " + allQuote.getBid()+ ", " + allQuote.getAsk() + "," +" '" + stamp + "');"; 
        
        statement.executeUpdate(query);
    }
    
    //Sleeps bot for (1 * scale) seconds. Default 1 second.
    void sleepBot()
    {
        try {
                Thread.sleep(1000 * sleepScale);
            } catch (InterruptedException ex) {
                Logger.getLogger(MarketBot.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
