

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBase.DBConnection;

/**
 * Servlet implementation class ReviewSubmissionServlet
 */
@WebServlet("/ReviewSubmissionServlet")
public class ReviewSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewSubmissionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subId = request.getParameter("subId");
        
        try (Connection con = DBConnection.getConnection()) {
            // Note: I added a fallback for file_name logic
            PreparedStatement ps = con.prepareStatement("SELECT file_path FROM submissions WHERE submission_id = ?");
            ps.setString(1, subId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String relativePath = rs.getString("file_path"); 
                
                // 1. Get Absolute Path
                String absolutePath = getServletContext().getRealPath("/") + relativePath;
                System.out.println("DEBUG: Absolute Path: " + absolutePath);
                
                File file = new File(absolutePath);

                if (file.exists()) {
                    // 2. Extract Filename from the path (e.g., STU1_ASGN3...zip)
                    String fileName = file.getName(); 
                    
                    // 3. Set proper headers for binary download
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                    response.setContentLength((int) file.length()); // Good practice for progress bars
                    
                    // 4. Stream the file
                    Files.copy(file.toPath(), response.getOutputStream());
                    response.getOutputStream().flush();
                    
                } else {
                    // Fallback UI if file is missing on disk
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    String escapedPath = absolutePath.replace("\\", "\\\\");
                    out.println("<script>alert('ERROR: File missing from storage.\\nPath: " + escapedPath + "'); history.back();</script>");
                }
            } else {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('Record not found in Database!'); history.back();</script>");
            }
            
        } catch (Exception e) { 
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error: " + e.getMessage());
        }
    }

}
