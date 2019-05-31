import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class MainWindow {
	/*
	 * This project is a java client that can communicate with 3 types of servers.
	 * 
	 * PHP Server(Port:1234) / Servlet Server(Port:8080) / JSP Server(Port:8080).
	 * 
	 * All three servers communicate with the same MySql database that has records
	 * about the clients. [They are added manually].
	 * 
	 * PHP Server uses: Apatche Web Server. Servlet and JSP use: Tomcat v7.0 Web
	 * Server.
	 * 
	 * Both Web Servers and MySql database are gathered in Xampp Package.
	 */
	public JFrame frmBankingSystem;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public static JTextField accountNum;
	public static JPasswordField password;
	public static JComboBox<String> server;
	private JRadioButton get, post;

	public static void main(String[] args) throws MalformedURLException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmBankingSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		initialize();
	}

	private void initialize() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
		}

		frmBankingSystem = new JFrame();
		frmBankingSystem.setResizable(false);
		frmBankingSystem.setTitle("Banking System");
		frmBankingSystem.setSize(472, 407);
		frmBankingSystem.setLocationRelativeTo(null);
		frmBankingSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBankingSystem.getContentPane().setLayout(null);

		server = new JComboBox<String>();
		server.setModel(new DefaultComboBoxModel<String>(new String[] { "-", "PHP: http://localhost:1234/PHPServer.php",
				"Servlet: http://localhost:8080/ServletServer/MyServlet",
				"JSP: http://localhost:8080/JSPServer/MyJSP.jsp" }));
		server.setBounds(111, 98, 297, 30);
		frmBankingSystem.getContentPane().add(server);

		JLabel lblBankingSystem = new JLabel("Banking System");
		lblBankingSystem.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblBankingSystem.setBounds(10, 11, 290, 48);
		frmBankingSystem.getContentPane().add(lblBankingSystem);

		JLabel lblNewLabel = new JLabel("Server");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 98, 91, 30);
		frmBankingSystem.getContentPane().add(lblNewLabel);

		JLabel lblMethod = new JLabel("Method");
		lblMethod.setHorizontalAlignment(SwingConstants.CENTER);
		lblMethod.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMethod.setBounds(10, 155, 91, 30);
		frmBankingSystem.getContentPane().add(lblMethod);

		get = new JRadioButton("  GET");
		buttonGroup.add(get);
		get.setFont(new Font("Tahoma", Font.PLAIN, 14));
		get.setBounds(111, 161, 66, 23);
		frmBankingSystem.getContentPane().add(get);

		post = new JRadioButton("  POST");
		buttonGroup.add(post);
		post.setFont(new Font("Tahoma", Font.PLAIN, 14));
		post.setBounds(234, 161, 91, 23);
		frmBankingSystem.getContentPane().add(post);

		accountNum = new JTextField();
		accountNum.setFont(new Font("Tahoma", Font.PLAIN, 14));
		accountNum.setBounds(111, 215, 297, 30);
		frmBankingSystem.getContentPane().add(accountNum);
		accountNum.setColumns(10);

		password = new JPasswordField();
		password.setEchoChar('•');
		password.setFont(new Font("Tahoma", Font.PLAIN, 14));
		password.setColumns(10);
		password.setBounds(111, 256, 297, 30);
		frmBankingSystem.getContentPane().add(password);

		JButton signInBtn = new JButton("Sign In");
		signInBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		signInBtn.setBounds(200, 320, 89, 30);
		frmBankingSystem.getContentPane().add(signInBtn);
		signInBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// CookieManager cookieManager = new CookieManager();
					// cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
					// CookieHandler.setDefault(cookieManager);

					if (server.getSelectedIndex() == 0 || !get.isSelected() && !post.isSelected()
							|| accountNum.getText().isEmpty() || String.valueOf(password.getPassword()).isEmpty()) {
						JOptionPane.showMessageDialog(frmBankingSystem, "Please fill all required fields!",
								"Attribute Missing", JOptionPane.ERROR_MESSAGE, null);
						return;
					}
					String userNum = accountNum.getText();
					String userPassword = String.valueOf(password.getPassword());
					boolean isGetMethod;
					String response = "";
					URL url = null;

					// If GET method, data are enclosed within the URL
					// If POST method, data are enclosed within HTTP request body
					isGetMethod = get.isSelected() ? true : false;

					if (isGetMethod) {
						if (server.getSelectedIndex() == 1)
							url = new URL("http://localhost:1234/PHPServer.php?user=" + userNum + "&pass=" + userPassword);
						else if (server.getSelectedIndex() == 2)
							url = new URL("http://localhost:8080/ServletServer/MyServlet?user=" + userNum + "&pass="
									+ userPassword);
						else if (server.getSelectedIndex() == 3)
							url = new URL("http://localhost:8080/JSPServer/MyJSP.jsp?user=" + userNum + "&pass="
									+ userPassword);

						URLConnection urlc = url.openConnection();
						urlc.setAllowUserInteraction(true);
						urlc.setDoInput(true);
						urlc.setDoOutput(true);

						// Get the HTTP Response from the server.
						java.io.InputStream is = urlc.getInputStream();
						for (int ch = 0; (ch = is.read()) != -1;)
							response += (char) ch;

						// CookieStore cookieStore = cookieManager.getCookieStore();
						// //getting cookies which returns in the form of List of type HttpCookie
						// List<HttpCookie> listOfcookies = cookieStore.getCookies();
						// String lastLogIn = listOfcookies.get(0).getValue();

						response = response.trim();
						if (response.indexOf("Found") == 0) {
							String balance = response.split(";")[1];
							accessSecondWindow(userNum, balance);
						} else if (response.equals("Not Found")) {
							JOptionPane.showMessageDialog(frmBankingSystem, "Wrong User or Password",
									"Wrong Credentials", JOptionPane.ERROR_MESSAGE, null);
							return;
						}
					} else {
						if (server.getSelectedIndex() == 1)
							url = new URL("http://localhost:1234/PHPServer.php");
						else if (server.getSelectedIndex() == 2)
							url = new URL("http://localhost:8080/ServletServer/MyServlet");
						else if (server.getSelectedIndex() == 3)
							url = new URL("http://localhost:8080/JSPServer/MyJSP.jsp");

						URLConnection urlc = url.openConnection();
						urlc.setAllowUserInteraction(true);
						urlc.setDoInput(true);
						urlc.setDoOutput(true);

						// Send HTTP Request to the server with this body data.
						String str1 = "&user=" + userNum;
						String str2 = "&pass=" + userPassword;
						java.io.OutputStream os = urlc.getOutputStream();
						os.write(str1.getBytes());
						os.write(str2.getBytes());

						java.io.InputStream is = urlc.getInputStream();
						for (int ch = 0; (ch = is.read()) != -1;)
							response += (char) ch;

						response = response.trim();
						if (response.indexOf("Found") == 0) {
							String balance = response.split(";")[1];
							accessSecondWindow(userNum, balance);
						} else if (response.equals("Not Found")) {
							JOptionPane.showMessageDialog(frmBankingSystem, "Wrong User or Password",
									"Wrong Credentials", JOptionPane.ERROR_MESSAGE, null);
							return;
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frmBankingSystem, "Check Server Connection!", "Server Connection",
							JOptionPane.ERROR_MESSAGE, null);
					return;
				}
			}
		});

		JLabel lblUsername = new JLabel("Acc. Num.");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUsername.setBounds(10, 215, 91, 30);
		frmBankingSystem.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(10, 256, 91, 30);
		frmBankingSystem.getContentPane().add(lblPassword);
	}

	private void accessSecondWindow(String userNum, String balance) {
		frmBankingSystem.setVisible(false);
		new SecondWindow(userNum, balance, frmBankingSystem).setVisible(true);
	}
}
