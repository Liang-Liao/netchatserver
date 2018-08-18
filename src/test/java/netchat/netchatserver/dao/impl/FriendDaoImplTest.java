package netchat.netchatserver.dao.impl;

import org.junit.Test;

import netchat.dao.FriendDao;
import netchat.dao.impl.FriendDaoImpl;

public class FriendDaoImplTest {
	FriendDao friendDao = new FriendDaoImpl();
	
	@Test
	public void testGetFriendByAccount() {
		System.out.println(friendDao.getFriendByAccount("123456789", "777777777"));
	}
	
	@Test
	public void testGetFriendList() {
		System.out.println(friendDao.getApplyList(1));
	}
	
	@Test
	public void testUpdateFriendStatus() {
		System.out.println(friendDao.updateFriendStatus(1, 4, 1));
	}
}
