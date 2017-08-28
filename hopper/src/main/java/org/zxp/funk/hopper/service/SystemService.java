package org.zxp.funk.hopper.service;

import java.util.List;

import org.zxp.funk.hopper.jpa.entity.JdkConfig;
import org.zxp.funk.hopper.jpa.entity.ServerConfig;

public interface SystemService {
	public void addTomcat(ServerConfig server);
	public ServerConfig findOneTomcat(String id);
	public void delTomcat(String id);
	public List<ServerConfig> findAllTomcat();
	
	public void addJdk(JdkConfig jdk);
	public JdkConfig findOneJdk(String id);
	public void delJdk(String id);
	public List<JdkConfig> findAllJdks();
}
