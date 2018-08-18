package netchat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netchat.dao.UserDao;
import netchat.netchatserver.utils.JDBCUtils;

public class UserDaoImpl implements UserDao {

	/**
	 * 获取用户信息
	 */
	@Override
	public Map getUserByAccount(String account) {
		String sql = "select * from user where account=?";
		List paramList = new ArrayList();
		paramList.add(account);

		JDBCUtils.queryData(sql, paramList);

		Map map = new HashMap();
		ResultSet rs = JDBCUtils.rs;
		try {
			if (rs.next()) {
				map.put("id", rs.getInt("id"));
				map.put("username", rs.getString("username"));
				map.put("account", rs.getString("account"));
				map.put("password", rs.getString("password"));
				map.put("online", rs.getInt("online"));
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

		return map;
	}

	/**
	 * 修改用户在线状态
	 */
	@Override
	public Boolean updateOnline(String account, int online) {
		String sql = "update user set online=? where account=?";
		List paramList = new ArrayList();
		paramList.add(online);
		paramList.add(account);

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
	public Boolean addUser(Map userMap) {
		String sql = "insert into user(username, account, password, online) values(?,?,?,?)";
		List paramList = new ArrayList();
		paramList.add(userMap.get("username"));
		paramList.add(userMap.get("account"));
		paramList.add(userMap.get("password"));
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
	public List<Map<String, String>> getFriends(String account) {
		String sql = "select usa.username, usa.account, usa.online from user usa "
				+ "where usa.id in (select fa.u_id2 from user ua, friend fa where ua.account=? and fa.status=1 and ua.id=fa.u_id1) "
				+ "union "
				+ "select usb.username, usb.account, usb.online from user usb "
				+ "where usb.id in (select fb.u_id1 from user ub, friend fb where ub.account=? and fb.status=1 and ub.id=fb.u_id2)";
		List paramList = new ArrayList();
		paramList.add(account);
		paramList.add(account);

		JDBCUtils.queryData(sql, paramList);

		List<Map<String, String>> friendList = new ArrayList<Map<String, String>>();
		ResultSet rs = JDBCUtils.rs;
		try {
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				map.put("account", rs.getString("account"));
				map.put("online", new Integer(rs.getInt("online")).toString());
				friendList.add(map);
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
		return friendList;
	}

	@Override
	public List<Map<String, String>> getOnlineUser() {
		String sql = "select * from user where online=1";

		JDBCUtils.queryData(sql, null);

		List onlineUserList = new ArrayList();
		Map map;
		ResultSet rs = JDBCUtils.rs;
		try {
			while (rs.next()) {
				map = new HashMap();
				map.put("id", rs.getInt("id"));
				map.put("username", rs.getString("username"));
				map.put("account", rs.getString("account"));
				map.put("online", rs.getInt("online"));
				onlineUserList.add(map);
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

		return onlineUserList;
	}

	@Override
	public List<Map<String, String>> getUsers(String account) {
		String sql = "select * from user where account like ?";
		List paramList = new ArrayList();
		paramList.add("%" + account + "%");

		JDBCUtils.queryData(sql, paramList);

		List usersList = new ArrayList();
		Map map;
		ResultSet rs = JDBCUtils.rs;
		try {
			while (rs.next()) {
				map = new HashMap();
				map.put("id", rs.getInt("id"));
				map.put("username", rs.getString("username"));
				map.put("account", rs.getString("account"));
				map.put("online", rs.getInt("online"));
				usersList.add(map);
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

		return usersList;
	}
}
