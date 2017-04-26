package org.zxp.funk.hopper.service;

import java.util.List;

import org.zxp.funk.hopper.jpa.model.ServerConfig;

public interface SystemService {
	public void addTomcat(ServerConfig server);
	public List<ServerConfig> findAllTomcat();
}
