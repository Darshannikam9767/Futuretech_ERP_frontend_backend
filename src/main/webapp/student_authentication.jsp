<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Futuretech software training institute | Student Login</title>
    <link rel="stylesheet" href="Assets/Student_css/student_authentication.css">
    <link rel="shortcut icon" href="Assets/images/logo.png" type="image/x-icon">
</head>

<body>

    <div class="Student_authentication">
        <div id="login_section">
            <div class="admin_btn">
                <button id="login_as_admin">Admin Login</button>
            </div>

            <div id="card">
                <header>
                    <h1>Student Portal</h1>
                    <p>Sign in to access your dashboard</p>
                </header>
                <hr>
                <p id="email_add">Email Address</p>
                <form action="LoginServlet" method="post">
                    <input type="email" name="email" placeholder="Enter your Email" autocomplete="email" required autofocus>
                    <input type="password" name="password" id="password" placeholder="Enter your Password"
                        autocomplete="current-password" required>
                	<button id="sign_in_btn" type="submit">Sign In</button>
                </form>
                <span id="forget_password">Forget Password?</span>
            </div>
        </div>


        <div id="forget_section">
            <div id="card">
                <header>
                    <h1>Verify Email Address</h1>
                    <p>Enter your email to receive an OTP</p>
                </header>
                <hr>
                <p id="email_add">Email Address</p>
                <form action="ForgetPasswordServlet" method="post">
                	<input type="hidden" name="role" value="student">
                    <input type="email" name="email" placeholder="Enter your Email" autocomplete="email" required>
                	<button id="request_otp_btn" type="submit">Request OTP</button>
                </form>
                <span id="back_to_login" >Back to Login</span>
            </div>
        </div>
        <div id="reset_section">
            <div id="card">
                <header>
                    <h1>
                        Set New Password
                    </h1>
                    <p>Enter OTP & New Password</p>
                </header>
                <hr>
                <form action="PasswordResetServlet" method="post">
                	<input type="hidden" name="role" value="student">
                    <input name="email" type="email" placeholder="Enter your Email" autocomplete="email" required>
                    <input type="text" id="otp_input" name="otp" placeholder="Enter 6-digit OTP"
                        autocomplete="one-time-code" maxlength="6" required>
                    <input type="password" name="new_password" placeholder="Enter new Password"
                        autocomplete="current-password" required>
                     <button id="set_password_btn" type="submit">Set Password</button>
                </form>
                <span id="resend_otp">Didn't get a OTP? Resend</span>
            </div>
        </div>

    </div>
	<script>
	const login_section = document.getElementById('login_section');
	const forget_section = document.getElementById('forget_section');
	const reset_section = document.getElementById('reset_section');

	const forget_password = document.getElementById('forget_password');
	const back_to_login = document.getElementById('back_to_login');
	const login_as_admin = document.getElementById('login_as_admin');

	const params = new URLSearchParams(window.location.search);
	const view = params.get("view");
	const status = params.get("status");
	const error = params.get("error");

	// Hide all sections first
	login_section.style.display = "none";
	forget_section.style.display = "none";
	reset_section.style.display = "none";

	// Show correct section
	if (view === "forget") {
	    forget_section.style.display = "block";
	} 
	else if (view === "reset") {
	    reset_section.style.display = "block";
	} 
	else {
	    login_section.style.display = "block";
	}

	// Status alerts
	if (status === "otp_sent") {
	    alert("OTP has been sent to your email.");
	}

	if (status === "password_changed") {
	    alert("Your password has been changed successfully.");
	}

	if (status === "invalid_otp") {
	    alert("Invalid or expired OTP.");
	}

	if (error === "notfound") {
	    alert("Invalid email. Please register first.");
	}

	// Navigation
	forget_password.addEventListener('click', () => {
	    window.location.href = "student_authentication.jsp?view=forget";
	});

	back_to_login.addEventListener('click', () => {
	    window.location.href = "student_authentication.jsp?view=login";
	});

	login_as_admin.addEventListener('click', () => {
	    window.location.href = "admin_authentication.jsp";
	});
    </script>
</body>

</html>