

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DataBase.DBConnection;

@WebServlet("/PasswordResetServlet")
public class PasswordResetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PasswordResetServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String role = request.getParameter("role"); 
	    String email = request.getParameter("email");
	    String otp = request.getParameter("otp");
	    String newPassword = request.getParameter("new_password");

	    String table = ("admin".equals(role)) ? "admin" : "students";
	    String authPage = ("admin".equals(role)) ? "admin_authentication.jsp" : "student_authentication.jsp";
	    
	    System.out.println("Password reset \n"+role+"\n"+email+"\n"+otp);

	    try (Connection con = DBConnection.getConnection()) {
	        String verifyQuery = "select * from " + table + " where email=? and reset_otp=? and otp_expiry > now()";
	        PreparedStatement pst = con.prepareStatement(verifyQuery);
	        pst.setString(1, email);
	        pst.setString(2, otp);
	        
	        if (pst.executeQuery().next()) {
	            // Success: Update password and clear OTP
	            String updateQuery = "update " + table + " set password=?, reset_otp=NULL, otp_expiry=NULL where email=?";
	            PreparedStatement psUpdate = con.prepareStatement(updateQuery);
	            psUpdate.setString(1, newPassword);
	            psUpdate.setString(2, email);
	            psUpdate.executeUpdate();

	            // 4. Redirect to login with a success parameter
	            response.sendRedirect(authPage + "?view=login&status=password_changed");
	        } else {
	            // 5. Failure: Redirect back to reset view
	            response.sendRedirect(authPage + "?view=reset&status=invalid_otp&email=" + email);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
