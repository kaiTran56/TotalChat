package com.tranquyet.dictionary;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tranquyet.data.User;

public class Decryption {

	private static Pattern createAccount = Pattern
			.compile(Dictionary.SESSION_OPEN + Dictionary.PEER_NAME_OPEN + ".*" + Dictionary.PEER_NAME_CLOSE
					+ Dictionary.PORT_OPEN + ".*" + Dictionary.PORT_CLOSE + Dictionary.SESSION_CLOSE);

	private static Pattern users = Pattern.compile(Dictionary.SESSION_ACCEPT_OPEN + "(" + Dictionary.PEER_OPEN
			+ Dictionary.PEER_NAME_OPEN + ".+" + Dictionary.PEER_NAME_CLOSE + Dictionary.IP_OPEN + ".+"
			+ Dictionary.IP_CLOSE + Dictionary.PORT_OPEN + "[0-9]+" + Dictionary.PORT_CLOSE + Dictionary.PEER_CLOSE
			+ ")*" + Dictionary.SESSION_ACCEPT_CLOSE);

	private static Pattern request = Pattern.compile(Dictionary.SESSION_KEEP_ALIVE_OPEN + Dictionary.PEER_NAME_OPEN
			+ "[^<>]+" + Dictionary.PEER_NAME_CLOSE + Dictionary.STATUS_OPEN + "(" + Dictionary.SERVER_ONLINE + "|"
			+ Dictionary.SERVER_OFFLINE + ")" + Dictionary.STATUS_CLOSE + Dictionary.SESSION_KEEP_ALIVE_CLOSE);

	private static Pattern message = Pattern.compile(Dictionary.CHAT_MSG_OPEN + ".*" + Dictionary.CHAT_MSG_CLOSE);

	public static ArrayList<String> getUser(String msg) {
		System.out.println("[----GETUSER()]: ");
		ArrayList<String> user = new ArrayList<String>();

		System.out.println("[----Message]: " + msg);

		if (createAccount.matcher(msg).matches()) {

			Pattern findName = Pattern.compile(Dictionary.PEER_NAME_OPEN + ".*" + Dictionary.PEER_NAME_CLOSE);
			Pattern findPort = Pattern.compile(Dictionary.PORT_OPEN + "[0-9]*" + Dictionary.PORT_CLOSE);
			Matcher find = findName.matcher(msg);

			if (find.find()) {

				String name = find.group(0);
				System.out.println("[----NameUser]: " + name);
				user.add(name.substring(11, name.length() - 12));
				find = findPort.matcher(msg);

				if (find.find()) {
					String port = find.group(0);
					user.add(port.substring(6, port.length() - 7));

					System.out.println("[----User Port]: " + port);
				} else
					return null;
			} else
				return null;
		} else
			return null;
		System.out.println("[--------User ArrayList]: " + user.toString());
		return user;
	}

	/*
	 * @getAllUser:
	 * 
	 * @Description: convert the session which contain the information of user to
	 * ArrayList<User>
	 * 
	 * @Ex:
	 * <SESSION_ACCEPT><PEER><PEER_NAME>Kim</PEER_NAME><IP>/192.168.0.106</IP><PORT>
	 * 10234</PORT></PEER><PEER><PEER_NAME>Kai</PEER_NAME><IP>/192.168.0.106</IP><
	 * PORT>10180</PORT></PEER></SESSION_ACCEPT>
	 * 
	 * ---> [USER OBJECT: Kim192.168.0.10610234, USER OBJECT: Kai192.168.0.10610180]
	 * 
	 */

	public static ArrayList<User> getAllUser(String msg) {

		ArrayList<User> user = new ArrayList<User>();

		Pattern findPeer = Pattern.compile(Dictionary.PEER_OPEN + Dictionary.PEER_NAME_OPEN + "[^<>]*"
				+ Dictionary.PEER_NAME_CLOSE + Dictionary.IP_OPEN + "[^<>]*" + Dictionary.IP_CLOSE
				+ Dictionary.PORT_OPEN + "[0-9]*" + Dictionary.PORT_CLOSE + Dictionary.PEER_CLOSE);
		Pattern findName = Pattern.compile(Dictionary.PEER_NAME_OPEN + ".*" + Dictionary.PEER_NAME_CLOSE);
		Pattern findPort = Pattern.compile(Dictionary.PORT_OPEN + "[0-9]*" + Dictionary.PORT_CLOSE);
		Pattern findIP = Pattern.compile(Dictionary.IP_OPEN + ".+" + Dictionary.IP_CLOSE);

		if (users.matcher(msg).matches()) {
			Matcher find = findPeer.matcher(msg);
			while (find.find()) {
				String peer = find.group(0);
				System.out.println("[-----Peer:] " + peer);
				String data = "";
				User dataPeer = new User();
				Matcher findInfo = findName.matcher(peer);

				/*
				 * @purpose: create the peer object: name + ip + port
				 */
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setName(data.substring(11, data.length() - 12));
				}
				findInfo = findIP.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setHost(findInfo.group(0).substring(5, data.length() - 5));
				}
				findInfo = findPort.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setPort(Integer.parseInt(data.substring(6, data.length() - 7)));
				}

				/*
				 * 
				 */
				user.add(dataPeer);
			}
		} else
			return null;
		return user;
	}

	public static ArrayList<User> updatePeerOnline(ArrayList<User> peerList, String msg) {
		System.out.println("[----UpdatePeerOnline]: ");
		Pattern alive = Pattern.compile(Dictionary.STATUS_OPEN + Dictionary.SERVER_ONLINE + Dictionary.STATUS_CLOSE);
		Pattern killUser = Pattern.compile(Dictionary.PEER_NAME_OPEN + "[^<>]*" + Dictionary.PEER_NAME_CLOSE);

		if (request.matcher(msg).matches()) {
			Matcher findState = alive.matcher(msg);

			if (findState.find())
				return peerList;

			System.out.println("[----peerList_02]: " + peerList.toString());

			findState = killUser.matcher(msg);
			if (findState.find()) {
				String findPeer = findState.group(0);
				int size = peerList.size();
				String name = findPeer.substring(11, findPeer.length() - 12);
				for (int i = 0; i < size; i++)
					if (name.equals(peerList.get(i).getName())) {
						peerList.remove(i);
						break;
					}
			}
		}
		System.out.println("[----peerList_02]: " + peerList.toString());
		return peerList;
	}

	/*
	 * @getMessage:
	 * 
	 * @Description: tranfer the session become to the raw data (message)
	 * 
	 * @EX: msg session -> message (<CHAT_MSG>heeeehhehehehehe</CHAT_MSG> ->
	 * heeeehhehehehehe)
	 */
	public static String getMessage(String msg) {

		if (message.matcher(msg).matches()) {
			int begin = Dictionary.CHAT_MSG_OPEN.length();
			int end = msg.length() - Dictionary.CHAT_MSG_CLOSE.length();
			String message = msg.substring(begin, end);
			return message;
		}
		return null;
	}

	public static String getNameRequestChat(String msg) {
		System.out.println("--------[getNameRequestChat()]: " + msg);
		Pattern checkRequest = Pattern.compile(Dictionary.CHAT_REQ_OPEN + Dictionary.PEER_NAME_OPEN + "[^<>]*"
				+ Dictionary.PEER_NAME_CLOSE + Dictionary.CHAT_REQ_CLOSE);
		if (checkRequest.matcher(msg).matches()) {
			int lenght = msg.length();
			int start = (Dictionary.CHAT_REQ_OPEN + Dictionary.PEER_NAME_OPEN).length();

			int end = lenght - (Dictionary.PEER_NAME_CLOSE + Dictionary.CHAT_REQ_CLOSE).length();

			String name = msg.substring(start, end);

			System.out.println("--------[Name]: " + name);
			return name;
		}
		return null;
	}

	public static void createrBy() {

		StringBuilder creater = new StringBuilder();

		creater.append("This is My Project. My name is Quyet and I am Creator of This SoftWare!")
				.append("\nThank you for your attention!");

		System.out.println(creater.toString());

	}
}
