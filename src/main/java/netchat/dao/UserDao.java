package netchat.dao;

import java.util.List;
import java.util.Map;

public interface UserDao {
	Map getUserByAccount(String account);
	Boolean updateOnline(String account, int online);
	Boolean addUser(Map userMap);
	List<Map<String, String>> getFriends(String account);
}
