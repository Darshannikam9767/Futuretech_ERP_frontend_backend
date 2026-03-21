<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css"> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/course_management.css"> 
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
                <a href="${pageContext.request.contextPath}/AdminCourseManagement" class="active">Course
                    Management</a>
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

    <main class="main_content">
        <div class="quick_info_pannel">
            <div class="total_courses quick_pannel_subcard">
                <p>Total Courses</p>
                <h1 id="total_courses">4</h1>
            </div>

            <div class="total_students quick_pannel_subcard">
                <p>Total Students</p>
                <h1 id="total_students">31</h1>
            </div>

            <div class="domains quick_pannel_subcard">
                <p>Domains</p>
                <h1 id="total_domains">3</span></h1>
            </div>

        </div>

        <div class="course_and_domain_management">
            <div id="top_title_and_button">
                <h3>Course & Domain Management</h3>
                <button id="add_new_course_btn">+ <span>Add New Course</span></button>
            </div>
            <div id="add_course_form" class="hidden">
                <form action="" method="POST">
                    <input type="text" id="new_course_name" name="course_name" placeholder="Course Name" required>
                    <input type="text" name="fees" placeholder="Total Fees" required>
                    <input type="text" name="duration" placeholder="Duration (e.g. 6 Months)" required>
                    <input type="text" name="description" placeholder="Course Description"></>
                    <button type="" id="add_course_btn">Add Course</button>
                </form>
            </div>

            <div id="edit_course_form" class="hidden">
                <form action="" method="POST">
                    <input type="text" id="real_c_id" name="course_id" placeholder="Course ID">
                    <input type="text" id="edit_course_name" name="course_name" placeholder="Course Name" required>
                    <input type="text" id="edit_course_fees" name="fees" placeholder="Total Fees" required>
                    <input type="text" id="edit_course_duration" name="duration" placeholder="Duration (e.g. 6 Months)"
                        required>
                    <input type="text" id="edit_course_description" name="description" placeholder="Course Description">
                    </>
                    <button type="" id="update_course_btn">Save Course Changes</button>
                </form>
            </div>

            <div class="all_courses_cards">
                <div class="course_card">
                    <div class="title_info">
                        <h3 id="course_name">
                            Web Development
                        </h3>
                        <h4 id="course_body">
                            Complete Web Development, HTML, CSS, JS, Bootstrap
                        </h4>
                    </div>
                    <div class="course_info">
                        <h3 hidden id="real_course_id">1</h3>
                        <div class="duration_subcard">
                            <h3 id="duration">6 Months</h3>
                            <span id="d_span">Duration</span>
                        </div>
                        <div class="fees_subcard">
                            <h3 id="fees">₹ <span>42000</span></h3>
                            <span id="f_span">Fee</span>
                        </div>
                    </div>
                    <div class="action_div">
                        <button class="edit-btn">EDIT</button>
                        <button class="delete-btn">DELETE</button>
                    </div>
                </div>

                <div class="course_card">
                    <div class="title_info">
                        <h3 id="course_name">
                            Data Science
                        </h3>
                        <h4 id="course_body">
                            Complete Data Science course, All necessary libraries
                        </h4>
                    </div>
                    <div class="course_info">
                        <h3 hidden id="real_course_id">2</h3>
                        <div class="duration_subcard">
                            <h3 id="duration">6 Months</h3>
                            <span id="d_span">Duration</span>
                        </div>
                        <div class="fees_subcard">
                            <h3 id="fees">₹ <span>42000</span></h3>
                            <span id="f_span">Fee</span>
                        </div>
                    </div>
                    <div class="action_div">
                        <button class="edit-btn">EDIT</button>
                        <button class="delete-btn">DELETE</button>
                    </div>
                </div>

                <div class="course_card">
                    <div class="title_info">
                        <h3 id="course_name">
                            Java Full Stack Development
                        </h3>
                        <h4 id="course_body">
                            Complete Java development with Spring Boot, Hibernate, and React
                        </h4>
                    </div>
                    <div class="course_info">
                        <h3 hidden id="real_course_id">3</h3>
                        <div class="duration_subcard">
                            <h3 id="duration">6 Months</h3>
                            <span id="d_span">Duration</span>
                        </div>
                        <div class="fees_subcard">
                            <h3 id="fees">₹ <span>42000</span></h3>
                            <span id="f_span">Fee</span>
                        </div>
                    </div>
                    <div class="action_div">
                        <button class="edit-btn">EDIT</button>
                        <button class="delete-btn">DELETE</button>
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


        const addNewCourseBtn = document.getElementById("add_new_course_btn");
        const addCourseFormContainer = document.getElementById("add_course_form");
        const editCourseForm = document.getElementById("edit_course_form");

        addNewCourseBtn.addEventListener('click', () => {
            addCourseFormContainer.classList.toggle("hidden");
            if (!addCourseFormContainer.classList.contains("hidden")) {
                document.getElementById("new_course_name").focus();
                addCourseFormContainer.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center'
                });
            }
        });

        const addCourseSubmitBtn = document.getElementById("add_course_btn");
        addCourseSubmitBtn.addEventListener('click', (e) => {
            e.preventDefault();
            addCourseFormContainer.classList.add("hidden");
        });

        document.addEventListener('click', (e) => {
            const target = e.target;

            if (target && target.classList.contains('edit-btn')) {
                e.preventDefault();
                const card = target.closest('.course_card');

                const id = card.querySelector("#real_course_id").innerText;
                const name = card.querySelector("#course_name").innerText.trim();
                const desc = card.querySelector("#course_body").innerText.trim();
                const duration = card.querySelector("#duration").innerText;
                const fees = card.querySelector("#fees span").innerText;

                document.getElementById("real_c_id").value = id;
                document.getElementById("edit_course_name").value = name;
                document.getElementById("edit_course_fees").value = fees;
                document.getElementById("edit_course_duration").value = duration;
                document.getElementById("edit_course_description").value = desc;

                editCourseForm.classList.remove("hidden");
                editCourseForm.style.display = "block";

                requestAnimationFrame(() => {
                    editCourseForm.scrollIntoView({
                        behavior: 'smooth',
                        block: 'center'
                    });
                    document.getElementById("edit_course_name").focus();
                });

            } else if (target && target.classList.contains('delete-btn')) {
                e.preventDefault();
                const card = target.closest('.course_card');
                const id = card.querySelector("#real_course_id").innerText;
                const courseName = card.querySelector("#course_name").innerText.trim();

                if (confirm(`Delete "${courseName}"?`)) {
                    window.location.href = `DeleteCourseServlet?course_id=${id}`;
                }
            }
        });

        const updateCourseBtn = document.getElementById("update_course_btn")
        updateCourseBtn.addEventListener('click', (e) => {
            e.preventDefault();
            editCourseForm.classList.add("hidden");
        })

    </script>
</body>

</html>