package org.zxp.funk.hopper.core;

import java.util.Iterator;
import java.util.Vector;

public abstract class HopperOutputSteam2  extends BaseOutputSteam {

	public HopperOutputSteam2(int cacheSize,String charset) {
		super(cacheSize,charset);
	}
    
    private Vector<HopperLogEventListener> logeventlist=new Vector<HopperLogEventListener>();
    
    public void addLogEventListener(HopperLogEventListener listener){
    	logeventlist.add(listener);
    }
    
    public void removeLogEventListener(HopperLogEventListener listener){
    	logeventlist.remove(listener);
    }
    
    
    public void notifyLogEvent(HopperLogEventObject obj)  
    {   
         Iterator<HopperLogEventListener> it=logeventlist.iterator();  
         while(it.hasNext())  
         {  
             it.next().logEvent(obj); 
         }  
    } 
    
    
	@Override
	public void handleLine(String line) {
		
		notifyLogEvent(new HopperLogEventObject(this, line));
    	
	}
	
}
