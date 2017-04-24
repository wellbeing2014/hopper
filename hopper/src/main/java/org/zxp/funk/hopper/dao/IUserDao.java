package org.zxp.funk.hopper.dao;

import org.zxp.funk.hopper.entity.User;

public interface IUserDao extends IBaseDao<User> {
	public void add(User user,int gid); 
}
