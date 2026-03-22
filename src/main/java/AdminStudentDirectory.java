

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
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(session == null ||!"admin".equals(session.getAttribute("userrole"))) {
			response.sendRedirect("admin_authentication.jsp");
			return;
		}
		
		request.getRequestDispatcher("/Admin/student_directory.jsp").include(request, response);
		
		try(Connection con = DBConnection.getConnection()){
			Statement st = con.createStatement();
				
			ResultSet rsCourses = st.executeQuery("select course_name from courses where is_active = true");
			out.println("<script>");
			while(rsCourses.next()) {
				String courseName=rsCourses.getString("course_name");
				out.println("document.getElementById('courses').innerHTML+=`<option value='"+courseName+"'>"+courseName+"</option>`");
			}
			
			String adminName = (String) session.getAttribute("username");
			
			out.println("document.getElementById('username').innerText = '" + adminName + "';");
			out.println("document.getElementById('profile_logo').innerText = '"+adminName.toUpperCase().charAt(0)+"'");
			
				
			
			String query = "SELECT " +
				    "(SELECT COUNT(*) FROM students where is_accesable=true) AS total_students, " +
				    "(SELECT COUNT(*) FROM students WHERE status not in ('Dropped','Completed') and is_accesable=true) AS active_students, " +
				    "(SELECT COUNT(*) FROM students WHERE status = 'Completed' and is_accesable=true) AS completed, " +
				    "(SELECT COUNT(*) FROM students WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) " + // Fixed bracket here
				    "AND YEAR(created_at) = YEAR(CURRENT_DATE()) AND is_accesable=true) AS this_month_students";

				ResultSet rs = st.executeQuery(query);			
			while(rs.next()) {
				int total_students = rs.getInt("total_students");
				int active_students=rs.getInt("active_students");
				int completed = rs.getInt("completed");
				int this_month = rs.getInt("this_month_students");
				
				
				out.println("document.getElementById('total_students').innerText ='"+total_students+"'");
				out.println("document.getElementById('total_active_student').innerText = '"+active_students+"'");
				out.println("document.getElementById('total_completed_students').innerText = '"+completed+"'");
				out.println("document.getElementById('total_this_month_students').innerText ='"+this_month+"'");
				
			}
			
			query="select s.student_id,s.full_name,s.contact,s.email,c.course_name,s.status from students s join courses c on s.course_id=c.course_id where s.is_accesable=true";
			
			rs=st.executeQuery(query);
			
			out.println("let tbody = document.querySelector('.student_directory_table table tbody')");
			out.println("if(tbody){tbody.innerHTML = ''}");
			int student_id = 1;
			while(rs.next()) {
				int real_student_id=rs.getInt("student_id");
				String name = rs.getString("full_name");
    			String contact = rs.getString("contact");
    			String email=rs.getString("email");
    			String course_name = rs.getString("course_name");
    			String progress = "67%";
    			String status = rs.getString("status");
    			String actions="<div class='action_div'>"+
    	                "<i class='fa-solid fa-pen edit'></i>"+
    	                "<i class='fa-solid fa-trash delete'></i>"+
    	                "</div>";
    			String row ="<tr>"+
    					"<td hidden>"+real_student_id+"</td>"+
    					"<td>"+student_id+"</td>"+
    					"<td>"+name+"</td>"+
    					"<td><span>"+contact+"</span></td>"+
    					"<td>"+email+"</td>"+
    					"<td>"+course_name+"</td>"+
    					"<td>"+progress+"</td>"+
    					"<td>"+status+"</td>"+
    					"<td>"+actions+"</td>"+
    					"</tr>";
    			
    			out.println("if(tbody){ tbody.innerHTML += `" + row + "`; }");
    			student_id++;
			}
			out.println("</script>");
			
		}catch(Exception e) {
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
