<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/register.css">
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
                <a href="${pageContext.request.contextPath}/AdminAssignments" class="deactive">Assignments</a>
                <a href="${pageContext.request.contextPath}/AdminRegister" class="active">Register</a>
                <a href="${pageContext.request.contextPath}/AdminSettings" class="deactive">Settings</a>
            </nav>
        </div>

        <div class="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
    <main class="main_content">
     		<div id="registration_section">
            	<div id="student_registration_section" hidden>
            		<div class="student_btn">
                		<button id="register_admin">Register Admin</button>
            		</div>

            		<div id="card">
                		<header>
                    		<h1>Student Registration</h1>
                    		<p>Enter Student Details</p>
               			</header>
                		<hr>
                		<p id="email_add">Student Information</p>
                		<form action="AdminStudentRegistrastion" method="post">
    						<div class="form_row">
        						<input type="text" id="student_name" name="student_name" placeholder="Enter Student Name" autocomplete="name" required autofocus>
        						<input type="email" name="email" placeholder="Enter Student Email" autocomplete="email" required>
    						</div>

    						<div class="form_row">
        						<input type="text" name="phone_number" id="phnumber" placeholder="Enter Phone Number" autocomplete="mobile" required maxLength="10"
    								oninput="this.value = this.value.replace(/[^0-9]/g, '');">
        						<select name="student_course" id="courses">
            					</select>
    						</div>
    
    						<input type="password" name="password" id="password" placeholder="Enter Password For Student" autocomplete="current-password" required>
    						<button id="student_register_btn" type="submit">Register</button>
						</form>
            		</div>
            	</div>
            	
            	<div id="admin_registration_section" class="hidden">
            			<div class="admin_btn">
                		<button id="register_student">Register Student</button>
            		</div>

            		<div id="card">
                		<header>
                    		<h1>Admin Registration</h1>
                    		<p>Enter Admin Details</p>
               			</header>
                		<hr>
                		<p id="email_add">Admin Information</p>
                		<form action="AdminAdminRegister" method="post">
    						<div class="form_row">
        						<input type="text" id="admin_name" name="admin_name" placeholder="Enter Admin Name" autocomplete="name" required autofocus>
        						<input type="email" name="email" placeholder="Enter Admin Email" autocomplete="email" required>
    						</div>

    						<div class="form_row">
        						<input type="text" name="phone_number" id="phnumber" placeholder="Enter Phone Number" autocomplete="mobile" required maxLength="10"
    								oninput="this.value = this.value.replace(/[^0-9]/g, '');">
    							<input type="password" name="password" id="password" placeholder="Enter Password For Admin" autocomplete="current-password" required>	
    						</div>
    						<button id="admin_register_btn" type="submit">Register</button>
						</form>
            		</div>
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
        
        
		const studentSec = document.getElementById("student_registration_section");
		const adminSec = document.getElementById("admin_registration_section");

		const toAdminBtn = document.getElementById("register_admin");
		const toStudentBtn = document.getElementById("register_student");

		// Switch to Admin Form
		toAdminBtn.addEventListener('click', () => {
    		studentSec.classList.add("hidden");
    		adminSec.classList.remove("hidden");
    		document.getElementById("admin_name").focus()
		});

		// Switch to Student Form
		toStudentBtn.addEventListener('click', () => {
    		adminSec.classList.add("hidden");
    		studentSec.classList.remove("hidden");
    		document.getElementById("student_name").focus()
		});
		
		const params = new URLSearchParams(window.location.search);
		if (params.get("status") === "success") {
		    alert("Registration successful!");
		    const newUrl = window.location.pathname;
		    window.history.replaceState({}, document.title, newUrl);
		} else if (params.get("status") === "error") {
		    alert("Registration failed. Please try again.");
		    window.history.replaceState({}, document.title, window.location.pathname);
		}
        
    </script>
</body>

</html>