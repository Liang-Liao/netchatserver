package netchat.services;

import netchat.entities.User;

public interface UserService {
	public String login(String data);
	public String register(String data);
	public String getUserInfo(String data);
}
