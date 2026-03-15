

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;


@WebServlet("/AdminDeleteStudentRecord")
public class AdminDeleteStudentRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public AdminDeleteStudentRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int real_student_id = Integer.parseInt(request.getParameter("real_student_id"));
		System.out.println("student id = "+real_student_id+ " for deleting");
		try(Connection con = DBConnection.getConnection()){
			String deleteQuery="update students set is_accesable = false where student_id = ?";
			
			PreparedStatement psDelete = con.prepareStatement(deleteQuery);
			psDelete.setInt(1, real_student_id);
			
			int result = psDelete.executeUpdate();
			if(result>0) {
				response.sendRedirect("AdminStudentDirectory?delete=success");
			}else {
				response.sendRedirect("AdminStudentDirectory?delete=fail");
			}
		}catch(Exception e) {
			System.out.println("error while deleting student record");
			e.printStackTrace();
			response.sendRedirect("AdminStudentDirectory?delete=error");
		}
	}

}
