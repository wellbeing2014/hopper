package org.zxp.funk.hopper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.jpa.model.TomcatServer;
import org.zxp.funk.hopper.service.ServerService;
import org.zxp.funk.hopper.utils.WebUtil;
@Controller
@RequestMapping({"server"})
public class ServerController {
	private static Logger logger = LoggerFactory.getLogger(ServerController.class);
	
	@Autowired
	ServerService ss;
	
	@Autowired
	private  WebUtil webutil;
	
	
	/**
	 * @Title: 服务列表页
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	@RequestMapping({""})
	public String listPage()
	{
	    return "server/index";
	}
	
	
	/**
	 * @Title: 服务编辑页
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	@RequestMapping({"editServer"})
	public String editPage()
	{
        return "server/serverEdit1";  
	}
	
	/**
	 * @Title: 根据ID获取一个服务的详细信息
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: HopperBaseReturn
	 */
	@RequestMapping({"getServer.json"})
	@ResponseBody
	public HopperBaseReturn getOneServer(@RequestParam(value="serverid") String id)
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			ret.setSuccess(true);
			ret.setRetObj(ss.findOne(id));
			ret.setMsg("获取服务成功");
			
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("获取服务错误："+e.getMessage());
		}
		return ret;
	}
	
	/**
	 * @Title: 返回所有服务的状态
	 * @Description: TODO
	 * @return
	 * @return: HopperBaseReturn
	 */
	@RequestMapping({"allserver.json"})
	@ResponseBody
	public HopperBaseReturn getAll()
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			ret.setSuccess(true);
			ret.setRetObj(ss.getStatus());
			ret.setMsg("查询服务成功");
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，服务未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("查询服务错误："+e.getMessage());
		}
		return ret;
	}
	
	/**
	 * @Title: 添加一个服务
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
	public HopperBaseReturn addServer(@RequestBody TomcatServer server){
		HopperBaseReturn ret = new HopperBaseReturn();
		boolean isNew = server.getServerid()==null||server.getServerid().isEmpty();
		try{
			TomcatServer saved = ss.addServer(server);
			ret.setSuccess(true);
			ret.setRetId(saved.getServerid());
			if (isNew)
				ret.setMsg("添加服务成功啦~");
			else
				ret.setMsg("修改服务成功啦~");
			
		}catch(Exception e){
			ret.setSuccess(false);
			
			if (isNew)
				ret.setMsg("抱歉，服务未能添加成功，请查看详细原因~");
			else
				ret.setMsg("抱歉，服务未能修改成功，请查看详细原因~");
			ret.setErrorDetail(e.getMessage());
			logger.error("添加服务错误："+e.getMessage());
		}
		return ret;
	}
	
	
	@RequestMapping(value="remove.json", method = RequestMethod.GET)
	@ResponseBody
	public HopperBaseReturn delServer(@RequestParam(value="serverid") String id){
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			TomcatServer toDel = ss.findOne(id);
			ret.setSuccess(true);
			ret.setMsg("["+toDel.getServername()+"]服务成功删除啦~");
			ss.delServer(id);
			
		}catch(Exception e){
			ret.setSuccess(false);
			
			ret.setMsg("抱歉，服务未能如愿删除，请查看详细原因~");

			ret.setErrorDetail(HopperException.getStackTrace(e));
			logger.error("删除服务错误："+HopperException.getStackTrace(e));
		}
		return ret;
	}
	
	
	/**
	 * @Title: 操作类
	 * @Description: TODO
	 * @param id
	 * @param type 1：启动 2：关闭
	 * @return
	 * @return: HopperBaseReturn
	 */
	@RequestMapping(value="operate.json", method = RequestMethod.GET)
	@ResponseBody
	public HopperBaseReturn operate(
										@RequestParam("id") String id, 
										@RequestParam(value="type") int type){
		System.out.println(webutil.getClientIp());
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
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
			ret.setMsg("命令执行成功！");
			ret.setSuccess(true);
		}catch(Exception e)
		{
			ret.setMsg("命令执行失败！");
			ret.setErrorDetail(HopperException.getStackTrace(e));
			ret.setSuccess(false);
		}
		return ret;
	}
	
	
}

