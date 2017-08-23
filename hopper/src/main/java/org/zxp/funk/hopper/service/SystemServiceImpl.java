package org.zxp.funk.hopper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.jpa.model.JdkConfig;
import org.zxp.funk.hopper.jpa.model.ServerConfig;
import org.zxp.funk.hopper.jpa.repository.JdkConfigRepository;
import org.zxp.funk.hopper.jpa.repository.ServerConfigRepository;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	private ServerConfigRepository srRep;
	@Autowired
	private JdkConfigRepository jdk;
	
	

	@Transactional(readOnly=false)
	@Override
	public void addTomcat(ServerConfig server) {
		srRep.save(server);
	}



	@Override
	public List<ServerConfig> findAllTomcat() {
		return srRep.findAll();
	}



	@Override
	public void delTomcat(String id) {
		srRep.delete(id);
	}



	@Override
	public ServerConfig findOneTomcat(String id) {
		return srRep.findOne(id);
	}



	@Override
	public void addJdk(JdkConfig jdkcof) {
		jdk.save(jdkcof);
	}

	@Override
	public JdkConfig findOneJdk(String id) {
		return jdk.findOne(id);
	}

	@Override
	public List<JdkConfig> findAllJdks() {
		return jdk.findAll();
	}



	@Override
	public void delJdk(String id) {
		jdk.delete(id);
	}






	
	
	
}
