package hopper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class startTomcat {
	
	public static void main(String args[])
	{
		if (args.length < 1)
		{
			System.out.println("USAGE: java GoodWindowsExec <cmd></cmd>");
			System.exit(1);
			}
			try
			{
			String[] cmd = new String[3];
			
			cmd[0] = "cmd.exe" ;
			cmd[1] = "/C" ;
			cmd[2] = args[0];
		
			Runtime rt = Runtime.getRuntime();
			System.out.println("Execing " + cmd[0] + " " + cmd[1]
			+ " " + cmd[2]);
			Process proc = rt.exec(cmd,new String[]{"TOMCAT=WODETAOMCAT"});
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			// any output?
			StreamGobbler outputGobbler = new
			StreamGobbler(proc.getInputStream(), "OUTPUT");
			// kick them off
			errorGobbler.start();
			outputGobbler.start();
			// any error???
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (Throwable t)
		{
		t.printStackTrace();
		}
	}
	static class StreamGobbler extends Thread
	{
		InputStream is;
		String type;
		StreamGobbler(InputStream is, String type)
		{
			this.is = is;
			this.type = type;
		}
		public void run()
		{
			try
			{
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line=null;
				while ( (line = br.readLine()) != null)
				System.out.println(type + ">" + line);
			} catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}

}
