package org.zxp.funk.hopper.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zxp.funk.hopper.core.IhopperExecutor;
import org.zxp.funk.hopper.core.ServerBehavior;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerOperation;
import org.zxp.funk.hopper.jpa.model.TomcatServer;
import org.zxp.funk.hopper.jpa.repository.ServerOperationRepository;
import org.zxp.funk.hopper.jpa.repository.TomcatServerRepository;

@Component
@Scope("singleton")
public class ServerList {
	private static Logger logger=LoggerFactory.getLogger("核心服务列表");
	private   ConcurrentLinkedQueue<ServerBehavior> list = new ConcurrentLinkedQueue<ServerBehavior>();
	private   ConcurrentLinkedQueue<ServerStatus> cache = new ConcurrentLinkedQueue<ServerStatus>();
	ReadWriteLock rwlock = new ReentrantReadWriteLock(); 
	
	@Autowired
	TomcatServerRepository serverRep;
	
	@Autowired
	ServerOperationRepository operationRep;
	
	@PostConstruct
	public void init(){
		logger.info("正在装填服务："+serverRep.count()+"条");
		List<TomcatServer> serverlist = serverRep.findAll();
		
		for(TomcatServer server:serverlist){
			ServerBehavior sb = new ServerBehavior(server);
			sb.operations= operationRep.findByServerid(server.getServerid());
			list.add(sb);
		}
		flush();
	}
	
	public void flush(){
		
		//if(rwlock.writeLock().tryLock()){
		//	try{
		cache.clear();
		for(ServerBehavior sb:list){
			cache.add(sb.status);
		}
			//}
			//finally{rwlock.writeLock().unlock();}
		//}
	}
	
	public ServerStatus[] getAll(){
			return cache.toArray(new ServerStatus[0]);
	}
	
	public boolean add(TomcatServer server){
		ServerBehavior sb  =new ServerBehavior(server);
		list.add(sb);
		flush();
		return true;
	}
	
	public void startup(String id) throws Exception{
		
		ServerBehavior serverb= null;
		for(ServerBehavior sb:list){
			if(sb.server.getServerid().equals(id)){
				serverb = sb;
				break;
			}
		}
		if(serverb==null) throw new Exception("未找到相应服务："+id);
		
		Date oprtime = new Date();
		serverb.server.setOperations(serverb.server.getOperations()+1);
		serverb.server.setLasttime(oprtime);
		ServerOperation so = new ServerOperation(serverb.server.getServerid());
		so.setOperationtime(oprtime);
		so.setOperationtype(1);
		so.setOperator("testor");
		serverb.startup();
		operationRep.save(so);
		serverRep.save(serverb.server);
		serverb.operations.add(0, so);
		
	}
	
	public void shutdown(String id) throws Exception{
		
		ServerBehavior serverb= null;
		for(ServerBehavior sb:list){
			if(sb.server.getServerid().equals(id)){
				serverb = sb;
				break;
			}
		}
		if(serverb==null) throw new Exception("未找到相应服务："+id);
		serverb.shutdown();
		
	}
}
