package etradebot;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnectionPool extends ObjectPool
{
    private String serverAddress;
    private String database;
    private String userName;
    private String password;
    
    //Connection Pool constructor.
    public SQLConnectionPool(String driver, String serverAddress, String database, String userName, String password)
    {
        try
        {
            Class.forName(driver).newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        this.serverAddress = serverAddress;
        this.database = database;
        this.userName = userName;
        this.password = password;
    }
    
    Object create()
    {
        try
        {
            return( DriverManager.getConnection(serverAddress, userName, password));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    void expire(Object object)
    {
        try
        {
            ((Connection) object).close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    boolean validate(Object object)
    {
        try
        {
            return(!((Connection) object).isClosed());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return(false);
        }
    }
    
    public Connection borrowConnection()
    {
        return((Connection) super.getInstance());
    }
    
    public void returnConnection(Connection connection)
    {
        super.freeInstance(connection);
    }
}
    

