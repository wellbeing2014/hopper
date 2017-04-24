package org.zxp.funk.hopper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.dao.IUserDao;
import org.zxp.funk.hopper.entity.User;

@Service 
public class UserServiceImpl implements UserService {

	@Autowired  
	private IUserDao userDao;
	
	
	@Override @Transactional 
	public User findUser(String id) {
		return userDao.load(id);
	}
	
	
	
}
