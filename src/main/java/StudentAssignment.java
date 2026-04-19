

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBase.DBConnection;

@WebServlet("/StudentAssignment")
public class StudentAssignment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StudentAssignment() {
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

        request.getRequestDispatcher("/Student/student_assignment.jsp").include(request, response);

        String studentName = (String) session.getAttribute("username");

        try (Connection con = DBConnection.getConnection()) {
            // THE WATERFALL QUERY: Filters out already submitted tasks
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
                           "ORDER BY a.due_date ASC";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, studentName);
            ps.setString(2, studentName);
            ResultSet rs = ps.executeQuery();

            out.println("<script>");
            out.println("document.getElementById('username').innerText = '" + studentName + "';");
            out.println("document.getElementById('profile_logo').innerText = '" + studentName.toUpperCase().charAt(0) + "';");

            out.println("const taskDueContainer = document.querySelector('.task_due');");
            out.println("if(taskDueContainer) { taskDueContainer.innerHTML = '<h2>Task Due</h2>'; }");

            int taskCounter = 0;
            boolean hasTasks = false;

            while (rs.next()) {
                hasTasks = true;
                taskCounter++;
                int id = rs.getInt("assignment_id");
                String title = rs.getString("title");
                String desc = rs.getString("instruction").replace("'", "\\'");
                int daysLeft = rs.getInt("days_left");

                String timeText = (daysLeft < 0) ? Math.abs(daysLeft) + " days overdue" : (daysLeft == 0 ? "Due Today" : daysLeft + " days left");
                String color = (daysLeft <= 1) ? "#ff4d4d" : "#2bf02b";
                String statusClass = (daysLeft < 0) ? "status_overdue" : "status_pending";

                // ZERO-CLICK LOGIC: Auto-assign the FIRST task to the hidden input
                if (taskCounter == 1) {
                    out.println("document.getElementsByName('assignment_id')[0].value = '" + id + "';");
                    out.println("document.getElementById('fileName').innerText = 'Submit for: " + title + "';");
                    out.println("document.getElementById('fileName').style.color = 'var(--btn_g_right)';");
                }

                out.println("   taskDueContainer.innerHTML += `");
                out.println("       <div id='due_subcard' style='margin-bottom:15px; border-left: 5px solid " + (taskCounter == 1 ? color : "#333") + "; opacity: " + (taskCounter == 1 ? "1" : "0.6") + "'>");
                out.println("           <h3>" + title + (taskCounter > 1 ? " 🔒" : "") + "</h3>");
                out.println("           <p id='description'>" + desc + "</p>");
                out.println("           <span id='assignment_time_left' style='color:" + color + "; font-weight:600;'>" + timeText + "</span><br>");
                out.println("           <p id='assignment_status' class='" + statusClass + "'>" + (taskCounter == 1 ? "Active" : "Locked") + "</p>");
                out.println("       </div>`;");
            }

            if (!hasTasks) {
                out.println("taskDueContainer.innerHTML += '<p style=\"color:var(--gray_text); padding:20px;\">All assignments completed! 🎓</p>';");
                // Hide the upload section if nothing is due
                out.println("document.querySelector('.upload_assignment_section').style.display = 'none';");
            }
            out.println("</script>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
