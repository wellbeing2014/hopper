package org.zxp.funk.hopper.core;

import org.zxp.funk.hopper.jpa.model.ServerInstance;

public class ServerBehavior {
	private TomcatExecutor executor;
	private ServerInstance instance;
	private TomcatStatus currentStatus=TomcatStatus.STOPPED;
	
	public ServerBehavior(ServerInstance instance){
		this.instance=instance;
		executor = new TomcatExecutor();
		this.executor.addTomcatStatusEventListener(new TomcatStatusEventListener() {
			
			@Override
			public void statusChanged(TomcatStatusEventObject obj) {
				// TODO Auto-generated method stub
				currentStatus=obj.status;
			}
		});
	}
	
	public ServerInstance getServerInstance(){
		return this.instance;
	}
	
	public TomcatStatus getTomcatStatus(){
		return this.currentStatus;
	}
}
