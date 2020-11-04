package com.tranquyet.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.tranquyet.dictionary.Decryption;
import com.tranquyet.dictionary.Dictionary;
import com.tranquyet.dictionary.Encryption;

public class ChatUserGui {

	private ChatRoom chat;
	private Socket socketChat;
	private String nameUser = "";
	private String nameGuest = "";
	private JFrame frameChatGui;
	private JTextField textName;
	private JPanel panelMessage;
	private JTextPane txtDisplayChat;
	private Label textState;

	private JButton btnDisConnect, btnSend;

	public boolean isStop = false, isSendFile = false, isReceiveFile = false;

	private int portServer = 0;
	private JTextField txtMessage;
	private JScrollPane scrollPane;

	/**
	 * @wbp.parser.entryPoint
	 */
	public ChatUserGui(String user, String guest, Socket socket, int port) {
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		this.portServer = port;
		EventQueue.invokeLater(() -> {

			try {
				ChatUserGui window = new ChatUserGui(nameUser, nameGuest, socketChat, portServer, 0);
				window.frameChatGui.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			try {
				ChatUserGui window = new ChatUserGui();
				window.frameChatGui.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public ChatUserGui() throws BadLocationException, IOException {
		initialize();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public ChatUserGui(String user, String guest, Socket socket, int port, int a) throws Exception {
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		this.portServer = port;
		initialize();
		chat = new ChatRoom(socketChat, nameUser, nameGuest);
		chat.start();
	}

	public void updateChatReceive(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat,
				"<div class='left' style='width: 40%; background-color: #f1f0f0;'>" + message + "</div>");
	}

	public void updateChatSend(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #0084ff;'>"
						+ message + "</td> </tr>" + "</table>");
	}

	public void updateChatNotify(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #f1c40f;'>"
						+ message + "</td> </tr>" + "</table>");
	}

	public void updateSendSymbol(String message) throws BadLocationException, IOException {
		appendToPane(txtDisplayChat, "<table style='width: 100%;'>" + "<tr align='right'>"
				+ "<td style='width: 59%;'></td>" + "<td style='width: 40%;'>" + message + "</td> </tr>" + "</table>");
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() throws BadLocationException, IOException {

		frameChatGui = new JFrame();
		frameChatGui.setResizable(false);
		frameChatGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameChatGui.getContentPane().setBackground(Color.DARK_GRAY);
		frameChatGui.setTitle("Private Chat");
		frameChatGui.setBounds(200, 200, 491, 426);
		frameChatGui.getContentPane().setLayout(null);

		JLabel lblClientIP = new JLabel("Friend:");
		lblClientIP.setForeground(Color.GREEN);
		lblClientIP.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblClientIP.setBounds(21, 6, 50, 40);

		frameChatGui.getContentPane().add(lblClientIP);

		textName = new JTextField(nameUser);
		textName.setForeground(SystemColor.textHighlight);
		textName.setFont(new Font("MS PGothic", Font.BOLD | Font.ITALIC, 16));
		textName.setEditable(false);
		textName.setBounds(70, 11, 100, 28);
		frameChatGui.getContentPane().add(textName);
		textName.setText(nameGuest);
		textName.setColumns(10);

		panelMessage = new JPanel();
		panelMessage.setBounds(6, 277, 465, 101);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		frameChatGui.getContentPane().add(panelMessage);
		panelMessage.setLayout(null);

		txtMessage = new JTextField("");
		txtMessage.setBounds(10, 21, 250, 62);
		panelMessage.add(txtMessage);
		txtMessage.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSend.setBounds(270, 21, 50, 25);
		btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelMessage.add(btnSend);
		/*
		 * @symbol <like>:
		 * 
		 * @Description: instead of using byte to convert image to binary, we can use
		 * <img> tag in html to send message which contain the address of this symbol on
		 * the system of ohter people
		 * 
		 * @tg
		 */
		JButton btnSendLike = new JButton("");
		btnSendLike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "<img src='" + ChatUserGui.class.getResource("/image/like.png") + "'></img>";
				try {
					chat.sendMessage(Encryption.sendMessage(message));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					updateSendSymbol(message);
				} catch (BadLocationException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSendLike.setFont(new Font("Tahoma", Font.BOLD, 13));

		btnSendLike.setBackground(new Color(240, 240, 240));
		btnSendLike.setBounds(270, 48, 40, 30);

		btnSendLike.setIcon(new javax.swing.ImageIcon(ChatUserGui.class.getResource("/image/like.png")));
		// transparent button
		btnSendLike.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSendLike.setContentAreaFilled(false);
		panelMessage.add(btnSendLike);

		btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if (isStop) {
					try {
						updateChatSend(txtMessage.getText().toString());

					} catch (BadLocationException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					txtMessage.setText(""); // reset text Send
					return;
				}
				String message = txtMessage.getText();
				if (message.equals(""))
					return;
				try {
					chat.sendMessage(Encryption.sendMessage(message));
					updateChatSend(message);
					txtMessage.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		txtMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = txtMessage.getText();
					if (isStop) {
						try {
							updateChatSend(txtMessage.getText().toString());
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						txtMessage.setText("");
						return;
					}
					if (message.equals("")) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
						return;
					}
					try {
						chat.sendMessage(Encryption.sendMessage(message));
						updateChatSend(message);
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					} catch (Exception e) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					}
				}
			}
		});

		btnDisConnect = new JButton("Out");
		btnDisConnect.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Dictionary.show(frameChatGui, "Are you sure to close chat with account: " + nameGuest,
						true);
				if (result == 0) {
					try {
						isStop = true;
						frameChatGui.dispose();
						chat.sendMessage(Dictionary.CHAT_CLOSE);
						chat.stopChat();
						System.gc();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnDisConnect.setBounds(386, 12, 68, 28);
		frameChatGui.getContentPane().add(btnDisConnect);

		textState = new Label("");
		textState.setBounds(6, 570, 158, 22);
		textState.setVisible(false);
		frameChatGui.getContentPane().add(textState);

		txtDisplayChat = new JTextPane();
		txtDisplayChat.setBackground(SystemColor.scrollbar);
		txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDisplayChat.setEditable(false);
		txtDisplayChat.setContentType("text/html");
		txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
		txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		appendToPane(txtDisplayChat, "<div class='clear' style='background-color:black'/>");

		frameChatGui.getContentPane().add(txtDisplayChat);

		scrollPane = new JScrollPane(txtDisplayChat);
		scrollPane.setBounds(6, 50, 465, 216);
		frameChatGui.getContentPane().add(scrollPane);

	}

	// send html to pane
	/**
	 * @wbp.parser.entryPoint
	 */
	private void appendToPane(JTextPane tp, String message) throws BadLocationException, IOException {
		HTMLDocument docHtml = (HTMLDocument) tp.getDocument();
		HTMLEditorKit editorKitHtml = (HTMLEditorKit) tp.getEditorKit();

		editorKitHtml.insertHTML(docHtml, docHtml.getLength(), message, 0, 0, null);
		tp.setCaretPosition(docHtml.getLength());

	}

	public class ChatRoom extends Thread {

		private Socket connect;
		private ObjectOutputStream outPeer;
		private ObjectInputStream inPeer;

		public ChatRoom(Socket connection, String name, String guest) throws Exception {
			connect = new Socket();
			connect = connection;
			nameGuest = guest;
		}

		@Override
		public void run() {
			super.run();
			// OutputStream out = null;
			while (!isStop) {
				try {
					inPeer = new ObjectInputStream(connect.getInputStream());
					Object obj = inPeer.readObject();
					if (obj instanceof String) {
						String messageObj = obj.toString();
						if (messageObj.equals(Dictionary.CHAT_CLOSE)) {
							isStop = true;

							Dictionary.show(frameChatGui, nameGuest + " closed chat with you! ", false);
							try {
								isStop = true;
								// frameChatGui.dispose();
								chat.sendMessage(Dictionary.CHAT_CLOSE);
								// chat.stopChat();
							} catch (Exception e) {
								e.printStackTrace();
							}
							connect.close();
							break;
						}

						String message = Decryption.getMessage(messageObj);
						updateChatReceive(message);

						// FriendListGui.updateChatReceive(nameGuest + ": " + message);
					}
				} catch (Exception e) {

				}
			}
		}

		// void send Message
		public synchronized void sendMessage(Object obj) throws Exception {
			outPeer = new ObjectOutputStream(connect.getOutputStream());
			// only send text
			if (obj instanceof String) {
				String message = obj.toString();

				outPeer.writeObject(message);
				outPeer.flush();

				FriendListGui.updateChatSend(nameUser + ": " + message + " to " + nameGuest);
			}

		}

		public void stopChat() {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
