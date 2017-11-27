package org.zxp.funk.hopper.core.tomcat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.zxp.funk.hopper.core.BaseOutputSteam;
import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.core.HopperLogEventListener;
import org.zxp.funk.hopper.core.HopperLogEventObject;
import org.zxp.funk.hopper.core.HopperStatus;
import org.zxp.funk.hopper.core.HopperStatusEventListener;
import org.zxp.funk.hopper.core.HopperStatusEventObject;
import org.zxp.funk.hopper.core.IhopperExecutor;

public class TomcatExecutor  extends DefaultExecutor implements IhopperExecutor{

    private final BaseOutputSteam stdOutLog = new BaseOutputSteam(100,"gbk") {
		@Override
		public void handleLine(String line) {
			
			notifyLogEvent(new HopperLogEventObject(this, line));
			boolean isRunning = Pattern.matches(".*Server startup in \\d+ ms", line);
	    	if(isRunning) {
	    		Pattern pattern = Pattern.compile("\\d+");
	            Matcher matcher = pattern.matcher(line);
	            if(matcher.find())
	            	notifyStatusEvent(new HopperStatusEventObject(this, HopperStatus.RUNNING));
	    	}
		}
	};
	
	private final ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
    
    private Map<String,String> environment =new HashMap<String,String>();

    private DefaultExecuteResultHandler handler = null;
    
    private boolean running = false;
    public TomcatExecutor() {
        super();
        this.setStreamHandler(new PumpStreamHandler(stdOutLog));
        this.setWatchdog(watchdog);
        handler =new DefaultExecuteResultHandler(){
			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				running = false;
				notifyTomcatStatus(new HopperStatusEventObject(this, HopperStatus.STOPPED));
			}
			
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				running = false;
				notifyTomcatStatus(new HopperStatusEventObject(this, HopperStatus.STOPPED));
			}
		}; 
    }
    
    private Vector<HopperStatusEventListener> statuseventlist=new Vector<HopperStatusEventListener>();
    
    public void addStatusEventListener(HopperStatusEventListener listener){
    	statuseventlist.add(listener);
    }
    
    public void removeStatusEventListener(HopperStatusEventListener listener){
    	statuseventlist.remove(listener);
    }
    
    public void notifyStatusEvent(HopperStatusEventObject obj)  
    {   
         Iterator<HopperStatusEventListener> it=statuseventlist.iterator();  
         while(it.hasNext())  
         {  
             it.next().statusChanged(obj); 
         }  
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
    		running = true;
			execute(new CommandLine(getTomcatHome()+"/bin/startup.bat"), environment,handler);
			notifyTomcatStatus(new HopperStatusEventObject(this, HopperStatus.STARTED));
		} catch (Exception e){
			throw new HopperException("00101","启动服务失败"+e.getMessage());
		}
    	
    }
    
    public void shutdown() throws HopperException{
    	validateEnv();
    	try {
    		running = true;
			execute(new CommandLine(getTomcatHome()+"/bin/shutdown.bat"), environment,handler);
			notifyTomcatStatus(new HopperStatusEventObject(this, HopperStatus.SHUTDOWN));
		} catch (Exception e){
			throw new HopperException("00102","关闭服务失败"+e.getMessage());
		}
    	
    }
    
    public void shutdownForce(int port) throws HopperException{
    	CommandLine killprogress = new CommandLine("netstat");
    	killprogress.addArgument("-aon |findstr");
    	killprogress.addArgument(port+"");
    	try {
			execute(killprogress, environment,handler);
			notifyTomcatStatus(new HopperStatusEventObject(this, HopperStatus.SHUTDOWN));
		} catch (Exception e){
			throw new HopperException("00103","强制关闭服务失败"+e.getMessage());
		}
    	
    }
    
   
    
    public void notifyTomcatStatus(HopperStatusEventObject obj)  
    {   
         Iterator<HopperStatusEventListener> it=statuseventlist.iterator();  
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


	@Override
	public boolean isRunning() {
		return running;
	}
    

}
