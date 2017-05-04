package org.zxp.funk.hopper.service;

import java.util.List;

import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerInstance;

public interface ServerService {
	public boolean startup(ServerInstance server);
	public boolean shutdown(ServerInstance server);
	public List<String> getlog(String id);
	public List<ServerStatus> getStatus();
}
