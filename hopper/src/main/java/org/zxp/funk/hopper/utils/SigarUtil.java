package org.zxp.funk.hopper.utils;

import java.text.DecimalFormat;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class SigarUtil {

	private static DecimalFormat df = new DecimalFormat("#.###");
	public static void main(String[] args) {

		Sigar sigar = new Sigar();
		String monitorif = "";
		
		String[] a = "C|D".split("\\|");
				
		for(int i=0; i<a.length; i++){  
            System.out.print(" \""+a[i]+"\"");  
        }  
		
		try {
		FileSystem[]  disk =sigar.getFileSystemList();
        for(FileSystem pan:disk) {
        	
        	if(pan.getType()==2) {
        		
        		FileSystemUsage usage = null;  
        		usage = sigar.getFileSystemUsage(pan.getDirName()); 
        		System.out.print(pan.getDirName()+"共："+(double)usage.getTotal()/1024/1024);
        		System.out.print("\t使用："+(double)usage.getUsed());
        		System.out.print("\t占用率："+(double)usage.getUsePercent());
        	}
        }
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		double rxbps;    
		double txbps; 
		
		
		try {
			String[] ifNames = sigar.getNetInterfaceList();
			for(String ifname:ifNames) {
				System.out.println(ifname);
				NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(ifname); 
				
	    	    if("94:DE:80:B6:BE:96".equals(ifconfig.getHwaddr()) )
	    	    {
	    	    	monitorif = ifname;
	    	    	break;
	    	    }
			}
			if(monitorif.isEmpty())
				throw new SigarException("网卡不存在");
				long start = System.currentTimeMillis();    
	            NetInterfaceStat statStart = sigar.getNetInterfaceStat(monitorif);  
	            double rxBytesStart = statStart.getRxBytes();    
	            double txBytesStart = statStart.getTxBytes();    
	            Thread.sleep(1000);    
	            long end = System.currentTimeMillis();    
	            NetInterfaceStat statEnd = sigar.getNetInterfaceStat(monitorif);    
	            double rxBytesEnd = statEnd.getRxBytes();    
	            double txBytesEnd = statEnd.getTxBytes();    
	                
	            rxbps = (rxBytesEnd - rxBytesStart)/(end-start)*1000/1024;    
	            txbps = (txBytesEnd - txBytesStart)/(end-start)*1000/1024;    
				System.out.println("流量：下载"+df.format(rxbps) +"上传"+df.format(txbps));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
