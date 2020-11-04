package com.tranquyet.data;

public class User {

	private String nameUser = "";
	private String hostUser = "";
	private int portUser = 0;
	private static String message;

	public void setUser(String name, String host, int port) {
		nameUser = name;
		hostUser = host;
		portUser = port;
	}

	public User(String nameUser, String hostUser, int portUser, String message) {
		super();
		this.nameUser = nameUser;
		this.hostUser = hostUser;
		this.portUser = portUser;

	}

	public User() {
		super();
	}

	public void setName(String name) {
		nameUser = name;
	}

	public void setHost(String host) {
		hostUser = host;
	}

	public void setPort(int port) {
		portUser = port;
	}

	public String getName() {
		return nameUser;
	}

	public String getHost() {
		return hostUser;
	}

	public int getPort() {
		return portUser;
	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		User.message = message;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "USER: " + nameUser + hostUser + " : " + portUser + " : " + message;
	}
}
