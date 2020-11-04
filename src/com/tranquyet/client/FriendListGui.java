package com.tranquyet.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.tranquyet.dictionary.Dictionary;

public class FriendListGui {

	private Client clientNode;
	private static String IPClient = "", nameUser = "", dataUser = "";
	private static int portClient = 0;
	private JFrame frameFriendTable;
	private JTextField txtNameFriend;
	private JButton btnChat, btnExit;
	private JLabel lblLogo;
	private JLabel lblActiveNow;
	private static JList<String> listActive;

	public static DefaultListModel<String> model = new DefaultListModel<>();
	private JLabel lblUsername;
	private JPanel panel;
	private JScrollPane scrollPane;
	public JTextField txtMessage;
	private static JTextPane txtDisplayChat;
	public boolean isStop = false, isSendFile = false, isReceiveFile = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			try {
				FriendListGui window = new FriendListGui();
				window.frameFriendTable.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	public FriendListGui(String arg, int arg1, String name, String message) throws Exception {
		IPClient = arg;
		portClient = arg1;
		nameUser = name;
		dataUser = message;
		EventQueue.invokeLater(() -> {

			try {
				FriendListGui window = new FriendListGui();
				window.frameFriendTable.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	public FriendListGui() throws Exception {
		initialize();
		clientNode = new Client(IPClient, portClient, nameUser, dataUser);

	}

	public static void updateFriendFriendTable(String message) {
		model.addElement(message);
	}

	public static void resetList() {
		model.clear();
	}

	/**
	 * @throws IOException
	 * @throws BadLocationException
	 * @wbp.parser.entryPoint
	 */
	private void initialize() throws BadLocationException, IOException {
		frameFriendTable = new JFrame();
		frameFriendTable.setResizable(false);
		frameFriendTable.setForeground(Color.BLACK);
		frameFriendTable.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));
		frameFriendTable.setBackground(Color.BLACK);
		frameFriendTable.getContentPane().setBackground(Color.GRAY);
		frameFriendTable.setTitle("Friend Table");
		frameFriendTable.setBounds(100, 100, 439, 459);
		frameFriendTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameFriendTable.getContentPane().setLayout(null);

		JLabel lblHello = new JLabel("Welcome: ");
		lblHello.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 15));
		lblHello.setBounds(12, 58, 76, 24);
		frameFriendTable.getContentPane().add(lblHello);

		JLabel lblFriendsName = new JLabel("Name: ");
		lblFriendsName.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblFriendsName.setBounds(297, 245, 110, 16);
		frameFriendTable.getContentPane().add(lblFriendsName);

		txtNameFriend = new JTextField("");
		txtNameFriend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtNameFriend.setColumns(10);
		txtNameFriend.setBounds(297, 272, 120, 28);
		frameFriendTable.getContentPane().add(txtNameFriend);

		btnChat = new JButton("Chat");
		btnChat.setFont(new Font("SansSerif", Font.BOLD, 15));

		btnChat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				String name = txtNameFriend.getText();
				if (name.equals("") || Client.userList == null) {
					Dictionary.show(frameFriendTable, "Invaild username", false);
					return;
				}
				if (name.equals(nameUser)) {
					Dictionary.show(frameFriendTable, "This software doesn't support chat yourself function", false);
					return;
				}

				/*
				 * @Test
				 */

				Client.userList.stream().forEach(p -> {
					if (p.getName().equals(name)) {
						try {
							clientNode.intialNewChat(p.getHost(), p.getPort(), name);
						} catch (Exception e) {
							Dictionary.show(frameFriendTable,
									"Friend is not found. Please wait to update your list friend", false);
							e.printStackTrace();
						}
					}
				});

			}
		});
		btnChat.setBounds(297, 311, 120, 44);
		frameFriendTable.getContentPane().add(btnChat);

		btnExit = new JButton("Exit");
		btnExit.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Dictionary.show(frameFriendTable, "Are you sure ?", true);
				if (result == 0) {
					try {
						clientNode.exit();
						frameFriendTable.dispose();
						System.exit(0);
					} catch (Exception e) {
						frameFriendTable.dispose();
						System.exit(0);
					}
				}
			}
		});
		btnExit.setBounds(297, 366, 120, 44);

		frameFriendTable.getContentPane().add(btnExit);

		lblLogo = new JLabel("List Friends");
		lblLogo.setForeground(Color.BLACK);

		lblLogo.setFont(new Font("Roboto Condensed", Font.BOLD, 22));
		lblLogo.setBounds(151, 11, 120, 38);
		frameFriendTable.getContentPane().add(lblLogo);

		lblActiveNow = new JLabel("Friends:");
		lblActiveNow.setForeground(Color.BLACK);
		lblActiveNow.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
		lblActiveNow.setBounds(297, 54, 98, 31);
		frameFriendTable.getContentPane().add(lblActiveNow);

		listActive = new JList<>(model);
		listActive.setForeground(Color.BLUE);
		listActive.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 15));
		listActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String value = (String) listActive.getModel().getElementAt(listActive.locationToIndex(arg0.getPoint()));
				txtNameFriend.setText(value);
			}
		});
		listActive.setBounds(297, 89, 120, 149);
		frameFriendTable.getContentPane().add(listActive);

		lblUsername = new JLabel(nameUser);
		lblUsername.setForeground(Color.GREEN);
		lblUsername.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
		lblUsername.setBounds(86, 47, 165, 49);
		frameFriendTable.getContentPane().add(lblUsername);

		panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(131, 11, 145, 38);
		frameFriendTable.getContentPane().add(panel);

		txtDisplayChat = new JTextPane();
		txtDisplayChat.setBackground(SystemColor.scrollbar);
		txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDisplayChat.setEditable(false);
		txtDisplayChat.setContentType("text/html");
		txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
		txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		txtDisplayChat.setBounds(1, 1, 478, 205);
		appendToPane(txtDisplayChat, "<div class='clear' style='background-color:black'/>");

		frameFriendTable.getContentPane().add(txtDisplayChat);

		scrollPane = new JScrollPane(txtDisplayChat);
		scrollPane.setBounds(12, 92, 275, 318);
		frameFriendTable.getContentPane().add(scrollPane);

		/*
		 * JButton btnSend = new JButton("Send"); btnSend.setFont(new Font("Segoe UI",
		 * Font.BOLD, 12)); btnSend.setBounds(224, 368, 63, 44);
		 * frameFriendTable.getContentPane().add(btnSend); btnSend.addActionListener(new
		 * ActionListener() {
		 * 
		 * public void actionPerformed(ActionEvent arg0) { String message =
		 * txtMessage.getText(); if (isStop) { try { String messageTemp =
		 * Encryption.sendMessage(nameUser + ": " + message); String messageTemp2 =
		 * Decryption.getMessage(messageTemp);
		 * 
		 * System.out.println("Encryption Message: " + messageTemp + "/n --->>" +
		 * " Decryption Message: " + messageTemp2); ; User.setMessage(message);
		 * updateChatSend(nameUser + ": " + message); } catch (BadLocationException |
		 * IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * txtMessage.setText(""); // reset text Send return; }
		 * 
		 * if (message.equals("")) return; try { String messageTemp =
		 * Encryption.sendMessage(nameUser + ": " + message); String messageTemp2 =
		 * Decryption.getMessage(messageTemp);
		 * 
		 * System.out.println( "Encryption Message: " + messageTemp + " --->>" +
		 * " Decryption Message: " + messageTemp2); User.setMessage(message);
		 * updateChatSend(nameUser + ": " + message); txtMessage.setText(""); } catch
		 * (Exception e) { e.printStackTrace(); } } });
		 */

		/*
		 * txtMessage = new JTextField(); txtMessage.setBounds(12, 366, 206, 44);
		 * frameFriendTable.getContentPane().add(txtMessage); txtMessage.setColumns(10);
		 * txtMessage.addKeyListener(new KeyListener() {
		 * 
		 * @Override public void keyTyped(KeyEvent arg0) {
		 * 
		 * }
		 * 
		 * @Override public void keyReleased(KeyEvent arg0) {
		 * 
		 * }
		 * 
		 * @Override public void keyPressed(KeyEvent arg0) { if (arg0.getKeyCode() ==
		 * KeyEvent.VK_ENTER) { String message = txtMessage.getText(); if (isStop) { try
		 * { updateChatSend(txtMessage.getText().toString()); } catch
		 * (BadLocationException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } txtMessage.setText(""); return; } if
		 * (message.equals("")) { txtMessage.setText("");
		 * txtMessage.setCaretPosition(0); return; } try { //
		 * chat.sendMessage(Encryption.sendMessage(message)); updateChatSend(message);
		 * txtMessage.setText(""); txtMessage.setCaretPosition(0); } catch (Exception e)
		 * { txtMessage.setText(""); txtMessage.setCaretPosition(0); } } } });
		 */

	}

	public static int request(String message, boolean type) {
		JFrame frameMessage = new JFrame();
		return Dictionary.show(frameMessage, message, type);
	}

	public static void updateChatReceive(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat,
				"<div class='left' style='width: 40%; background-color: #f1f0f0;'>" + message + "</div>");
	}

	public static void updateChatSend(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='left'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #0084ff;'>"
						+ message + "</td> </tr>" + "</table>");

	}

	public void updateChatNotify(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='left'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #f1c40f;'>"
						+ message + "</td> </tr>" + "</table>");
	}

	public void updateSendSymbol(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat, "<table style='width: 100%;'>" + "<tr align='left'>"
				+ "<td style='width: 59%;'></td>" + "<td style='width: 40%;'>" + message + "</td> </tr>" + "</table>");
	}

	private static void appendToPane(JTextPane tp, String message) throws BadLocationException, IOException {
		HTMLDocument docHtml = (HTMLDocument) tp.getDocument();
		HTMLEditorKit editorKitHtml = (HTMLEditorKit) tp.getEditorKit();

		editorKitHtml.insertHTML(docHtml, docHtml.getLength(), message, 0, 0, null);
		tp.setCaretPosition(docHtml.getLength());

	}

}
