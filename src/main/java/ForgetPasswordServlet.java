import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import DataBase.DBConnection;

@WebServlet("/ForgetPasswordServlet")
public class ForgetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 public ForgetPasswordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String referer = request.getHeader("referer");
		
		String role = request.getParameter("role"); 
	    
	    if (role == null || role.isEmpty()) {
	        if(referer != null && referer.contains("admin_authentication.jsp")) {
	            role = "admin";
	        } else {
	            role = "student";
	        }
	    }
		
		String table=(role.equals("admin"))?"admin":"students";
		String redirectPage = (role.equals("admin")) ? "admin_authentication.jsp" : "student_authentication.jsp";
		
		try(Connection con = DBConnection.getConnection()){
			String checkQuery="select * from "+table+" where email = ?";
			PreparedStatement pst =con.prepareStatement(checkQuery);
			pst.setString(1, email);
			
			ResultSet rs =pst.executeQuery();
			
			if(rs.next()) {
				SecureRandom secureRandom = new SecureRandom();
				int otpInt = secureRandom.nextInt(900000) + 100000;
				String otp = String.valueOf(otpInt);
				
				String updateQuery = "update "+table+" set reset_otp = ?,otp_expiry = date_add(now(), interval 5 minute) where email = ?";
				PreparedStatement psUpdate = con.prepareStatement(updateQuery);
				
				psUpdate.setString(1, otp);
				psUpdate.setString(2, email);
				psUpdate.executeUpdate();
				
				sendEmail(email,otp,role);
				
				response.sendRedirect(redirectPage+"?view=reset&status=otp_sent&email=" + email);
            } else {
            	response.sendRedirect(redirectPage+"?view=forget&error=notfound");
            }
		}catch(Exception e) {
			System.out.println("From the forget passward servlet.......");
			e.printStackTrace();
		}
	}
	
	private void sendEmail(String toEmail, String otp, String role) {
	    // For Gmail, use an "App Password," not your regular login password
	    final String fromEmail = "chatbotc32@gmail.com"; 
	    final String password = "hbwlrfravmhvmjnl"; 

	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");

	    jakarta.mail.Session mailSession = jakarta.mail.Session.getInstance(props, new jakarta.mail.Authenticator() {
	        protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
	            return new jakarta.mail.PasswordAuthentication(fromEmail, password);
	        }
	    });

	    try {
	        jakarta.mail.Message message = new jakarta.mail.internet.MimeMessage(mailSession);
	        message.setFrom(new jakarta.mail.internet.InternetAddress(fromEmail));
	        message.setRecipients(jakarta.mail.Message.RecipientType.TO, jakarta.mail.internet.InternetAddress.parse(toEmail));
	        message.setSubject("Futuretech " + role.toUpperCase() + " Password Reset OTP");
	        
	        String content = "Hello,\n\nYour 6-digit OTP for resetting your " + role + " portal password is: " + otp + 
	                         "\n\nThis code will expire in 5 minutes.";
	        
	        message.setText(content);
	        jakarta.mail.Transport.send(message);
	        
	        System.out.println("OTP successfully sent to " + toEmail);
	    } catch (jakarta.mail.MessagingException e) {
	        System.out.println("Failed to send email to " + toEmail);
	        e.printStackTrace();
	    }
	}

}
