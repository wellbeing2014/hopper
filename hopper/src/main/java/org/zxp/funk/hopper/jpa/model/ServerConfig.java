package org.zxp.funk.hopper.jpa.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.zxp.funk.hopper.utils.Platform;

import com.fasterxml.jackson.annotation.JsonInclude;
 
@Entity
@Table(name="conf_servers")
public class ServerConfig implements Serializable {
	
	
 
    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    //@Column(columnDefinition = "CHAR(32)")
    @Column(name = "ID")
	@Id
    private String id;
 
    @Size(min=3, max=50)
    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Size(min=3, max=200)
    @Column(name = "PATH", nullable = false)
    private String path;
    
    @Column(name = "ARGS")
    private String args;
    
    @Column(name = "PLAT",columnDefinition="INT(1) default 1")
    private int plat; 
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private int platform;

	public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() {
        return path;
    }
 
    public void setPath(String path) {
        this.path = path;
    }
    
    
    public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public Platform getPlat() {
		return Platform.parse(this.plat);
	}

	public void setPlat(int plat) {
		this.plat = plat;
	}
    
 
    public int getPlatform() {
		return plat;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ServerConfig))
            return false;
        ServerConfig other = (ServerConfig) obj;
        if (id != other.id)
            return false;
        return true;
    }
 
    @Override
    public String toString() {
        return "serverConfig [id=" + id + ", name=" + name +  "]";
    }
     
}