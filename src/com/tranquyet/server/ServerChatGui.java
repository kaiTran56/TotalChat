package com.tranquyet.server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ServerChatGui {

	public static int port = 8080;
	private JFrame frameServer;
	private JTextField txtIP, txtPort;
	private JLabel lblStatus;
	private static TextArea txtMessage;
	public static JLabel lblUserOnline;
	ServerCode server;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			try {
				ServerChatGui window = new ServerChatGui();
				window.frameServer.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	public ServerChatGui() {
		initialize();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frameServer = new JFrame();
		frameServer.getContentPane().setBackground(Color.LIGHT_GRAY);
		frameServer.setForeground(UIManager.getColor("RadioButtonMenuItem.foreground"));
		frameServer.getContentPane().setFont(new Font("SimSun", Font.BOLD | Font.ITALIC, 14));
		frameServer.getContentPane()
				.setForeground(UIManager.getColor("RadioButtonMenuItem.acceleratorSelectionForeground"));
		frameServer.setTitle("ServerChat");
		frameServer.setResizable(false);
		frameServer.setBounds(200, 200, 392, 600);
		frameServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameServer.getContentPane().setLayout(null);
		frameServer.setBackground(Color.DARK_GRAY);

		JLabel lblIP = new JLabel("Ip Address:");
		lblIP.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lblIP.setBounds(140, 77, 89, 16);
		frameServer.getContentPane().add(lblIP);

		txtIP = new JTextField();
		txtIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtIP.setEditable(false);
		txtIP.setBounds(239, 72, 146, 28);
		frameServer.getContentPane().add(txtIP);
		txtIP.setColumns(10);
		try {
			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		JLabel lblNewLabel = new JLabel("Port:");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel.setBounds(280, 273, 48, 16);
		frameServer.getContentPane().add(lblNewLabel);

		txtPort = new JTextField();
		txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setBounds(338, 268, 48, 28);
		frameServer.getContentPane().add(txtPort);
		txtPort.setText("8080");

		JButton btnStart = new JButton("Start");
		btnStart.setBackground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));
		btnStart.setFont(new Font("SansSerif", Font.BOLD, 14));

		btnStart.setBounds(201, 397, 78, 33);
		frameServer.getContentPane().add(btnStart);

		JLabel lblNhom = new JLabel("Server Management");
		lblNhom.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNhom.setBounds(97, 11, 182, 52);

		frameServer.getContentPane().add(lblNhom);

		txtMessage = new TextArea();
		txtMessage.setBackground(Color.BLACK);
		txtMessage.setForeground(Color.GREEN);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 14));
		txtMessage.setEditable(false);
		txtMessage.setBounds(0, 100, 385, 162);
		frameServer.getContentPane().add(txtMessage);

		JButton btnStop = new JButton("Stop");
		btnStop.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblUserOnline.setText("0");
				try {
					server.stopserver();
					ServerChatGui.updateMessage("Stop Server!");
					lblStatus.setText("<html><font color='red'>Offline</font></html>");
				} catch (Exception e) {
					e.printStackTrace();
					ServerChatGui.updateMessage("Stop Server!");
					lblStatus.setText("<html><font color='red'>Offline</font></html>");
				}
			}
		});
		btnStop.setBounds(69, 397, 78, 33);
		frameServer.getContentPane().add(btnStop);

		JLabel lblnew111 = new JLabel("Status:");
		lblnew111.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lblnew111.setBounds(6, 326, 89, 16);
		frameServer.getContentPane().add(lblnew111);

		lblStatus = new JLabel("New label");
		lblStatus.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
		lblStatus.setBounds(115, 326, 98, 16);

		frameServer.getContentPane().add(lblStatus);
		lblStatus.setText("<html><font color='blue'>Offline</font></html>");

		JLabel lblRecord = new JLabel("LOG");
		lblRecord.setFont(new Font("Verdana", Font.BOLD, 15));
		lblRecord.setBounds(0, 77, 89, 16);
		frameServer.getContentPane().add(lblRecord);

		JLabel lbllabelUserOnline = new JLabel("Online User:");
		lbllabelUserOnline.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 14));
		lbllabelUserOnline.setBounds(6, 353, 89, 16);
		frameServer.getContentPane().add(lbllabelUserOnline);

		lblUserOnline = new JLabel("0");
		lblUserOnline.setForeground(Color.BLUE);
		lblUserOnline.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
		lblUserOnline.setBounds(115, 353, 56, 16);
		frameServer.getContentPane().add(lblUserOnline);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server = new ServerCode(port);
					ServerChatGui.updateMessage("Server Starting....!");
					lblStatus.setText("<html><font color='green'>Online</font></html>");
				} catch (Exception e) {
					ServerChatGui.updateMessage("Something wrong when Server start. Please, check again!");
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * <Functions>
	 * 
	 */

	public static void updateNumber() {
		int number = Integer.parseInt(lblUserOnline.getText());
		lblUserOnline.setText(Integer.toString(number + 1));
	}

	public static void decreaseNumber() {
		int number = Integer.parseInt(lblUserOnline.getText());
		lblUserOnline.setText(Integer.toString(number - 1));

	}

	public static void updateMessage(String message) {
		System.out.println("Message: " + message);
		txtMessage.append(message + "\n");
		if (!message.equals("Server Starting....!") && !message.equals("Stop Server!")) {
			txtMessage.append("______________________________________________" + "\n");
		} else {
			txtMessage.append(message + "\n");
		}

	}

	public static String getNumberUserOnline() {
		return lblUserOnline.getText();
	}

	/*
	 * </Functions>
	 * 
	 */

}
