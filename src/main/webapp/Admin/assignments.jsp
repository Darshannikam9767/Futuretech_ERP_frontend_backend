<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/assignments.css">
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
                <a href="${pageContext.request.contextPath}/AdminStudentDirectory" class="deactive">Student Directory</a>
                <a href="${pageContext.request.contextPath}/AdminCourseManagement" class="deactive">Course Management</a>
                <a href="${pageContext.request.contextPath}/AdminFeeTracker" class="deactive">Fee Tracker</a>
                <a href="${pageContext.request.contextPath}/AdminAssignments" class="active">Assignments</a>
                <a href="${pageContext.request.contextPath}/AdminRegister" class="deactive">Register</a>
                <a href="${pageContext.request.contextPath}/AdminSettings" class="deactive">Settings</a>
            </nav>
        </div>

        <div class="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
    
    <main class="assignment_main_content">
     	<div class="assignment_card">
     		<h3>Create New Assignment</h3>
     		<div id="add_assignment_form" >
                <form action="AdminAssignmentServlet" method="POST">
                    	<div id="a_c">
                    	<input type="text" id="new_assignment_name" name="assignment_name" placeholder="Assignment Name" required>
                    	<select name="student_course" id="courses">
                   	
                    </select>
                    <input type="date" name="due_date" required>
                    	</div>
                    <textarea name="description" placeholder="Enter assignment instructions..." rows="4"></textarea>                    
                    <div class="form_footer_row">
    						<button type="submit" id="add_assignment_btn">Add Assignment</button>
					</div>                
				</form>
            </div>
     	</div>
     	
     	<div class="submission_tracker_table">
    		<h3>Assignment Directory</h3>
     	  <div class="enable_scroll ">
     	  
                <table>
                    <thead>
                        <tr>
                        	<th hidden >
                        		Real Student ID
                        	</th>
                        	<th>
                        		ID
                        	</th>
                            <th>
                                Student Name
                            </th>
                            <th>
                                Assignment Name
                            </th>
                            <th>
                            		Due Date
                            </th>
                            <th>
                                Status
                            </th>
                            <th>
                                Action
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
        const params = new URLSearchParams(window.location.search);
        if(params.get("status") == "created"){
        		alert("Assignment created successfully!")
        		 window.history.replaceState({}, document.title, window.location.pathname);
        }
        
        
        function openReview(submissionId) {
            // This will call the servlet we create to download/view the file
            window.location.href = "${pageContext.request.contextPath}/ReviewSubmissionServlet?subId=" + submissionId;
        }
       
    </script>
</body>

</html>