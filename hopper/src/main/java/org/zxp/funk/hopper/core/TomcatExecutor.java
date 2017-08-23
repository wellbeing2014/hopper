package org.zxp.funk.hopper.core;

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

public class TomcatExecutor  extends DefaultExecutor implements IhopperExecutor{

    private final HopperOutputSteam stdOutLog = new HopperOutputSteam(100,"gbk") {
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
    
    public void setJavaOpts(String javaopts){
    	environment.put("JAVA_OPTS", javaopts);
    }
    
    public String getJavaOpts(){
    	return environment.get("JAVA_OPTS");
    }
    
    public void setTomcatBase(String tomcatBase){
    	environment.put("CATALINA_BASE", tomcatBase);
    }
    
    public String getTomcatBase(){
    	return environment.get("CATALINA_BASE");
    }
    
    
    public void setJavaHome(String javaHome){
    	environment.put("JAVA_HOME", javaHome);
    	environment.put("PATH", "%JAVA_HOME%/bin;%PATH%");
    	environment.put("CLASSPATH", ".;%JAVA_HOME%/lib;%JAVA_HOME%/lib/tools.jar");
    }
    
    public void setTomcatPort(int port){
    	
    }

    public void startup() throws HopperException{
    	validateEnv();
    	try {
			execute(new CommandLine(getTomcatHome()+"/bin/startup.bat"), environment,new DefaultExecuteResultHandler(){
				@Override
				public void onProcessComplete(int exitValue) {
					super.onProcessComplete(exitValue);
					notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STOPPED));
				}
				
				@Override
				public void onProcessFailed(ExecuteException e) {
					super.onProcessFailed(e);
					notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STOPPED));
				}
			});
			
			notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STARTED));
		} catch (Exception e){
			throw new HopperException("00101","启动服务失败"+e.getMessage());
		}
    	
    }
    
    public void shutdown() throws HopperException{
    	validateEnv();
    	try {
			execute(new CommandLine(getTomcatHome()+"/bin/shutdown.bat"), environment,new DefaultExecuteResultHandler(){
				@Override
				public void onProcessComplete(int exitValue) {
					super.onProcessComplete(exitValue);
					notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STOPPED));
				}
				
				@Override
				public void onProcessFailed(ExecuteException e) {
					super.onProcessFailed(e);
					notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.STOPPED));
				}
			});
			notifyTomcatStatus(new TomcatStatusEventObject(this, TomcatStatus.SHUTDOWN));
		} catch (Exception e){
			throw new HopperException("00102","关闭服务失败"+e.getMessage());
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
    
    private void validateEnv() throws HopperException{
    	if(!environment.containsKey("JAVA_HOME")){
    		throw new HopperException("00100","未找到环境变量：JAVA_HOME");
    	}
    	
    	if(!environment.containsKey("CATALINA_HOME")){
    		throw new HopperException("00100","未找到环境变量：CATALINA_HOME");
    	}
    }
    

}
