package org.zxp.funk.hopper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerInstance;

@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	public ServerList sl;

	@Override
	public boolean startup(ServerInstance server) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shutdown(ServerInstance server) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getlog(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerStatus> getStatus() {
		return sl.wapper();
	}
	
}
