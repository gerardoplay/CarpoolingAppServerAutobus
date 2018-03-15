

import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Connessione {

	private static Connection connection;

	public static boolean testConnection() {
		try {
			Connection con = getConnection();

			System.out.println("Connessione OK \n");
			con.close();
		} catch (SQLException e) {
			System.out.println("Connessione Fallita \n");
			System.out.println(e);
			return false;
		}

		return true;
	} // end main

	public static Connection getConnection() throws SQLException {
		System.out.println("ciao pippo0");
		
		if (connection == null || connection.isClosed()) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				String url = "jdbc:mysql://localhost:3306/autobus";
				connection = DriverManager.getConnection(url, "root", "");
			} catch (java.lang.ClassNotFoundException e) {
				throw new SQLException(e);
			} catch (IllegalAccessException e) {
				throw new SQLException(e);
			} catch (InstantiationException e) {
				throw new SQLException(e);
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		System.out.println("ciao pippo0");
		
		return connection;
	}


	public static boolean controlloSessione(HttpServletRequest r) {
		// TODO Auto-generated method stub
		HttpSession sess=r.getSession(false);

		if(sess==null)
			return false;
		else
			if(sess.getAttribute("utente")==null)
				return false;
			else{
				//System.out.println(sess.getAttribute("utente"));

				return true;
			}



	}
	
	
	
}
