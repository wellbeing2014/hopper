package org.zxp.funk.hopper.core;

public enum HopperStatus {
	STARTED("正在启动"),
	RUNNING("运行中"),
	SHUTDOWN("正在停止"),
	STOPPED("已停止"),
	FORBIDDEN("已禁止");
	private String status;
	HopperStatus(String status){
		this.status = status;
	}
	
	@Override
	public String toString() {
		return this.status;
	}
	
	 public static HopperStatus parse(String status) {
		 HopperStatus plat = HopperStatus.FORBIDDEN; // Default
        for (HopperStatus item : HopperStatus.values()) {
            if (item.toString().equals(status)) {
            	plat = item;
                break;
            }
        }
        return plat;
    }

}
