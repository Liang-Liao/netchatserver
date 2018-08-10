package netchat.netchatserver;

import java.util.Map;

import com.google.gson.Gson;

import netchat.services.UserService;
import netchat.services.impl.UserServiceImpl;

public class RequestMapping {
	private static UserService userService = new UserServiceImpl();

	public static String mapping(String data) {
		String cmd = (String) new Gson().fromJson(data, Map.class).get("cmd");
		String rep = null;
		switch (cmd) {
		case "login":
			rep = userService.login(data);
			break;
		}
		return rep;
	}
}
