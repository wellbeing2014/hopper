package org.zxp.funk.hopper.pojo;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.zxp.funk.hopper.jpa.entity.OperationType;
import org.zxp.funk.hopper.jpa.entity.ServerOperation;

public class OperationLog extends ServerOperation {
	
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 操作扩展信息
	 */
	private static final long serialVersionUID = 7114335610043677328L;
	private String servername;

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	
	public OperationLog(ServerOperation f,String servername){
		BeanUtils.copyProperties(f, this);
		this.servername = servername;
	}
	
	public OperationLog() {
	}
  
	public OperationLog(String operator,Date operationtime,OperationType operationtype,String servername){
		this.servername = servername;
		this.setOperationtime(operationtime);
		this.setOperator(operator);
		this.setOperationtype(operationtype);
	}
     
}