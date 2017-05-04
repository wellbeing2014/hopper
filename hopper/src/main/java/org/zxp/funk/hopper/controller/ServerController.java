package org.zxp.funk.hopper.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.service.ServerService;

@Controller
@RequestMapping({"server"})
public class ServerController {
	private static Logger logger = LoggerFactory.getLogger(ServerController.class);
	
	@Autowired
	ServerService ss;
	
	
	
	@RequestMapping({""})
	public String loginPage()
	{
	    return "server/index";
	}
	
	
	@RequestMapping({"all"})
	@ResponseBody
	public List<ServerStatus> getAll()
	{
	    return ss.getStatus();
	}
	
}

