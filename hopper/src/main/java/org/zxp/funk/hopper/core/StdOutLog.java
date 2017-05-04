package org.zxp.funk.hopper.core;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.LogOutputStream;

public abstract class StdOutLog extends LogOutputStream {
	
    private  ArrayBlockingQueue<String> cache;
    
    private String runningRegex;
    
    public void setRunningRegex(String runningRegex) {
		this.runningRegex = runningRegex;
	}


	public StdOutLog(int cacheSize) {
    	cache=new ArrayBlockingQueue<String>(cacheSize);
	}
    
    
    @Override
    protected void processLine(String line, int level) {
    	notifyLogEvent(new TomcatLogEventObject(this, line));
    	boolean isRunning = Pattern.matches(runningRegex, line);
    	if(isRunning) {
    		Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            changeRunning(Integer.valueOf(matcher.group()));
    	}
    	while(!cache.offer(line)) cache.poll();
    }
    
    public Iterator<String> getCache(){
    	return cache.iterator();
    }
    
    
    private Vector<TomcatLogEventListener> logeventlist=new Vector<TomcatLogEventListener>();
    
    
    
    
    public void addTomcatLogEventListener(TomcatLogEventListener listener){
    	logeventlist.add(listener);
    }
    
    public void removeTomcatLogEventListener(TomcatLogEventListener listener){
    	logeventlist.remove(listener);
    }
    
   
    
    public void notifyLogEvent(TomcatLogEventObject obj)  
    {   
         Iterator<TomcatLogEventListener> it=logeventlist.iterator();  
         while(it.hasNext())  
         {  
             it.next().logEvent(obj); 
         }  
    }  
    
    public  abstract void changeRunning(int i);
};
