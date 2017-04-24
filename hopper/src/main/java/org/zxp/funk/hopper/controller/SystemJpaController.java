package org.zxp.funk.hopper.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.zxp.funk.hopper.jpa.model.ServerConfig;
import org.zxp.funk.hopper.jpaservice.SystemService;

@Controller
@RequestMapping({"system1"})
public class SystemJpaController {
	private static Logger logger = LoggerFactory.getLogger(SystemJpaController.class);
	
	@Autowired
	SystemService sysService;
	
	
	@RequestMapping({""})
	public String index()
	{
	    return "system/index";
	}
	
	@RequestMapping({"addTomcat"})
	public String addPage()
	{
	    return "system/addServerConifg";
	}
	
	@RequestMapping(value="addTomcat.json", method = RequestMethod.POST)
	public String addTomcat(HttpServletRequest req, @RequestParam("name") String name, 
													@RequestParam("path") String path, 
													@RequestParam(value="args", required=false) String agrs)
	{
	    
		ServerConfig sc = new ServerConfig();
		sc.setName(name);
		sc.setPath(path);
		sysService.addTomcat(sc);
		return "system/addServerConifg";
	}
}

