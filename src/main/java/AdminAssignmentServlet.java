

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
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
public class AdminAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAssignmentServlet() {
        super();
        
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String assignmentId = request.getParameter("assignment_id");
        int studentId = (int) request.getSession().getAttribute("studentId");
        Part filePart = request.getPart("submission_file");

        String fileName = "SUB_Std" + studentId + "_Asgn" + assignmentId + ".zip";
        String savePath = getServletContext().getRealPath("/") + "uploads/assignments/submissions/" + fileName;
        filePart.write(savePath);

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO submissions (assignment_id, student_id, file_path) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(assignmentId));
            ps.setInt(2, studentId);
            ps.setString(3, "uploads/assignments/submissions/" + fileName);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        
        response.sendRedirect("StudentAssignments?status=submitted");
    }
	
}


