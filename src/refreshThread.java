import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class refreshThread extends TimerTask{

	public refreshThread(){

	}
	@Override
	public void run(){
		try{
			updateDB udb = new updateDB();
			queryDB qdb = new queryDB();
			ResultSet rs =  qdb.query("select * from percorso where stato='programmato'");
			GregorianCalendar gc = new GregorianCalendar();
			double sumani =0, sumcos=0,sumdist = 0, sumdistind=0, sumaniind=0, sumcosind=0;

			while(rs.next()){
				//ystem.out.println(rs.getString("indirizzo"));
				String utente = rs.getString("nomeutente");
				String cod = rs.getString("cod");
				String dateper = rs.getString("data");

				GregorianCalendar gcper = new GregorianCalendar(Integer.parseInt(dateper.substring(6,10)),
						Integer.parseInt(dateper.substring(3,5))-1, 
						Integer.parseInt(dateper.substring(0, 2)));

				if(gc.compareTo(gcper)>0){
					double dist = getDistanzaPooling(rs.getString("cod"));
					double distind = getDistanzaIndividual(rs.getString("cod"));
					udb.inserimento("update percorso set stato='completo' where cod='"+cod+"'");
					ResultSet rscontr = qdb.query("select count(cod) from richiesta where codpercorso="+cod);
					rscontr.next();
					if(rscontr.getInt("count(cod)")!=0){
						ResultSet rscount =qdb.query("select * from utente where nomeutente='"+utente+"'");
						rscount.next();
						int isd = (rscount.getInt("nrper")+1);
						int isf = rscount.getInt("feedp")+1;
						udb.inserimento("update utente set nrper="+isd+", feedp="+isf+" where nomeutente='"+utente+"'");
					}
					ResultSet rs1 = qdb.query("select * from versione where cod=(select codversione from autoutente where targa='"+ rs.getString("targa")+"')");
					rs1.next();
					sumdist +=dist;
					sumani += (rs1.getFloat("anidride")*dist)/10000;
					sumcos += dist/rs1.getFloat("consumo");

					sumdistind += distind;
					sumaniind += (rs1.getFloat("anidride")*distind)/10000;
					sumcosind += distind/rs1.getFloat("consumo");
				}
			}
			System.out.println("carpoolinf"+sumdist+" "+sumani+" "+sumcos+"  senza: "+sumdistind+" "+sumaniind+" "+sumcosind);
			udb.inserimento("insert into sistema(poolingdist,poolingani,poolingcos,unpoolingdist,unpoolingani,unpoolingcos) values("+sumdist+","+sumani+","+sumcos+","+sumdistind+","+sumaniind+","+sumcosind+")");


		}catch(SQLException e){
			e.printStackTrace();
		}

	}
	private double getDistanzaIndividual(String codper) throws SQLException {
		// TODO Auto-generated method stub
		queryDB qdb = new queryDB();
		updateDB udb = new updateDB();
		ResultSet rs = qdb.query("select * from percorso where cod='"+codper+"'");
		rs.next();
		String str_origin = "origin="+rs.getDouble("destlat")+","+rs.getDouble("destlon");
		String str_dest = "destination="+rs.getDouble("partlat")+","+rs.getDouble("partlon"); 
		ResultSet rs1 = qdb.query("select * from richiesta where codpercorso='"+codper+"'");
		String sensor = "sensor=false";         
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		Double somma=distanceCalculator(url);

		rs = qdb.query("select * from richiesta where cod='"+codper+"'");
		while(rs.next()){
			str_origin = "origin="+rs.getDouble("indlat")+","+rs.getDouble("indlon");
			str_dest = "destination="+40.773720 +","+ 14.794522; 
			rs1 = qdb.query("select * from richiesta where codpercorso='"+codper+"'");
			rs1.next();
			ResultSet rs2 = qdb.query("select * from utente where nomeutente='"+rs1.getString("nomeutenterichiedente")+"'");
			rs2.next();
			int vvv=rs2.getInt("nrric")+1;
			int zzz = rs2.getInt("feedr")+1;
			udb.inserimento("update utente set nrric="+vvv +", feedr="+zzz+" where nomeutente='"+rs1.getString("nomeutenterichiedente")+"'");
			sensor = "sensor=false";         
			parameters = str_origin+"&"+str_dest+"&"+sensor;
			output = "json";
			url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
			somma+=distanceCalculator(url);
		}
		return somma;

	}
	private double distanceCalculator(String url){
		double dis=0;
		try {
			JSONObject js =downloadUrl(url);
			JSONArray jRoutes = js.getJSONArray("routes");       
			JSONArray jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");
			JSONObject distance = jLegs.getJSONObject(0).getJSONObject("distance");
			dis = distance.getDouble("value")/1000;
			System.out.println(dis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dis ;
	}

	private double getDistanzaPooling(String codper) throws SQLException {
		// TODO Auto-generated method stub
		queryDB qdb = new queryDB();

		ResultSet rs = qdb.query("select * from percorso where cod='"+codper+"'");
		rs.next();
		String str_origin = "origin="+rs.getDouble("destlat")+","+rs.getDouble("destlon");
		String str_dest = "destination="+rs.getDouble("partlat")+","+rs.getDouble("partlon"); 
		ResultSet rs1 = qdb.query("select * from richiesta where codpercorso='"+codper+"'");
		String waipoints="";
		while(rs1.next()){
			if(!rs1.isLast())
				waipoints+=rs1.getDouble("indlat")+","+rs1.getDouble("indlon")+"|" ;
			else
				waipoints+=rs1.getDouble("indlat")+","+rs1.getDouble("indlon") ;
		}
		String sensor = "sensor=false";         
		String parameters = str_origin+"&"+str_dest+"&"+waipoints+"&"+sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		double dis=0;
		try {
			JSONObject js =downloadUrl(url);
			JSONArray jRoutes = js.getJSONArray("routes");       
			JSONArray jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");
			JSONObject distance = jLegs.getJSONObject(0).getJSONObject("distance");
			dis = distance.getDouble("value")/1000;
			System.out.println(dis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dis ;
	}



	private JSONObject downloadUrl(String strUrl) throws IOException{
		String data = "";
		JSONObject js =null;
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.connect();

			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb  = new StringBuffer();

			String line = "";
			while( ( line = br.readLine())  != null){
				sb.append(line);
			}

			data = sb.toString();
			js = new JSONObject(data);
			br.close();

		}catch(Exception e){
			//Log.d("Exception while downloading url", e.toString());
		}finally{
			iStream.close();
			urlConnection.disconnect();
		}
		return js;
	}



}
