package org.zxp.funk.hopper.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zxp.funk.hopper.core.ServerBehavior;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerInstance;
import org.zxp.funk.hopper.jpa.repository.ServerInstanceRepository;

@Component
@Scope("singleton")
public class ServerList {
	private static Logger logger=LoggerFactory.getLogger("核心列表");
	private List<ServerBehavior> list = new ArrayList<ServerBehavior>();
	@Autowired
	ServerInstanceRepository rep;
	
	@PostConstruct
	public void init(){
		logger.debug("正在装载服务列表，共 "+rep.count()+" 个");
		List<ServerInstance> list1 = rep.findAll();
		
		for(ServerInstance ins:list1){
			list.add(new ServerBehavior(ins));
		}
		
	}
	
	public List<ServerStatus> wapper(){
		
		List<ServerStatus> ret = new ArrayList<ServerStatus>();
		for(ServerBehavior sb: list){
			
			ServerStatus ss = new ServerStatus();
			ss.setId(sb.getServerInstance().getId());
			ss.setName(sb.getServerInstance().getName());
			ss.setStatus(sb.getTomcatStatus().toString());
			ret.add(ss);
		}
		return ret;
	}
}
