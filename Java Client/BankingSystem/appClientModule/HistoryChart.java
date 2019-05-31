import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class HistoryChart extends JFrame {

	private JTable table;
	private JFrame frame;
	private Object[] records;

	public HistoryChart(Object[] trans, JFrame secondWindow) {
		frame = secondWindow;
		records = trans;

		setTitle("History Of Operations");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prepareUI();
		pack();
		setVisible(true);
	}

	private void prepareUI() {
		MyChart myChart = new MyChart();
		myChart.setPreferredSize(new Dimension(800, 500));

		table = new JTable(new MyTableModel());

		Button backBtn = new Button("Back");
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				frame.setVisible(true);
				HistoryChart.this.setVisible(false);
			}
		});

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < table.getColumnCount(); i++) {
			list.add(Integer.parseInt(String.valueOf(table.getValueAt(0, i))));
		}
		myChart.updateList(list);

		getContentPane().add(myChart, BorderLayout.NORTH);
		getContentPane().add(backBtn, BorderLayout.SOUTH);
	}

	private class MyChart extends JComponent {
		ArrayList<Integer> chartList;

		public void updateList(ArrayList<Integer> list) {
			chartList = list;
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			if (chartList != null) {
				paintMe(g);
			}
		}

		private void paintMe(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setColor(Color.red);
			graphics2d.setStroke(new BasicStroke(2.0f));

			int width = getWidth();
			int height = getHeight();

			float hDiv = (float) width / (float) (chartList.size() - 1);
			float vDiv = (float) height / (float) (Collections.max(chartList));

			for (int i = 0; i < chartList.size() - 1; i++) {
				int value1, value2;
				if (chartList.get(i) == null)
					value1 = 0;
				else
					value1 = chartList.get(i);

				if (chartList.get(i + 1) == null)
					value2 = 0;
				else
					value2 = chartList.get(i + 1);

				graphics2d.drawLine((int) (i * hDiv), height - ((int) (value1 * vDiv)), (int) ((i + 1) * hDiv),
						height - ((int) (value2 * vDiv)));
			}
			graphics2d.drawRect(0, 0, width, height);
		}
	}

	class MyTableModel extends AbstractTableModel {
		private Object[][] tableData = { records };

		@Override
		public int getColumnCount() {
			return records.length;
		}

		@Override
		public int getRowCount() {
			return 1;
		}

		@Override
		public String getColumnName(int col) {
			return "";
		}

		@Override
		public Object getValueAt(int row, int col) {
			return tableData[row][col];
		}
	}
}
