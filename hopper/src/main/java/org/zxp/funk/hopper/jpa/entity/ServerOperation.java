package org.zxp.funk.hopper.jpa.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
 
@Entity
@Table(name="HPR_OPERATION")
public class ServerOperation implements Serializable {
	
	
 
    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 操作表
	 */
	private static final long serialVersionUID = 1L;

//	@GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
	@GenericGenerator(name="systemUUID",strategy="uuid")
	@GeneratedValue(generator="systemUUID")
    //@Column(columnDefinition = "CHAR(32)")
	@Id
    private String operationid;
	private String serverid;
	@Enumerated(EnumType.ORDINAL)
	private OperationType operationtype;
	private String operator;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  
	private Date operationtime;
	
	
	public String getOperationid() {
		return operationid;
	}
	public void setOperationid(String operationid) {
		this.operationid = operationid;
	}
	public String getServerid() {
		return serverid;
	}
	public void setServerid(String serverid) {
		this.serverid = serverid;
	}
	public OperationType getOperationtype() {
		return operationtype;
	}
	public void setOperationtype(OperationType operationtype) {
		this.operationtype = operationtype;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperationtime() {
		return operationtime;
	}
	public void setOperationtime(Date operationtime) {
		this.operationtime = operationtime;
	}
	
	public ServerOperation(String serverid){
		this.serverid = serverid;
		this.operationtime = new Date();
	}
	public ServerOperation(){
		
	}
     
}