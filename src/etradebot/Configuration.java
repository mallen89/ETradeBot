package etradebot;

import com.etrade.etws.account.Account;
import com.etrade.etws.account.AccountBalanceResponse;
import com.etrade.etws.account.AccountListResponse;
import com.etrade.etws.market.*;
import com.etrade.etws.oauth.sdk.client.IOAuthClient;
import com.etrade.etws.oauth.sdk.client.OAuthClientImpl;
import com.etrade.etws.oauth.sdk.common.Token;
import com.etrade.etws.sdk.client.AccountsClient;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.client.Environment;
import com.etrade.etws.sdk.client.MarketClient;
import com.etrade.etws.sdk.common.ETWSException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.awt.Desktop;
import java.net.URI;
public class Configuration extends Thread
{
    public Scanner keyboard = new  Scanner(System.in);

    private String oauth_consumer_key = "1775cbfea968fa82cb7ee8c5ca63c160"; // Your consumer key **SANDBOX**
    private String oauth_consumer_secret = "edabae17a8a6b897c540737da3e66b93"; // Your consumer secret **SANDBOX**
    private String oauth_request_token = null; // Request token
    private String oauth_request_token_secret = null; // Request token secret
    private String oauth_access_token = null; // Access Token
    private String oauth_access_token_secret = null; // Access Token Secret
    private String oauth_verify_code = null; // Verification Code

    private IOAuthClient ioAuthClient = null; // Client used to authorize bot
    private ClientRequest clientRequest = null;
    
    private Token requestToken = null;
    private Token accessToken = null;

    public synchronized void run()
    {
        ioAuthClient = OAuthClientImpl.getInstance(); // Instantiate IOAUthClient
        clientRequest = new ClientRequest(); // Instantiate ClientRequest
        clientRequest.setEnv(Environment.SANDBOX); // Use sandbox environment
        clientRequest.setConsumerKey(oauth_consumer_key); // Set consumer key
        clientRequest.setConsumerSecret(oauth_consumer_secret); //Set consumer secret

        requestToken = obtainRequestToken(clientRequest); // Get request-token object
        oauth_request_token = requestToken.getToken(); // Get token string
        oauth_request_token_secret = requestToken.getSecret(); // Get token secret

        clientRequest.setToken(oauth_request_token);
        clientRequest.setTokenSecret(oauth_request_token_secret);

        obtainVerificationCode(ioAuthClient,clientRequest);

        clientRequest.setVerifierCode(oauth_verify_code);

        obtainAccessToken(ioAuthClient, clientRequest);

        oauth_access_token = accessToken.getToken();
        oauth_access_token_secret = accessToken.getSecret();

        clientRequest.setToken(oauth_access_token);
        clientRequest.setTokenSecret(oauth_access_token_secret);
        
        notifyAll();

    }


    public Token obtainRequestToken(ClientRequest newClientRequest)
    {
        System.out.println("Obtaining Request Token");

        Token newRequestToken = new Token();

        try {
            newRequestToken = ioAuthClient.getRequestToken(newClientRequest);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ETWSException e)
        {
            e.printStackTrace();
        }

        return newRequestToken;
    }


    public void obtainVerificationCode(IOAuthClient newIOAuthClient, ClientRequest newClientRequest)
    {
        System.out.println("Obtaining Verification Code");

        String authorizeURL = null;
        URI uri = null;

        try
        {
            authorizeURL = newIOAuthClient.getAuthorizeUrl(newClientRequest); // E*TRADE authorization URL
        }

        catch (ETWSException e)
        {
            e.printStackTrace();
        }

        try
        {
            uri = new URI(authorizeURL);
        }

        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        Desktop desktop = Desktop.getDesktop();

        try
        {
            desktop.browse(uri);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        do
        {
            System.out.println("Please enter a valid verification code: ");
            oauth_verify_code = keyboard.nextLine();
        }
        while(oauth_verify_code == null);
    }

    public void obtainAccessToken(IOAuthClient newIOAuthClient, ClientRequest newClientRequest)
    {
        try
        {
            accessToken = newIOAuthClient.getAccessToken(newClientRequest); // Get Access-Token Object
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        catch (ETWSException e)
        {
            e.printStackTrace();
        }
    }
    
    public ClientRequest getClientRequest()
    {
        return clientRequest;
    }
}
