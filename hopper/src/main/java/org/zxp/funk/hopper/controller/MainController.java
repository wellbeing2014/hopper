package org.zxp.funk.hopper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxp.funk.hopper.config.MyUserDetails;

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
	
	@RequestMapping("/getUser.json")
	@ResponseBody
	public Map<String,String> getUser()
	{
		Map<String,String> map = new HashMap<String, String>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = null;
        if (authentication != null) {
            principal = authentication.getPrincipal();
        }
        if (principal != null && principal instanceof MyUserDetails ) {
        	MyUserDetails user = (MyUserDetails)principal;
        	map.put("realname", user.getRealname());
        }
        return map;
    }
	
	
	@SubscribeMapping("/topic/serverstatus")
	public void chatInit() {
	    System.out.println("订阅");
	}
	
}

