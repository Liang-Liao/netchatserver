package netchat.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import netchat.services.UserService;

public class UserServiceImpl implements UserService {
	private Gson gson = new Gson();
	
	@Override
	public String login(String data) {
		Map<String, String> map = gson.fromJson(data, Map.class);
		Map<String, String> reMap = new HashMap<String, String>();
		String account = (String) map.get("account");
		String password = (String) map.get("password");
		boolean flag = false;
		//读取数据库，验证账号密码
		if ("liaoliang".equals(account) && "123456".equals(password)) {
			reMap.put("username", "liaoliang");
			flag = true;
		}
		reMap.put("flag", ((Boolean)flag).toString());
		return gson.toJson(reMap);
	}

	@Override
	public String register(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserInfo(String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
