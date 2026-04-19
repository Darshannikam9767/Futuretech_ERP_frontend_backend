<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Student Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Student_css/student_dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Student_css/student_assignment.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/Assets/images/logo.png" type="image/x-icon">
	<link rel="preload" href="${pageContext.request.contextPath}/Assets/Student_css/student_dashboard.css" as="style">
	<link rel="preload" href="${pageContext.request.contextPath}/Assets/images/logo.png" as="image">
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
                <a href="${pageContext.request.contextPath}/StudentDashboard" class="deactive">Dashboard</a>
                <a href="${pageContext.request.contextPath}/StudentMyDomain" class="deactive">My Domain</a>
                <a href="${pageContext.request.contextPath}/StudentAssignment" class="active">Assignments</a>
                <a href="${pageContext.request.contextPath}/StudentFeeHistory" class="deactive">Fee History</a>
                <a href="${pageContext.request.contextPath}/StudentProfile" class="deactive">Profile</a>
            </nav>
        </div>

        <div class="logout_section" id="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
    <main class="assignment_main_content">
    <div class="upload_assignment_section" id="assignment_design_card">
        <h2>Upload Submission</h2>
        <form action="${pageContext.request.contextPath}/StudentSubmissionServlet" 
              method="POST" 
              enctype="multipart/form-data" 
              id="submissionForm">
          
            <input type="hidden" name="assignment_id" value="">
			<input type="hidden" name="source" value="assignment">
            <div class="drop_area" id="dropArea" style="cursor:pointer;">
                <p id="fileName">Click to browse</p>
                <span>Supports PDF, ZIP (Max 10MB)</span>
                
                <input type="file" name="submission_file" id="select_file" 
                       accept=".pdf,.zip" style="display:none;">
            </div>

            <button type="submit" class="assignment_submit_btn">Submit Assignment</button>
        </form>
    </div>
    
    <div class="task_due" id="assignment_design_card">
        <h2>Task Due</h2>
        <div id="due_subcard">
            <h3>Java Collection Framework</h3>
            <p id="description">Implement ArrayList, LinkedList, and HashMap with custom examples</p>
            <span id="assignment_time_left">5 days left</span>
            <p id="assignment_status">Pending</p>
        </div>
    </div>
</main>

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
    <script>
    
    const dropArea = document.getElementById('dropArea');
    const select_file = document.getElementById('select_file');
    const fileNameDisplay = document.getElementById('fileName');
    const submissionForm = document.getElementById('submissionForm');

    // 1. Trigger file browser on click
    if(dropArea) {
        dropArea.addEventListener('click', () => select_file.click());
    }

    // 2. Update filename and size display
   select_file.addEventListener('change', function () {
    if (this.files && this.files.length > 0) {
        const file = this.files[0];
        let displaySize = "";

        // 1. Smart Unit Logic
        if (file.size < 1024 * 1024) {
            // Less than 1MB? Show KB
            displaySize = (file.size / 1024).toFixed(2) + " KB";
        } else {
            // 1MB or more? Show MB
            displaySize = (file.size / (1024 * 1024)).toFixed(2) + " MB";
        }

        // 2. Update the UI
        fileNameDisplay.innerText = file.name + " (" + displaySize + ")";
        fileNameDisplay.style.color = "#2ecc71"; // Success Green
        
    } else {
        fileNameDisplay.innerText = "Click to browse";
        fileNameDisplay.style.color = "";
    }
});

    // 3. Simple Form Validation
    submissionForm.addEventListener('submit', function(e) {
        if (select_file.files.length === 0) {
            e.preventDefault(); 
            alert("Please select a file first!");
        } else {
            // Visual feedback that upload has started
            document.querySelector('.assignment_submit_btn').innerText = "Uploading...";
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
        
        window.addEventListener('load', function() {
            // 1. Check if the 'upload' parameter exists in the URL
            const urlParams = new URLSearchParams(window.location.search);
            
            if (urlParams.get('upload') === 'success') {
                // 2. Show the alert
                alert("Success! Your assignment has been submitted.");

                // 3. Clear the URL status without reloading the page
                if (window.history.replaceState) {
                    const cleanUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
                    window.history.replaceState({ path: cleanUrl }, '', cleanUrl);
                }
            }
        });

    </script>
</body>

</html>