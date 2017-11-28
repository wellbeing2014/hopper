package org.zxp.funk.hopper.core.tomcat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.core.HopperStatus;
import org.zxp.funk.hopper.core.HopperStatusEventListener;
import org.zxp.funk.hopper.core.HopperStatusEventObject;
import org.zxp.funk.hopper.jpa.entity.TomcatPath;
import org.zxp.funk.hopper.jpa.entity.TomcatServer;
import org.zxp.funk.hopper.pojo.ServerStatus;
import org.zxp.funk.hopper.utils.IOUtil;
import org.zxp.funk.hopper.utils.XmlUtil;

import com.google.common.io.Files;

/**
 * tomcat的运行时类
 * 封装一些tomcatexecutor产生的数据
 * @author 朱新培 zxp@wisoft.com.cn
 *
 */
public class TomcatBehavior extends TomcatExecutor {
	
	/**
	 * tomcat 基本信息
	 */
	public TomcatServer server;
	
	/**
	 * 服务运行时状态信息
	 */
	public ServerStatus status = new ServerStatus();
	private StringBuffer currentStatus= new StringBuffer(HopperStatus.STOPPED.toString());
	
	private String confBaseDir;
	private static String TOMCAT_BASE_CONF_DIR = "conf";
	
	public TomcatBehavior(TomcatServer instance,String confBaseDir) throws HopperException{
		super();
		int pid = 0;
		try {
			pid = findPids(instance.getMainport());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(pid>0) {
			try {
				killprogress(pid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resetServerStatus(instance);
		this.confBaseDir = confBaseDir;
		
		addStatusEventListener(new HopperStatusEventListener() {
			
			@Override
			public void statusChanged(HopperStatusEventObject obj) {
				currentStatus.setLength(0);
				currentStatus.append(obj.status.toString());
			}
		});
		
		tomcatBaseInit();
	}
	
	public void resetServerStatus(TomcatServer instance) {
		this.server=instance;
		status.setServerid(server.getServerid());
		status.setServername(server.getServername());
		status.setStatus(currentStatus);
		status.setMainport(server.getMainport());
		status.setLoalpaths(server.getLoalPaths());
		status.setOpr(server.getOperations());
		status.setMark(server.getDesc());
		status.setLasttime(server.getLasttime());
		setJavaOpts(server.getOpts());
		setJavaHome(server.getJdk().getJavahome());
		
	}
	
	public void addOnceOpr(Date datetime){
		status.setOpr(status.getOpr()+1);
		status.setLasttime(datetime);
	}
	
	
	
	/**
	 * @Title: tomcatBaseDir
	 * @Description: TOMCATBASE配置的主要初始工作
	 * @param configDir
	 * @throws HopperException
	 * @return: void
	 */
	public void tomcatBaseInit() throws HopperException{
		String confDir = this.confBaseDir+File.separator+this.server.getConfigDirName();
		File tomcatbase = new File(server.getTomcat().getPath()+File.separator+TOMCAT_BASE_CONF_DIR);
		if(!tomcatbase.exists()) throw new HopperException("01001","TOMCAT_BASE 配置不存在！");
		File alreadyConfDir = new File(confDir);
		if(alreadyConfDir.exists()) tomcatBaseDelete();
		try {
			copyBaseConf(tomcatbase,confDir);
			File tmpfile = new File(confDir+File.separator+"temp");
			if(!tmpfile.exists())
				tmpfile.mkdirs();
		} catch (IOException e) {
			throw new HopperException("01002","TOMCAT_BASE 配置复制错误："+e.getMessage());
		}
		
        try {
			Document doc = XmlUtil.parseByPath(confDir+File.separator+TOMCAT_BASE_CONF_DIR+File.separator+"server.xml");
			Element serverNode = (Element)XmlUtil.getNode(doc.getDocumentElement(), "/Server");
			serverNode.setAttribute("port", Integer.toString(server.getShutport()));
			if(true){
				Element sericeNode = (Element)XmlUtil.getNode(serverNode, "Service[@name='Catalina']");
				sericeNode.removeChild(XmlUtil.getNode(sericeNode, "Connector[@protocol='AJP/1.3']"));
			}
			Element mainConnector = (Element)XmlUtil.getNode(serverNode, "/Server/Service[@name='Catalina']/Connector[@protocol='HTTP/1.1']");
			mainConnector.setAttribute("port", Integer.toString(server.getMainport()));
			
			Element host = (Element)XmlUtil.getNode(serverNode, "Service[@name='Catalina']/Engine/Host[@appBase='webapps']");
			
			for(TomcatPath path:server.getTomcatpaths()){
				Element context =XmlUtil.addElement(host,"Context");
				context.setAttribute("path", path.getContextpath());
				context.setAttribute("docBase", path.getDocbase());
				//context.setAttribute("debug", "0");
				//context.setAttribute("reloadable", "false");
			}
			
			XmlUtil.saveDoc(doc, confDir+File.separator+TOMCAT_BASE_CONF_DIR+File.separator+"server.xml");
		} catch (Exception e) {
			e.printStackTrace();
			throw new HopperException("01003","TOMCAT_BASE 解析配置错误："+e.getMessage());
		}
        
        setTomcatHome(server.getTomcat().getPath());
		setTomcatBase(confDir);
	}
	
	
	/**
	 * @Title: tomcatBaseDelete
	 * @Description: 删除配置，目前是新增修改 启动平台 都去删除配置，然后再重新添加
	 * @throws HopperException
	 * @return: void
	 */
	public void tomcatBaseDelete() throws HopperException{
		String confDir = this.confBaseDir+File.separator+this.server.getConfigDirName();
		File tomcatbase = new File(confDir);
		try {
			IOUtil.deleteDirectory(tomcatbase,true);
		} catch (Exception e1) {
			throw new HopperException("01004","TOMCAT_BASE 删除错误："+e1.getMessage());
		}

	}
	
	/**
	 * 复制tomcat配置
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private  void copyBaseConf(File source,String dest) throws IOException{
		File desfile = new File(dest);
		if(!desfile.exists()){  
			desfile.mkdirs();  
        } 
		if(!source.isDirectory())
			Files.copy(source, new File(dest+File.separator+source.getName()));
		else{
			File[] zsources=source.listFiles();  
	         
	        for (File zfile : zsources) {  
	        	copyBaseConf(zfile,dest+File.separator+source.getName());
	        }  
		}
	}
	
	public void shutdownForce() throws HopperException {
		try {
			shutdownForce(server.getMainport());
		} catch (Exception e) {
			throw new HopperException("01005", "强制停止端口["+server.getMainport()+"]失败："+e.getMessage());
		}
	}
	
	public HopperStatus getCurrentStatus() {
		return HopperStatus.parse(currentStatus.toString());
	}
	
}
