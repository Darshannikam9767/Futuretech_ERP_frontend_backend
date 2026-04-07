<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.List"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/student_directory.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/Assets/images/logo.png" type="image/x-icon">
	<link rel="preload" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css" as="style">
	<link rel="preload" href="${pageContext.request.contextPath}/Assets/images/logo.png" as="image">
</head>

<body>
    <div class="admin_container">
        <div class="logo_name">
            <img src="${pageContext.request.contextPath}/Assets/images/logo.png" alt="institute logo" style="height:60px; border-radius:3px;">
            <div class="name">
                <h2>Futuretech</h2>
                <p>Admin Portal</p>
            </div>
        </div>
        <div class="heading_section">
            <span id="menuBtn" class="menuBtn">&#9776;</span>
            <div class="title">
                <h2>My Dashboard</h2>
                <p>Welcome back, <span id="username">Admin Name</span></p>
            </div>
            <div class="profile_logo">
                <h3 id="profile_logo">AN</h3>
            </div>
        </div>
    </div>
    <div id="navbar_and_logout_section">
        <div class="navbar_section">
            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/AdminDashboard" class="deactive">Dashboard</a>
                <a href="${pageContext.request.contextPath}/AdminStudentDirectory" class="active">Student Directory</a>
                <a href="${pageContext.request.contextPath}/AdminCourseManagement" class="deactive">Course Management</a>
                <a href="${pageContext.request.contextPath}/AdminFeeTracker" class="deactive">Fee Tracker</a>
                <a href="${pageContext.request.contextPath}/AdminAssignments" class="deactive">Assignments</a>
                <a href="${pageContext.request.contextPath}/AdminRegister" class="deactive">Register</a>
                <a href="${pageContext.request.contextPath}/AdminSettings" class="deactive">Settings</a>
            </nav>
        </div>

        <div class="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
     <main class="main_content" id="main_content">
     	<div class="quick_info_pannel">
            <div class="total_students quick_pannel_subcard">
                <p>Total Students</p>
                <h1 id="total_students">2300</h1>
            </div>

            <div class="active_student quick_pannel_subcard">
                <p>Active Students</p>
                <h1 id="total_active_student">1051</h1>
            </div>

            <div class="completed_students quick_pannel_subcard">
                <p>Completed</p>
                <h1 id="total_completed_students">31</h1>
            </div>

            <div class="this_month quick_pannel_subcard">
                <p>This Month</p>
                <h1 id="total_this_month_students">+ <span>35</span></h1>
            </div>

        </div>


        <div class="student_directory">
            <div id="top_title_and_button">
                <h3>Student Directory</h3>
                <button id="add_new_student_btn">+ <span>Add New Student</span></button>
            </div>
            
            <div class="edit_form show_edit_form">
                <form action="AdminUpdateStudentRecord" method="post">
                	<input type="hidden" name="real_student_id" id="edit_Student_id">
                    <input type="text" name="student_name" placeholder="Student Name">
                    <input type="text" name="student_contact" placeholder="Student Contact" minlength="10" maxlength="10">
                    <input type="email" name="student_email" placeholder="Student Email">
                    <select name="student_course" id="courses">
                   	
                    </select>
                    <input type="text" name="student_progress_value"placeholder="Progress Value">
                    <select name="student_status" id="status">
    					<option value="In Training">In Training</option>
    					<option value="Pending">Pending</option>
    					<option value="Completed">Completed</option>
    					<option value="Dropped">Dropped</option>
					</select>

                    <button id="update_btn" type="submit">Update Record</button>
                </form>
            </div>
            <div class="enable_scroll student_directory_table">
                <table>
                    <thead>
                        <tr>
                        	<th hidden>
                        		Real Student ID
                        	</th>
                        	<th>
                        		ID
                        	</th>
                            <th>
                                Student Name
                            </th>
                            <th>
                                Contact
                            </th>
                            <th>
                            		EmailID
                            </th>
                            <th>
                                Course
                            </th>
                            <th>
                                Progress
                            </th>
                            <th>
                                Status
                            </th>
                            <th>
                                Actions
                            </th>

                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
     </main>
    
    
    
    <%
	if(session.getAttribute("userrole")==null){
%>
	<script type="text/javascript">
		alert("Access Denied! Please login first.")
		window.location.href="../admin_authentication.jsp"
	</script>
	<%
	return;
	}
	%>
	<script>
        const menuBtn = document.getElementById("menuBtn");
        const navbar_and_logout_section = document.getElementById("navbar_and_logout_section");

        menuBtn.addEventListener("click", () => {
            if (window.innerWidth <= 768) { navbar_and_logout_section.classList.toggle("show"); }
        });
        window.addEventListener("resize", () => {
            if (window.innerWidth > 768) {
                navbar_and_logout_section.classList.add("show");
            } else {
                navbar_and_logout_section.classList.remove("show");
            }
        });
        
        const logout_btn=document.getElementById("logout_btn");
        logout_btn.addEventListener('click',()=>{
        	if(confirm("Do you realy want to Logout"))
        		window.location.href="${pageContext.request.contextPath}/LogoutServlet";
        })
        
        
		const addNewStudentBtn = document.getElementById("add_new_student_btn");
		addNewStudentBtn.addEventListener('click',()=>{
			window.location.href="${pageContext.request.contextPath}/AdminRegister";
		})
		
		const mainDashboard = document.getElementById("main_content");
		const editForm = document.querySelector(".edit_form");

		if (mainDashboard) {
    			mainDashboard.addEventListener('click', (event) => {
      
        		const btn = event.target.closest(".edit");

        		if (btn) {
            		const row = btn.closest("tr");
            
           		editForm.querySelector("#edit_Student_id").value=row.cells[0].innerText;
            		editForm.querySelector("input[placeholder='Student Name']").value = row.cells[2].innerText;
            		editForm.querySelector("input[placeholder='Student Contact']").value = row.cells[3].innerText;
            		editForm.querySelector("input[placeholder='Student Email']").value = row.cells[4].innerText;
            		editForm.querySelector("input[placeholder='Progress Value']").value = row.cells[6].innerText;
            
          
            		const courseValue = row.cells[5].innerText.trim();
            		const statusValue = row.cells[7].innerText.trim();
            
            		document.getElementById("courses").value = courseValue;
            		document.getElementById("status").value = statusValue;

          
         		editForm.style.display = "flex";
            		editForm.scrollIntoView({ behavior: 'smooth', block: 'center' });
        			}
    			});
		}

        const update_btn = document.getElementById("update_btn")

        update_btn.addEventListener('click', () => {
        	
            editForm.style.display = "none";
        })
        
        
        
		mainDashboard.addEventListener('click', (event) => {
    	const deleteBtn = event.target.closest(".delete");

    		if (deleteBtn) {
        		const row = deleteBtn.closest("tr");
        		// Remember: row.cells[1] is our HIDDEN real database ID
        		const real_student_id = row.cells[0].innerText;
        		const studentName = row.cells[2].innerText;

        		if (confirm("Are you sure you want to delete record of "+studentName+"?")) {
            		if(confirm("Are you really sure you want to delete record of "+studentName+"?"))
                		window.location.href ="${pageContext.request.contextPath}/AdminDeleteStudentRecord?real_student_id="+real_student_id;
        }
    }
});
    </script>
</body>

</html>