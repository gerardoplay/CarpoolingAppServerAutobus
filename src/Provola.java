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
		String codreg = request.getParameter("cod");
		System.out.println("Provolaq"+codreg);
		
		response.getWriter().write(codreg);
	
	}

		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("sono in post di provola");
	}

		
		
		
	
}
