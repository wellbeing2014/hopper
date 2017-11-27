package hopper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.zxp.funk.hopper.core.HopperStatus;
import org.zxp.funk.hopper.core.HopperStatusEventObject;

public class Exec {
	
	public static void main(String... args) {
		test2();
		//test3();
	}
	
	public void test1() {
		final CommandLine cmdLine = CommandLine.parse("ping www.baidu.com -t");
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		try {
		executor.setWatchdog(watchdog);
		executor.execute(cmdLine, resultHandler);

		Thread.sleep(10000);//等进程执行一会，再终止它
		System.out.println("--> Watchdog is watching ? " + watchdog.isWatching());
		watchdog.destroyProcess();//终止进程
		System.out.println("--> destroyProcess done.");
		System.out.println("--> Watchdog is watching ? " + watchdog.isWatching());
		System.out.println("--> Watchdog should have killed the process : " + watchdog.killedProcess());
		System.out.println("--> wait result is : " + resultHandler.hasResult());
		System.out.println("--> exit value is : " + resultHandler.getExitValue());
		System.out.println("--> exception is : " + resultHandler.getException());

		resultHandler.waitFor(5000);//等待5秒。下面加上上面的几个System.out，看看进程状态是什么。
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test2() {
		Map<String,String> environment =new HashMap<String,String>();
		environment.put("CATALINA_HOME", "D:\\Develop\\Apache\\Tomcat\\apache-tomcat-7.0.73");
		environment.put("CATALINA_BASE", "D:\\test");
		environment.put("JAVA_HOME", "D:\\Develop\\Java\\jdk1.7.0_71");
    	environment.put("PATH", "%JAVA_HOME%/bin;%PATH%");
    	environment.put("CLASSPATH", ".;%JAVA_HOME%/lib;%JAVA_HOME%/lib/tools.jar");
    	
    	DefaultExecutor executor = new DefaultExecutor();
    	final ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
    	DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler(){
			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				System.out.println("运行完成");
			}
			
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				System.out.println("运行退出");
			}
		}; 
    	executor.setWatchdog(watchdog);
    	try {
    	executor.execute(new CommandLine("D:\\Develop\\Apache\\Tomcat\\apache-tomcat-7.0.73/bin/startup.bat"), environment,handler);
    	
    	Thread.sleep(20000);//等进程执行一会，再终止它
    	
    	System.out.println("--> Watchdog is watching ? " + watchdog.isWatching());
		watchdog.destroyProcess();//终止进程
		System.out.println("--> destroyProcess done.");
		System.out.println("--> Watchdog is watching ? " + watchdog.isWatching());
		System.out.println("--> Watchdog should have killed the process : " + watchdog.killedProcess());
		System.out.println("--> wait result is : " + handler.hasResult());
		//System.out.println("--> running : " + executor.getProcessDestroyer().size());
		//System.out.println("--> exit value is : " + handler.getExitValue());
		System.out.println("--> exception is : " + handler.getException());

		handler.waitFor(5000);//等待5秒。下面加上上面的几个System.out，看看进程状态是什么。
		
    	}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void test3() {
		Map<String,String> environment =new HashMap<String,String>();
		environment.put("CATALINA_HOME", "D:\\Develop\\Apache\\Tomcat\\apache-tomcat-7.0.73");
		environment.put("CATALINA_BASE", "D:\\test");
		environment.put("JAVA_HOME", "D:\\Develop\\Java\\jdk1.7.0_71");
    	environment.put("PATH", "%JAVA_HOME%/bin;%PATH%");
    	environment.put("CLASSPATH", ".;%JAVA_HOME%/lib;%JAVA_HOME%/lib/tools.jar");
    	
    	DefaultExecutor executor = new DefaultExecutor();
    	final ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
    	DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler(){
			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				System.out.println("关闭完成");
			}
			
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				System.out.println("关闭退出");
			}
		}; 
    	executor.setWatchdog(watchdog);
    	try {
    	executor.execute(new CommandLine("D:\\Develop\\Apache\\Tomcat\\apache-tomcat-7.0.73/bin/shutdown.bat"), environment,handler);
    	
    	}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
