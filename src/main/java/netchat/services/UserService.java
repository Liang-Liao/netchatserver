package netchat.services;

import java.util.Map;

public interface UserService {
	public String login(Map map);
	public String register(Map map);
	public String getUserInfo(Map map);
	public String getFriends(Map map);
	public String signOut(Map map);
	public String searchUsers(Map map);
	public String addFriend(Map map);
	public String applyList(Map map);
	public String disAgree(Map map);
	public String agree(Map map);
}
