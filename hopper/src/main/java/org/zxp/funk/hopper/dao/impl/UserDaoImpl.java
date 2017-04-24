package org.zxp.funk.hopper.dao.impl;

import org.springframework.stereotype.Repository;
import org.zxp.funk.hopper.dao.IUserDao;
import org.zxp.funk.hopper.entity.User;

@Repository("userDao")
public class UserDaoImpl extends BaseDao<User> implements IUserDao{

	@Override
	public void add(User user, int gid) {
		// TODO Auto-generated method stub
	}

	
}
