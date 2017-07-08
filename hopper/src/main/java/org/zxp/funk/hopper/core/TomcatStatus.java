package org.zxp.funk.hopper.core;

public enum TomcatStatus {
	STARTED("正在启动"),
	RUNNING("运行中"),
	SHUTDOWN("正在停止"),
	STOPPED("已停止"),
	FORBIDDEN("已禁止");
	private String status;
	TomcatStatus(String status){
		this.status = status;
	}
	
	@Override
	public String toString() {
		return this.status;
	}

}
