package com.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

import com.inter.UserDAO;
import com.user.User;

@RunWith(SpringRunner.class)
@ContextConfiguration({"/spring/spring-config.xml"})
@Transactional
public class util{
	
	@Autowired
	private UserDAO userDAO;
	private User user;
	
	@Before
	public void init() {
		user=new User();
		user.setUsername("111");
		user.setPassword("222222");
	}
	@Test
	public void add() {
		int row=userDAO.addUser(user);
		assertEquals(1, row);
	}
}


	
