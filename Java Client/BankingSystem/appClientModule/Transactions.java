import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

public class Transactions extends JFrame {

	private JFrame frame;
	private JTable table;
	private String[][] rows;

	public Transactions(String[][] recordsArray, JFrame SecondWindow) {
		setTitle("Transactions");
		rows = recordsArray;
		frame = SecondWindow;

		setResizable(false);
		setSize(515, 405);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblTransactions = new JLabel("Transactions");
		lblTransactions.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblTransactions.setBounds(10, 11, 173, 46);
		getContentPane().add(lblTransactions);

		table = new JTable(new MyTableModel());
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 18));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(30);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(10, 68, 489, 175);
		scroll.setColumnHeaderView(table.getTableHeader());
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		getContentPane().add(scroll);

		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Transactions.this.setVisible(false);
				frame.setVisible(true);
			}
		});
		backBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		backBtn.setBounds(198, 300, 116, 30);
		getContentPane().add(backBtn);

	}

	private class MyTableModel extends AbstractTableModel {
		private String[] columnNames = { "Account Num.", "Operation", "Amount", "Time" };
		private Object[][] data = rows;

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}
	}
}
