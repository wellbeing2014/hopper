package org.zxp.funk.hopper.jpa.repository;

import java.util.List;

import org.zxp.funk.hopper.jpa.model.ServerOperation;

public interface ServerOperationRepository extends BaseRepository<ServerOperation, String>{

	 //@Query("select blog from Blog blog join blog.creator creator where creator.role = ?1")
	 List<ServerOperation> findByServerid(String serverid);
}
