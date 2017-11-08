package org.zxp.funk.hopper.controller;

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
import org.zxp.funk.hopper.jpa.entity.JdkConfig;
import org.zxp.funk.hopper.jpa.entity.ServerConfig;
import org.zxp.funk.hopper.service.SystemService;

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
        mav.addObject("server", sysService.findOneTomcat(id));
	    return mav;
	}
	
	@RequestMapping(value="add.json", method = RequestMethod.POST)
	@ResponseBody
	public HopperBaseReturn addTomcat(HttpServletRequest req, 
										@RequestParam("name") String name, 
										@RequestParam("path") String path, 
										@RequestParam("plat") int plat, 
										@RequestParam(value="id", required=false) String id,
										@RequestParam(value="args", required=false) String args){
		
		
		
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			ServerConfig sc = new ServerConfig();
			sc.setId(id);
			sc.setName(name);
			sc.setPath(path);
			sc.setArgs(args);
			sc.setPlat(plat);
			
			sc = sysService.addTomcat(sc);;
			ret.setRetObj(sc);
			ret.setRetId(sc.getId());
			ret.setSuccess(true);
			ret.setMsg("保存成功");
		}
		catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("保存失败");
			ret.setErrorDetail(e.getMessage());
			logger.error("保存tomcat失败："+e.getMessage());
		}
		return ret;
		
	}
	
	
	
	@RequestMapping(value="delServer.json", method = RequestMethod.POST)
	@ResponseBody
	public HopperBaseReturn delTomcat(HttpServletRequest req, @RequestParam("id") String id)
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			ret.setSuccess(true);
			sysService.delTomcat(id);
			ret.setMsg("删除成功");
			
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("删除失败");
			ret.setErrorDetail(e.getMessage());
			logger.error("删除tomcat失败："+e.getMessage());
		}
		return ret;
	}
	
	@RequestMapping(value="getTomcats.json", method = RequestMethod.GET)
	@ResponseBody
	public HopperBaseReturn findAllServerConfig()
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			ret.setSuccess(true);
			ret.setRetObj(sysService.findAllTomcat());
			ret.setMsg("获取所有tomcat");
			
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("获取tomcat错误："+e.getMessage());
		}
		return ret;
		
	}
	
	@RequestMapping(value="getaTomcat.json", method = RequestMethod.GET)
	@ResponseBody
	public HopperBaseReturn findOneServerConfig(String id)
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		ret.setRetObj(sysService.findOneTomcat(id));
		ret.setMsg("获取成功");
		ret.setSuccess(true);
		return ret;
	}
	
	@RequestMapping(value="addJdk.json", method = RequestMethod.POST)
	@ResponseBody
	public HopperBaseReturn addJdk(String jdkname,String id,String classpath,String remark,String javahome){
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			JdkConfig jdk = new JdkConfig();
			jdk.setId(id);
			jdk.setClasspath(classpath);
			jdk.setJavahome(javahome);
			jdk.setJdkname(jdkname);
			jdk.setRemark(remark);
			
			jdk = sysService.addJdk(jdk);
			ret.setRetObj(jdk);
			ret.setRetId(jdk.getId());
			ret.setSuccess(true);
			ret.setMsg("保存成功");
		}
		catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("保存失败");
			ret.setErrorDetail(e.getMessage());
			logger.error("保存JDK失败："+e.getMessage());
		}
		return ret;
	}
	
	@RequestMapping(value="delJdk.json", method = RequestMethod.POST)
	@ResponseBody
	public HopperBaseReturn delJdk(@RequestParam("id") String id)
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			ret.setSuccess(true);
			sysService.delJdk(id);
			ret.setMsg("删除成功");
			
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("删除失败");
			ret.setErrorDetail(e.getMessage());
			logger.error("删除JDK失败："+e.getMessage());
		}
		return ret;
		
	}
	
	/**
	 * 获取jdk
	 * @return
	 */
	@RequestMapping(value="getJdks.json", method = RequestMethod.GET)
	@ResponseBody
	public HopperBaseReturn findAllJdks()
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			ret.setSuccess(true);
			ret.setRetObj(sysService.findAllJdks());
			ret.setMsg("获取所有jdk");
			
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("获取服务错误："+e.getMessage());
		}
		return ret;
		
	}
	
	
}

