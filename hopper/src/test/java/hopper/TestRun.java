package hopper;

import java.nio.charset.Charset;

import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.core.HopperLogEventListener;
import org.zxp.funk.hopper.core.HopperLogEventObject;
import org.zxp.funk.hopper.core.HopperStatusEventListener;
import org.zxp.funk.hopper.core.HopperStatusEventObject;
import org.zxp.funk.hopper.core.tomcat.TomcatExecutor;

public class TestRun {
	public static void main(String args[]) throws HopperException
	{
		System.out.println(Charset.defaultCharset().name());
		TomcatExecutor tomcat = new TomcatExecutor();
		tomcat.setTomcatHome("D:/Develop/Apache/Tomcat/apache-tomcat-7.0.73");
		tomcat.setJavaHome("D:/Develop/Java/jdk1.7.0_71");
		tomcat.addLogEventListener(new HopperLogEventListener() {
			
			@Override
			public void logEvent(HopperLogEventObject obj) {
				System.out.println(obj.log);
			}
		});
		tomcat.addStatusEventListener(new HopperStatusEventListener() {
			
			@Override
			public void statusChanged(HopperStatusEventObject obj) {
				switch(obj.status){
					case STARTED:
						System.out.println("��������");
						break;
					case RUNNING:
						System.out.println("������");
						break;
					case SHUTDOWN:
						System.out.println("����ֹͣ");
						break;
					case STOPPED:
						System.out.println("��ֹͣ");
						break;
				}
			}
		});
		tomcat.startup();
	}
}
