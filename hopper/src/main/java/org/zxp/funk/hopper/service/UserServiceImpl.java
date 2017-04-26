package org.zxp.funk.hopper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxp.funk.hopper.jpa.model.User;
import org.zxp.funk.hopper.jpa.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRep;
	
	

	@Transactional(readOnly=false)
	@Override
	public User findUser(String id) {
		return userRep.findOne(id);
	}






	
	
	
}
