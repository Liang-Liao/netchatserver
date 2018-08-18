package netchat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netchat.dao.FriendDao;
import netchat.netchatserver.utils.JDBCUtils;

public class FriendDaoImpl implements FriendDao {

	@Override
	public int getFriendByAccount(String fromAccount, String toAccount) {
		String sql = "select * from friend f "
				+ "where f.u_id1=(select u1.id from user u1 where u1.account=?) "
				+ "and f.u_id2=(select u2.id from user u2 where u2.account=?) "
				+ "union " + 
				"select * from friend f "
				+ "where f.u_id2=(select u1.id from user u1 where u1.account=?) "
				+ "and f.u_id1=(select u2.id from user u2 where u2.account=?);";
		List paramList = new ArrayList();
		paramList.add(fromAccount);
		paramList.add(toAccount);
		paramList.add(fromAccount);
		paramList.add(toAccount);

		JDBCUtils.queryData(sql, paramList);

		ResultSet rs = JDBCUtils.rs;
		int status = -1;
		try {
			if (rs.next()) {
				status = rs.getInt("status");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				JDBCUtils.closeAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return status;
	}

	@Override
	public boolean addFriend(int fromId, int toId) {
		String sql = "insert into friend(u_id1, u_id2, status) values(?,?,?)";
		List paramList = new ArrayList();
		paramList.add(fromId);
		paramList.add(toId);
		paramList.add(0);

		int res = JDBCUtils.updateData(sql, paramList);
		try {
			JDBCUtils.closeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (res > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, String>> getApplyList(int userId) {
		String sql = "select * from user where id in"
				+ "(select f.u_id1 from friend f where f.u_id2=? and f.status=0)";
		List paramList = new ArrayList();
		paramList.add(userId);

		JDBCUtils.queryData(sql, paramList);

		List list = new ArrayList();
		ResultSet rs = JDBCUtils.rs;
		try {
			while (rs.next()) {
				Map map = new HashMap();
				map.put("id", rs.getInt("id"));
				map.put("username", rs.getString("username"));
				map.put("account", rs.getString("account"));
				map.put("online", Integer.toString(rs.getInt("online")));
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				JDBCUtils.closeAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public boolean deleteFriend(int fromId, int toId) {
		String sql = "delete from friend where u_id1=? and u_id2=?";
		List paramList = new ArrayList();
		paramList.add(fromId);
		paramList.add(toId);

		int res = JDBCUtils.updateData(sql, paramList);
		try {
			JDBCUtils.closeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (res > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateFriendStatus(int fromId, int toId, int i) {
		String sql = "update friend set status=? where u_id1=? and u_id2=?";
		List paramList = new ArrayList();
		paramList.add(i);
		paramList.add(fromId);
		paramList.add(toId);

		int res = JDBCUtils.updateData(sql, paramList);
		try {
			JDBCUtils.closeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (res > 0) {
			return true;
		}
		return false;
	}

}
