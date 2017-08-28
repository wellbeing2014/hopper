package org.zxp.funk.hopper.service;

import org.zxp.funk.hopper.jpa.entity.User;

public interface UserService {
	public User findUser(String id);
}
