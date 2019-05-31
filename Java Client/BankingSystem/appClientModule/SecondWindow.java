import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SecondWindow extends JFrame {
	// All operations in this window use Post method.

	private static JFrame frmMainWindow;
	private static JTextField amountTF;
	private static JButton updateBtn, queryBtn, historyBtn;

	public SecondWindow(String userAccNum, String userBalance, JFrame main) {
		frmMainWindow = main;

		setResizable(false);
		setTitle("Second Window");
		setSize(515, 405);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("User Account:");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Courier New", Font.BOLD, 18));
		lblNewLabel.setBounds(10, 11, 150, 30);
		getContentPane().add(lblNewLabel);

		JLabel userNum = new JLabel(userAccNum);
		userNum.setForeground(Color.RED);
		userNum.setFont(new Font("Courier New", Font.BOLD, 18));
		userNum.setBounds(170, 11, 225, 30);
		getContentPane().add(userNum);

		JComboBox<String> operation = new JComboBox<String>();
		operation.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (operation.getSelectedIndex() == 1 || operation.getSelectedIndex() == 2) {
					amountTF.setEditable(true);
					updateBtn.setEnabled(true);
				} else {
					amountTF.setEditable(false);
					updateBtn.setEnabled(false);
				}
			}
		});
		operation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		operation.setModel(new DefaultComboBoxModel<String>(new String[] { "-", "Deposit", "Withdraw" }));
		operation.setBounds(112, 68, 116, 30);
		getContentPane().add(operation);

		JLabel lblNewLabel_1 = new JLabel("Operation");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setBounds(10, 68, 92, 30);
		getContentPane().add(lblNewLabel_1);

		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setHorizontalAlignment(SwingConstants.LEFT);
		lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAmount.setBounds(10, 109, 92, 30);
		getContentPane().add(lblAmount);

		amountTF = new JTextField();
		amountTF.setEditable(false);
		amountTF.setBounds(112, 109, 116, 30);
		getContentPane().add(amountTF);
		amountTF.setColumns(10);

		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalance.setForeground(Color.BLACK);
		lblBalance.setFont(new Font("Courier New", Font.BOLD, 18));
		lblBalance.setBounds(300, 84, 150, 30);
		getContentPane().add(lblBalance);

		JLabel balance = new JLabel(userBalance);
		balance.setHorizontalAlignment(SwingConstants.CENTER);
		balance.setForeground(Color.RED);
		balance.setFont(new Font("Courier New", Font.BOLD, 30));
		balance.setBounds(279, 115, 192, 50);
		getContentPane().add(balance);

		updateBtn = new JButton("Update");
		updateBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		updateBtn.setBounds(112, 149, 116, 30);
		updateBtn.setEnabled(false);
		getContentPane().add(updateBtn);
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				URL url = null;
				try {
					String userBalance = amountTF.getText();
					try {
						Double.parseDouble(userBalance);
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(SecondWindow.this, "This field must contain valid number!",
								"Invalid Content", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (MainWindow.server.getSelectedIndex() == 1)
						url = new URL("http://localhost:1234/PHPServer.php");
					else if (MainWindow.server.getSelectedIndex() == 2)
						url = new URL("http://localhost:8080/ServletServer/MyServlet");
					else if (MainWindow.server.getSelectedIndex() == 3)
						url = new URL("http://localhost:8080/JSPServer/MyJSP.jsp");

					URLConnection urlc = url.openConnection();
					urlc.setAllowUserInteraction(true);
					urlc.setDoInput(true);
					urlc.setDoOutput(true);

					String amount = "";
					String accNum = "&accnum=" + userAccNum;
					if (operation.getSelectedIndex() == 1) {
						amount = "&addBalance=" + userBalance;
					} else if (operation.getSelectedIndex() == 2) {
						amount = "&subtractBalance=" + userBalance;
					}

					java.io.OutputStream os = urlc.getOutputStream();
					os.write(amount.getBytes());
					os.write(accNum.getBytes());

					String response = "";
					java.io.InputStream is = urlc.getInputStream();
					for (int ch = 0; (ch = is.read()) != -1;)
						response += (char) ch;

					response = response.trim();
					if (response.indexOf("success") == 0) {
						String newBalance = response.split(";")[1];
						balance.setText(newBalance);
						amountTF.setText("");
					} else if (response.equals("fail")) {
						JOptionPane.showMessageDialog(SecondWindow.this,
								"You may deposited or withdrew incorrect amount!", "Invalid Content",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(SecondWindow.this, "Check Server Connection!", "Server Connection",
							JOptionPane.ERROR_MESSAGE, null);
					return;
				}
			}
		});

		JButton signOutBtn = new JButton("Sign Out");
		signOutBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		signOutBtn.setBounds(279, 328, 116, 30);
		getContentPane().add(signOutBtn);
		signOutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				SecondWindow.this.setVisible(false);
				SecondWindow.frmMainWindow.setVisible(true);
				MainWindow.accountNum.setText("");
				MainWindow.password.setText("");
			}
		});

		historyBtn = new JButton("History");
		historyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					URL url = null;
					if (MainWindow.server.getSelectedIndex() == 1)
						url = new URL("http://localhost:1234/PHPServer.php");
					else if (MainWindow.server.getSelectedIndex() == 2)
						url = new URL("http://localhost:8080/ServletServer/MyServlet");
					else if (MainWindow.server.getSelectedIndex() == 3)
						url = new URL("http://localhost:8080/JSPServer/MyJSP.jsp");

					URLConnection urlc = url.openConnection();
					urlc.setAllowUserInteraction(true);
					urlc.setDoInput(true);
					urlc.setDoOutput(true);

					String accNum = "&accnum=" + userAccNum;

					java.io.OutputStream os = urlc.getOutputStream();
					os.write("history".getBytes());
					os.write(accNum.getBytes());

					String response = "";
					java.io.InputStream is = urlc.getInputStream();
					for (int ch = 0; (ch = is.read()) != -1;)
						response += (char) ch;

					response = response.trim();
					if (!response.isEmpty()) {
						String[] transactions = response.split("/");
						Object[] trans = new String[transactions.length];
						for (int i = 0; i < transactions.length; i++) {
							trans[i] = transactions[i];
						}
						new HistoryChart(trans, SecondWindow.this);
						SecondWindow.this.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(SecondWindow.this, "No Records Exist!", "Not Found!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(SecondWindow.this, "Check Server Connection!", "Server Connection",
							JOptionPane.ERROR_MESSAGE, null);
					return;
				}
			}
		});
		historyBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		historyBtn.setBounds(112, 328, 116, 30);
		getContentPane().add(historyBtn);

		JComboBox<String> numOfOp = new JComboBox<String>();
		numOfOp.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (numOfOp.getSelectedIndex() == 0) {
					queryBtn.setEnabled(false);
				} else {
					queryBtn.setEnabled(true);
				}
			}
		});
		numOfOp.setMaximumRowCount(3);
		numOfOp.setModel(new DefaultComboBoxModel<String>(new String[] { "-", "1", "2", "3", "4", "5" }));
		numOfOp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		numOfOp.setBounds(112, 227, 116, 30);
		getContentPane().add(numOfOp);

		JLabel lblOperation = new JLabel("# Operation");
		lblOperation.setHorizontalAlignment(SwingConstants.LEFT);
		lblOperation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOperation.setBounds(10, 227, 92, 30);
		getContentPane().add(lblOperation);

		queryBtn = new JButton("Query");
		queryBtn.setEnabled(false);
		queryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int numOfOperations = numOfOp.getSelectedIndex();

				try {
					URL url = null;
					if (MainWindow.server.getSelectedIndex() == 1)
						url = new URL("http://localhost:1234/PHPServer.php");
					else if (MainWindow.server.getSelectedIndex() == 2)
						url = new URL("http://localhost:8080/ServletServer/MyServlet");
					else if (MainWindow.server.getSelectedIndex() == 3)
						url = new URL("http://localhost:8080/JSPServer/MyJSP.jsp");

					URLConnection urlc = url.openConnection();
					urlc.setAllowUserInteraction(true);
					urlc.setDoInput(true);
					urlc.setDoOutput(true);

					String opNum = "&opNum=" + numOfOperations;
					String accNum = "&accnum=" + userAccNum;

					java.io.OutputStream os = urlc.getOutputStream();
					os.write(opNum.getBytes());
					os.write(accNum.getBytes());

					String response = "";
					java.io.InputStream is = urlc.getInputStream();
					for (int ch = 0; (ch = is.read()) != -1;)
						response += (char) ch;

					response = response.trim();
					if (response.equals("")) {
						JOptionPane.showMessageDialog(SecondWindow.this, "No Records Exist!", "Not Found!",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else if (response.indexOf(userAccNum) == 0) {
						String[] rows = response.split("/");

						if (rows.length - numOfOperations < 0) {
							numOfOperations = rows.length;
						}
						String[][] recordsArray = new String[numOfOperations][4];
						for (int i = rows.length - 1; i >= rows.length - numOfOperations; i--) {
							for (int j = 0; j < 4; j++) {
								recordsArray[rows.length - i - 1][j] = rows[i].split(";")[j];
							}
						}
						new Transactions(recordsArray, SecondWindow.this).setVisible(true);
						SecondWindow.this.setVisible(false);
					} else if (response.equals("fail")) {
						JOptionPane.showMessageDialog(SecondWindow.this, "An Error Occurred!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(SecondWindow.this, "Check Server Connection!", "Server Connection",
							JOptionPane.ERROR_MESSAGE, null);
					return;
				}
			}
		});
		queryBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		queryBtn.setBounds(320, 227, 116, 30);
		getContentPane().add(queryBtn);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 194, 489, 2);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 315, 489, 2);
		getContentPane().add(separator_1);

		JLabel lastLogin = new JLabel("Last login:");
		lastLogin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lastLogin.setBounds(10, 295, 218, 22);
		getContentPane().add(lastLogin);
	}
}
