

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import DataBase.DBConnection;

/**
 * Servlet implementation class AdminAssignments
 */
@WebServlet("/AdminAssignments")
public class AdminAssignments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAssignments() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        if(session == null || !"admin".equals(session.getAttribute("userrole"))) {
            response.sendRedirect("admin_authentication.jsp");
            return;
        }
        
        // 1. Include the JSP
        request.getRequestDispatcher("/Admin/assignments.jsp").include(request, response);
        
        try(Connection con = DBConnection.getConnection(); 
            Statement st = con.createStatement()){ // Declare 'st' once here
            
            String adminName = (String) session.getAttribute("username");
            
            // 2. Start SINGLE script block for all injections
            out.println("<script>");
            out.println("document.addEventListener('DOMContentLoaded', function() {");
            
            // Update Profile Info
            out.println("document.getElementById('username').innerText = '" + adminName + "';");
            out.println("document.getElementById('profile_logo').innerText = '" + adminName.toUpperCase().charAt(0) + "';");
            
            // --- PART A: Fetch and inject Courses ---
            ResultSet rsCourses = st.executeQuery("SELECT course_id, course_name FROM courses WHERE is_active = true");
            while(rsCourses.next()) {
                int cId = rsCourses.getInt("course_id");
                String cName = rsCourses.getString("course_name");
                out.println("document.getElementById('courses').innerHTML += `<option value='" + cId + "'>" + cName + "</option>`;");
            }
            rsCourses.close(); // Close result set before reusing statement

            // --- PART B: Fetch and inject Student Directory ---
            String query = "SELECT s.student_id, s.full_name, a.title, a.due_date, sub.submission_id " +
                    "FROM assignments a " +
                    "/* 1. Link every assignment to every student in that course */ " +
                    "JOIN students s ON s.course_id = a.course_id " +
                    "/* 2. Check if a submission exists for that specific pair */ " +
                    "LEFT JOIN submissions sub ON s.student_id = sub.student_id AND a.assignment_id = sub.assignment_id " +
                    "ORDER BY a.created_at DESC, s.full_name ASC";
            
            ResultSet rs = st.executeQuery(query);
            out.println("const tbody = document.querySelector('.submission_tracker_table tbody');");
            out.println("tbody.innerHTML = '';"); 

            int index = 1; // FIX: Move this ABOVE the while loop
            while(rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("full_name");
                String aTitle = rs.getString("title");
                
                // Safety check for date format
                String dDate = rs.getString("due_date");
                if (dDate == null || dDate.isEmpty()) { dDate = "-"; }
                
                boolean isSubmitted = rs.getObject("submission_id") != null;
                String status = isSubmitted ? "Submitted" : "Missing"; 
                String statusClass = isSubmitted ? "status_submitted" : "status_missing";

                out.println("tbody.innerHTML += `<tr>" +
                    "<td hidden >" + id + "</td>" +
                    "<td>" + index + "</td>" +
                    "<td>" + name + "</td>" +
                    "<td>" + aTitle + "</td>" +
                    "<td>" + dDate + "</td>" +
                    "<td><span class='status_badge " + statusClass + "'>" + status + "</span></td>" +
                    "<td><button class='review_btn'>Review</button></td>" +
                    "</tr>`;");
                
                index++; 
            }
            
            out.println("});"); // End of DOMContentLoaded
            out.println("</script>");
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}