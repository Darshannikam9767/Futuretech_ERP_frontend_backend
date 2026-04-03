

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
//			Statement st = con.createStatement();
			
			String adminName = (String) session.getAttribute("username");
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + adminName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+adminName.toUpperCase().charAt(0)+"'");
			out.println("</script>");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		String formType = request.getParameter("formType");

        try (Connection con = DBConnection.getConnection()) {
            if ("student".equals(formType)) {
                String sql = "INSERT INTO students (name, contact, email, domain) VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("name"));    // JSP: name="name"
                    ps.setString(2, request.getParameter("contact")); // JSP: name="contact"
                    ps.setString(3, request.getParameter("email"));   // JSP: name="email"
                    ps.setString(4, request.getParameter("domain"));  // JSP: name="domain"
                    ps.executeUpdate();
                }
            } else if ("admin".equals(formType)) {
                String sql = "INSERT INTO admins (name, email, password) VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("admin_name"));
                    ps.setString(2, request.getParameter("admin_email"));
                    ps.setString(3, request.getParameter("admin_pass"));
                    ps.executeUpdate();
                }
            }
//            response.sendRedirect("AdminRegister?msg=success");
            
        } catch (Exception e) {
            e.printStackTrace(); 
            response.sendRedirect("AdminRegister?msg=error");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
