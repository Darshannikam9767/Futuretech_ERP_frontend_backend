

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;


@WebServlet("/UpdateCourseServlet")
public class UpdateCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public UpdateCourseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id = Integer.parseInt(request.getParameter("course_id"));
		String courseName = request.getParameter("course_name");
		int fees = Integer.parseInt(request.getParameter("fees"));
		int duration =  Integer.parseInt(request.getParameter("duration"));
		String description =  request.getParameter("description");
		
		try(Connection con = DBConnection.getConnection()){
				String updateQuery="update courses set course_name=?,fees=?,description=?,duration=? where course_id=?";
				PreparedStatement ps = con.prepareStatement(updateQuery);
				ps.setString(1, courseName);
				ps.setDouble(2, fees);
				ps.setString(3, description);
				ps.setInt(4, duration);
				ps.setInt(5, id);
				
				int result =ps.executeUpdate();
				if(result>0) {
					response.sendRedirect("AdminCourseManagement?update=success");
				}
					
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("AdminCourseManagement?update=error");

		}
	}

}
