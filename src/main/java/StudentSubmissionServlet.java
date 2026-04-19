

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
        
        // 1. Correct Security Check (Using username instead of studentId)
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("student_authentication.jsp"); // Match your login page name
            return;
        }

        String studentName = (String) session.getAttribute("username");
        String assignmentId = request.getParameter("assignment_id");
        Part filePart = request.getPart("submission_file");

        // Safety check for file
        if (filePart == null || filePart.getSubmittedFileName().isEmpty()) {
            response.sendRedirect("StudentAssignment?upload=missing_file");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            // 2. Lookup the student_id since it's not in session
            String idLookup = "SELECT student_id FROM students WHERE full_name = ?";
            PreparedStatement psId = con.prepareStatement(idLookup);
            psId.setString(1, studentName);
            java.sql.ResultSet rsId = psId.executeQuery();

            if (rsId.next()) {
                int actualStudentId = rsId.getInt("student_id");

                // 3. Define the Save Directory
                String uploadPath = getServletContext().getRealPath("/") + "uploads" + File.separator + "submissions";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String originalName = filePart.getSubmittedFileName();
                String extension = originalName.substring(originalName.lastIndexOf("."));
                String fileName = "STU" + actualStudentId + "_ASGN" + assignmentId + "_" + System.currentTimeMillis() + extension;
                
                filePart.write(uploadPath + File.separator + fileName);

                // 4. Database Entry
                String sql = "INSERT INTO submissions (assignment_id, student_id, file_path, status) VALUES (?, ?, ?, 'Submitted')";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(assignmentId));
                ps.setInt(2, actualStudentId);
                ps.setString(3, "uploads/submissions/" + fileName); 
                
                int result= ps.executeUpdate();
                String source = request.getParameter("source");
                String redirectTarget;

                // Determine where to send the student back to
                if ("dashboard".equals(source)) {
                    redirectTarget = "StudentDashboard";
                } else {
                    redirectTarget = "StudentAssignment";
                }

                if (result > 0) {
                    response.sendRedirect(redirectTarget + "?upload=success");
                } else {
                    response.sendRedirect(redirectTarget + "?upload=failed");
                }}
        } catch (Exception e) {
            e.printStackTrace();
            // This prevents the "Connection Reset" by ensuring a response is sent even on error
            if (!response.isCommitted()) {
                response.sendRedirect("StudentAssignment?upload=error");
            }
        }
    }
}
