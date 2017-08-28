package org.zxp.funk.hopper.jpa.entity;


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
@Table(name="conf_jdks")
public class JdkConfig implements Serializable {
	
	
 
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
    @Column(name = "JDKNAME", nullable = false)
    private String jdkname;
    
    @Size(min=3, max=200)
    @Column(name = "CLASSPATH", nullable = false)
    private String classpath;
    
    @Column(name = "JAVAHOME")
    private String javahome;
    
    @Column(name = "REMARK")
    private String remark;
   

	public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
    
    public String getJdkname() {
		return jdkname;
	}

	public void setJdkname(String jdkname) {
		this.jdkname = jdkname;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public String getJavahome() {
		return javahome;
	}

	public void setJavahome(String javahome) {
		this.javahome = javahome;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof JdkConfig))
            return false;
        JdkConfig other = (JdkConfig) obj;
        if (id != other.id)
            return false;
        return true;
    }
 
    @Override
    public String toString() {
        return "jdk [id=" + id + ", jdkname=" + jdkname +  "]";
    }
     
}