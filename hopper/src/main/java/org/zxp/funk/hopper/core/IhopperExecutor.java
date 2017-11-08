package org.zxp.funk.hopper.core;

public interface IhopperExecutor  {
 
	public void startup() throws Exception;
	
	public void shutdown() throws Exception;
	
	public boolean isRunning();
}