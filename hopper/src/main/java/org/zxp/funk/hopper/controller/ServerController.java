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
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerConfig;
import org.zxp.funk.hopper.jpa.model.TomcatServer;
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
	
	
	@RequestMapping({"allserver.json"})
	@ResponseBody
	public ServerStatus[] getAll()
	{
	    return ss.getStatus();
	}
	
	
	@RequestMapping({"editServer"})
	public ModelAndView editPage( @RequestParam(value="id",required=false) String id)
	{
		ModelAndView mav = new ModelAndView();  
        mav.setViewName("server/serverEdit");  
        if(id!=null&&!id.isEmpty())
        mav.addObject("server", ss.findOne(id));
	    return mav;
	}
	
	
	
	/**
	 * @Title: addServer
	 * @Description: TODO
	 * @param name
	 * @param path
	 * @param plat
	 * @param id
	 * @param args
	 * @return
	 * @return: Map<String,String>
	 */
	@RequestMapping(value="add.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> addServer(TomcatServer server){
		
		ss.addServer(server);
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("aaa", "bbb");
		return ret;
	}
	
	
	@RequestMapping(value="operate.json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> operate(
										@RequestParam("id") String id, 
										@RequestParam(value="type") int type){
		String message="成功";
		try{
			switch(type)
			{
			case 1:
				ss.startup(id);
				break;
			case 2:
				ss.shutdown(id);
				break;
			default:
				throw new Exception("未知命令");
			}
		}catch(Exception e)
		{
			message=e.getMessage();
		}
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("message", message);
		return ret;
	}
}

