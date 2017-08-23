package org.zxp.funk.hopper.controller;

public class HopperBaseReturn {
	
	
	
	private boolean isSuccess;
	private String retId;
	private String msg;
	private String errorDetail;
	private Object retObj;
	
	
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getRetId() {
		return retId;
	}
	public void setRetId(String retId) {
		this.retId = retId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public Object getRetObj() {
		return retObj;
	}
	public void setRetObj(Object retObj) {
		this.retObj = retObj;
	}
	
}
