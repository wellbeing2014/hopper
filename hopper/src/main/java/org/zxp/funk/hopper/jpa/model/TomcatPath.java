package org.zxp.funk.hopper.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PATH")
public class TomcatPath{
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	private String pathid;
	private String docbase;
	private String contextpath;
	public String getDocbase() {
		return docbase;
	}
	public void setDocbase(String docbase) {
		this.docbase = docbase;
	}
	public String getContextpath() {
		return contextpath;
	}
	public void setContextpath(String contextpath) {
		this.contextpath = contextpath;
	}
	
	public String toString(){
		return "{'docbase':'"+this.docbase+"','contextpath':'"+this.contextpath+"'}";
	}
}