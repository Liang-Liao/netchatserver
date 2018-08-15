package netchat.netchatserver.dao.impl;

import org.junit.Test;

import netchat.dao.UserDao;
import netchat.dao.impl.UserDaoImpl;

public class UserDaoImplTest {
	UserDao userDao = new UserDaoImpl();
	
	@Test
	public void testGetFriends() {
		System.out.println(userDao.getFriends("123456789"));
	}
}
