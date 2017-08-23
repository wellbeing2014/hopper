package org.zxp.funk.hopper.service;

import java.util.List;

import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerOperation;
import org.zxp.funk.hopper.jpa.model.TomcatServer;

public interface ServerService {
	public void startup(String id) throws Exception;
	public void shutdown(String id) throws Exception;
	public List<String> getlog(String id);
	public ServerStatus[] getStatus();
	public void addOperation(ServerOperation operation);
	public TomcatServer findOne(String id);
	TomcatServer addServer(TomcatServer sc) throws Exception;
	void delServer(String id) throws Exception;
	
}
