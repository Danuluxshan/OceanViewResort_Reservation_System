<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Ocean View Resort - Login</title>
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>

        <div class="card">
            <h2>Ocean View Resort</h2>

            <form action="${pageContext.request.contextPath}/login" method="post">

                <div class="input-group">
                    <label>Username</label>
                    <input type="text" name="username"
                           value="${username}">
                    <small class="error">${usernameError}</small>
                </div>

                <div class="input-group">
                    <label>Password</label>
                    <input type="password" name="password">
                    <small class="error">${passwordError}</small>
                </div>

                <small class="error">${generalError}</small>

                <button type="submit" class="btn">Login</button>
            </form>

            <div class="message">
                ${message}
            </div>

            <div class="link">
                <p>New Guest? <a href="${pageContext.request.contextPath}/register">
                        Register Here
                    </a></p>
            </div>
        </div>

    </body>
</html>