package hopper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class PBstartTomcat2 {
	
	public static void main(String args[]) throws IOException
	{
		// ProcessBuilder 例子 Java程序自重启  
        // 用一条指定的命令去构造一个进程生成器  
        ProcessBuilder pb = new ProcessBuilder("cmd","/c", "echo","%TOMCAT%");  
        // 让这个进程的工作区空间改为F:\dist  
        // 这样的话,它就会去F:\dist目录下找Test.jar这个文件  
        // 得到进程生成器的环境 变量,这个变量我们可以改,  
        // 改了以后也会反应到新起的进程里面去  
        Map<String, String> map = pb.environment(); 
        map.put("TOMCAT", "WOASDFIASDFHAISDHFIASDFHAISDF");
        Process p1 = pb.start();  
        // 然后就可以对p做自己想做的事情了  
        // 自己这个时候就可以退出了  
        BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream(),"GBK"));  
        String s = "";  
        while((s=br.readLine())!= null){  
            System.out.println(s);  
        }  
        br.close();
        
        System.exit(0);  
	}
	

}
