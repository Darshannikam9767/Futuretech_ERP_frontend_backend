

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBase.DBConnection;

@WebServlet("/StudentDashboard")
public class StudentDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public StudentDashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (session == null || !"student".equals(session.getAttribute("userrole"))) {
            response.sendRedirect("student_authentication.jsp");
            return;
        }

        request.getRequestDispatcher("Student/student_dashboard.jsp").include(request, response);

        try (Connection con = DBConnection.getConnection()) {
            String studentName = (String) session.getAttribute("username");

            // WATERFALL QUERY: Gets only top 2 pending assignments not yet submitted
            String query = "SELECT a.assignment_id, a.title, a.instruction, a.due_date, " +
                           "DATEDIFF(a.due_date, CURDATE()) as days_left " +
                           "FROM assignments a " +
                           "JOIN students s ON a.course_id = s.course_id " +
                           "WHERE s.full_name = ? " +
                           "AND a.assignment_id NOT IN (" +
                           "    SELECT sub.assignment_id FROM submissions sub " +
                           "    JOIN students st ON sub.student_id = st.student_id " +
                           "    WHERE st.full_name = ?" +
                           ") " +
                           "ORDER BY a.due_date ASC LIMIT 2";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, studentName);
            ps.setString(2, studentName);
            ResultSet rs = ps.executeQuery();

            out.println("<script>");
            // 1. Sync Profile Info
            out.println("document.getElementById('username').innerText = '" + studentName + "';");
            out.println("document.getElementById('profile_logo').innerText = '" + studentName.toUpperCase().charAt(0) + "';");

            // 2. Target the Dashboard Task Container
            out.println("const dashTaskContainer = document.querySelector('.task_due');");
            out.println("if(dashTaskContainer) {");
            out.println("   dashTaskContainer.innerHTML = '<h2>Task Due</h2>';");

            int taskCounter = 0;
            boolean hasTasks = false;

            while (rs.next()) {
                hasTasks = true;
                taskCounter++;
                int id = rs.getInt("assignment_id");
                String title = rs.getString("title");
                String instruction = rs.getString("instruction").replace("'", "\\'");
                int daysLeft = rs.getInt("days_left");

                String timeText = (daysLeft < 0) ? Math.abs(daysLeft) + " days overdue" : (daysLeft == 0 ? "Due Today" : daysLeft + " days left");
                String color = (daysLeft <= 1) ? "#ff4d4d" : "#2bf02b";
                String statusClass = (daysLeft < 0) ? "status_overdue" : "status_pending";
                String statusText = (daysLeft < 0) ? "Overdue" : "Pending";

                // ZERO-CLICK AUTO-TARGET: Assign oldest ID to hidden input on Dashboard
                if (taskCounter == 1) {
                    out.println("const hiddenInput = document.getElementsByName('assignment_id')[0];");
                    out.println("if(hiddenInput) { hiddenInput.value = '" + id + "'; }");
                    
                    out.println("const fileNameDisp = document.getElementById('fileName');");
                    out.println("if(fileNameDisp) { ");
                    out.println("   fileNameDisp.innerText = 'Submit for: " + title + "';");
                    out.println("   fileNameDisp.style.color = 'var(--btn_g_right)';");
                    out.println("}");
                }

                // 3. Injecting HTML using Dashboard IDs
                out.println("   dashTaskContainer.innerHTML += `");
                out.println("       <div id='subcard' style='margin-bottom:12px; border-left: 4px solid " + (taskCounter == 1 ? color : "#333") + "; opacity: " + (taskCounter == 1 ? "1" : "0.7") + "'>");
                out.println("           <h3>" + title + (taskCounter > 1 ? " 🔒" : "") + "</h3>");
                out.println("           <p id='description' style='font-size:0.85rem;'>" + instruction + "</p>");
                out.println("           <span id='time_left' style='color:" + color + "; font-weight:600; font-size:0.85rem;'>" + timeText + "</span>");
                out.println("           <p id='status' class='" + statusClass + "' style='font-size:0.7rem;'>" + (taskCounter == 1 ? "Active" : "Locked") + "</p>");
                out.println("       </div>`;");
            }

            if (!hasTasks) {
                out.println("   dashTaskContainer.innerHTML += '<p style=\"color:var(--gray_text); padding:10px;\">No upcoming tasks.</p>';");
                out.println("   const uploadCard = document.querySelector('.upload_assignment');");
                out.println("   if(uploadCard) { uploadCard.style.display = 'none'; }");
            }
            out.println("}");
            out.println("</script>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	

}
