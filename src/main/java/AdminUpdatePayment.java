

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;


@WebServlet("/AdminUpdatePayment")
public class AdminUpdatePayment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public AdminUpdatePayment() {
        super();
        // TODO Auto-generated constructor stub
    }
 	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 HttpSession session = request.getSession(false);
		    
		    
		    if (session == null || !"admin".equals(session.getAttribute("userrole"))) {
		        response.sendRedirect("admin_authentication.jsp");
		        return;
		    }
		    
		    
		    String studentIdRaw = request.getParameter("real_student_id");
		    String paidAmountRaw = request.getParameter("paid_fee");

		    // Check nulls BEFORE parsing
		    if (studentIdRaw == null || paidAmountRaw == null || studentIdRaw.isEmpty() || paidAmountRaw.isEmpty()) {
		        response.sendRedirect("AdminFeeTracker?error=missing_data");
		        return;
		    }

		    try (Connection con = DBConnection.getConnection()) {
		        int studentId = Integer.parseInt(studentIdRaw);
		        double newPaidAmount = Double.parseDouble(paidAmountRaw);
		        System.out.println(studentId+"\n"+newPaidAmount);
		        
		        String updateQuery = "UPDATE students SET amount_paid = ? WHERE student_id = ?";
		        PreparedStatement ps = con.prepareStatement(updateQuery);
		        
		        ps.setDouble(1, newPaidAmount);
		        ps.setInt(2, studentId); 

		        
		        int rowsAffected = ps.executeUpdate(); 

		        if (rowsAffected > 0) {
		            
		            response.sendRedirect("AdminFeeTracker?update=success");
		        } else {
		            response.sendRedirect("AdminFeeTracker?update=failed");
		        }

		    } catch (NumberFormatException e) {
		        response.sendRedirect("AdminFeeTracker?error=invalid_format");
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.sendRedirect("AdminFeeTracker?error=server_error");
		    }

	}

}
