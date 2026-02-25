

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
		            "(SELECT COUNT(*) FROM students WHERE status != 'Dropped') AS active_count, " +
		            "(SELECT IFNULL(SUM(amount_paid), 0) FROM students) AS total_revenue, " +
		            "(SELECT COUNT(*) FROM staff WHERE is_active = 1) AS staff_count, " +
		            "(SELECT IFNULL(SUM(total_fees - amount_paid), 0) FROM students) AS total_pending";

		        Statement st = con.createStatement();
		        ResultSet rs = st.executeQuery(statsQuery);

		        if (rs.next()) {
		            int active = rs.getInt("active_count");
		            double revenue = rs.getDouble("total_revenue");
		            int staff = rs.getInt("staff_count");
		            double pending = rs.getDouble("total_pending");
		            String adminName = (String) session.getAttribute("username");

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
		        		ResultSet rsTable = stTable.executeQuery("select full_name,course,created_at,amount_paid, status from students order by created_at desc limit 7")){
		        	out.println("<script>");
		        	out.println("let tbody = document.querySelector('.enable_scroll table tbody')");
		        	out.println("if(tbody){tbody.innerHTML = ''}");
		        	while(rsTable.next()) {
		        			String name = rsTable.getString("full_name");
		        			String course_name = rsTable.getString("course");
		        			String date = rsTable.getString("created_at").split(" ")[0];
		        			int amount = rsTable.getInt("amount_paid");
		        			String status = rsTable.getString("status");
		        			
		        			String tableRow="<tr>"+
		        							"<td>"+name+"</td>"+
		        							"<td>"+course_name+"</td>"+
		        							"<td>"+date+"</td>"+
		        							"<td>₹ <span>"+amount+"</span></td>"+
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
