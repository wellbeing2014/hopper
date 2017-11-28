package org.zxp.funk.hopper.core.tomcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
        //this.setStreamHandler(new PumpStreamHandler(stdOutLog));
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
    

    public void startup() throws HopperException{
    	notifyLogEvent(new HopperLogEventObject(this, "[HOPPER] 正在执行启动操作。"));
    	this.setStreamHandler(new PumpStreamHandler(stdOutLog));
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
    	notifyLogEvent(new HopperLogEventObject(this, "[HOPPER] 正在执行停止操作。"));
    	this.setStreamHandler(new PumpStreamHandler(stdOutLog));
    	validateEnv();
    	try {
    		running = true;
			execute(new CommandLine(getTomcatHome()+"/bin/shutdown.bat"), environment,handler);
			notifyTomcatStatus(new HopperStatusEventObject(this, HopperStatus.SHUTDOWN));
		} catch (Exception e){
			throw new HopperException("00102","关闭服务失败"+e.getMessage());
		}
    }
    
    
    
    protected synchronized void killprogress(int pid) throws Exception{
    	isKill = false;
    	this.setStreamHandler(new PumpStreamHandler(new BaseOutputSteam(100,"gbk") {
    		@Override
    		public void handleLine(String line) {
    			notifyLogEvent(new HopperLogEventObject(this, line));
    		}
    	}));
    	CommandLine cl = new CommandLine("taskkill");
    	cl.addArgument("/F");
    	cl.addArgument("/PID");
    	cl.addArgument(""+pid);
    	environment.put("PATH", "C:\\Windows\\system32");
    	
		execute(cl, environment,new DefaultExecuteResultHandler() {
			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				isKill = true;
				notifyLogEvent(new HopperLogEventObject(this, "[HOPPER] 进程PID["+pid+"]成功杀死。"));
			}
			
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				isKill = true;
				notifyLogEvent(new HopperLogEventObject(this, "[HOPPER] 进程PID["+pid+"]杀不死它:"+e.getMessage()));
			}
		});
		
		while(!isKill) {
			Thread.sleep(1000);
		}
			
    }
    
    boolean isKill = false;
    boolean isFind = false;
    protected synchronized int findPids(int port) throws Exception {
    	List<String[]> netstat = new ArrayList<>();
    	Pattern pattern = Pattern.compile("\\S+");
    	isFind = false;
    	this.setStreamHandler(new PumpStreamHandler(new BaseOutputSteam(100,"gbk") {
    		@Override
    		public void handleLine(String line) {
    			notifyLogEvent(new HopperLogEventObject(this, line));
    			Matcher matcher = pattern.matcher(line);
    			String XieYi = "";
    			String BenDi = "";
    			String WaiBu = "";
    			String ZhuangT = "";
    			String PID = "";
    			if(matcher.find()) XieYi = matcher.group();
    			if(matcher.find()) BenDi = matcher.group();
    			if(matcher.find()) WaiBu = matcher.group();
    			if(matcher.find()) ZhuangT = matcher.group();
    			if(matcher.find()) PID = matcher.group();
    			netstat.add(new String[] {XieYi,BenDi,WaiBu,ZhuangT,PID});
    		}
    	}));
    	CommandLine cl = new CommandLine("cmd");
    	cl.addArgument("/C");
    	cl.addArgument("netstat -aon|findstr "+port);
    	environment.put("PATH", "C:\\Windows\\system32");
		execute(cl, environment,new DefaultExecuteResultHandler(){
			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				isFind = true;
			}
			
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				isFind = true;
			}
		});
		
		while(!isFind) {
			Thread.sleep(1000);
		}
		
		int ret = 0;
    	for(String[] progress:netstat) {
    		boolean got = ("TCP".equals(progress[0]))
    				&&("LISTENING".equals(progress[3]))
    				&&(("0.0.0.0:"+port).equals(progress[1]));
    		if(got) ret = Integer.parseInt(progress[4]);
    		else continue;
    	}
    	return ret;
    }
    
    
    public void shutdownForce(int port) throws Exception{
    	notifyLogEvent(new HopperLogEventObject(this, "[HOPPER] 正在通过杀进程的方式强制停止端口["+port+"]"));
    	int pid = findPids(port);
    	notifyLogEvent(new HopperLogEventObject(this, "[HOPPER] 找到端口["+port+"]所在的进程PID["+pid+"]"));
    	killprogress(pid);
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
    
	@Override
	public void  cacheLogs() {
		
		Iterator<String> logs = stdOutLog.getCache();
		while(logs.hasNext())
		{
			notifyLogEvent(new HopperLogEventObject(this, logs.next()));
		}
	}

}
