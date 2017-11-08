package org.zxp.funk.hopper.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SystemMetricThread implements DisposableBean, Runnable {
	private final Logger logger = LoggerFactory.getLogger(SystemMetricThread.class);
	private Thread thread;
	private static Sigar sigar;
	private static DecimalFormat df = new DecimalFormat("#.###");
	private static ObjectMapper mapper = new ObjectMapper();
    private volatile boolean destroyCondition =true;
    /**
     * 资源收集的间隔默认5秒
     */
    @Value("${sys.monitor.interval}")
    private volatile int interval = 1000;
    
    @Value("${sys.monitor.mac}")
	String mac;
    
    @Value("${sys.monitor.disk}")
   	String disk;
    
	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;
    
	private String monitorif ="";
	
    SystemMetricThread(){
    	sigar = new Sigar();
        this.thread = new Thread(this);
    }
    
    @PostConstruct
    private void init() {
    	String[] ifNames;
		try {
			ifNames = sigar.getNetInterfaceList();
			for(String ifname:ifNames) {
				NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(ifname); 
	    	    if(mac.equals(ifconfig.getHwaddr()) )
	    	    {
	    	    	monitorif = ifname;
	    	    	break;
	    	    }
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}
    }
    
    /**
     *  获取cpu占用
     * 
     *
     * @变更记录 2017年9月20日 下午3:56:54 朱新培 zxp@wisoft.com.cn 创建
     *
     */
    private Map<String,Object> getCpu() {
    	Map<String,Object> cpum = new HashMap<>();
    	double userPer = 0;
    	try {
			CpuPerc cpu = sigar.getCpuPerc();
			userPer = cpu.getCombined();
			//System.out.print("cpu:"+userPer+"\t");
		} catch (SigarException e) {
		}
    	
    	cpum.put("cpu", df.format(userPer*100)+"%");
    	return cpum;
    }
    
    /**
     * 获取内存占用
     * 
     *
     * @变更记录 2017年9月20日 下午3:57:53 朱新培 zxp@wisoft.com.cn 创建
     *
     */
    private Map<String,Object> getMem() {
    	Map<String,Object> memm = new HashMap<>();
    	double UsedPer = 0;
    	try {
    		 Mem mem = sigar.getMem();
	       // double memtotal =(double)mem.getTotal()/1024/1024/1024;
	       // double memFree =(double)mem.getFree()/1024/1024/1024;
    		 UsedPer =(double)mem.getUsedPercent();
		} catch (SigarException e) {
		}
    	memm.put("mem", df.format(UsedPer)+"%");
    	return memm;
    }
    
    
    /**
     * 获取硬盘占用
     * 
     *
     * @变更记录 2017年9月20日 下午3:57:53 朱新培 zxp@wisoft.com.cn 创建
     *
     */
    private Map<String,Object>  getDisk() {
    	Map<String,Object> diskm = new HashMap<>();
    	String[] disks = disk.split("\\|");
    	try {
            for(String pan:disks) {
            		FileSystemUsage usage = null;  
            		usage = sigar.getFileSystemUsage(pan+":\\"); 
            		diskm.put(pan, (double)usage.getUsePercent());
            }
		} catch (SigarException e) {
		}
    	return new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
    			put("disk",diskm);
    		}
    	};
    }
    
    /**
     * 获取网卡流量
     * 
     *
     * @变更记录 2017年9月20日 下午3:57:53 朱新培 zxp@wisoft.com.cn 创建
     *
     */
    private Map<String,Object> getNetWork() {
    	double rxbps = 0;    
    	double txbps = 0;
    	try {
	    	if(monitorif.isEmpty())
				throw new SigarException("网卡不存在");
			long start = System.currentTimeMillis();    
	        NetInterfaceStat statStart = sigar.getNetInterfaceStat(monitorif);  
	        double rxBytesStart = statStart.getRxBytes();    
	        double txBytesStart = statStart.getTxBytes();    
	        Thread.sleep(500);    
	        long end = System.currentTimeMillis();    
	        NetInterfaceStat statEnd = sigar.getNetInterfaceStat(monitorif);    
	        double rxBytesEnd = statEnd.getRxBytes();    
	        double txBytesEnd = statEnd.getTxBytes();    
	            
	        rxbps = (rxBytesEnd - rxBytesStart)/(end-start)*1000/1024;    
	        txbps = (txBytesEnd - txBytesStart)/(end-start)*1000/1024;    
    	}catch(Exception e) {
    		
    	}
    	
    	String str = "接收"+df.format(rxbps) +"kb | 上传"+df.format(txbps)+"kb";
    	return new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
    			put("net",str);
    		}
    	};
		 
    }
    
    
    private Map<String, Object> getTotal() {
    	Map<String, Object> map = new HashMap<String, Object>(); 
    	map.putAll(getCpu());
    	map.putAll(getMem());
    	map.putAll(getDisk());
    	map.putAll(getNetWork());
    	return map;
    }
    
    @Override
    public void run(){
    	logger.info("启动资源监控！");
    	while(destroyCondition) {
    	
    		try {
    			Thread.sleep(interval);
    		} catch (InterruptedException e) {}
    		
    		String ret = "";
			try {
				ret = mapper.writeValueAsString(getTotal());
			} catch (JsonProcessingException e) {
			}
			brokerMessagingTemplate.convertAndSend("/topic/metric", ret);
    	}
    }

    @Override
    public void destroy(){
    	destroyCondition = false;
    }
    
    @PostConstruct
    public void initfinished() {
    	
    	 this.thread.start();
    }

}
