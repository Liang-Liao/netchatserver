package netchat.services;

import java.util.Map;

public interface UserService {
	public String login(Map map);
	public String register(Map map);
	public String getUserInfo(Map map);
	public String getFriends(Map map);
	public String signOut(Map map);
}
