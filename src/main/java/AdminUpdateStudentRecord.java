

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;

import DataBase.DBConnection;

@WebServlet("/AdminUpdateStudentRecord")
public class AdminUpdateStudentRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminUpdateStudentRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int real_student_id= Integer.parseInt(request.getParameter("real_student_id"));
    	String student_name=request.getParameter("student_name");
		String student_contact=request.getParameter("student_contact");
		String student_email=request.getParameter("student_email");
		String student_course=request.getParameter("student_course");
//		String student_progress_value=request.getParameter("student_progress_value");
		String student_status=request.getParameter("student_status");
		
		try(Connection con=DBConnection.getConnection()){
			PreparedStatement pscourse=con.prepareStatement("select course_id from courses where course_name=?");
			pscourse.setString(1, student_course);
			
			ResultSet rscourseid=pscourse.executeQuery();
			int course_id = 0;
			
			if(rscourseid.next())
				course_id=rscourseid.getInt("course_id");
			
			
			String updateQuery="update students set full_name =?, contact =?,email=?,course_id=?,status=? where student_id=?";
			
			PreparedStatement ps = con.prepareStatement(updateQuery);
			
			ps.setString(1, student_name);
			ps.setString(2, student_contact);
			ps.setString(3, student_email);
			ps.setInt(4, course_id);
//			ps.setString(5, student_progress_value);
			ps.setString(5, student_status);
			ps.setInt(6, real_student_id);
			
			int result = ps.executeUpdate();
			
			if(result>0) {
				response.sendRedirect("AdminStudentDirectory?update=success");
			}
		}catch(Exception e) {
			System.out.println("Error during updating student record");
			e.printStackTrace();
			response.sendRedirect("AdminStudentDirectory?update=error");
		}
	}


}
