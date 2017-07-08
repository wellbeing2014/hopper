package org.zxp.funk.hopper.jpa.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.hibernate.annotations.GenericGenerator;
 
@Entity
@Table(name="OPERATION")
public class ServerOperation implements Serializable {
	
	
 
    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 操作表
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    //@Column(columnDefinition = "CHAR(32)")
	@Id
    private String operationid;
	private String serverid;
	private int operationtype;
	private String operator;
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
	public int getOperationtype() {
		return operationtype;
	}
	public void setOperationtype(int operationtype) {
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