

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
import java.sql.Timestamp;

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

            // --- 1. OVERALL COURSE PROGRESS CALCULATION (Weighted 70/30) ---
            // Now fetching 'duration' directly from courses table as an INT
            String statsQuery = "SELECT s.created_at, c.duration, " +
                               "(SELECT COUNT(*) FROM assignments WHERE course_id = s.course_id) as total_tasks, " +
                               "(SELECT COUNT(*) FROM submissions WHERE student_id = s.student_id) as done_tasks " +
                               "FROM students s " +
                               "JOIN courses c ON s.course_id = c.course_id " +
                               "WHERE s.full_name = ?";
            
            PreparedStatement psStats = con.prepareStatement(statsQuery);
            psStats.setString(1, studentName);
            ResultSet rsStats = psStats.executeQuery();

            int overallProgress = 0;
            if (rsStats.next()) {
                int totalTasks = rsStats.getInt("total_tasks");
                int doneTasks = rsStats.getInt("done_tasks");
                int totalCourseDays = rsStats.getInt("duration"); // The new INT duration
                Timestamp joined = rsStats.getTimestamp("created_at");

                // Task Score (70%)
                double taskScore = (totalTasks > 0) ? ((double)doneTasks / totalTasks) * 100 : 0;
                
                // Time Score (30%) - Calculated based on actual days since start
                long diffInMillis = System.currentTimeMillis() - joined.getTime();
                long daysEnrolled = diffInMillis / (1000 * 60 * 60 * 24);
                double timeScore = (totalCourseDays > 0) ? Math.min(((double)daysEnrolled / totalCourseDays) * 100, 100) : 0;
                
                overallProgress = (int)((taskScore * 0.7) + (timeScore * 0.3));
            }

            out.println("<script>");
            // Sync Profile & Overall Progress
            out.println("document.getElementById('username').innerText = '" + studentName + "';");
            out.println("document.getElementById('profile_logo').innerText = '" + studentName.toUpperCase().charAt(0) + "';");
            out.println("document.getElementById('course_progress').value = " + overallProgress + ";");
            out.println("document.getElementById('course_progress_value').innerText = '" + overallProgress + "%';");

            // --- 2. DYNAMIC MODULE BREAKDOWN (Progress Bars) ---
            String moduleQuery = "SELECT a.title, " +
                                 "IF((SELECT COUNT(*) FROM submissions sub JOIN students s ON sub.student_id = s.student_id " +
                                 "    WHERE sub.assignment_id = a.assignment_id AND s.full_name = ?) > 0, 100, 20) as progress " +
                                 "FROM assignments a JOIN students s ON a.course_id = s.course_id " +
                                 "WHERE s.full_name = ? ORDER BY a.due_date ASC";

            PreparedStatement psMod = con.prepareStatement(moduleQuery);
            psMod.setString(1, studentName);
            psMod.setString(2, studentName);
            ResultSet rsMod = psMod.executeQuery();

            out.println("const moduleContainer = document.querySelector('.sub_mudules');");
            out.println("if(moduleContainer) {");
            out.println("   moduleContainer.innerHTML = '';"); 
            while (rsMod.next()) {
                String title = rsMod.getString("title");
                int prog = rsMod.getInt("progress");
                out.println("   moduleContainer.innerHTML += `");
                out.println("       <div id='subcard'>");
                out.println("           <div class='sub_progress_info'>");
                out.println("               <h3>" + title + "</h3>");
                out.println("               <span id='sub_progress_value'>" + prog + "%</span>");
                out.println("           </div>");
                out.println("           <progress id='sub_progressbar' value='" + prog + "' max='100'></progress>");
                out.println("       </div>`;");
            }
            out.println("}");

            // --- 3. WATERFALL TASK QUEUE ---
            String taskQuery = "SELECT a.assignment_id, a.title, a.instruction, a.due_date, " +
                               "DATEDIFF(a.due_date, CURDATE()) as days_left " +
                               "FROM assignments a JOIN students s ON a.course_id = s.course_id " +
                               "WHERE s.full_name = ? AND a.assignment_id NOT IN (" +
                               "    SELECT sub.assignment_id FROM submissions sub JOIN students st ON sub.student_id = st.student_id " +
                               "    WHERE st.full_name = ?" +
                               ") ORDER BY a.due_date ASC LIMIT 2";

            PreparedStatement psTask = con.prepareStatement(taskQuery);
            psTask.setString(1, studentName);
            psTask.setString(2, studentName);
            ResultSet rsTask = psTask.executeQuery();

            out.println("const dashTaskContainer = document.querySelector('.task_due');");
            out.println("if(dashTaskContainer) { dashTaskContainer.innerHTML = '<h2>Task Due</h2>'; }");

            int taskCounter = 0;
            boolean hasTasks = false;
            while (rsTask.next()) {
                hasTasks = true;
                taskCounter++;
                int id = rsTask.getInt("assignment_id");
                String title = rsTask.getString("title");
                String desc = rsTask.getString("instruction").replace("'", "\\'");
                int daysLeft = rsTask.getInt("days_left");

                String timeText = (daysLeft < 0) ? Math.abs(daysLeft) + " days overdue" : (daysLeft == 0 ? "Due Today" : daysLeft + " days left");
                String color = (daysLeft <= 1) ? "#ff4d4d" : "#2bf02b";

                if (taskCounter == 1) {
                    out.println("const hiddenInput = document.getElementsByName('assignment_id')[0];");
                    out.println("if(hiddenInput) { hiddenInput.value = '" + id + "'; }");
                    out.println("const fileNameDisp = document.getElementById('fileName');");
                    out.println("if(fileNameDisp) { fileNameDisp.innerText = 'Submit for: " + title + "'; fileNameDisp.style.color = '#2ecc71'; }");
                }

                out.println("dashTaskContainer.innerHTML += `");
                out.println("    <div id='subcard' style='margin-bottom:12px; border-left: 4px solid " + (taskCounter == 1 ? color : "#333") + "; opacity: " + (taskCounter == 1 ? "1" : "0.7") + "'>");
                out.println("        <h3>" + title + (taskCounter > 1 ? " 🔒" : "") + "</h3>");
                out.println("        <p id='description' style='font-size:0.85rem;'>" + desc + "</p>");
                out.println("        <span id='time_left' style='color:" + color + "; font-weight:600; font-size:0.85rem;'>" + timeText + "</span>");
                out.println("        <p id='status' class='" + (daysLeft < 0 ? "status_overdue" : "status_pending") + "' style='font-size:0.7rem;'>" + (taskCounter == 1 ? "Active" : "Locked") + "</p>");
                out.println("    </div>`;");
            }

            if (!hasTasks) {
                out.println("dashTaskContainer.innerHTML += '<p style=\"color:gray; padding:10px;\">No upcoming tasks.</p>';");
                out.println("const uploadCard = document.querySelector('.upload_assignment'); if(uploadCard) { uploadCard.style.display = 'none'; }");
            }
            out.println("</script>");

        } catch (Exception e) { e.printStackTrace(); }
    }	
	

}
