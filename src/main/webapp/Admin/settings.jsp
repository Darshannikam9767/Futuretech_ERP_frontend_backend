<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/settings.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/Assets/images/logo.png" type="image/x-icon">
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
                <a href="${pageContext.request.contextPath}/AdminStudentDirectory" class="deactive">Student
                    Directory</a>
                <a href="${pageContext.request.contextPath}/AdminCourseManagement" class="deactive">Course
                    Management</a>
                <a href="${pageContext.request.contextPath}/AdminFeeTracker" class="deactive">Fee Tracker</a>
                <a href="${pageContext.request.contextPath}/AdminAssignments" class="deactive">Assignments</a>
                <a href="${pageContext.request.contextPath}/AdminRegister" class="deactive">Register</a>
                <a href="${pageContext.request.contextPath}/AdminSettings" class="active">Settings</a>
            </nav>
        </div>

        <div class="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
    <main class="main_content">
        <div class="institute_information" id="design_card">
            <h2>Institute Information</h2>
            <div class="actual_info">
                <div class="short_info_cards">
                    <div class="row_1">
                        <div class="institute_name">
                            <h3 class="sub_heading">Institute Name</h3>
                            <div id="subcard">
                                <h3>Futuretech Software Training Institue</h3>
                            </div>
                        </div>

                        <div class="institute_email">
                            <h3 class="sub_heading">Email Address</h3>
                            <div id="subcard">
                                <h3>info@futuretech.com</h3>
                            </div>
                        </div>
                    </div>

                    <div class="row_2">
                        <div class="phone_number">
                            <h3 class="sub_heading">Phone Number</h3>
                            <div id="subcard">
                                <h3>+91 <span>9876643210</span> </h3>
                            </div>
                        </div>

                        <div class="website_url">
                            <h3 class="sub_heading">Website</h3>
                            <div id="subcard">
                                <a href="www.futuretech.com">
                                    <h3>www.futuretech.com</h3>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="long_info_card">
                    <div class="address">
                        <h3 class="sub_heading">Address</h3>
                        <div id="subcard">
                            <h3>123 Tech Park, Bangalore, Karnataka 560001</h3>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>


     <% if(session.getAttribute("userrole")==null){ %>
        <script type="text/javascript">
            alert("Access Denied! Please login first.")
            window.location.href = "../admin_authentication.jsp"
        </script>
        <% return; } %>
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

        const logout_btn = document.getElementById("logout_btn");
        logout_btn.addEventListener('click', () => {
            if (confirm("Do you realy want to Logout"))
                window.location.href = "${pageContext.request.contextPath}/LogoutServlet";
        })
    </script>
</body>

</html>