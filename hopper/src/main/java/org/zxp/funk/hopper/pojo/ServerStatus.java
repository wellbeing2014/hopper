package org.zxp.funk.hopper.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerStatus {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String serverid;
	private int mainport;
	private String servername;
	private String mark;
	private int opr;
	private String lasttime;
	private StringBuffer status;
	private String[] loalpaths;
	private int viewcount;
	
	
	
	
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public String getServerid() {
		return serverid;
	}
	public void setServerid(String serverid) {
		this.serverid = serverid;
	}
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public int getOpr() {
		return opr;
	}
	public void setOpr(int opr) {
		this.opr = opr;
	}
	public StringBuffer getStatus() {
		return status;
	}
	public void setStatus(StringBuffer status) {
		this.status = status;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(Date lasttime) {
		this.lasttime = sdf.format(lasttime);
	}
	
	public String[] getLoalpaths() {
		return loalpaths;
	}
	public void setLoalpaths(String[] loalpaths) {
		this.loalpaths = loalpaths;
	}
	public int getMainport() {
		return mainport;
	}
	public void setMainport(int mainport) {
		this.mainport = mainport;
	}

}
