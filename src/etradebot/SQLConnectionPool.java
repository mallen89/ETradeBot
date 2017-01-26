package etradebot;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnectionPool extends ObjectPool
{
    private String dsn;
    private String usr;
    private String pwd;
    
    //Connection Pool constructor.
    public SQLConnectionPool(String driver, String dsn, String usr, String pwd)
    {
        try
        {
            Class.forName(driver).newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        this.dsn = dsn;
        this.usr = usr;
        this.pwd = pwd;
    }
    
    Object create()
    {
        try
        {
            return( DriverManager.getConnection(dsn, usr, pwd));
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
    

