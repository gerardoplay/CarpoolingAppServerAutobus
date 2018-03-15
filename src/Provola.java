import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Provola")
public class Provola extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	public Provola() {
		
		super();
		// TODO Auto-generated constructor stub
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("hello0");
		//String email = request.getParameter("email");
		String codreg = request.getParameter("codregistrazione");
		System.out.println("Provolaq"+codreg);
		
		response.getWriter().write("vai fortissimooooo");
		/*updateDB ins = new updateDB();
		String contr = ins.inserimento("update utente set stato='registrato' where codregistrazione like '"+codreg+"' and stato like 'inattivo'");
		if(contr.equalsIgnoreCase("1")){
			response.getWriter().write("Registrazione conclusa con successo");
		}else
			if(contr.equalsIgnoreCase("0")){
				response.getWriter().write("Utente già registrato");
			}else
				response.getWriter().write("CodErrore: "+contr);
				*/
	}

		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("sono in post di provola");
	}

		
		
		
	
}
