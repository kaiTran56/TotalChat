package com.tranquyet.dictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @Encryption
 *  
 * @Purpose: create the session which contain the information relate the connection of client 
 * and the session about the data when they send each other 
 */

public class Encryption {
	/*
	 * @Regex:[^<>]*[<>]
	 * 
	 * @Description: negate the symbol <> and repeat {0,n}
	 * 
	 */
	private static String messageInstance;
	
	private static Pattern checkMessage = Pattern.compile("[^<>]*[<>]");

	/*
	 * @getCreateAccount
	 * 
	 * @Description: create a session to order create account with a form
	 * <HELLO><PEER_NAME> with all account
	 */
	public static String getCreateAccount(String name, String port) {
		String message = Dictionary.SESSION_OPEN + Dictionary.PEER_NAME_OPEN + name + Dictionary.PEER_NAME_CLOSE
				+ Dictionary.PORT_OPEN + port + Dictionary.PORT_CLOSE + Dictionary.SESSION_CLOSE;

		return message;
	}

	/*
	 * @sendRequest
	 * 
	 * @Purpose: create a session message which describe status of Account on the
	 * System [Live or Die]
	 * 
	 * @Structure:
	 * hello_wrold<PEER_NAME>Kai</PEER_NAME><STATUS>Running........</STATUS>
	 * hello_world!
	 * 
	 */
	public static String sendRequest(String name) {
		String message = Dictionary.SESSION_KEEP_ALIVE_OPEN + Dictionary.PEER_NAME_OPEN + name
				+ Dictionary.PEER_NAME_CLOSE + Dictionary.STATUS_OPEN + Dictionary.SERVER_ONLINE
				+ Dictionary.STATUS_CLOSE + Dictionary.SESSION_KEEP_ALIVE_CLOSE;

		return message;
	}

	/*
	 * @sendMessage
	 * 
	 * @Description: create a session which contain the content of message which
	 * will be sent to the peer or everyone on system
	 * 
	 * @Struture: <CHAT_MSG> + message + </CHAT_MSG>
	 */
	public static String sendMessage(String message) {

		Matcher findMessage = checkMessage.matcher(message);
		String result = "";
		while (findMessage.find()) {
			/*
			 * @subMessage: capture the matched subsequence of message which was created by
			 * user
			 * 
			 * @group(): return the message after check by find()
			 * 
			 * @EX: subMessage: <
			 */
			String subMessage = findMessage.group(0);

			int begin = subMessage.length();

			result += subMessage;
			subMessage = message.substring(begin, message.length());
			message = subMessage;
			findMessage = checkMessage.matcher(message);

		}
		result += message;
		setMessageInstance(result);
		
		String messageTemp = Dictionary.CHAT_MSG_OPEN + result + Dictionary.CHAT_MSG_CLOSE;

		return messageTemp;
	}

	public static String sendRequestChat(String name) {
		String message = Dictionary.CHAT_REQ_OPEN + Dictionary.PEER_NAME_OPEN + name + Dictionary.PEER_NAME_CLOSE
				+ Dictionary.CHAT_REQ_CLOSE;

		return message;
	}
	
	public static String getMessageInstance() {
		return messageInstance;
	}

	public static void setMessageInstance(String messageInstance) {
		Encryption.messageInstance = messageInstance;
	}

	/*
	 * @exit:
	 * 
	 * @Description: create a session message to notify that user out
	 * 
	 * @Structure: hello_wrold<PEER_NAME>+ name
	 * +</PEER_NAME><STATUS>.......Stop!</STATUS>hello_world!
	 */
	public static String exit(String name) {
		String message = Dictionary.SESSION_KEEP_ALIVE_OPEN + Dictionary.PEER_NAME_OPEN + name
				+ Dictionary.PEER_NAME_CLOSE + Dictionary.STATUS_OPEN + Dictionary.SERVER_OFFLINE
				+ Dictionary.STATUS_CLOSE + Dictionary.SESSION_KEEP_ALIVE_CLOSE;
		return message;
	}
}
