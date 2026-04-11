

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;


@WebServlet("/AdminAssignmentServlet")
//REMOVED @MultipartConfig since we are text-only now
public class AdminAssignmentServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
     // Capture parameters - Ensure names match the HTML 'name' attributes
     String title = request.getParameter("assignment_name");
     String courseId = request.getParameter("student_course");
     String dueDate = request.getParameter("due_date");
     String description = request.getParameter("description");

     // Basic Validation: Ensure no null values are processed
     if (title == null || courseId == null || dueDate == null) {
         response.sendRedirect("AdminAssignments?status=invalid");
         return;
     }

     try (Connection con = DBConnection.getConnection()) {
         String sql = "INSERT INTO assignments (course_id, title, instruction, due_date) VALUES (?, ?, ?, ?)";
         PreparedStatement ps = con.prepareStatement(sql);
         
         ps.setInt(1, Integer.parseInt(courseId)); // This requires the ID from the JS injection
         ps.setString(2, title);
         ps.setString(3, description);
         ps.setString(4, dueDate);

         int result = ps.executeUpdate();
         
         if (result > 0) {
             response.sendRedirect("AdminAssignments?status=created");
         } else {
             response.sendRedirect("AdminAssignments?status=failed");
         }
         
     } catch (Exception e) {
         e.printStackTrace();
         response.sendRedirect("AdminAssignments?status=error");
     }
 }
}