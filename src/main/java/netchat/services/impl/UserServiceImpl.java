package netchat.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import netchat.dao.UserDao;
import netchat.dao.impl.UserDaoImpl;
import netchat.netchatserver.PublisherServer;
import netchat.services.UserService;

public class UserServiceImpl implements UserService {
	private UserDao userDao = new UserDaoImpl();

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
}
