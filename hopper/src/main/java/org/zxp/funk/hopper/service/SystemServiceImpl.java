package org.zxp.funk.hopper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.jpa.model.ServerConfig;
import org.zxp.funk.hopper.jpa.repository.ServerConfigRepository;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	private ServerConfigRepository srRep;
	
	

	@Transactional(readOnly=false)
	@Override
	public void addTomcat(ServerConfig server) {
		srRep.save(server);
	}



	@Override
	public List<ServerConfig> findAllTomcat() {
		return srRep.findAll();
	}






	
	
	
}
