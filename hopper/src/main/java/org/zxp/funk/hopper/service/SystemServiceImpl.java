package org.zxp.funk.hopper.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.dao.IBaseDao;
import org.zxp.funk.hopper.entity.ServerConfig;

@Service 
public class SystemServiceImpl implements SystemService {

	@Resource(name="serverConfigDao") 
	private IBaseDao serverConfigDao;
	
	

	@Transactional(readOnly=false)
	@Override
	public void addTomcat(ServerConfig server) {
		serverConfigDao.add(server);
	}



	
	
	
}
