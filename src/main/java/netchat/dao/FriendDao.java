package netchat.dao;

import java.util.List;
import java.util.Map;

public interface FriendDao {
	public int getFriendByAccount(String fromAccount, String toAccount);
	public boolean addFriend(int fromId, int toId);
	public List<Map<String, String>> getApplyList(int userId);
	public boolean deleteFriend(int fromId, int toId);
	public boolean updateFriendStatus(int fromId, int toId, int i);
}
