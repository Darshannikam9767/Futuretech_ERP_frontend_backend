/*
 * create tabel course 
 * 
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import DataBase.DBConnection;

@WebServlet("/AdminDashboard")
public class AdminDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminDashboard() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		
		if(session == null ||!"admin".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("admin_authentication.jsp");
			return;
		}
		
		request.getRequestDispatcher("Admin/admin_dashboard.jsp").include(request, response);
		System.out.println("Entered in Admin Dashboard section........");
		try(Connection con = DBConnection.getConnection()){
			String statsQuery = "SELECT " +
				    "(SELECT COUNT(*) FROM students WHERE status != 'Dropped' AND status != 'Completed' AND is_accesable=true) AS active_count, " +
				    "(SELECT IFNULL(SUM(amount_paid), 0) FROM students WHERE is_accesable=true) AS total_revenue, " +
				    "(SELECT COUNT(*) FROM admin) AS staff_count, " +
				    "(SELECT IFNULL(SUM(c.fees - s.amount_paid), 0) FROM students s JOIN courses c ON s.course_id = c.course_id WHERE s.is_accesable=true) AS total_pending";

		        Statement st = con.createStatement();
		        ResultSet rs = st.executeQuery(statsQuery);

		        if (rs.next()) {
		            int active = rs.getInt("active_count");
		            String adminName = (String) session.getAttribute("username");
		            double revenue = rs.getDouble("total_revenue");
		            int staff = rs.getInt("staff_count");
		            double pending = rs.getDouble("total_pending");

		            out.println("<script>");
		            // User Profile Info
		            out.println("document.getElementById('username').innerText = '" + adminName + "';"); 
		            out.println("document.getElementById('profile_logo').innerText = '" + adminName.toUpperCase().charAt(0) + "';");
		            
		            // Stats Cards
		            out.println("document.getElementById('total_active_student').innerText = '" + active + "';");
		            
		            // Using querySelector to target the <span> inside the card so we don't lose the '₹' symbol
		            out.println("document.getElementById('total_monthly_revenue').innerText = '" + (int)revenue + "';");
		            out.println("document.getElementById('working_in_org').innerText = '" + staff + "';");
		            
		            // Fixed typo: total_pending_fees
		            out.println("document.querySelector('#total_pending_fees span').innerText = '" + (int)pending + "';");
		            out.println("</script>");
		        }
		        
		    
		        
		        try(Statement stTable = con.createStatement();
		        		
//		        		String studentquery = "select s.full_name, c.course_name, s.created_at, s.amount_paid, s.status from students s join courses c on s.course_id = c.course_id where s.is_accesable = true order by s.created_at desc limit 7";
		        		ResultSet rsTable = stTable.executeQuery("select s.full_name, c.course_name, s.created_at, s.amount_paid, s.status, c.fees as total_fees from students s join courses c on s.course_id = c.course_id where s.is_accesable = true order by s.created_at desc limit 7")){
		        	out.println("<script>");
		        	out.println("let tbody = document.querySelector('.recent_activity_table table tbody')");
		        	out.println("if(tbody){tbody.innerHTML = ''}");
		        	while(rsTable.next()) {
		        			String name = rsTable.getString("full_name");
		        			String course_name = rsTable.getString("course_name");
		        			String date = rsTable.getString("created_at").split(" ")[0];
		        			int amount_paid = rsTable.getInt("amount_paid");
		        			String status = rsTable.getString("status");
		        			int total_fees=rsTable.getInt("total_fees");
		        			int pending_fees = total_fees-amount_paid;
		        			String tableRow="<tr>"+
		        							"<td>"+name+"</td>"+
		        							"<td>"+course_name+"</td>"+
		        							"<td>"+date+"</td>"+
		        							"<td>₹ <span>"+amount_paid+"</span></td>"+
		        							"<td>₹ <span>"+pending_fees+"</span></td>"+
		        							"<td>"+status+"</td>"+
		        							"</tr>";
		        			
		        			out.println("if(tbody) { tbody.innerHTML += '" + tableRow + "'; }");
		        			
		        		}
		        	out.println("</script>");
		        }catch(Exception e) {
		        	System.out.println("Error during fetching recent activity.......");
		        	e.printStackTrace();
		        }
		    } catch (Exception e) {
		        // This will print the error to the Eclipse console if something fails
		    	System.out.println("Error during fetching stats......");
		        e.printStackTrace();
		    }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
