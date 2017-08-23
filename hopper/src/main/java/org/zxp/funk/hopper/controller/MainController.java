package org.zxp.funk.hopper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxp.funk.hopper.entity.ServerStatus;

@Controller
@RequestMapping({""})
public class MainController {
	
	@Value("${version}")
	private String version;
	
	
	@RequestMapping({"version.json"})
	@ResponseBody
	public Map<String, String> version()
	{
		Map<String, String> versionMap = new HashMap<String, String>();
		versionMap.put("version", this.version);
		return versionMap;
	}
	
	
	@RequestMapping("/")
	public String index()
	{
		return "index";
	}
	
	@SubscribeMapping("/topic/serverstatus")
	public void chatInit() {
	    System.out.println("订阅");
	}
	
}

