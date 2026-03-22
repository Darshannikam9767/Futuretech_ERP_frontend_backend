

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBase.DBConnection;

@WebServlet("/StudentProfile")
public class StudentProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public StudentProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		
		if(session == null || !"student".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("student_authentication.jsp");
			return;
		}
		
		request.getRequestDispatcher("/Student/student_profile.jsp").include(request, response);
		
		try(Connection con = DBConnection.getConnection()){
			
			String studentName = (String) session.getAttribute("username");
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + studentName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+studentName.toUpperCase().charAt(0)+"'");
			out.println("</script>");
			
			
			String query = "SELECT s.full_name, s.contact, s.email, c.course_name FROM students s " +
                    "JOIN courses c ON s.course_id = c.course_id WHERE s.full_name = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, studentName);
			ResultSet rs =ps.executeQuery();
			
			out.println("<script>");
				if(rs.next()) {
					out.println("document.querySelector('.student_name').innerText = '" + rs.getString("full_name") + "';");
		            out.println("document.querySelector('.course_name').innerText = '" + rs.getString("course_name") + "';");
		            out.println("document.getElementById('email_id').innerText = '" + rs.getString("email") + "';");
		            out.println("document.getElementById('contact_number').innerText = '" + rs.getString("contact") + "';");
		            out.println("document.getElementById('profile_logo_div_name').innerText = '" + rs.getString("full_name").toUpperCase().charAt(0) + "';");				}
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
