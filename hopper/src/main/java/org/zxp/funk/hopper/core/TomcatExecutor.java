package org.zxp.funk.hopper.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

public class TomcatExecutor extends DefaultExecutor{

    private final StdOutLog stdOutLog = new StdOutLog(100) {
		
		@Override
		public void changeRunning(int i) {
			notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.RUNNING));
		}
	};
	
	private final ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
    
    private Map<String,String> environment =new HashMap<String,String>();

    private Vector<TomcatStatusEventListener> statuseventlist=new Vector<TomcatStatusEventListener>();
    
    public TomcatExecutor() {
        super();
        stdOutLog.setRunningRegex(".*Server startup in \\d+ ms");
        this.setStreamHandler(new PumpStreamHandler(stdOutLog));
        this.setWatchdog(watchdog);
    }

    public void addTomcatLogEventListener(TomcatLogEventListener listener){
    	stdOutLog.addTomcatLogEventListener(listener);
    }
    
    public void removeTomcatLogEventListener(TomcatLogEventListener listener){
    	stdOutLog.removeTomcatLogEventListener(listener);
    }
    
    
    public void addTomcatStatusEventListener(TomcatStatusEventListener listener){
    	statuseventlist.add(listener);
    }
    
    public void removeTomcatStatusEventListener(TomcatStatusEventListener listener){
    	statuseventlist.remove(listener);
    }
    
    public void setTomcatHome(String tomcatHome){
    	environment.put("CATALINA_HOME", tomcatHome);
    }
    
    public String getTomcatHome(){
    	return environment.get("CATALINA_HOME");
    }
    
    public void setTomcatBase(String tomcatBase){
    	environment.put("TOMCAT_BASE", tomcatBase);
    }
    
    public String getTomcatBase(){
    	return environment.get("TOMCAT_BASE");
    }
    
    
    public void setJavaHome(String javaHome){
    	environment.put("JAVA_HOME", javaHome);
    	environment.put("PATH", "%JAVA_HOME%/bin;%PATH%");
    	environment.put("CLASSPATH", ".;%JAVA_HOME%/lib;%JAVA_HOME%/lib/tools.jar");
    }
    
    public void setTomcatPort(int port){
    	
    }

    public void startup(){
    	
    	try {
			execute(new CommandLine(getTomcatHome()+"/bin/startup.bat"), environment,new DefaultExecuteResultHandler(){
				@Override
				public void onProcessComplete(int exitValue) {
					super.onProcessComplete(exitValue);
					notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STOPPED));
				}
				
				@Override
				public void onProcessFailed(ExecuteException e) {
					// TODO Auto-generated method stub
					super.onProcessFailed(e);
					notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.SHUTDOWN));
				}
			});
			
			notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STARTED));
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void shutdown(){
    	
    	try {
			execute(new CommandLine(getTomcatHome()+"/bin/shutdown.bat"), environment);
			notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.SHUTDOWN));
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    
    
    public void notifyTomcatStatus(TomcatStatusEventObject obj)  
    {   
         Iterator<TomcatStatusEventListener> it=statuseventlist.iterator();  
         while(it.hasNext())  
         {  
             it.next().statusChanged(obj); 
         }  
    }  
    
    
    

}
