package org.zxp.funk.hopper.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.zxp.funk.hopper.jpa.model.ServerConfig;
import org.zxp.funk.hopper.service.SystemService;
import org.zxp.funk.hopper.utils.Platform;

@Controller
@RequestMapping({"system"})
public class SystemController {
	private static Logger logger = LoggerFactory.getLogger(SystemController.class);
	
	@Autowired
	SystemService sysService;
	
	
	@RequestMapping({""})
	public String index()
	{
	    return "system/index";
	}
	
	@RequestMapping({"addServer"})
	public String addPage()
	{
	    return "system/serverConifg";
	}
	
	@RequestMapping({"editServer"})
	public ModelAndView editPage( @RequestParam("sid") String id)
	{
		ModelAndView mav = new ModelAndView();  
        mav.setViewName("system/serverConifg");   
        mav.addObject("server", sysService.findOne(id));
	    return mav;
	}
	
	@RequestMapping(value="add.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> addTomcat(HttpServletRequest req, 
										@RequestParam("servername") String name, 
										@RequestParam("localpath") String path, 
										@RequestParam("platform") int plat, 
										@RequestParam(value="sid", required=false) String id,
										@RequestParam(value="args", required=false) String args){
		ServerConfig sc = new ServerConfig();
		sc.setId(id);
		sc.setName(name);
		sc.setPath(path);
		sc.setArgs(args);
		sc.setPlat(plat);
		sysService.addTomcat(sc);
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("aaa", "bbb");
		return ret;
	}
	
	@RequestMapping(value="test.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> delTomcat1(HttpServletRequest req, @RequestParam("fileName") String id)
	{
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("aaa", "bbb");
		return ret;
	}
	
	@RequestMapping(value="delServer.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> delTomcat(HttpServletRequest req, @RequestParam("id") String id)
	{
		sysService.delTomcat(id);
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("aaa", "bbb");
		return ret;
	}
	
	@RequestMapping(value="getTomcat.json", method = RequestMethod.GET)
	@ResponseBody
	public List<ServerConfig> findAllServerConfig()
	{
		return sysService.findAllTomcat();
	}
	
	
}

