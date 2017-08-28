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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.zxp.funk.hopper.core.ServerBehavior;
import org.zxp.funk.hopper.core.TomcatLogEventListener;
import org.zxp.funk.hopper.core.TomcatLogEventObject;
import org.zxp.funk.hopper.core.TomcatStatusEventListener;
import org.zxp.funk.hopper.core.TomcatStatusEventObject;
import org.zxp.funk.hopper.jpa.entity.OperationType;
import org.zxp.funk.hopper.jpa.entity.ServerOperation;
import org.zxp.funk.hopper.jpa.entity.TomcatServer;
import org.zxp.funk.hopper.jpa.repository.JdkConfigRepository;
import org.zxp.funk.hopper.jpa.repository.ServerConfigRepository;
import org.zxp.funk.hopper.jpa.repository.ServerOperationRepository;
import org.zxp.funk.hopper.jpa.repository.TomcatServerRepository;
import org.zxp.funk.hopper.pojo.ServerStatus;

import com.google.common.base.Strings;

@Component
@Scope("singleton")
public class ServerList {
	private static Logger logger=LoggerFactory.getLogger("核心服务列表");
	private   ConcurrentLinkedQueue<ServerBehavior> list = new ConcurrentLinkedQueue<ServerBehavior>();
	private   ConcurrentLinkedQueue<ServerStatus> cache = new ConcurrentLinkedQueue<ServerStatus>();
	ReadWriteLock rwlock = new ReentrantReadWriteLock(); 
	
	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;
	
	@Autowired
	TomcatServerRepository serverRep;
	
	@Autowired
	ServerOperationRepository operationRep;
	
	@Autowired
	ServerConfigRepository scRep;
	
	@Autowired
	JdkConfigRepository jdkRep;
	
	@Value("${server.config.dir}")
	String serverConfigDir;
	
	@PostConstruct
	public void init() throws Exception{
		logger.info("正在装填服务："+serverRep.count()+"条");
		List<TomcatServer> serverlist = serverRep.findAll();
		
		for(TomcatServer server:serverlist){
			
			if(server.getTomcat()==null||Strings.isNullOrEmpty(server.getTomcat().getId())) {
				logger.error("无法获取TOMCAT_HOME,serverid:"+server.getServerid());
				throw new Exception("无法获取TOMCAT_HOME,serverid:"+server.getServerid());
			}
			if(Strings.isNullOrEmpty(server.getTomcat().getPath()))
				server.setTomcat(scRep.findOne(server.getTomcat().getId()));
			ServerBehavior sb = new ServerBehavior(server,serverConfigDir);
			sb.addTomcatLogEventListener(new TomcatLogEventListener() {
				@Override
				public void logEvent(TomcatLogEventObject obj) {
					brokerMessagingTemplate.convertAndSend("/topic/serverlog/"+server.getServerid(), obj.log);
				}
			});
			
			sb.addTomcatStatusEventListener(new TomcatStatusEventListener() {
				
				@Override
				public void statusChanged(TomcatStatusEventObject obj) {
					brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
				}
			});
			
			list.add(sb);
		}
		flush();
		logger.info("正在检测服务配置目录是否可用："+serverConfigDir);
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
	
	/**
	 * @Title: add
	 * @Description: 添加一个服务
	 * @param server
	 * @return
	 * @throws Exception
	 * @return: boolean
	 */
	public boolean add(TomcatServer server) throws Exception{
		if(server.getTomcat()==null||Strings.isNullOrEmpty(server.getTomcat().getId())) {
			logger.error("无法获取TOMCAT_HOME,serverid:"+server.getServerid());
			throw new Exception("无法获取TOMCAT_HOME,serverid:"+server.getServerid());
		}
		if(Strings.isNullOrEmpty(server.getTomcat().getPath()))
			server.setTomcat(scRep.findOne(server.getTomcat().getId()));
		for(ServerBehavior sb_d:list){
			if(sb_d.server.getServerid().equals(server.getServerid())){
				sb_d.tomcatBaseDelete();
				list.remove(sb_d);
			}
		}
		ServerBehavior sb  =new ServerBehavior(server,serverConfigDir);
	
		sb.addOnceOpr(server.getLasttime());
		
		sb.addTomcatLogEventListener(new TomcatLogEventListener() {
			@Override
			public void logEvent(TomcatLogEventObject obj) {
				brokerMessagingTemplate.convertAndSend("/topic/serverlog/"+server.getServerid(), obj.log);
			}
		});
		sb.addTomcatStatusEventListener(new TomcatStatusEventListener() {
			
			@Override
			public void statusChanged(TomcatStatusEventObject obj) {
				brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
				
			}
		});
		list.add(sb);
		flush();
		brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
		
		serverRep.save(sb.server);
		return true;
	}
	
	/**
	 * @Title: remove
	 * @Description: 删除一个服务
	 * @param server
	 * @return
	 * @throws Exception
	 * @return: boolean
	 */
	public boolean remove(TomcatServer server) throws Exception{
		for(ServerBehavior sb_d:list){
			if(sb_d.server.getServerid().equals(server.getServerid())){
				sb_d.tomcatBaseDelete();
				list.remove(sb_d);
			}
		}
		flush();
		brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
		return true;
	}
	public void startup(String id,String operator) throws Exception{
		
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
		so.setOperationtype(OperationType.启动);
		so.setOperator(operator);
		operationRep.save(so);
		serverRep.save(serverb.server);
		serverb.addOnceOpr(oprtime);
		flush();
		serverb.startup();
	}
	
	public void shutdown(String id,String operator) throws Exception{
		
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
		so.setOperationtype(OperationType.停止);
		so.setOperator(operator);
		operationRep.save(so);
		serverRep.save(serverb.server);
		serverb.addOnceOpr(oprtime);
		flush();
		serverb.shutdown();
		
	}
	
	public void loghandle(String id,TomcatLogEventListener listener) throws Exception{
		
		ServerBehavior serverb= null;
		for(ServerBehavior sb:list){
			if(sb.server.getServerid().equals(id)){
				serverb = sb;
				break;
			}
		}
		if(serverb==null) throw new Exception("未找到相应服务："+id);
		serverb.addTomcatLogEventListener(listener);
		
	}
}
