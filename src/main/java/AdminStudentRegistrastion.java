

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;

@WebServlet("/AdminStudentRegistrastion")
public class AdminStudentRegistrastion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public AdminStudentRegistrastion() {
        super();
        // TODO Auto-generated constructor stub
    }

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // 1. Security Check
        if (session == null || !"admin".equals(session.getAttribute("userrole"))) {
            response.sendRedirect("admin_authentication.jsp");
            return;
        }

        // 2. Capture Form Parameters
        String name = request.getParameter("student_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone_number");
        String courseIdStr = request.getParameter("student_course");
        String password = request.getParameter("password");

        // 3. Database Operation
        String query = "INSERT INTO students (full_name, email, contact, course_id, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            
            // Safety check for empty course selection
            int courseId = (courseIdStr != null && !courseIdStr.isEmpty()) ? Integer.parseInt(courseIdStr) : 0;
            pst.setInt(4, courseId);
            
            pst.setString(5, password); 

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                // Success: Redirect to the AdminRegister SERVLET so it reloads the JSP with data
                response.sendRedirect("AdminRegister?status=success");
            } else {
                response.sendRedirect("AdminRegister?status=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminRegister?status=error");
        }
    }
}
