package org.zxp.funk.hopper.service;

import org.zxp.funk.hopper.jpa.model.User;

public interface UserService {
	public User findUser(String id);
}
