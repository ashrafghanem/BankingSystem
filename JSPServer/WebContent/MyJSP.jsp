<%@ page import="java.io.*,java.util.*,java.text.*"%>
<%!java.sql.Connection con;
	java.sql.Statement st;
	java.sql.ResultSet rs;

	String userAccNum, userPass, amountAdd, accNum, amountSub, opNum, history;

	public void jspInit() { //Build-In Method
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingsystem",
					/*db username*/"ashraf", /*db pass*/"ashraf1998");
			st = con.createStatement();
		} catch (Exception ex) {
		}
	}%>
<%
	try {
		userAccNum = request.getParameter("user");
		userPass = request.getParameter("pass");

		amountAdd = request.getParameter("addBalance");
		accNum = request.getParameter("accnum");

		amountSub = request.getParameter("subtractBalance");

		opNum = request.getParameter("opNum");

		history = request.getParameter("history");
		String name = request.getParameter("uname");

		if (userAccNum != null && userPass != null) {
			try {
				String query = "SELECT * FROM CLIENTS;";
				rs = st.executeQuery(query);
				while (rs.next()) {
					if (rs.getInt(1) == Integer.parseInt(userAccNum) && rs.getString(2).equals(userPass)) {
						out.print("Found;" + rs.getInt(3));
						return;
					}
				}
				out.print("Not Found");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (amountAdd != null && accNum != null) {
			try {
				String query = "UPDATE CLIENTS SET BALANCE=BALANCE+" + Integer.parseInt(amountAdd)
						+ " WHERE ACCOUNT_NUM=" + Integer.parseInt(accNum) + ";";

				st.executeUpdate(query);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				query = "INSERT INTO transactions(account_num, operation, amount, date) VALUES ("
						+ Integer.parseInt(accNum) + ",'deposit'," + Integer.parseInt(amountAdd) + ",'" + date
						+ "');";
				st.executeUpdate(query);

				query = "SELECT BALANCE FROM clients WHERE ACCOUNT_NUM=" + accNum + ";";
				rs = st.executeQuery(query);
				rs.next();
				int newBalance = rs.getInt(1);
				out.print("success;" + newBalance);
			} catch (Exception ex) {
				out.print("fail");
				return;
			}
		} else if (amountSub != null && accNum != null) {
			try {
				String query = "UPDATE CLIENTS SET BALANCE=BALANCE-" + Integer.parseInt(amountSub)
						+ " WHERE ACCOUNT_NUM=" + Integer.parseInt(accNum) + ";";

				st.executeUpdate(query);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				query = "INSERT INTO transactions(account_num, operation, amount, date) VALUES ("
						+ Integer.parseInt(accNum) + ",'withdraw'," + Integer.parseInt(amountSub) + ",'" + date
						+ "');";
				st.executeUpdate(query);

				query = "SELECT BALANCE FROM clients WHERE ACCOUNT_NUM=" + accNum + ";";
				rs = st.executeQuery(query);
				boolean result = rs.next();
				if (result == false)
					throw new Exception();
				int newBalance = rs.getInt(1);
				out.print("success;" + newBalance);
			} catch (Exception ex) {
				out.print("fail");
				return;
			}
		} else if (opNum != null && accNum != null) {
			try {
				String query = "SELECT * FROM transactions WHERE ACCOUNT_NUM=" + Integer.parseInt(accNum) + ";";

				st.executeQuery(query);

				rs = st.executeQuery(query);
				if (rs.next() == false)
					throw new Exception();
				while (rs.next()) {
					out.print(rs.getInt(1) + ";" + rs.getString(2) + ";" + rs.getInt(3) + ";" + rs.getString(4)
							+ "/");
				}
			} catch (Exception ex) {
				out.print("fail");
				return;
			}
		} else if (history != null && accNum != null) {
			try {
				String resp = "";
				String query = "SELECT AMOUNT FROM transactions WHERE ACCOUNT_NUM=" + accNum + ";";
				st.executeQuery(query);

				rs = st.executeQuery(query);
				if (rs.next() == false)
					throw new Exception();
				while (rs.next()) {
					resp += (rs.getInt(1) + "/");
				}
				out.print(resp);
			} catch (Exception ex) {
				out.print("");
				return;
			}
		}
	} catch (Exception e) {

	}
%>