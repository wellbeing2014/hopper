package org.zxp.funk.hopper.core;

import java.util.List;

import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerOperation;
import org.zxp.funk.hopper.jpa.model.TomcatServer;

public class ServerBehavior {
	private TomcatExecutor executor;
	public TomcatServer server;
	public List<ServerOperation> operations;
	
	public ServerStatus status = new ServerStatus();
	
	private StringBuffer currentStatus= new StringBuffer(TomcatStatus.STOPPED.toString());
	
	public ServerBehavior(TomcatServer instance){
		this.server=instance;
		
		status.setServerid(server.getServerid());
		status.setServername(server.getServername());
		status.setStatus(currentStatus);
		status.setMainport(server.getMainport());
		status.setLoalpaths(server.getLoalPaths());
		status.setOpr(Integer.toString(server.getOperations()));
		status.setMark(server.getDesc());
		status.setLasttime(server.getLasttime());
		
		
		executor = new TomcatExecutor();
		executor.setTomcatHome("D:/Develop/Apache/Tomcat/apache-tomcat-7.0.73");
		executor.setJavaHome("D:/Develop/Java/jdk1.7.0_71");
		this.executor.addTomcatStatusEventListener(new TomcatStatusEventListener() {
			
			@Override
			public void statusChanged(TomcatStatusEventObject obj) {
				currentStatus.setLength(0);
				currentStatus.append(obj.status.toString());
			}
		});
		
		this.executor.addTomcatLogEventListener(new TomcatLogEventListener() {
			
			@Override
			public void logEvent(TomcatLogEventObject obj) {
				System.out.println(obj.log);
			}
		});
	}
	
	
	public void startup() throws HopperException {
		executor.startup();
	}
	
	
	public void shutdown() throws HopperException {
		
		executor.shutdown();
	
	}
	
	
	
	public IhopperExecutor getExecutor() {
		return this.executor;
	}
	
	
}
