import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/ServletTrovaDettagliAutobus")
public class ServletTrovaDettagliAutobus extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public ServletTrovaDettagliAutobus() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("ci sonoooooooooooooooo");
		String cod = request.getParameter("cod");
		
		JSONArray indFermata = new JSONArray();
		JSONArray oraFermata = new JSONArray();
		JSONArray latFermata = new JSONArray();
		JSONArray lonFermata = new JSONArray();
		
        JSONObject js = new JSONObject();

		//QUERY andiamo a cercare indirizzo,ora,lat e lon delle fermate riguardanti il nostro percorso tutte belle ordinate
		queryDB db = new queryDB();
		ResultSet rs;
		try {
			rs = db.query("select ora,indirizzo,indlat,indlon from passaggio,fermata where passaggio.cod_percorso='"+cod+"' and passaggio.nome_fermata =fermata.nome order by n_ordine");
			 while (rs.next()) {
				  indFermata.put(rs.getString("indirizzo"));
				  oraFermata.put(rs.getString("ora"));
				  latFermata.put(rs.getString("indlat"));
				  lonFermata.put(rs.getString("indlon"));
				  System.out.println(rs.getString("indirizzo")+rs.getString("ora")+rs.getString("indlat")+rs.getString("indlon"));
			    }
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//risposta inviamo i dati raccolti al server primario
				if(indFermata.length()>0) {
					try {
						js.put("indFermata", indFermata);
						js.put("oraFermata", oraFermata);
						js.put("latFermata", latFermata);
						js.put("lonFermata", lonFermata);
			
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				response.getWriter().write(js.toString());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}
	
	
}
