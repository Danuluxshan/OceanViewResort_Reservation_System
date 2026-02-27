<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Guest Registration</title>
        <link rel="stylesheet" 
              href="${pageContext.request.contextPath}/css/style.css">

        <script>
            function validateForm() {

                let email = document.forms["regForm"]["email"].value;
                let password = document.forms["regForm"]["password"].value;
                let confirmPassword = document.forms["regForm"]["confirmPassword"].value;
                let contact = document.forms["regForm"]["contact"].value;

                if (!email.includes("@")) {
                    alert("Enter valid email!");
                    return false;
                }

                if (password.length < 6) {
                    alert("Password must be at least 6 characters!");
                    return false;
                }

                if (password !== confirmPassword) {
                    alert("Passwords do not match!");
                    return false;
                }

                if (!/^[0-9]{10}$/.test(contact)) {
                    alert("Contact must be 10 digits!");
                    return false;
                }

                return true;
            }
        </script>
    </head>
    <body>

        <div class="card">
            <h2>Guest Registration</h2>

            <form name="regForm" 
                  action="${pageContext.request.contextPath}/register"
                  method="post"
                  onsubmit="return validateForm()">

                <div class="input-group">
                    <label>Full Name</label>
                    <input type="text" name="fullName"
                           value="${fullName}">
                    <small class="error">${fullNameError}</small>
                </div>

                <div class="input-group">
                    <label>Username</label>
                    <input type="text" name="username"
                           value="${username}">
                    <small class="error">${usernameError}</small>
                </div>

                <div class="input-group">
                    <label>Email</label>
                    <input type="email" name="email"
                           value="${email}">
                    <small class="error">${emailError}</small>
                </div>

                <div class="input-group">
                    <label>Contact Number</label>
                    <input type="text" name="contact"
                           value="${contact}">
                    <small class="error">${contactError}</small>
                </div>

                <div class="input-group">
                    <label>Address</label>
                    <input type="text" name="address" required>
                </div>

                <div class="input-group">
                    <label>Password</label>
                    <input type="password" name="password" required>
                </div>

                <div class="input-group">
                    <label>Confirm Password</label>
                    <input type="password" name="confirmPassword">
                    <small class="error">${confirmPasswordError}</small>
                </div>

                <button type="submit" class="btn">Register</button>
            </form>

            <div class="message">
                ${message}
            </div>

            <div class="link">
                <a href="${pageContext.request.contextPath}/login">
                    Already have account? Login
                </a>
            </div>
        </div>

    </body>
</html>