
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class updateDB {
// >0 OK, =0 ko sql, =-1 ko key già inserita
	public updateDB(){
		
	}
	public String inserimento(String sql){
		int res=0;
		try{
			Connection con = Connessione.getConnection();
			Statement st = con.createStatement();
			
		
			res = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode()==1062)
				res=-1;
			else	
				System.out.println("ERRORE UPDATEDB ERR SQLEXCEPTION:  "+e.getErrorCode()+e.getMessage());
		} 
		return res+"";
		
	}
}
