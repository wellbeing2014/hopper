package org.zxp.funk.hopper.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.zxp.funk.hopper.dao.IBaseDao;

public abstract class BaseDao<T> extends HibernateDaoSupport implements IBaseDao<T> {
	private final Class<T> persistentClass;
    
    @SuppressWarnings("unchecked")
    public BaseDao(){
        this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
     
    @Autowired
    public void setSuperSessionFactory(SessionFactory sessionFactory) {  
        super.setSessionFactory(sessionFactory);  
    }  
    
 
    public void add(T t) {  
        this.getHibernateTemplate().save(t);  
    }  
  
    public void delete(String id) {  
        this.getHibernateTemplate().delete(this.load(id));  
    }  
  
    public void update(T t) {  
        this.getHibernateTemplate().update(t);  
    }  
  
    public T load(String id) {  
        return this.getHibernateTemplate().load(persistentClass, id);  
    }  
  
    @SuppressWarnings("unchecked")  
    public List<T> list(String hql, Object[] args) {  
        Query query=this.currentSession().createQuery(hql);  
        for (int i = 0; i < args.length; i++) {  
            query.setParameter(i, args[i]);  
        }  
        List<T> list=query.list();  
        return list;  
    }  

}
