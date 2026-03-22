

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import DataBase.DBConnection;


@WebServlet("/AddCourseServlet")
public class AddCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public AddCourseServlet() {
        super();
       
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String courseName = request.getParameter("course_name");
        String feesStr = request.getParameter("fees").replaceAll("[^\\d.]", "");
        double fees = Double.parseDouble(feesStr);
        String duration = request.getParameter("duration");
        String description = request.getParameter("description");
        
        try(Connection con = DBConnection.getConnection()){
            
            String addCourseQuery = "insert into courses(course_name, fees, description, duration) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(addCourseQuery);
            ps.setString(1, courseName);
            ps.setDouble(2, fees);
            ps.setString(3, description);
            ps.setString(4, duration);
            
            int rows = ps.executeUpdate();
            
            if(rows > 0) {
                response.sendRedirect("AdminCourseManagement?status=success");
            } else {
                response.sendRedirect("AdminCourseManagement?status=fail");
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminCourseManagement?status=error");
        }
		
	}

}
