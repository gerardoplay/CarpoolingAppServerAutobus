
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class queryDB {

	public queryDB(){
	
	}
	public ResultSet query(String sql) throws SQLException{
		Connection con = Connessione.getConnection();
		Statement st = con.createStatement();
		ResultSet res = st.executeQuery(sql);
		return res;
	}
}
