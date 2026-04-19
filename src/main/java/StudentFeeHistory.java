

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBase.DBConnection;

/**
 * Servlet implementation class StudentFeeHistory
 */
@WebServlet("/StudentFeeHistory")
public class StudentFeeHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentFeeHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
HttpSession session = request.getSession(false);
		
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		
		if(session == null || !"student".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("student_authentication.jsp");
			return;
		}
		
		request.getRequestDispatcher("/Student/student_fee_history.jsp").include(request, response);
		
		try(Connection con = DBConnection.getConnection()){
			String studentName = (String) session.getAttribute("username");
			String query = "SELECT c.course_name, c.fees, s.amount_paid " +
		               "FROM students s " +
		               "JOIN courses c ON s.course_id = c.course_id " +
		               "WHERE s.full_name = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, studentName);
			ResultSet rs = ps.executeQuery();
			out.println("<script>");
			out.println("document.getElementById('username').innerText = '" + studentName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+studentName.toUpperCase().charAt(0)+"'");
			if (rs.next()) {
		        String courseName = rs.getString("course_name");
		        double totalFees = rs.getDouble("fees");
		        double paidAmount = rs.getDouble("amount_paid");
		        double balance = totalFees - paidAmount;

		        // Inject Course Name
		        out.println("document.getElementById('course_name_display').innerText = '" + courseName + "';");

		        // Inject Values into the Pills with clean formatting
		        out.println("document.querySelector('.total .value').innerText = '₹ " + String.format("%.2f", totalFees) + "';");
		        out.println("document.querySelector('.paid .value').innerText = '₹ " + String.format("%.2f", paidAmount) + "';");
		        out.println("document.querySelector('.balance .value').innerText = '₹ " + String.format("%.2f", balance) + "';");
		    } else {
		        // Fallback if no student data is found
		        out.println("document.getElementById('course_name_display').innerText = 'No Course Assigned';");
		    }
			out.println("</script>");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
