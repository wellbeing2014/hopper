package org.zxp.funk.hopper.dao;

import java.util.List;

public interface IBaseDao<T> {
	
    public void add(T t) ;
  
    public void delete(String id); 
  
    public void update(T t);  
  
    public T load(String id); 
  
    public List<T> list(String hql, Object[] args) ; 

}
