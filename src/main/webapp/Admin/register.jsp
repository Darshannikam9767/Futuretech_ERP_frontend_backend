<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css">
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
    <main class="main_wrapper">
     	<div class="registration-card">
            <% if("success".equals(request.getParameter("msg"))) { %>
                <div style="color: #2ecc71; text-align: center; margin-bottom: 15px; font-weight: bold;">Registration Successful!</div>
            <% } %>
            <% if("error".equals(request.getParameter("msg"))) { %>
                <div style="color: #ff4d4d; text-align: center; margin-bottom: 15px; font-weight: bold;">Error! Please check Console.</div>
            <% } %>
            
            <div class="switch-container">
                <div class="switch-toggle" id="switch-toggle"></div>
                <button type="button" class="switch-btn active-btn" id="student-tab" onclick="showForm('student')">Student</button>
                <button type="button" class="switch-btn" id="admin-tab" onclick="showForm('admin')">Admin</button>
            </div>

            <div id="student-form-box">
                <h2 class="form-header">New Student Enrollment</h2>
                <form action="AdminRegister" method="post">
                    <input type="hidden" name="formType" value="student">
                    <div class="form-group">
                        <label>Student ID</label>
                        <input type="text" id="auto_sid" name="sid" readonly style="background: #2c2c2c; color: #aaa;">
                    </div>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" placeholder="Enter Name" required>
                    </div>
                    <div class="form-group">
                        <label>Contact</label>
                        <input type="text" name="contact" placeholder="Enter Contact Number" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" placeholder="Enter Email" required>
                    </div>
                    <div class="form-group">
                        <label>Domain</label>
                        <select name="domain">
                            <option value="Java Full Stack">Java Full Stack</option>
                            <option value="Web Development">Web Development</option>
                            <option value="Data Science">Data Science</option>
                        </select>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="submit-btn">Submit</button>
                        <button type="reset" class="cancel-btn" onclick="generateSID()">Cancel</button>
                    </div>
                </form>
            </div>

            <div id="admin-form-box" style="display: none;">
                <h2 class="form-header">New Admin Registration</h2>
                <form action="AdminRegister" method="post">
                    <input type="hidden" name="formType" value="admin">
                    <div class="form-group">
                        <label>Admin Name</label>
                        <input type="text" name="admin_name" placeholder="Enter Admin Name" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="admin_email" placeholder="Enter Email" required>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="admin_pass" placeholder="Create Password" required>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="submit-btn">Register Admin</button>
                        <button type="reset" class="cancel-btn">Cancel</button>
                    </div>
                </form>
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
        
        
        
        function generateSID() {
            const date = new Date();
            const timestamp = date.getDate().toString() + (date.getMonth() + 1).toString() + date.getSeconds().toString() + Math.floor(Math.random() * 10);
            document.getElementById('auto_sid').value = "STU" + timestamp;
        }

        function showForm(role) {
            const sBox = document.getElementById('student-form-box');
            const aBox = document.getElementById('admin-form-box');
            const toggle = document.getElementById('switch-toggle');
            const sTab = document.getElementById('student-tab');
            const aTab = document.getElementById('admin-tab');

            if (role === 'admin') {
                sBox.style.display = 'none';
                aBox.style.display = 'block';
                toggle.style.transform = 'translateX(100%)';
                aTab.classList.add('active-btn');
                sTab.classList.remove('active-btn');
            } else {
                sBox.style.display = 'block';
                aBox.style.display = 'none';
                toggle.style.transform = 'translateX(0)';
                sTab.classList.add('active-btn');
                aTab.classList.remove('active-btn');
            }
        }
    </script>
</body>

</html>