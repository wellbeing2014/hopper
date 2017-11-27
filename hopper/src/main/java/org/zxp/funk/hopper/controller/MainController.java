package org.zxp.funk.hopper.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({""})
public class MainController {
	
	@Value("${version}")
	private String version;
	
	
	
	@Value("${sys.login.username}")
	private String username;
	@Value("${sys.login.password}")
	private String password;
	
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
	
	
	
    /** 
     * 退出系统 
     * @param session 
     *          Session 
     * @return 
     * @throws Exception 
     */  
    @RequestMapping(value="/logout")  
    public String logout(HttpSession session) throws Exception{  
        //清除Session  
        session.invalidate();  
          
        return "redirect:hopper_login.html";  
    } 
    
    @RequestMapping(value="/login")
    @ResponseBody
    public Map<String,Object> login(HttpSession session, String username,String password) {
    	Map<String,Object> map = new HashMap<>();
    	if(this.username.equals(username)&&this.password.equals(password)) {
    		session.setAttribute("username", username);
    		map.put("success", "true");
    		map.put("location", "hopper.html");
    	}
    	else
    	{
    		map.put("success", false);
    		map.put("msg", "用户名密码错误");
    	}
		return map;
	}
}

