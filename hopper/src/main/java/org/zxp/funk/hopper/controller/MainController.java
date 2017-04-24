package org.zxp.funk.hopper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxp.funk.hopper.service.UserService;

@Controller
@RequestMapping({""})
public class MainController {
	
	@Value("${version}")
	private String version;
	
	@Autowired
    UserService userService;
	
	@RequestMapping({"version.json"})
	@ResponseBody
	public Map<String, String> version()
	{
		Map<String, String> versionMap = new HashMap<String, String>();
		versionMap.put("version", this.version);
		return versionMap;
	}
	
	
	
	
	
}

