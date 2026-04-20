

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

/**
 * Servlet implementation class AdminCourseManagement
 */
@WebServlet("/AdminCourseManagement")
public class AdminCourseManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminCourseManagement() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
HttpSession session = request.getSession(false);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(session == null ||!"admin".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("admin_authentication.jsp");
			return;
		}
		
		request.getRequestDispatcher("/Admin/course_management.jsp").include(request, response);
		
		try(Connection con = DBConnection.getConnection()){
			Statement st = con.createStatement();
			
			String adminName = (String) session.getAttribute("username");
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + adminName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+adminName.toUpperCase().charAt(0)+"'");
			out.println("</script>");
			
			String stats="select"+
						"(select count(*) from courses ) as total_courses,"+
						"(select count(*) from courses where is_active) as active_courses,"+
						"(select count(*) from students) as total_students";
			
			ResultSet rs = st.executeQuery(stats);
			
			if(rs.next()) {
				int total_courses = rs.getInt("total_courses");
				int total_active_courses=rs.getInt("active_courses");
				int total_students=rs.getInt("total_students");
				
				out.println("<script>");
				out.println("document.getElementById('total_courses').innerText='"+total_courses+"'");
				out.println("document.getElementById('total_active_courses').innerText='"+total_active_courses+"'");
				out.println("document.getElementById('total_students').innerText='"+total_students+"'");
				out.println("</script>");
			}
			
			out.println("<script>");
			StringBuilder cardsHtml = new StringBuilder();
	        rs = st.executeQuery("SELECT * FROM courses WHERE is_active = true");
	        
	        while(rs.next()) {
	            cardsHtml.append("<div class='course_card'>");
	            cardsHtml.append("<div class='title_info'>");
	            cardsHtml.append("<h3 id='course_name'>").append(rs.getString("course_name")).append("</h3>");
	            cardsHtml.append("<h4 id='course_body'>").append(rs.getString("description")).append("</h4>");
	            cardsHtml.append("</div><div class='course_info'>");
	            cardsHtml.append("<h3 hidden id='real_course_id'>").append(rs.getInt("course_id")).append("</h3>");
	            cardsHtml.append("<div class='duration_subcard'><h3 id='duration'>").append(rs.getString("duration")).append(" Days</h3><span id='d_span'>Duration</span></div>");
	            cardsHtml.append("<div class='fees_subcard'><h3 id='fees'>₹ <span>").append(rs.getInt("fees")).append("</span></h3><span id='f_span'>Fee</span></div>");
	            cardsHtml.append("</div><div class='action_div'>");
	            cardsHtml.append("<button class='edit-btn'>EDIT</button>");
	            cardsHtml.append("<button class='delete-btn'>DELETE</button>");
	            cardsHtml.append("</div></div>");
	        }

	        out.println("document.getElementById('course_container').innerHTML = `" + cardsHtml.toString() + "`;");
	        out.println("</script>");
			
			
		}catch(Exception e) {
			out.println("<p>Error loading courses: " + e.getMessage() + "</p>");
			e.printStackTrace();
		}
	}


}
