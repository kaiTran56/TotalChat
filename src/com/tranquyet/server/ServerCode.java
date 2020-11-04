package com.tranquyet.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.tranquyet.data.User;
import com.tranquyet.dictionary.Decryption;
import com.tranquyet.dictionary.Dictionary;

public class ServerCode {

	private ArrayList<User> userData = null;
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream obOutputClient;
	private ObjectInputStream obInputStream;
	private boolean isStop = false;
	private boolean isExit = false;
	private String msg;

	public ArrayList<User> getUserData() {
		return userData;
	}

	public void setUserData(ArrayList<User> userData) {
		this.userData = userData;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	public ServerCode(int port) throws Exception {
		server = new ServerSocket(port);
		userData = new ArrayList<User>();
		(new WaitForConnect()).start();
	}

	/*
	 * @SESSION: name1 + ip1 + port1 + name2 + ip2 + port2 + .....
	 * 
	 * @Description: session will comprise the infromation of all client on the
	 * system with: name + ip + port
	 * 
	 * @EX_01:
	 * <SESSION_ACCEPT><PEER><PEER_NAME>Kim</PEER_NAME><IP>/192.168.0.106</IP><PORT>
	 * 9210</PORT></PEER><PEER><PEER_NAME>Kai</PEER_NAME><IP>/192.168.0.106</IP><
	 * PORT>9034</PORT></PEER></SESSION_ACCEPT>
	 * 
	 * @EX_02:
	 * <SESSION_ACCEPT><PEER><PEER_NAME>Kim</PEER_NAME><IP>/192.168.0.106</IP><PORT>
	 * 9210</PORT></PEER><PEER><PEER_NAME>Kai</PEER_NAME><IP>/192.168.0.106</IP><
	 * PORT>9034</PORT></PEER><PEER><PEER_NAME>Kai1</PEER_NAME><IP>/192.168.0.106</
	 * IP><PORT>10180</PORT></PEER></SESSION_ACCEPT>
	 * 
	 */
	private String sendSessionAccept() throws Exception {

		msg = Dictionary.SESSION_ACCEPT_OPEN;
		this.userData.stream().forEach(p -> {
			msg += Dictionary.PEER_OPEN;
			msg += Dictionary.PEER_NAME_OPEN;
			msg += p.getName();
			msg += Dictionary.PEER_NAME_CLOSE;
			msg += Dictionary.IP_OPEN;
			msg += p.getHost();
			msg += Dictionary.IP_CLOSE;
			msg += Dictionary.PORT_OPEN;
			msg += p.getPort();
			msg += Dictionary.PORT_CLOSE;
			msg += Dictionary.PEER_CLOSE;
		});
		msg += Dictionary.SESSION_ACCEPT_CLOSE;
		System.out.println("Message of Session: " + msg);

		return msg;

	}

	public void stopserver() throws Exception {
		isStop = true;
		server.close();
		connection.close();
	}

	private boolean connectToServer() throws Exception {
		connection = server.accept();
		obInputStream = new ObjectInputStream(connection.getInputStream());

		/*
		 * @msg : message of session include name + port / name + status <running.... or
		 * .....Stop>
		 * 
		 * @EX_01: hello_KAi<PEER_NAME>Kim</PEER_NAME><PORT>9210</PORT>hello_KAi_Quyet
		 * 
		 * @EX_02 :
		 * hello_wrold<PEER_NAME>Kim</PEER_NAME><STATUS>Running........</STATUS>
		 * hello_world!
		 */

		String msg = (String) obInputStream.readObject();
		System.out.println("MSG: " + msg);

		/*
		 * @getData: use to store the instance of user include: name + port
		 * 
		 * @EX: [Kai, 9034]
		 * 
		 * @Note: just in 1 time
		 */

		ArrayList<String> getData = Decryption.getUser(msg);
		System.out.println("getData ArrayList: " + getData);

		ServerChatGui.updateMessage(msg);
		if (getData != null) {
			if (!isExsistName(getData.get(0))) {
				saveNewPeer(getData.get(0), connection.getInetAddress().toString(), Integer.parseInt(getData.get(1)));
				ServerChatGui.updateMessage(getData.get(0));
				ServerChatGui.updateNumber();
			} else
				return false;
		} else {
			int size = userData.size();

			Decryption.updatePeerOnline(userData, msg);
			if (size != userData.size()) {
				isExit = true;
				ServerChatGui.decreaseNumber();
			}
		}
		return true;
	}

	private void saveNewPeer(String user, String ip, int port) throws Exception {
		User newPeer = new User();
		if (userData.size() == 0)
			userData = new ArrayList<User>();
		newPeer.setUser(user, ip, port);
		userData.add(newPeer);

		System.out.println("<userData ArrayList> : " + userData.toString());
	}

	private boolean isExsistName(String name) throws Exception {
		if (userData == null)
			return false;
		int size = userData.size();
		for (int i = 0; i < size; i++) {
			User peer = userData.get(i);
			if (peer.getName().equals(name))
				return true;
		}
		return false;
	}

	public class WaitForConnect extends Thread {

		public void run() {
			super.run();
			try {
				while (!isStop) {
					if (connectToServer()) {
						if (isExit) {
							isExit = false;
						} else {
							obOutputClient = new ObjectOutputStream(connection.getOutputStream());
							obOutputClient.writeObject(sendSessionAccept());
							obOutputClient.flush();
							obOutputClient.close();
						}
					} else {
						obOutputClient = new ObjectOutputStream(connection.getOutputStream());
						obOutputClient.writeObject(Dictionary.SESSION_DENY);
						obOutputClient.flush();
						obOutputClient.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
