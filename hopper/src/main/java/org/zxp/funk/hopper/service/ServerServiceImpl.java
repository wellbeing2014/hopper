package org.zxp.funk.hopper.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerOperation;
import org.zxp.funk.hopper.jpa.model.TomcatServer;
import org.zxp.funk.hopper.jpa.repository.ServerOperationRepository;
import org.zxp.funk.hopper.jpa.repository.TomcatServerRepository;

@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	private ServerList serverlist;
	
	@Autowired
	private TomcatServerRepository serverrep;
	
	@Autowired
	private ServerOperationRepository operationRep;

	@Override
	public void startup(String id) throws Exception {
		
		serverlist.startup(id);
	}

	@Override
	public void shutdown(String id) throws Exception{
		serverlist.shutdown(id);
	}

	@Override
	public List<String> getlog(String id) {
		return null;
	}

	@Override
	public ServerStatus[] getStatus() {
		
		return serverlist.getAll();
	}

	@Override@Transactional
	public void addServer(TomcatServer sc) {
		Date createtime=new Date();
		sc.setOperations(sc.getOperations()+1);
		sc.setLasttime(createtime);
		sc = serverrep.save(sc);
		ServerOperation so = new ServerOperation(sc.getServerid());
		so.setOperationtype(0);
		so.setOperator("管理员");
		so.setOperationtime(createtime);
		operationRep.save(so);
		serverlist.add(sc);
		
	}
	
	@Override
	public TomcatServer findOne(String id){
		return null;
	}

	@Override
	public void addOperation(ServerOperation operation) {
		
	}

	
}
