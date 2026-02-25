

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import DataBase.DBConnection;

@WebServlet("/StudentDashboard")
public class StudentDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public StudentDashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if(session == null || !"student".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("student_authentication.jsp");
			return;
		}
		request.getRequestDispatcher("Student/student_dashboard.jsp").include(request, response);
		try(Connection con=DBConnection.getConnection()){
			Statement st =con.createStatement();
			
			String studentName = (String) session.getAttribute("username");
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + studentName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+studentName.toUpperCase().charAt(0)+"'");
			out.println("</script>");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
