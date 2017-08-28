package org.zxp.funk.hopper.jpa.entity;

import javax.persistence.Entity;

import org.springframework.beans.BeanUtils;
import org.zxp.funk.hopper.jpa.entity.ServerOperation;

//@Entity
public class ServerOperationExt extends ServerOperation {
	
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 操作扩展信息
	 */
	private static final long serialVersionUID = 7114335610043677328L;
	private String servername1;

	public String getServername() {
		return servername1;
	}

	public void setServername(String servername) {
		this.servername1 = servername;
	}
	
	public ServerOperationExt(ServerOperation f,String servername){
		BeanUtils.copyProperties(f, this);
		this.servername1 = servername;
	}
	
	public ServerOperationExt() {
	}
  
     
}