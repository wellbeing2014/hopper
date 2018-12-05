package org.zxp.funk.hopper.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.jpa.entity.TomcatServer;
import org.zxp.funk.hopper.pojo.OperationLog;
import org.zxp.funk.hopper.service.ServerService;
import org.zxp.funk.hopper.utils.FilesUtil;
import org.zxp.funk.hopper.utils.WebUtil;

@Controller
@RequestMapping({"server"})
public class ServerController {
	private static Logger logger = LoggerFactory.getLogger(ServerController.class);
	
	@Autowired
	ServerService ss;
	
	@Autowired
	private  WebUtil webutil;
	
	@Value("${server.war.dir}")
	String serverWarDir;
	/**
	 * @Title: 服务列表页
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
	 * @Description: 
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
	 * @Description: 添加服务
	 * @param name
	 * @param path
	 * @param plat
	 * @param id
	 * @param args
	 * @return
	 * @return: HopperBaseReturn
	 */
	@RequestMapping(value="add.json", method = RequestMethod.POST)
	@ResponseBody
	public HopperBaseReturn addServer(@RequestPart("server")TomcatServer server ,@RequestPart("file")MultipartFile[] files){
		HopperBaseReturn ret = new HopperBaseReturn();
		boolean isNew = server.getServerid()==null||server.getServerid().isEmpty();
		try{
			String ip = webutil.getClientIp() ;
			for (MultipartFile file : files)
	        {
				String localPath = serverWarDir + File.separatorChar + file.getOriginalFilename();  
             	File newFile = FilesUtil.fileAutoRename(new File(localPath), 1);
             	//上传的文件写入到指定的文件中  
             	file.transferTo(newFile); 
	        }
			
			TomcatServer saved = ss.addServer(server,ip);
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
			ret.setErrorDetail(HopperException.getStackTrace(e));
			logger.error("添加修改服务错误：",e);
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
			
			ret.setMsg("抱歉，服务未能删除:"+e.getMessage());

			ret.setErrorDetail(HopperException.getStackTrace(e));
			logger.error("删除服务错误",e);
		}
		return ret;
	}
	
	
	/**
	 * @Title: 操作类
	 * @Description: 对服务进行起停操作
	 * @param id
	 * @param type 1：启动 2：关闭 8:推送缓存日志 9：强制关闭
	 * @return
	 * @return: HopperBaseReturn
	 */
	@RequestMapping(value="operate.json", method = RequestMethod.GET)
	@ResponseBody
	public HopperBaseReturn operate(	@RequestParam("id") String id, 
										@RequestParam(value="type") int type){
		String ip = webutil.getClientIp() ;
		HopperBaseReturn ret = new HopperBaseReturn();
		ret.setRetId(id);
		try{
			switch(type)
			{
			case 1:
				ss.startup(id,ip);
				break;
			case 2:
				ss.shutdown(id,ip);
				break;
			case 8:
				ss.pushCacheLogs(id);
				break;
			case 9:
				ss.shutdownForce(id, ip);
				break;
			default:
				throw new Exception("未知命令");
			}
			ret.setMsg("命令执行成功！");
			ret.setSuccess(true);
		}catch(Exception e)
		{
			ret.setMsg("命令执行失败:"+e.getMessage());
			ret.setErrorDetail(HopperException.getStackTrace(e));
			ret.setSuccess(false);
			logger.error("命令执行失败:",e);
		}
		return ret;
	}
	
	@RequestMapping({"allopers.json"})
	@ResponseBody
	public HopperBaseReturn getAllOperations1(@RequestParam(value="pageno") int pageno,@RequestParam(value="pagecount") int pagecount)
	{
		HopperPageReturn ret = new HopperPageReturn();
		try{
			Page<OperationLog> page = ss.getOperationLogsByPage1(pagecount,pageno);
			ret.setSuccess(true);
			ret.setRetObj(page.getContent());
			ret.setCount(page.getTotalElements());
			ret.setCountPer(page.getSize());
			ret.setPages(page.getTotalPages());
			ret.setMsg("所有操作返回成功");
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，服务未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("查询历史操作错误!",e);
		}
		return ret;
	}
	
	@RequestMapping({"allopers2.json"})
	@ResponseBody
	public HopperBaseReturn getAllOperations2(@RequestParam(value="pageno") int pageno,@RequestParam(value="pagecount") int pagecount)
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			ret.setSuccess(true);
			ret.setRetObj(ss.getOperationLogsByPage2(pagecount,pageno));
			ret.setMsg("所有操作返回成功");
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，服务未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("查询历史操作错误!",e);
		}
		return ret;
	}
	
	
	@RequestMapping({"allopers3.json"})
	@ResponseBody
	public HopperBaseReturn getAllOperations3()
	{
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			ret.setSuccess(true);
			ret.setRetObj(ss.getOperations());
			ret.setMsg("所有操作返回成功");
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("抱歉，服务未能返回数据。");
			ret.setErrorDetail(e.getMessage());
			logger.error("查询历史操作错误!",e);
		}
		return ret;
	}
	
	@RequestMapping({"verifiPort.json"})
	@ResponseBody
	public HopperBaseReturn verifiPort(@RequestParam(value="port")int port,@RequestParam(value="serverid")String serverid) {
		
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			ret.setSuccess(true);
			ret.setRetObj(ss.verifyPort(port,serverid));
			ret.setMsg("验证端口返回成功");
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("验证端口错误!");
			ret.setErrorDetail(e.getMessage());
			logger.error("验证端口错误!",e);
		}
		return ret;
	}
	
	@ResponseBody
	@RequestMapping({"upload"})
	public HopperBaseReturn upload(HttpServletRequest req, HttpServletResponse resp,
									@RequestParam(value="url", required=false) String url, 
									@RequestParam(value="name", required=false) String name) throws IOException{
		
		HopperBaseReturn ret = new HopperBaseReturn();
		try{
			
			if ((req instanceof MultipartHttpServletRequest) && 
					!CollectionUtils.isEmpty(((MultipartHttpServletRequest)req).getFileMap())) {
				Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest)req).getFileMap();
				for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet())
		        {
					MultipartFile file = (MultipartFile)entry.getValue();
		          
					//byte[] data = file.getBytes();
					if (StringUtils.isBlank(name)) {
						name = file.getOriginalFilename();
	                
					}
		          
					String localPath = serverWarDir + File.separatorChar + name;  
					
	             	File newFile = new File(localPath);  
	             	if(newFile.exists()) {
	             		throw new HopperException("01002", "war包已经存在！");
	             	}
	             	//上传的文件写入到指定的文件中  
	             	file.transferTo(newFile); 
	             	ret.setRetObj(newFile.getAbsolutePath());
		        }
			}
			ret.setSuccess(true);
			ret.setMsg("上传成功!");
		}catch(Exception e){
			ret.setSuccess(false);
			ret.setMsg("上传失败:"+e.getMessage());
			ret.setErrorDetail(e.getMessage());
			logger.error("上传失败!",e);
		}
		return ret;
		
	}
}

