package org.zxp.funk.hopper.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.zxp.funk.hopper.jpa.entity.ServerOperation;
import org.zxp.funk.hopper.pojo.OperationLog;

public interface ServerOperationRepository extends PagingAndSortingRepository<ServerOperation, String>{

	 //@Query("select blog from Blog blog join blog.creator creator where creator.role = ?1")
	 List<ServerOperation> findByServerid(String serverid);
	 void deleteByServerid(String serverid);
	 
	 @Query("select new org.zxp.funk.hopper.pojo.OperationLog(a.operator,a.operationtime,a.operationtype,b.servername) from ServerOperation a ,TomcatServer b where a.serverid = b.serverid")
	 List<OperationLog> findOperation2All();
	 
	 @Query("select new org.zxp.funk.hopper.pojo.OperationLog(a.operator,a.operationtime,a.operationtype,b.servername) from ServerOperation a ,TomcatServer b where a.serverid = b.serverid ")
	 Page<OperationLog> findOperationLogsByPage(Pageable able );
}
