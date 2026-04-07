

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.http.Part;

import DataBase.DBConnection;


@WebServlet("/StudentSubmissionServlet")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
	    maxFileSize = 1024 * 1024 * 20,      // 20MB (Enough for ZIP/PDF)
	    maxRequestSize = 1024 * 1024 * 50    // 50MB
	)
public class StudentSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public StudentSubmissionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
HttpSession session = request.getSession(false);
        
        // 1. Security Check
        if (session == null || session.getAttribute("studentId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int studentId = (int) session.getAttribute("studentId");
        String assignmentId = request.getParameter("assignment_id"); // Hidden field from JSP
        Part filePart = request.getPart("submission_file");

        // 2. Define the Save Directory
        String uploadPath = getServletContext().getRealPath("/") + "uploads" + File.separator + "submissions";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
        		uploadDir.mkdirs();

        String originalName = filePart.getSubmittedFileName();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = "STU" + studentId + "_ASGN" + assignmentId + "_" + System.currentTimeMillis() + extension;
        
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

       
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO submissions (assignment_id, student_id, file_path, status) VALUES (?, ?, ?, 'Submitted')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(assignmentId));
            ps.setInt(2, studentId);
            ps.setString(3, "uploads/submissions/" + fileName); 
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("StudentAssignment?upload=success");
            } else {
                response.sendRedirect("StudentAssignment?upload=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("StudentAssignment?upload=error");
        }
	}

}
