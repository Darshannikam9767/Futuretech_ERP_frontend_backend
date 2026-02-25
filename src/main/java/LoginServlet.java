import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import DataBase.DBConnection;
import java.sql.*;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        System.out.println(email+"\n"+password);
        
        String referer = request.getHeader("referer");
        String role = "student"; 
        if (referer != null && referer.contains("admin_authentication.jsp")) {
        	role = "admin";
        }
        
        try(Connection con = DBConnection.getConnection()){
            String query = (role.equals("admin")) 
                ? "select * from admin where email=? and password=?" 
                : "select * from students where email=? and password=?";
            
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("username", rs.getString("full_name"));
                session.setAttribute("userrole", role);
                
                out.println("<script>");
                out.println("alert('Login Successful as " + role + "!');");
                out.println("window.location.href='" + (role.equals("admin") ? "AdminDashboard" : "StudentDashboard") + "';");
                out.println("</script>");
            } else {
                out.println("<script>");
                out.println("alert('Invalid " + role + " Credentials!');");
                out.println("window.location.href='" + referer + "';");
                out.println("</script>");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
