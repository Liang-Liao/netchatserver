package netchat.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import netchat.dao.UserDao;
import netchat.dao.impl.UserDaoImpl;
import netchat.netchatserver.PublisherServer;
import netchat.services.ChatService;

public class ChatServiceImpl implements ChatService{
	private UserDao userDao = new UserDaoImpl();

	@Override
	public String personalChat(Map map) {
		map.remove("cmd");
		map.put("msgType", "personalChat");
		PublisherServer.publishMsg((String) map.get("toAccount"), new Gson().toJson(map));
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("flag", true);
		return new Gson().toJson(reMap);
	}

	@Override
	public String groupChat(Map map) {
		map.remove("cmd");
		map.put("msgType", "groupChat");
		List<Map<String, String>> onlineUserList = userDao.getOnlineUser();
		for (Map<String, String> user : onlineUserList) {
			if (!map.get("fromAccount").equals(user.get("account"))) {
				PublisherServer.publishMsg(user.get("account"), new Gson().toJson(map));
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("flag", true);
		return new Gson().toJson(reMap);
	}
	
	

}
