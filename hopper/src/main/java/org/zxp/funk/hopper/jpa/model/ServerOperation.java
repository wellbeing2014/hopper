package org.zxp.funk.hopper.jpa.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
 
@Entity
@Table(name="SERVER")
public class ServerOperation implements Serializable {
	
	
 
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
    @Column(name = "BASEPATH", nullable = false)
    private String basepath;
    
    @Column(name = "ARGS")
    private String args;
    
    

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
    
    public String getBasePath() {
        return basepath;
    }
 
    public void setBasePath(String basepath) {
        this.basepath = basepath;
    }
    
    
    public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
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
        if (!(obj instanceof ServerOperation))
            return false;
        ServerOperation other = (ServerOperation) obj;
        if (id != other.id)
            return false;
        return true;
    }
 
    @Override
    public String toString() {
        return "serverConfig [id=" + id + ", name=" + name +  "]";
    }
     
}