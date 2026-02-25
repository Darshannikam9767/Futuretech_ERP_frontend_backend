<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	if(session.getAttribute("userrole")==null){
%>
	<script type="text/javascript">
		alert("Access Denied! Please login first.")
		window.location.href="../student_authentication.jsp"
	</script>
	<%
	return;
	}
	%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Student Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Student_css/student_dashboard.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/Assets/images/logo.png" type="image/x-icon">
</head>

<body>
    <div class="student_container">
        <div class="logo_name" id="logo_name">
            <img id="institute_logo" src="${pageContext.request.contextPath}/Assets/images/logo.png" alt="institute logo">
            <div class="name">
                <h2>Futuretech</h2>
                <p>Student Portal</p>
            </div>
        </div>
        <div class="heading_section">
            <span id="menuBtn" class="menuBtn">&#9776;</span>
            <div class="title">
                <h2>My Dashboard</h2>
                <p>Welcome back, <span id="username">Student Name</span></p>
            </div>
            <div class="profile_logo">
                <h3 id="profile_logo">SN</h3>
            </div>
        </div>
    </div>
    <div id="navbar_and_logout_section">
        <div class="navbar_section" id="sidebar">
            <nav class="navbar" id="navbar">
                <a href="#dashboard" class="active">Dashboard</a>
                <a href="#my_domain" class="deactive">My Domain</a>
                <a href="#assignment" class="deactive">Assignments</a>
                <a href="#fee_history" class="deactive">Fee History</a>
                <a href="#profile" class="deactive">Profile</a>
            </nav>
        </div>

        <div class="logout_section" id="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
    <main class="main_content">
        <div class="training_progress  left" id="design_card">
            <h2>Training Progress</h2>
            <div class="progress_info">
                <label for="course completion">Course Completion</label>
                <span id="course_progress_value">50%</span>
            </div>
            <progress id="course_progress" max="100" value="50"></progress>
            <p>Module breakdown</p>
            <div class="sub_mudules">
                <div id="subcard">
                    <div class="sub_progress_info">
                        <h3>Core Java Fundamentals</h3>
                        <span id="sub_progress_value">50%</span>
                    </div>
                    <progress id="sub_progressbar" value="50" max="100"></progress>
                </div>
                <div id="subcard">
                    <div class="sub_progress_info">
                        <h3>OOPs concept</h3>
                        <span id="sub_progress_value">90%</span>
                    </div>
                    <progress id="sub_progressbar" value="90" max="100"></progress>
                </div>
            </div>
        </div>

        <div class="right">
            <div class="upload_assignment" id="design_card">
                <h2>Upload Submission</h2>
                <div class="drop_area">
                    <p> Click to browse</p>
                    <span>Supports PDF, DOC, ZIP (Max 10MB)</span>
                    <input type="file" id="select_file" accept=".pdf,.doc,.docx,.zip">
                </div>
                <button class="assignment_submit_btn">Submit Assignment</button>
            </div>

            <div class="task_due" id="design_card">
                <h2>Task Due</h2>
                <div id="subcard">
                    <h3>Java Collection Framework</h3>
                    <p id="description">Implement ArrayList, LinkedList, and HashMap with custom examples</p>
                    <span id="time_left">5 days left</span>
                    <p id="status">Pending</p>
                </div>
            </div>
        </div>
    </main>

    <script>
        const dropArea = document.querySelector('.drop_area');
        const select_file = document.getElementById('select_file');

        dropArea.addEventListener('click', () => {
            select_file.click()
        })

        select_file.addEventListener('change', function () {
            if (this.files.length > 0) {
                console.log(this.files[0].name);
            }
        });

        const logout_btn=document.getElementById("logout_btn");
        logout_btn.addEventListener('click',()=>{
        	if(confirm("Do you realy want to Logout"))
        		window.location.href="${pageContext.request.contextPath}/LogoutServlet";
        })
        const menuBtn = document.getElementById("menuBtn");
        const navbar_and_logout_section = document.getElementById("navbar_and_logout_section");

        menuBtn.addEventListener("click", () => {
            if (window.innerWidth <= 768) {
                navbar_and_logout_section.classList.toggle("show");
            }
        });

        window.addEventListener("resize", () => {
            if (window.innerWidth > 768) {
                navbar_and_logout_section.classList.add("show");
            } else {
                navbar_and_logout_section.classList.remove("show");
            }
        });

    </script>
</body>

</html>