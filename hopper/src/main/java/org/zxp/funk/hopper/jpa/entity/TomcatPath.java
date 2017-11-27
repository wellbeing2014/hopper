package org.zxp.funk.hopper.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="HPR_TOMCAT_PATH")
public class TomcatPath{
	@Id
//	@GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
	@GenericGenerator(name="systemUUID",strategy="uuid")
	@GeneratedValue(generator="systemUUID")
	private String pathid;
	private String docbase;
	private String contextpath;
	
	public String getPathid() {
		return pathid;
	}
	public void setPathid(String pathid) {
		this.pathid = pathid;
	}
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TomcatPath))
            return false;
        TomcatPath other = (TomcatPath) obj;

        if (pathid!=null&&!pathid.equals( other.getPathid()))
            return false;
        if (docbase!=null&&!docbase.equals( other.getDocbase()))
            return false;
        if (contextpath!=null&&!contextpath.equals( other.getContextpath()))
            return false;
        return true;
	}
}