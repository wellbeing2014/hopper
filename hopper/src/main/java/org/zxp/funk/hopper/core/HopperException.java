package org.zxp.funk.hopper.core;

public class HopperException extends Exception {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2982061651452223471L;
	
	private String errorCode;
	private String errorMsg;
	
	public HopperException(String code,String msg){
		super(msg);
		this.errorCode = code;
		this.errorMsg = msg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	

}
