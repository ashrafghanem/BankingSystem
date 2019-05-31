import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private java.sql.Connection con;
	private Statement st;
	private ResultSet rs;
	private PrintWriter pw;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		pw = response.getWriter();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingsystem",
					/* db username */"ashraf", /* db pass */"ashraf1998");
			st = con.createStatement();
			if (request.getMethod() == "GET")
				doGet(request, response);
			else if (request.getMethod() == "POST")
				doPost(request, response);

		} catch (Exception ex) {
			pw.print("Error Connecting to the database!");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		String userAccNum, userPass, amountAdd, accNum, amountSub, opNum, history;
		try {
			userAccNum = request.getParameter("user");
			userPass = request.getParameter("pass");

			amountAdd = request.getParameter("addBalance");
			accNum = request.getParameter("accnum");

			amountSub = request.getParameter("subtractBalance");

			opNum = request.getParameter("opNum");

			history = request.getParameter("history");

			if (userAccNum != null && userPass != null) {
				try {
					String query = "SELECT * FROM CLIENTS;";

					rs = st.executeQuery(query);
					while (rs.next()) {
						if (rs.getInt(1) == Integer.parseInt(userAccNum) && rs.getString(2).equals(userPass)) {
							String resp = "";
							resp = "Found;" + rs.getInt(3);

							Cookie cookie = new Cookie("lastLogIn", String.valueOf(new Date()));
							response.addCookie(cookie);

							// Cookie[] cookies = request.getCookies();
							// if(cookies!=null)
							// resp += (";"+cookies[0].getValue());

							pw.print(resp);
							return;
						}
					}
					pw.print("Not Found");
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
					pw.print("success;" + newBalance);
				} catch (Exception ex) {
					pw.print("fail");
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
					pw.print("success;" + newBalance);
				} catch (Exception ex) {
					pw.print("fail");
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
						pw.print(rs.getInt(1) + ";" + rs.getString(2) + ";" + rs.getInt(3) + ";" + rs.getString(4)
								+ "/");
					}
				} catch (Exception ex) {
					pw.print("fail");
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
					pw.print(resp);
				} catch (Exception ex) {
					pw.print("");
					return;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
