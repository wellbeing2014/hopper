package org.zxp.funk.hopper.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.zxp.funk.hopper.jpa.entity.ServerOperationExt;
import org.zxp.funk.hopper.pojo.OperationLog;



@Repository
public class CustomDao {
	
	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager entityManager;
	
	/**
	 * @Title: getOperations
	 * @Description: 全量获取操作日志
	 * @return
	 * @return: List<ServerOperation2>
	 */
	public List<ServerOperationExt> getOperations(){
		try {
            List<ServerOperationExt> list;
            Query query = entityManager.createNativeQuery("select a.*,b.servername from operation a ,server b where a.serverid = b.serverid",ServerOperationExt.class);
           // query.setParameter(1, operatorId);
            list = query.getResultList();
           System.out.println(list.size());
            return list;
        } catch (Exception e) {
            throw e;
        } 
	}
	
	/**
	 * @Title: getOperationsByPage
	 * @Description: 分页获取操作日志,使用原生
	 * @param countPerPage
	 * @param pageNo
	 * @return
	 * @return: List<ServerOperation2>
	 */
	public List<OperationLog> getOperationsByPage(int countPerPage,int pageNo){
		String dataSql = "select new org.zxp.funk.hopper.pojo.OperationLog(a,b.servername) from ServerOperation a ,TomcatServer b where a.serverid = b.serverid order by a.operationtime desc";
		Query dataQuery = entityManager.createQuery(dataSql);
		dataQuery.setMaxResults(countPerPage);
		dataQuery.setFirstResult(countPerPage*(pageNo-1));
        List<OperationLog> data = dataQuery.getResultList();
        return data;
	}
}
