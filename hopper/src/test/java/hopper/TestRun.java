package hopper;

import java.nio.charset.Charset;

import org.zxp.funk.hopper.core.HopperException;
import org.zxp.funk.hopper.core.TomcatExecutor;
import org.zxp.funk.hopper.core.TomcatLogEventListener;
import org.zxp.funk.hopper.core.TomcatLogEventObject;
import org.zxp.funk.hopper.core.TomcatStatusEventListener;
import org.zxp.funk.hopper.core.TomcatStatusEventObject;

public class TestRun {
	public static void main(String args[]) throws HopperException
	{
		System.out.println(Charset.defaultCharset().name());
		TomcatExecutor tomcat = new TomcatExecutor();
		tomcat.setTomcatHome("D:/Develop/Apache/Tomcat/apache-tomcat-7.0.73");
		tomcat.setJavaHome("D:/Develop/Java/jdk1.7.0_71");
		tomcat.addTomcatLogEventListener(new TomcatLogEventListener() {
			
			@Override
			public void logEvent(TomcatLogEventObject obj) {
				System.out.println(obj.log);
			}
		});
		tomcat.addTomcatStatusEventListener(new TomcatStatusEventListener() {
			
			@Override
			public void statusChanged(TomcatStatusEventObject obj) {
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
