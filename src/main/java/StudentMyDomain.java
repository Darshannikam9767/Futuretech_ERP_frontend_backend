

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBase.DBConnection;

@WebServlet("/StudentMyDomain")
public class StudentMyDomain extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public StudentMyDomain() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		if(session == null || !"student".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("student_authentication.jsp");
			return;
		}
		request.getRequestDispatcher("/Student/student_my_domain.jsp").include(request, response);
		
		try(Connection con = DBConnection.getConnection()){
			
			String studentName = (String) session.getAttribute("username");
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + studentName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+studentName.toUpperCase().charAt(0)+"'");
			out.println("</script>");
			
			String query = "SELECT c.course_name, c.duration, c.fees, c.description, s.created_at " +
	                   "FROM students s JOIN courses c ON s.course_id = c.course_id " +
	                   "WHERE s.full_name = ?";
	    
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, studentName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String courseName = rs.getString("course_name");
				String duration = rs.getString("duration");
				double courseFees = rs.getDouble("fees");
				String enrollmentDate = rs.getString("created_at").split(" ")[0];
	        
				String rawDesc = rs.getString("description");
				String cleanDesc = (rawDesc != null) ? rawDesc.replace("'", "\\'").replace("\n", " ") : "No description available.";

				out.println("<script>");
				out.println("document.getElementById('course_title').innerText = '" + courseName + "';");
				out.println("document.getElementById('start_date').innerText = '" + enrollmentDate + "';");
				out.println("document.getElementById('duration').innerText = '" + duration + "';");
				out.println("document.getElementById('fees').innerText = '₹ " + courseFees + "';");
				out.println("document.getElementById('description').innerText = '" + cleanDesc + "';");
				out.println("</script>");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
