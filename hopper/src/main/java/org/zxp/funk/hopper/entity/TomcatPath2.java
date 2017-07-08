package org.zxp.funk.hopper.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


public class TomcatPath2{
	
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
		return this.docbase+"|"+this.contextpath+";";
	}
}