package org.zxp.funk.hopper.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.jpa.dao.CustomDao;
import org.zxp.funk.hopper.jpa.entity.OperationType;
import org.zxp.funk.hopper.jpa.entity.ServerOperation;
import org.zxp.funk.hopper.jpa.entity.TomcatServer;
import org.zxp.funk.hopper.jpa.repository.ServerOperationRepository;
import org.zxp.funk.hopper.jpa.repository.TomcatServerRepository;
import org.zxp.funk.hopper.jpa.entity.ServerOperationExt;
import org.zxp.funk.hopper.pojo.OperationLog;
import org.zxp.funk.hopper.pojo.ServerStatus;

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
	
	@Autowired
	private CustomDao operdao;

	@Override
	public void startup(String id,String operator) throws Exception {
		
		serverlist.startup(id,  operator);
	}

	@Override
	public void shutdown(String id,String operator) throws Exception{
		serverlist.shutdown(id, operator);
	}
	
	@Override
	public void shutdownForce(String id,String operator) throws Exception{
		serverlist.shutdownForce(id, operator);
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
	public TomcatServer addServer(TomcatServer sc,String operator) throws Exception {
		boolean isNew = sc.getServerid()==null||sc.getServerid().isEmpty();
		Date createtime=new Date();
		sc.setOperations(sc.getOperations()+1);
		sc.setLasttime(createtime);
		sc.setJdk(system.findOneJdk(sc.getJdk().getId()));
		sc.setTomcat(system.findOneTomcat(sc.getTomcat().getId()));
		if(isNew)
			serverlist.add(sc);
		else 
			serverlist.update(sc);
		ServerOperation so = new ServerOperation(sc.getServerid());
		if (isNew)
			so.setOperationtype(OperationType.新建);
		else
			so.setOperationtype(OperationType.修改);
		so.setOperator(operator);
		so.setOperationtime(createtime);
		operationRep.save(so);
		
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
	
	
	
	@Override
	public Page<OperationLog> getOperationLogsByPage1(int pageCout, int pageno) {
		Pageable pageable = new PageRequest(pageno,pageCout, Sort.Direction.DESC,"operationtime");
		Page<OperationLog> page =  operationRep.findOperationLogsByPage(pageable);
		return page;
		
	}

	@Override
	public List<ServerOperationExt> getOperationsByPage(int pageCout, int pageno) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<OperationLog> getOperationLogsByPage2(int pageCout, int pageno) {
		return operdao.getOperationsByPage(pageCout, pageno);
	}

	@Override
	public List<OperationLog> getOperations() {
		return operationRep.findOperation2All();
	}
	
	@Override
	public long getOperationCount(){
		return operationRep.count();
	}
	
}
