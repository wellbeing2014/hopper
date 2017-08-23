package org.zxp.funk.hopper.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zxp.funk.hopper.entity.ServerStatus;
import org.zxp.funk.hopper.jpa.model.ServerOperation;
import org.zxp.funk.hopper.jpa.model.TomcatPath;
import org.zxp.funk.hopper.jpa.model.TomcatServer;
import org.zxp.funk.hopper.utils.XmlUtil;

import com.google.common.io.Files;

public class ServerBehavior {
	private TomcatExecutor executor;
	public TomcatServer server;
	
	public ServerStatus status = new ServerStatus();
	
	private StringBuffer currentStatus= new StringBuffer(TomcatStatus.STOPPED.toString());
	
	private String confDir;
	private static String TOMCAT_BASE_CONF_DIR = "conf";
	
	public ServerBehavior(TomcatServer instance,String confBaseDir) throws HopperException{
		
		this.server=instance;
		this.confDir = confBaseDir+File.separator+this.server.getConfigDirName();
		status.setServerid(server.getServerid());
		status.setServername(server.getServername());
		status.setStatus(currentStatus);
		status.setMainport(server.getMainport());
		status.setLoalpaths(server.getLoalPaths());
		
		status.setOpr(server.getOperations());
		status.setMark(server.getDesc());
		status.setLasttime(server.getLasttime());
		executor = new TomcatExecutor();
		executor.setTomcatHome(server.getTomcat().getPath());
		executor.setTomcatBase(confDir);
		executor.setJavaOpts(server.getOpts());
		executor.setJavaHome(server.getJdk().getJavahome());
		this.executor.addTomcatStatusEventListener(new TomcatStatusEventListener() {
			
			@Override
			public void statusChanged(TomcatStatusEventObject obj) {
				currentStatus.setLength(0);
				currentStatus.append(obj.status.toString());
			}
		});
		
		tomcatBaseInit();
	}
	
	public void addOnceOpr(){
		status.setOpr(status.getOpr()+1);
	}
	
	public void addTomcatLogEventListener(TomcatLogEventListener listener){
		this.executor.addTomcatLogEventListener(listener);
	}
	
	public void addTomcatStatusEventListener(TomcatStatusEventListener listener){
		this.executor.addTomcatStatusEventListener(listener);
	}
	
	public void startup() throws HopperException {
		executor.startup();
	}
	
	
	public void shutdown() throws HopperException {
		
		executor.shutdown();
	
	}
	
	/**
	 * @Title: tomcatBaseDir
	 * @Description: TOMCATBASE配置的主要初始工作
	 * @param configDir
	 * @throws HopperException
	 * @return: void
	 */
	public void tomcatBaseInit() throws HopperException{
		
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
				context.setAttribute("debug", "0");
				context.setAttribute("reloadable", "false");
			}
			
			XmlUtil.saveDoc(doc, confDir+File.separator+TOMCAT_BASE_CONF_DIR+File.separator+"server.xml");
		} catch (Exception e) {
			e.printStackTrace();
			throw new HopperException("01003","TOMCAT_BASE 解析配置错误："+e.getMessage());
		}
        
	}
	
	
	/**
	 * @Title: tomcatBaseDelete
	 * @Description: 删除配置，目前是新增修改 启动平台 都去删除配置，然后再重新添加
	 * @throws HopperException
	 * @return: void
	 */
	public void tomcatBaseDelete() throws HopperException{
		
		File tomcatbase = new File(confDir);
		try {
			java.nio.file.Files.walk(tomcatbase.toPath()).
			sorted((a, b) -> b.compareTo(a)). // reverse; files before dirs
			forEach(p -> {
			     try {
					java.nio.file.Files.delete(p);
				} catch (IOException e) {
				} 
			  });
		} catch (Exception e1) {
			throw new HopperException("01004","TOMCAT_BASE 删除错误："+e1.getMessage());
		}

	}
	
	
	private static void copyBaseConf(File source,String dest) throws IOException{
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
	
	
	public IhopperExecutor getExecutor() {
		return this.executor;
	}
	
	public static void main(String[] arg){
		
		try {
			copyBaseConf(new File("D:\\Develop\\Apache\\Tomcat\\apache-tomcat-7.0.73\\conf"),"D:\\Develop\\Apache\\Tomcat\\servers\\测试服务");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
