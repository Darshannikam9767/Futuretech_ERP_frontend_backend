

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

@WebServlet("/AdminStudentDirectory")
public class AdminStudentDirectory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminStudentDirectory() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (session == null || !"admin".equals(session.getAttribute("userrole"))) {
            response.sendRedirect("admin_authentication.jsp");
            return;
        }

        request.getRequestDispatcher("/Admin/student_directory.jsp").include(request, response);

        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();

            // 1. SYNC UI ELEMENTS (Admin Name & Courses)
            String adminName = (String) session.getAttribute("username");
            out.println("<script>");
            out.println("document.getElementById('username').innerText = '" + adminName + "';");
            out.println("document.getElementById('profile_logo').innerText = '" + adminName.toUpperCase().charAt(0) + "';");

            ResultSet rsCourses = st.executeQuery("SELECT course_name FROM courses WHERE is_active = true");
            while (rsCourses.next()) {
                String cName = rsCourses.getString("course_name");
                out.println("if(document.getElementById('courses')) { document.getElementById('courses').innerHTML += `<option value='" + cName + "'>" + cName + "</option>`; }");
            }

            // 2. FETCH DIRECTORY STATS
            String statsQuery = "SELECT " +
                    "(SELECT COUNT(*) FROM students WHERE is_accesable=true) AS total_students, " +
                    "(SELECT COUNT(*) FROM students WHERE status NOT IN ('Dropped','Completed') AND is_accesable=true) AS active_students, " +
                    "(SELECT COUNT(*) FROM students WHERE status = 'Completed' AND is_accesable=true) AS completed, " +
                    "(SELECT COUNT(*) FROM students WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE()) AND is_accesable=true) AS this_month_students";

            ResultSet rsStats = st.executeQuery(statsQuery);
            if (rsStats.next()) {
                out.println("document.getElementById('total_students').innerText = '" + rsStats.getInt("total_students") + "';");
                out.println("document.getElementById('total_active_student').innerText = '" + rsStats.getInt("active_students") + "';");
                out.println("document.getElementById('total_completed_students').innerText = '" + rsStats.getInt("completed") + "';");
                out.println("document.getElementById('total_this_month_students').innerText = '" + rsStats.getInt("this_month_students") + "';");
            }

            // 3. FETCH STUDENT DATA WITH PROGRESS LOGIC
            // Note: We pull total_tasks, done_tasks, and duration (INT) to calculate %
            String directoryQuery = "SELECT s.student_id, s.full_name, s.contact, s.email, c.course_name, s.status, s.created_at, c.duration, " +
                    "(SELECT COUNT(*) FROM assignments WHERE course_id = s.course_id) AS total_tasks, " +
                    "(SELECT COUNT(*) FROM submissions WHERE student_id = s.student_id) AS done_tasks " +
                    "FROM students s JOIN courses c ON s.course_id = c.course_id " +
                    "WHERE s.is_accesable = true ORDER BY s.student_id DESC";

            ResultSet rs = st.executeQuery(directoryQuery);
            out.println("let tbody = document.querySelector('.student_directory_table table tbody');");
            out.println("if(tbody) { tbody.innerHTML = ''; }");

            int display_id = 1;
            while (rs.next()) {
                int real_id = rs.getInt("student_id");
                String name = rs.getString("full_name").replace("'", "\\'");
                String contact = rs.getString("contact");
                String email = rs.getString("email");
                String course = rs.getString("course_name");
                String status = rs.getString("status");

                // --- PROGRESS CALCULATION ---
                int totalTasks = rs.getInt("total_tasks");
                int doneTasks = rs.getInt("done_tasks");
                int durationDays = rs.getInt("duration");
                java.sql.Timestamp joined = rs.getTimestamp("created_at");

                // Task Component (70%)
                double taskScore = (totalTasks > 0) ? ((double) doneTasks / totalTasks) * 100 : 0;
                // Time Component (30%)
                long daysEnrolled = (System.currentTimeMillis() - joined.getTime()) / (1000 * 60 * 60 * 24);
                double timeScore = (durationDays > 0) ? Math.min(((double) daysEnrolled / durationDays) * 100, 100) : 0;

                int progressPercent = (int) ((taskScore * 0.7) + (timeScore * 0.3));

                // Row construction
                String actions = "<div class='action_div'><i class='fa-solid fa-pen edit'></i> <i class='fa-solid fa-trash delete'></i></div>";
                String row = "<tr>" +
                        "<td hidden>" + real_id + "</td>" +
                        "<td>" + display_id + "</td>" +
                        "<td>" + name + "</td>" +
                        "<td><span>" + contact + "</span></td>" +
                        "<td>" + email + "</td>" +
                        "<td>" + course + "</td>" +
                        "<td style='font-weight:bold; color:" + (progressPercent > 75 ? "#2bf02b" : "#f0ad4e") + "'>" + progressPercent + "%</td>" +
                        "<td>" + status + "</td>" +
                        "<td>" + actions + "</td>" +
                        "</tr>";

                out.println("if(tbody) { tbody.innerHTML += `" + row + "`; }");
                display_id++;
            }
            out.println("</script>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
