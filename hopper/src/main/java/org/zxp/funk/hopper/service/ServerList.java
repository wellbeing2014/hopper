package org.zxp.funk.hopper.service;

import java.io.File;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.zxp.funk.hopper.core.HopperLogEventListener;
import org.zxp.funk.hopper.core.HopperLogEventObject;
import org.zxp.funk.hopper.core.HopperStatusEventListener;
import org.zxp.funk.hopper.core.HopperStatusEventObject;
import org.zxp.funk.hopper.core.IhopperExecutor;
import org.zxp.funk.hopper.core.tomcat.TomcatBehavior;
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
public class ServerList {
	private static Logger logger=LoggerFactory.getLogger("核心服务列表");
	private   ConcurrentLinkedQueue<TomcatBehavior> list = new ConcurrentLinkedQueue<TomcatBehavior>();
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
		logger.info("正在检测服务配置目录是否可用："+serverConfigDir);
		File configdir = new File(serverConfigDir);
		configdir.mkdirs();
		logger.info("正在装填服务："+serverRep.count()+"条");
		List<TomcatServer> serverlist = serverRep.findAll();
		
		for(TomcatServer server:serverlist){
			
			if(server.getTomcat()==null||Strings.isNullOrEmpty(server.getTomcat().getId())) {
				logger.error("无法获取TOMCAT_HOME,serverid:"+server.getServerid());
				throw new Exception("无法获取TOMCAT_HOME,serverid:"+server.getServerid());
			}
			if(Strings.isNullOrEmpty(server.getTomcat().getPath()))
				server.setTomcat(scRep.findOne(server.getTomcat().getId()));
			TomcatBehavior sb = new TomcatBehavior(server,serverConfigDir);
			sb.addLogEventListener(new HopperLogEventListener() {
				@Override
				public void logEvent(HopperLogEventObject obj) {
					brokerMessagingTemplate.convertAndSend("/topic/serverlog/"+server.getServerid(), obj.log);
				}
			});
			
			sb.addStatusEventListener(new HopperStatusEventListener() {
				
				@Override
				public void statusChanged(HopperStatusEventObject obj) {
					brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
				}
			});
			
			list.add(sb);
		}
		flush();
		
	}
	
	public void flush(){
		
		//if(rwlock.writeLock().tryLock()){
		//	try{
		cache.clear();
		for(TomcatBehavior sb:list){
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
		for(TomcatBehavior sb_d:list){
			if(sb_d.server.getServerid().equals(server.getServerid())){
				sb_d.tomcatBaseDelete();
				list.remove(sb_d);
			}
		}
		server.setServerid(serverRep.save(server).getServerid());
		TomcatBehavior sb  =new TomcatBehavior(server,serverConfigDir);
		sb.addLogEventListener(new HopperLogEventListener() {
			@Override
			public void logEvent(HopperLogEventObject obj) {
				brokerMessagingTemplate.convertAndSend("/topic/serverlog/"+server.getServerid(), obj.log);
			}
		});
		sb.addStatusEventListener(new HopperStatusEventListener() {
			
			@Override
			public void statusChanged(HopperStatusEventObject obj) {
				brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
				
			}
		});
		
		list.add(sb);
		flush();
		brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
		
		return true;
	}
	
	
	/**
	 * @param server
	 * @return
	 * @throws Exception
	 */
	public boolean update(TomcatServer server) throws Exception{
		
		TomcatBehavior update_o = null;
		for(TomcatBehavior sb_d:list){
			if(sb_d.server.getServerid().equals(server.getServerid())){
				update_o = sb_d;
			}
		}
		if(update_o.server.isCoreEdit(server))
		{
			
			while(update_o.isRunning()) {
				logger.info(update_o.server.getServername()+"-等待停止");
				update_o.shutdown();
				Thread.sleep(1500);
				
			}
			update_o.tomcatBaseDelete();
			update_o.server=server;
			update_o.tomcatBaseInit();
		}
		update_o.resetServerStatus(server);
		serverRep.save(server);
		flush();
		brokerMessagingTemplate.convertAndSend("/topic/serverstatus", getAll());
		return true;
	}
	
	public void shutdownForce(String id,String operator) throws Exception{
		
		TomcatBehavior serverb= null;
		for(TomcatBehavior sb:list){
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
		serverb.shutdownForce();
		operationRep.save(so);
		serverRep.save(serverb.server);
		serverb.addOnceOpr(oprtime);
		flush();
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
		for(TomcatBehavior sb_d:list){
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
		
		TomcatBehavior serverb= null;
		for(TomcatBehavior sb:list){
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
	
	public IhopperExecutor getExecutor(String serverid) throws Exception {
		
		TomcatBehavior serverb= null;
		for(TomcatBehavior sb:list){
			if(sb.server.getServerid().equals(serverid)){
				serverb = sb;
				break;
			}
		}
		if(serverb==null) throw new Exception("未找到相应服务："+serverid);
		
		return serverb;
	}
	
	public void shutdown(String id,String operator) throws Exception{
		
		TomcatBehavior serverb= null;
		for(TomcatBehavior sb:list){
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
	
	
}
