package org.zxp.funk.hopper.jpacontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"server"})
public class ServerController {
	private static Logger logger = LoggerFactory.getLogger(ServerController.class);
	
	
	
	
	@RequestMapping({""})
	public String loginPage()
	{
	    return "server/index";
	}

}

