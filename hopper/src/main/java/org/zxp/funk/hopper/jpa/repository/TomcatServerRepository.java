package org.zxp.funk.hopper.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.zxp.funk.hopper.jpa.entity.TomcatServer;

public interface TomcatServerRepository extends BaseRepository<TomcatServer, String>{
	@Query("select count(1) from TomcatServer a where a.mainport = ?1 or a.shutport = ?1")
	int countPortExists(int port);
}
