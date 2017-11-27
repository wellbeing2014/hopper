package org.zxp.funk.hopper.core.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.zxp.funk.hopper.core.BaseOutputSteam;

public class BaseExecutor  extends DefaultExecutor {
	
	
	private List<String[]> netstat = new ArrayList<>();
	private Pattern pattern = Pattern.compile("\\S+");
	
	private int port = 8080;

    private final BaseOutputSteam stdOutLog = new BaseOutputSteam(100,"gbk") {
		@Override
		public void handleLine(String line) {
			Matcher matcher = pattern.matcher(line);
			String XieYi = "";
			String BenDi = "";
			String WaiBu = "";
			String ZhuangT = "";
			String PID = "";
			if(matcher.find()) XieYi = matcher.group();
			if(matcher.find()) BenDi = matcher.group();
			if(matcher.find()) WaiBu = matcher.group();
			if(matcher.find()) ZhuangT = matcher.group();
			if(matcher.find()) PID = matcher.group();
			netstat.add(new String[] {XieYi,BenDi,WaiBu,ZhuangT,PID});
		}
	};
	
	private final ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
    
    private Map<String,String> environment =new HashMap<String,String>();
    
    public BaseExecutor() {
        super();
        
        this.setStreamHandler(new PumpStreamHandler(stdOutLog));
        this.setWatchdog(watchdog);
    }
    
    
    public void dogetprogress() throws Exception{
    	CommandLine cl = new CommandLine("cmd");
    	cl.addArgument("/C");
    	cl.addArgument("netstat -aon|findstr "+port);
    	environment.put("PATH", "C:\\Windows\\system32");
		execute(cl, environment,new DefaultExecuteResultHandler(){
			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				try {
					killprogress();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
			}
		});
			
    }
   

    public void killprogress() throws Exception{
    	
    	int pid = extractPid();
    	if(pid<1024) {
    		throw new Exception("找到pid="+pid+",小于1024,不予强制kill"); 
    	}
    	CommandLine cl = new CommandLine("taskkill");
    	cl.addArgument("/F");
    	cl.addArgument("/PID");
    	cl.addArgument(""+pid);
    	environment.put("PATH", "C:\\Windows\\system32");
		execute(cl, environment,new DefaultExecuteResultHandler());
			
    }
    
    public int extractPid() {
    	int ret = 0;
    	for(String[] progress:netstat) {
    		boolean got = ("TCP".equals(progress[0]))
    				&&("LISTENING".equals(progress[3]))
    				&&(("0.0.0.0:"+port).equals(progress[1]));
    		if(got) ret = Integer.parseInt(progress[4]);
    		else continue;
    	}
    	return ret;
    }
    
    public static void main(String... args) {
    	/*String aa="  TCP    0.0.0.0:135            0.0.0.0:0              LISTENING       964 ";
    	
    	
        

        matcher.find();
        String tcpudp = matcher.group();
        matcher.find();
        String port= matcher.group();
        matcher.find();
        String ip = matcher.group();
        matcher.find();
        String status = matcher.group();
        matcher.find();
        String pid = matcher.group();*/
        
    	
    	BaseExecutor be = new BaseExecutor();
    	try {
			be.dogetprogress();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    
    
    

}
