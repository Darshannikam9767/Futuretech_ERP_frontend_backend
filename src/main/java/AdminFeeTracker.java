

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

@WebServlet("/AdminFeeTracker")
public class AdminFeeTracker extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminFeeTracker() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
HttpSession session = request.getSession(false);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(session == null ||!"admin".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("admin_authentication.jsp");
			return;
		}
		
		request.getRequestDispatcher("/Admin/fee_tracker.jsp").include(request, response);
		
		try (Connection con = DBConnection.getConnection()) {
	        Statement st = con.createStatement();
	        String adminName = (String) session.getAttribute("username");

	        // 1. Header Updates
	        out.println("<script>");
	        out.println("document.getElementById('username').innerText = '" + adminName + "';");
	        out.println("document.getElementById('profile_logo').innerText = '" + adminName.toUpperCase().charAt(0) + "';");
	        out.println("</script>");

	        // 2. Fetch Summary Stats
	        String statsQuery = "SELECT SUM(c.fees) as total_rev, SUM(s.amount_paid) as collected " +
	                            "FROM students s JOIN courses c ON s.course_id = c.course_id";
	        ResultSet rsStats = st.executeQuery(statsQuery);
	        if (rsStats.next()) {
	            int totalRev = rsStats.getInt("total_rev");
	            int collected = rsStats.getInt("collected");
	            int pending = totalRev - collected;

	            out.println("<script>");
	            out.println("document.getElementById('total_revenue').innerText = '₹ " + totalRev + "';");
	            out.println("document.getElementById('total_collected').querySelector('span').innerText = '" + collected + "';");
	            out.println("document.getElementById('total_pending_fees').querySelector('span').innerText = '" + pending + "';");
	            out.println("</script>");
	        }

	        // 3. Fetch Table Records
	        String tableQuery = "SELECT s.student_id, s.full_name, c.course_name, c.fees, s.amount_paid, s.status " +
	                            "FROM students s JOIN courses c ON s.course_id = c.course_id";
	        ResultSet rsTable = st.executeQuery(tableQuery);

	        out.println("<script>");
	        out.println("document.querySelector('tbody').innerHTML = '';");
	        
	        int displayId = 1; // Serial Number for UI
	        while (rsTable.next()) {
	            int real_id = rsTable.getInt("student_id");
	            String name = rsTable.getString("full_name");
	            String course = rsTable.getString("course_name");
	            int total = rsTable.getInt("fees");
	            int paid = rsTable.getInt("amount_paid");
	            int balance = total - paid;
	            String status = rsTable.getString("status");

	            // Build the row
	            String row = "<tr>" +
	                         "<td hidden>" + real_id + "</td>" +
	                         "<td>" + displayId + "</td>" +      
	                         "<td>" + name + "</td>" +
	                         "<td>" + course + "</td>" +
	                         "<td>₹ " + total + "</td>" +
	                         "<td>₹ " + paid + "</td>" +
	                         "<td>₹ " + balance + "</td>" +
	                         "<td>" + status + "</td>" +
	                         "<td><div class='action_div'><i class='fa-solid fa-pen edit'></i></div></td>" +
	                         "</tr>";

	            out.println("document.querySelector('tbody').innerHTML += `" + row + "`;");
	            displayId++;
	        }
	        out.println("</script>");

	    }catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
