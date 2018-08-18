package netchat.netchatserver;

import java.util.Map;

import com.google.gson.Gson;

import netchat.services.ChatService;
import netchat.services.UserService;
import netchat.services.impl.ChatServiceImpl;
import netchat.services.impl.UserServiceImpl;

public class RequestMapping {
	private static UserService userService = new UserServiceImpl();
	private static ChatService chatService = new ChatServiceImpl();

	public static String mapping(String data) {
		Map map = new Gson().fromJson(data, Map.class);
		String cmd = (String) map.get("cmd");
		String rep = null;
		switch (cmd) {
		case "login":
			rep = userService.login(map);
			break;
		case "register":
			rep = userService.register(map);
			break;
		case "signout":
			rep = userService.signOut(map);
			break;
		//聊天
		case "personalChat":
			rep = chatService.personalChat(map);
			break;
		case "groupChat":
			rep = chatService.groupChat(map);
			break;
			
		//添加好友
		case "searchUsers":
			rep = userService.searchUsers(map);
			break;
		case "addFriend":
			rep = userService.addFriend(map);
			break;
		case "applyList":
			rep = userService.applyList(map);
			break;
		case "disAgree":
			rep = userService.disAgree(map);
			break;
		case "agree":
			rep = userService.agree(map);
			break;
		}
		
		return rep;
	}
}
