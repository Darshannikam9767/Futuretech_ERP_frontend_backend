

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;


@WebServlet("/DeleteCourseServlet")
public class DeleteCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public DeleteCourseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("course_id");
		
		if (id == null || id.trim().isEmpty()) {
	        response.sendRedirect("AdminCourseManagement?status=error");
	        return;
	    }

        try (Connection con = DBConnection.getConnection()) {
            
            String deleteQuery = "update courses set is_active=false where course_id = ?";
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setInt(1, Integer.parseInt(id));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("AdminCourseManagement?status=deleted");
            } else {
                response.sendRedirect("AdminCourseManagement?status=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminCourseManagement?status=error");
        }
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
