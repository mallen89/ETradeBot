package database;

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool 
{
    private long expirationTime;
    private Hashtable locked;
    private Hashtable unlocked;
    
   
    ObjectPool()
    {
        expirationTime = 600000; //10 Minutes
        locked = new Hashtable();
        unlocked = new Hashtable();
    }
    
    abstract Object create();
    abstract boolean validate(Object o);
    abstract void expire(Object o);
    
    synchronized Object checkOut()
    {
        long now = System.currentTimeMillis();
        Object object;
        
       if(unlocked.size() > 0)
       {
          Enumeration e = unlocked.keys();  
          while(e.hasMoreElements())
          {
             object = e.nextElement();           
             if((now - ((Long) unlocked.get(object)).longValue()) > expirationTime)
             {
                //Object has expired
                unlocked.remove(object);
                expire(object);
                object = null;
             }
             else
             {
                if( validate(object))
                {
                   unlocked.remove(object);
                   locked.put(object, new Long(now));                
                   return(object);
                }
                else
                {
                   //Object failed validation
                   unlocked.remove(object);
                   expire(object);
                   object = null;
                }
             }
          }
       }        
       //No objects available, create a new one
       object = create();        
       locked.put(object, new Long(now)); 
       return(object);
    }
    
    synchronized void checkIn(Object object)
    {
        locked.remove(object);
        unlocked.put(object, new Long(System.currentTimeMillis()));
    }
}
