package netchat.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import netchat.dao.FriendDao;
import netchat.dao.UserDao;
import netchat.dao.impl.FriendDaoImpl;
import netchat.dao.impl.UserDaoImpl;
import netchat.netchatserver.PublisherServer;
import netchat.services.UserService;

public class UserServiceImpl implements UserService {
	private UserDao userDao = new UserDaoImpl();
	private FriendDao friendDao = new FriendDaoImpl();

	@Override
	public String login(Map map) {
		Map<String, Object> reMap = new HashMap();
		String account = (String) map.get("account");
		String password = (String) map.get("password");

		Map queryMap = userDao.getUserByAccount(account);
		boolean flag = false;
		// 读取数据库，验证账号密码
		if (account.equals(queryMap.get("account")) && password.equals(queryMap.get("password"))) {
			userDao.updateOnline(account, 1);
			reMap.put("username", queryMap.get("username"));
			reMap.put("online", queryMap.get("online"));
			List<Map<String, String>> friendList = userDao.getFriends(account);
			Map<String, String> temp = null;
			for (Map<String, String> friend : friendList) {
				temp = new HashMap<String, String>();
				temp.put("msgType", "friendOnline");
				temp.put("onlineAccount", account);
				PublisherServer.publishMsg(friend.get("account"), new Gson().toJson(temp));
			}
			
			reMap.put("friendList", friendList);
			System.out.println(friendList);
			flag = true;
		}
		reMap.put("flag", flag);
		return new Gson().toJson(reMap);
	}

	@Override
	public String register(Map map) {
		String account = new Integer((int) (Math.random() * 900000000) + 100000000).toString();
		map.put("account", account);
		Map reMap = new HashMap();
		try {
			while (!userDao.addUser(map)) {
				account = new Integer((int) (Math.random() * 900000000) + 100000000).toString();
			}
			reMap.put("account", account);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return new Gson().toJson(reMap);
	}

	@Override
	public String getUserInfo(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFriends(Map map) {
		String account = (String) map.get("account");
		List<Map<String, String>> friendList = userDao.getFriends(account);
		return new Gson().toJson(friendList);
	}

	@Override
	public String signOut(Map map) {
		String account = (String) map.get("account");
		Map<String, String> reMap = new HashMap<String, String>();
		Boolean flag = userDao.updateOnline(account, 0);
		if (flag) {
			List<Map<String, String>> friendList = userDao.getFriends(account);
			Map<String, String> temp = null;
			for (Map<String, String> friend : friendList) {
				temp = new HashMap<String, String>();
				temp.put("msgType", "friendOutline");
				temp.put("outlineAccount", account);
				PublisherServer.publishMsg(friend.get("account"), new Gson().toJson(temp));
			}
		}
		reMap.put("flag", flag.toString());
		return new Gson().toJson(reMap);
	}

	@Override
	public String searchUsers(Map map) {
		String account = (String) map.get("account");
		Map<String, Object> reMap = new HashMap<>();
		
		List<Map<String, String>> usersList = userDao.getUsers(account);
		reMap.put("usersList", usersList);
		
		return new Gson().toJson(reMap);
	}

	@Override
	public String addFriend(Map map) {
		String fromAccount = (String) map.get("fromAccount");
		String toAccount = (String) map.get("toAccount");
		Map<String, Object> reMap = new HashMap<>();
		int status = friendDao.getFriendByAccount(fromAccount, toAccount);
		if (status == -1) {
			Map fromUser = userDao.getUserByAccount(fromAccount);
			int fromId = (int) fromUser.get("id");
			Map toUser = userDao.getUserByAccount(toAccount);
			int toId = (int) toUser.get("id");
			boolean flag = friendDao.addFriend(fromId, toId);
			if (flag) {
				reMap.put("flag", true);
			}else {
				reMap.put("flag", false);
			}
		}else {
			reMap.put("flag", false);
			switch (status) {
			case 0:
				reMap.put("promptMsg", "alreadySendApply");
				break;
			case 1:
				reMap.put("promptMsg", "alreadyFriend");
				break;
			}
		}
		
		return new Gson().toJson(reMap);
	}

	@Override
	public String applyList(Map map) {
		Map userMap = userDao.getUserByAccount((String) map.get("account"));
		int userId = (int) userMap.get("id");
		List<Map<String, String>> applyList = friendDao.getApplyList(userId);
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("applyList", applyList);
		return new Gson().toJson(reMap);
	}

	@Override
	public String disAgree(Map map) {
		String fromAccount = (String) map.get("fromAccount");
		String toAccount = (String) map.get("toAccount");
		int fromId = (int) userDao.getUserByAccount(fromAccount).get("id");
		int toId = (int) userDao.getUserByAccount(toAccount).get("id");
		
		boolean flag = friendDao.deleteFriend(fromId, toId);
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("flag", flag);
		return new Gson().toJson(reMap);
	}

	@Override
	public String agree(Map map) {
		String fromAccount = (String) map.get("fromAccount");
		String toAccount = (String) map.get("toAccount");
		int fromId = (int) userDao.getUserByAccount(fromAccount).get("id");
		int toId = (int) userDao.getUserByAccount(toAccount).get("id");
		System.out.println(fromId + "#" + toId);  //TODO
		boolean flag = friendDao.updateFriendStatus(fromId, toId, 1);
		if (flag) {
			String toUsername = (String) map.get("toUsername");
			Map<String, String> tempMap = new HashMap<>();
			tempMap.put("msgType", "agreeFriend");
			tempMap.put("agreeAccount", toAccount);
			tempMap.put("agreeUsername", toUsername);
			tempMap.put("online", "1");
			PublisherServer.publishMsg(fromAccount, new Gson().toJson(tempMap));
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("flag", flag);
		return new Gson().toJson(reMap);
	}
}
