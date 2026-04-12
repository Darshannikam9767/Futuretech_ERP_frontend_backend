

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
import java.sql.Statement;

import DataBase.DBConnection;

/**
 * Servlet implementation class AdminRegister
 */
@WebServlet("/AdminRegister")
public class AdminRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminRegister() {
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
		
		request.getRequestDispatcher("/Admin/register.jsp").include(request, response);
		
		try(Connection con = DBConnection.getConnection()){
			Statement st = con.createStatement();
			
			String adminName = (String) session.getAttribute("username");
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + adminName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+adminName.toUpperCase().charAt(0)+"'");
			out.println("</script>");
			
			ResultSet rsCourses = st.executeQuery("SELECT course_id, course_name FROM courses WHERE is_active = true");
			out.println("<script>");
			// 2. Clear the dropdown once before adding new options
			out.println("document.getElementById('courses').innerHTML = '';"); 

			while(rsCourses.next()) {
			    // 3. Get values from Java Resultset
			    int cId = rsCourses.getInt("course_id");
			    String cName = rsCourses.getString("course_name");
			    
			    // 4. Carefully concatenate Java variables into the JavaScript string
			    out.println("document.getElementById('courses').innerHTML += `<option value='" + cId + "'>" + cName + "</option>`;");
			}
			out.println("</script>");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


}
