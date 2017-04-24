package org.zxp.funk.hopper.service;

import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.entity.ServerConfig;

public interface SystemService {
	public void addTomcat(ServerConfig server);
}
