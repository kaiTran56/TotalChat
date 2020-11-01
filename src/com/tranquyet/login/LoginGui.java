package com.tranquyet.login;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.tranquyet.client.FriendListGui;
import com.tranquyet.dictionary.Decryption;
import com.tranquyet.dictionary.Dictionary;
import com.tranquyet.dictionary.Encryption;

public class LoginGui {

	private JFrame frameLogin;
	private JTextField txtPort;
	private JLabel lblError;
	private String name;
	private String IP;
	private JTextField txtIP;
	private JTextField txtUsername;
	private JButton btnLogin;
	/*
	 * @checkName
	 * 
	 * @Desctiption:
	 * 
	 */
	private Pattern checkName = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");

	public static void main(String[] args) {
		Decryption.createrBy();
		EventQueue.invokeLater(() -> {

			try {
				LoginGui window = new LoginGui();
				window.frameLogin.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	public LoginGui() {
		initialize();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frameLogin = new JFrame();
		frameLogin.getContentPane().setBackground(SystemColor.scrollbar);
		frameLogin.setTitle("Login Form");
		frameLogin.setResizable(false);
		frameLogin.setBounds(100, 100, 279, 302);
		frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameLogin.getContentPane().setLayout(null);

		JLabel lblWelcome = new JLabel("Login To Server");
		lblWelcome.setForeground(SystemColor.textText);
		lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblWelcome.setBounds(66, 0, 150, 48);
		frameLogin.getContentPane().add(lblWelcome);

		JLabel lblHostServer = new JLabel("IP Server:");
		lblHostServer.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lblHostServer.setBounds(10, 97, 86, 20);
		frameLogin.getContentPane().add(lblHostServer);

		JLabel lblPortServer = new JLabel("Port Server:");
		lblPortServer.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lblPortServer.setBounds(10, 66, 86, 14);
		frameLogin.getContentPane().add(lblPortServer);

		txtPort = new JTextField();
		txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtPort.setText("8080");
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setBounds(103, 60, 65, 28);
		frameLogin.getContentPane().add(txtPort);

		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lblUserName.setBounds(10, 128, 86, 38);
		frameLogin.getContentPane().add(lblUserName);

		lblError = new JLabel("");
		lblError.setBounds(10, 242, 238, 20);
		frameLogin.getContentPane().add(lblError);

		txtIP = new JTextField();
		txtIP.setEditable(false);
		txtIP.setBounds(102, 94, 134, 28);
		frameLogin.getContentPane().add(txtIP);
		txtIP.setColumns(10);
		try {
			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtUsername.setColumns(10);
		txtUsername.setBounds(103, 132, 133, 30);
		frameLogin.getContentPane().add(txtUsername);

		btnLogin = new JButton("Sign in");
		btnLogin.setForeground(SystemColor.windowText);
		btnLogin.setBackground(SystemColor.menu);
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));

		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				name = txtUsername.getText();
				lblError.setVisible(false);
				IP = txtIP.getText();

				if (checkName.matcher(name).matches() && !IP.equals("")) {
					try {
						/*
						 * @desc: automatically create the port of client 
						 * 
						 */
						Random rd = new Random();
						int portPeer = 10000 + rd.nextInt() % 1000;
						InetAddress ipServer = InetAddress.getByName(IP);
						int portServer = Integer.parseInt("8080");
						Socket socketClient = new Socket(ipServer, portServer);

						String message = Encryption.getCreateAccount(name, Integer.toString(portPeer));
						ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
						serverOutputStream.writeObject(message);
						serverOutputStream.flush();
						ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
						message = (String) serverInputStream.readObject();

						socketClient.close();
						if (message.equals(Dictionary.SESSION_DENY)) {
							lblError.setText("This name is Existed, please try again!");
							lblError.setVisible(true);
							return;
						}
						new FriendListGui(IP, portPeer, name, message);
						frameLogin.dispose();
					} catch (Exception e) {
						lblError.setText("Please turn on the Server....!");
						lblError.setVisible(true);
						e.printStackTrace();
					}
				} else {
					lblError.setText("Your name is Invalid!");
					lblError.setVisible(true);
					lblError.setText("Your name is Invalid!");
				}
			}
		});

		btnLogin.setBounds(79, 189, 88, 28);
		frameLogin.getContentPane().add(btnLogin);
		lblError.setVisible(false);

	}
}