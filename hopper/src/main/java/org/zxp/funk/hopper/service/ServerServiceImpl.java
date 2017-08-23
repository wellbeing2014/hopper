package org.zxp.funk.hopper.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.OperationType;
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
	private  SystemService system;
	
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
	public TomcatServer addServer(TomcatServer sc) throws Exception {
		boolean isNew = sc.getServerid()==null||sc.getServerid().isEmpty();
		Date createtime=new Date();
		sc.setOperations(sc.getOperations()+1);
		sc.setLasttime(createtime);
		sc.setJdk(system.findOneJdk(sc.getJdk().getId()));
		sc.setTomcat(system.findOneTomcat(sc.getTomcat().getId()));
		sc = serverrep.save(sc);
		ServerOperation so = new ServerOperation(sc.getServerid());
		if (isNew)
			so.setOperationtype(OperationType.新建);
		else
			so.setOperationtype(OperationType.修改);
		so.setOperator("管理员");
		so.setOperationtime(createtime);
		operationRep.save(so);
		serverlist.add(sc);
		return sc;
	}
	
	@Override
	public TomcatServer findOne(String id){
		return serverrep.findOne(id);
	}

	@Override
	public void addOperation(ServerOperation operation) {
		
	}

	@Override@Transactional
	public void delServer(String id) throws Exception {
		operationRep.deleteByServerid(id);
		serverlist.remove(serverrep.findOne(id));
		serverrep.delete(id);
	}
}
