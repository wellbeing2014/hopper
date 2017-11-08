package org.zxp.funk.hopper.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.zxp.funk.hopper.jpa.entity.ServerOperation;
import org.zxp.funk.hopper.jpa.entity.ServerOperationExt;
import org.zxp.funk.hopper.jpa.entity.TomcatServer;
import org.zxp.funk.hopper.pojo.OperationLog;
import org.zxp.funk.hopper.pojo.ServerStatus;

public interface ServerService {
	public void startup(String id,String operator) throws Exception;
	public void shutdown(String id,String operator) throws Exception;
	public void shutdownForce(String id,String operator) throws Exception;
	public List<String> getlog(String id);
	public ServerStatus[] getStatus();
	public void addOperation(ServerOperation operation);
	public TomcatServer findOne(String id);
	TomcatServer addServer(TomcatServer sc,String operator) throws Exception;
	void delServer(String id) throws Exception;
	
	public List<OperationLog> getOperations();
	public long getOperationCount();
	public List<ServerOperationExt> getOperationsByPage(int pageCout,int pageno);
	public Page<OperationLog> getOperationLogsByPage1(int pageCout,int pageno);
	public List<OperationLog> getOperationLogsByPage2(int pageCout,int pageno);
	
}
