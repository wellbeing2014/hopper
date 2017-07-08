package org.zxp.funk.hopper.service;

import java.util.List;

import org.zxp.funk.hopper.jpa.model.JdkConfig;
import org.zxp.funk.hopper.jpa.model.ServerConfig;

public interface SystemService {
	public void addTomcat(ServerConfig server);
	public ServerConfig findOne(String id);
	public void delTomcat(String id);
	public List<ServerConfig> findAllTomcat();
	
	public void addJdk(JdkConfig jdk);
	public void delJdk(String id);
	public List<JdkConfig> findAllJdks();
}
