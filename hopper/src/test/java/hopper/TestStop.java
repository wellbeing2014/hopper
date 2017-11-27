package hopper;

import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.core.tomcat.TomcatExecutor;

public class TestStop {
	public static void main(String args[]) throws HopperException
	{
		TomcatExecutor tomcat = new TomcatExecutor();
		tomcat.setTomcatHome("D:/Develop/Apache/Tomcat/apache-tomcat-7.0.73");
		tomcat.setJavaHome("D:/Develop/Java/jdk1.7.0_71");
		tomcat.shutdown();
	}
}
