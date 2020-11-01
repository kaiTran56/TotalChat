package com.tranquyet.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import com.tranquyet.data.User;
import com.tranquyet.dictionary.Decryption;
import com.tranquyet.dictionary.Dictionary;
import com.tranquyet.dictionary.Encryption;

public class Client {

	public static ArrayList<User> userList;
	private ClientServer server;
	private InetAddress IPserver;
	private int portServer = 8080;
	private String nameUser;

	private static int portClient = 10000;
	private int timeOut = 10000;
	private Socket socketClient;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;

	private boolean isStop = false;

	public Client(String arg, int arg1, String name, String dataUser) throws Exception {
		IPserver = InetAddress.getByName(arg);
		nameUser = name;
		portClient = arg1;
		userList = Decryption.getAllUser(dataUser);
		new Thread(() -> {
			updateFriend();

		}).start();
		server = new ClientServer(nameUser);
		(new Request()).start();
	}

	public static int getPort() {
		return portClient;
	}

	public void request() throws Exception {
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String message = Encryption.sendRequest(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(message);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		message = (String) serverInputStream.readObject();
		serverInputStream.close();
		userList = Decryption.getAllUser(message);
		new Thread(() -> {

			updateFriend();

		}).start();
	}

	public class Request extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					Thread.sleep(timeOut);
					request();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void intialNewChat(String IP, int host, String guest) throws Exception {
		final Socket connclient = new Socket(InetAddress.getByName(IP), host);
		ObjectOutputStream sendrequestChat = new ObjectOutputStream(connclient.getOutputStream());
		sendrequestChat.writeObject(Encryption.sendRequestChat(nameUser));
		sendrequestChat.flush();
		ObjectInputStream receivedChat = new ObjectInputStream(connclient.getInputStream());
		String message = (String) receivedChat.readObject();
		if (message.equals(Dictionary.CHAT_DENY)) {
			FriendListGui.request("Your friend denied connect with you!", false);
			connclient.close();
			return;
		}
		new ChatUserGui(nameUser, guest, connclient, portClient);

	}

	public void exit() throws IOException, ClassNotFoundException {
		isStop = true;
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String message = Encryption.exit(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(message);
		serverOutputStream.flush();
		serverOutputStream.close();
		server.exit();
	}

	public void updateFriend() {

		FriendListGui.resetList();
		userList.stream().forEach(p -> {
			if (!p.getName().equals(nameUser)) {
				FriendListGui.updateFriendFriendTable(p.getName());
			}
		});
	}
}