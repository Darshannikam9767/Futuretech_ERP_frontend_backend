<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/admin_dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Admin_css/fee_tracker.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
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
                <a href="${pageContext.request.contextPath}/AdminStudentDirectory" class="deactive">Student Directory</a>
                <a href="${pageContext.request.contextPath}/AdminCourseManagement" class="deactive">Course Management</a>
                <a href="${pageContext.request.contextPath}/AdminFeeTracker" class="active">Fee Tracker</a>
                <a href="${pageContext.request.contextPath}/AdminAssignments" class="deactive">Assignments</a>
                <a href="${pageContext.request.contextPath}/AdminRegister" class="deactive">Register</a>
                <a href="${pageContext.request.contextPath}/AdminSettings" class="deactive">Settings</a>
            </nav>
        </div>

        <div class="logout_section">
            <h2 id="logout_btn">Logout</h2>
        </div>
    </div>
    
    <main class="main_content" id="main_fee_content">
    
     	<div class="fee_info_pannel">
            <div class="total_revenue fee_pannel_subcard">
                <p>Total Revenue</p>
                <h1 id="total_revenue">23000</h1>
            </div>

            <div class="total_collected fee_pannel_subcard">
                <p>Collected</p>
                <h1 id="total_collected">₹ <span>12000</span></h1>
            </div>

            <div class="pending_fees fee_pannel_subcard">
                <p>Pending Fees</p>
                <h1 id="total_pending_fees">₹ <span>45000</span></h1>
            </div>

        </div>
     	
     	
     	<div class="fee_tracker show_add_payment_form">
            <div id="top_title_and_button">
                <h3>Fee Tracker - Detailed View</h3>
            </div>
            
            <div class="edit_form show_edit_form">
                <form action="AdminUpdatePayment" method="post">
                	    <input type="text" hidden name="real_student_id" id="edit_Student_id">
                    <input type="text" name="student_name" id="student_name" placeholder="Student Name" readonly>
                    <input type="text" name="paid_fee"  id="new_fees" placeholder="Paid Fees">
                    <button id="update_btn" type="submit">Update Payment</button>
                </form>
            </div>
            <div class="enable_scroll student_fee_table">
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
                                Course
                            </th>
                            <th>
                            	Total Fee
                            </th>
                            <th>
                                Paid
                            </th>
                            <th>
                                Balance
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
                    	<tr>
                    		<td hidden>100</td>
                    		<td>1</td>
                    		<td>Amit Kumar</td>
                    		<td>Java Full Stack</td>
                    		<td>$45000</td>
                    		<td>$20000</td>
                    		<td>25000</td>
                    		<td>Partial</td>
                    		<td>
                    			<div class='action_div'>
    	                			<i class='fa-solid fa-pen edit'></i>
    	                		</div>
    	                	</td>
                    	</tr>
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
        
        
        const mainDashboard = document.getElementById("main_fee_content");
		const editForm = document.querySelector(".edit_form");

		if (mainDashboard) {
    			mainDashboard.addEventListener('click', (event) => {
      
        		const btn = event.target.closest(".edit");

        		if (btn) {
            		const row = btn.closest("tr");
            
           			document.getElementById("edit_Student_id").value=row.cells[0].innerText.trim();
           			document.getElementById("student_name").value=row.cells[2].innerText;
           			
           			const rawFees = row.cells[5].innerText;
                    const numericFees = rawFees.replace(/[^\d.]/g, ''); 
                    document.getElementById("new_fees").value = numericFees;
           			
           			
         		editForm.style.display = "flex";
            		editForm.scrollIntoView({ behavior: 'smooth', block: 'center' });
            		setTimeout(() => {
                        document.getElementById("new_fees").focus();
                    }, 500);
        			}
    			});
		}
		
		const urlParams = new URLSearchParams(window.location.search);
		const updateStatus = urlParams.get('update');

		if (updateStatus === 'success') {
		    alert("✅ Payment Updated Successfully!");
		    // Clean the URL so the alert doesn't show again on refresh
		    window.history.replaceState({}, document.title, window.location.pathname);
		} else if (updateStatus === 'failed') {
		    alert("❌ Error: Could not update payment. Please try again.");
		} else if (urlParams.get('error') === 'invalid_format') {
		    alert("⚠️ Please enter a valid number for the fees.");
		}
        
        
    </script>
</body>

</html>