package netchat.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import netchat.netchatserver.PublisherServer;
import netchat.services.ChatService;

public class ChatServiceImpl implements ChatService{

	@Override
	public String personalChat(Map map) {
		map.remove("cmd");
		map.put("msgType", "personalChat");
		PublisherServer.publishMsg((String) map.get("toAccount"), new Gson().toJson(map));
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("flag", true);
		return new Gson().toJson(reMap);
	}

}
