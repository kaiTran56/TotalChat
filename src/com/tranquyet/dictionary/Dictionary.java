package com.tranquyet.dictionary;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Dictionary {

	public final static int IN_VALID = -1;


	public final static String SESSION_OPEN = "<HELLO>";
	public final static String SESSION_CLOSE = "<BYE>";
	public final static String PEER_NAME_OPEN = "<PEER_NAME>";
	public final static String PEER_NAME_CLOSE = "</PEER_NAME>";
	public final static String PORT_OPEN = "<PORT>";
	public final static String PORT_CLOSE = "</PORT>";
	public final static String SESSION_KEEP_ALIVE_OPEN = "hello_wrold";
	public final static String SESSION_KEEP_ALIVE_CLOSE = "hello_world!";
	public final static String STATUS_OPEN = "<STATUS>";
	public final static String STATUS_CLOSE = "</STATUS>";
	public final static String SESSION_DENY = "<SESSION_DENY />";
	public final static String SESSION_ACCEPT_OPEN = "<SESSION_ACCEPT>";
	public final static String SESSION_ACCEPT_CLOSE = "</SESSION_ACCEPT>";
	public final static String CHAT_REQ_OPEN = "<CHAT_REQ>";
	public final static String CHAT_REQ_CLOSE = "</CHAT_REQ>";
	public final static String IP_OPEN = "<IP>";
	public final static String IP_CLOSE = "</IP>";
	public final static String CHAT_DENY = "<CHAT_DENY/>";
	public final static String CHAT_ACCEPT = "<CHAT_ACCEPT />";
	public final static String CHAT_MSG_OPEN = "<CHAT_MSG>";
	public final static String CHAT_MSG_CLOSE = "</CHAT_MSG>";
	public final static String PEER_OPEN = "<PEER>";
	public final static String PEER_CLOSE = "</PEER>";
	public final static String CHAT_CLOSE = "<CHAT_CLOSE />";

	public final static String SERVER_ONLINE = "Running........";
	public final static String SERVER_OFFLINE = ".......Stop!";

	public static int show(JFrame frame, String msg, boolean type) {
		if (type)
			return JOptionPane.showConfirmDialog(frame, msg, null, JOptionPane.YES_NO_OPTION);
		JOptionPane.showMessageDialog(frame, msg);
		return IN_VALID;
	}
}
