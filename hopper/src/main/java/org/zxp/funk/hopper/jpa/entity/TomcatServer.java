package org.zxp.funk.hopper.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="HPR_TOMCAT_SERVER")
public class TomcatServer implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -4819142500293622156L;
	
	@Id
//	@GeneratedValue(generator = "uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid")
	@GenericGenerator(name="systemUUID",strategy="uuid")
	@GeneratedValue(generator="systemUUID")
	private String serverid;
	private int mainport;
	private int shutport;
	private String servername;
	
	@OneToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name="jdkid")
	private JdkConfig jdk;
	@OneToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name="tomcatid")
    private ServerConfig tomcat;
	private int operations;
	private Date lasttime;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER ,orphanRemoval=true)
	@JoinColumn(name="server_id")
	private List<TomcatPath> tomcatpaths;
	
	@Column(name="mark")
	private String desc;
	private String opts;
	
	public String getServerid() {
		return serverid;
	}
	public void setServerid(String serverid) {
		this.serverid = serverid;
	}
	public int getMainport() {
		return mainport;
	}
	public void setMainport(int mainport) {
		this.mainport = mainport;
	}
	public int getShutport() {
		return shutport;
	}
	public void setShutport(int shutport) {
		this.shutport = shutport;
	}
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
	public ServerConfig getTomcat() {
		return tomcat;
	}
	public void setTomcat(ServerConfig tomcat) {
		this.tomcat = tomcat;
	}
	public JdkConfig getJdk() {
		return jdk;
	}
	public void setJdk(JdkConfig jdk) {
		this.jdk = jdk;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOpts() {
		return opts;
	}
	public void setOpts(String opts) {
		this.opts = opts;
	}

	public Date getLasttime() {
		return lasttime;
	}
	public void setLasttime(Date lasttime) {
		this.lasttime = lasttime;
	}
	public int getOperations() {
		return operations;
	}
	public void setOperations(int operations) {
		this.operations = operations;
	}
	
	public List<TomcatPath> getTomcatpaths() {
		return tomcatpaths;
	}
	public void setTomcatpaths(List<TomcatPath> tomcatpaths) {
		this.tomcatpaths = tomcatpaths;
	}
	
	/**
	 * @Title: getLoalPaths
	 * @Description: 获取部署的本地服务地址
	 * @return
	 * @return: String[]
	 */
	public String[] getLoalPaths(){
		String[] ret = new String[this.getTomcatpaths().size()];
		int i=0;
		for(TomcatPath path:tomcatpaths){
			ret[i]=path.getDocbase();
			i++;
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "serverid:"+serverid+"servername:"+servername;
	}
	
	/**
	 * @Title: getConfigDirName
	 * @Description: 获取该服务的配置文件夹名称
	 * @return
	 * @return: String
	 */
	public String getConfigDirName(){
		return this.mainport+"_"+this.servername;
	}
	
	/** 是否改动了核心
	 * @param ts
	 * @return
	 */
	public boolean isCoreEdit(TomcatServer ts) {
		if(!this.getConfigDirName().equals(ts.getConfigDirName()))
			return true;
		if(this.shutport!=ts.getShutport())
			return true;
		if(!this.jdk.equals(ts.getJdk()))
			return true;
		if(!this.tomcat.equals(ts.getTomcat()))
			return true;
		if(this.tomcatpaths.size()==ts.getTomcatpaths().size()) {
			for(int i = 0; i<this.tomcatpaths.size(); i++) {
				if(!this.tomcatpaths.get(i).equals(ts.tomcatpaths.get(i)))
					return true;
			}
			return false;
		}
		else return true;
		
	}
	 
 
}
