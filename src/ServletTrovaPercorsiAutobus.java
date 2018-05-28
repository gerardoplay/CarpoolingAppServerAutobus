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

@WebServlet("/ServletTrovaPercorsiAutobus")
public class ServletTrovaPercorsiAutobus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	public ServletTrovaPercorsiAutobus() {
		
		super();
		// TODO Auto-generated constructor stub
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		double indlat=Double.parseDouble(request.getParameter("indlat"));
		double indlon=Double.parseDouble(request.getParameter("indlon"));
		String ar = request.getParameter("ar");
		String data = request.getParameter("data");
		String ora = request.getParameter("ora");
		double partenzalat;
		double partenzalon;
		double arrivolat;
		double arrivolon;
		if(ar.equals("1")){
            partenzalat=indlat; //cordinate di casa del richiedente passaggio
            partenzalon=indlon;
            arrivolat=40.77372; //cordinate di unisa
            arrivolon=14.794522;
        }
        else{
            partenzalat=40.77372; //cordinate di unisa
            partenzalon=14.794522;
            arrivolat=indlat; //cordinate di casa del richiedente passaggio
            arrivolon=indlon;
        }
		
     // System.out.println("risultato provola:"+data+" "+ora+" "+ar+" "+indlat+" "+indlon);
		JSONArray fermatePartenza = new JSONArray();
		JSONArray fermateArrivo = new JSONArray();
		JSONArray codPercorsi = new JSONArray();
		JSONArray codPercorsiFinali = new JSONArray();
		JSONArray dataPercorsiFinali = new JSONArray();
		JSONArray oraPartenzaPercorsiFinali = new JSONArray();
		JSONArray oraArrivoPercorsiFinali = new JSONArray();
		JSONArray numeroPullmanFinali = new JSONArray();



		
		JSONObject js = new JSONObject();

		
		//QUERY
		queryDB db = new queryDB();
		ResultSet rs,rs2,rs3;
		try {
			rs = db.query("select nome,indlat,indlon from fermata");
			while(rs.next()){
				String nomeFermata=rs.getString("nome");
				double fLat=rs.getDouble("indlat");
				double fLon=rs.getDouble("indlon");
				double d1=distance(partenzalat,fLat , partenzalon, fLon);
				double d2=distance(arrivolat,fLat , arrivolon, fLon);
				//se la distanza tra la nostra fermata di partenza e quella nel database è minore a 1,5km allora aggiungo la fermata al nostro jsonArray
				if(d1<1500) {
					fermatePartenza.put(nomeFermata);
				} //stessa cosa per la fermata di arrivo
				if(d2<1500) {
					fermateArrivo.put(nomeFermata);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    /* controllo le fermate che ha salvato
			try {
				for(int i=0;i<fermatePartenza.length();i++) {
				System.out.println("fermatap "+i+" "+fermatePartenza.getString(i));
				}
				for(int i=0;i<fermateArrivo.length();i++) {
					System.out.println("fermataarr "+i+" "+fermateArrivo.getString(i));
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/	
//adesso in fermatePartenza abbiamo tutte le fermate di partenza vicine al nostro punto di partenza
// e in fermateArrivo tutte le fermate di arrivo vicine al nostro punto di arrivo 

		
		//andiamo a prenderci tutti i cod percorsi che comprendono una nostra fermata di partenza e una nostra di arrivo 
		if(fermatePartenza.length()>0 && fermateArrivo.length()>0) {
		try {
			for(int i=0;i<fermatePartenza.length();i++) {
			for(int j=0;j<fermateArrivo.length();j++) {
				System.out.println(i+" aaa "+j);
				rs2 = db.query("select cod_percorso from passaggio where nome_fermata='"+fermatePartenza.getString(i)+"' and cod_percorso in("
						+ "select cod_percorso from passaggio where nome_fermata='"+fermateArrivo.getString(j)+"')");
			//verificare se questo while va dentro o fuori 
			    while (rs2.next()) {
				  System.out.println("percorso: "+rs2.getString("cod_percorso"));
				  codPercorsi.put(rs2.getString("cod_percorso"));
			    }
			
		}}} catch (JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		//query in tabella corsa andiamo a verificare quali sono i percorsi trovati fin ora che corrispondono con la data e con ar (andata o ritorno)
		if(codPercorsi.length()>0) {
		for (int i=0;i<codPercorsi.length();i++) {
		try {
			rs3 = db.query("select * from corsa where data_corsa='"+data+"' and ar='"+ar+"' and cod_percorso='"+codPercorsi.getString(i)+"'");
			//verificare se questo while va dentro o fuori 
		    while (rs3.next()) {
			  System.out.println("percorso buoni finali: "+rs3.getString("cod_percorso"));
			  codPercorsiFinali.put(rs3.getString("cod_percorso"));
			  dataPercorsiFinali.put(rs3.getString("data_corsa"));
			  oraPartenzaPercorsiFinali.put(rs3.getString("ora_partenza"));
			  oraArrivoPercorsiFinali.put(rs3.getString("ora_arrivo"));
			  numeroPullmanFinali.put(rs3.getString("numero"));
		    }
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		}
		
		//risposta inviamo i dati raccolti
		if(codPercorsiFinali.length()>0) {
			try {
				js.put("codPercorsi", codPercorsiFinali);
				js.put("dataPercorsi", dataPercorsiFinali);
				js.put("oraArrivo", oraArrivoPercorsiFinali);
				js.put("oraPartenza", oraPartenzaPercorsiFinali);
				js.put("numero", numeroPullmanFinali);
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.getWriter().write(js.toString());
	
	}

		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("sono in post di provola");
	}
	
	
	
	/**
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * @returns Distance in Meters
	 * (l'algoritmo non è precisissimo perchè vede la distanza da un posto all'altro senza tener
	 * conto delle vie quindi traccia una retta e misura, ma per il nostro scopo va più che bene!)
	 */
	public double distance(double lat1, double lat2, double lon1, double lon2) {
        double el1=0;
        double el2=0;
	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
	
}
