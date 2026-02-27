<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("loggedUser") == null ||
        !"ADMIN".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>

<div class="sidebar">
    <h2>OceanView</h2>

    <a href="#">Dashboard</a>
    <a href="${pageContext.request.contextPath}/manageReceptionists">Manage Receptionists</a>
    <a href="#">Manage Reservations</a>
    <a href="#">Reports</a>

    <a href="${pageContext.request.contextPath}/logout" class="logout">
        Logout
    </a>
</div>

<div class="main-content">

    <div class="header">
        <h1>Admin Dashboard</h1>
        <p>Welcome, ${sessionScope.loggedUser.username}</p>
    </div>

    <div class="cards">

        <div class="card">
            <h3>Total Users</h3>
            <p>120</p>
        </div>

        <div class="card">
            <h3>Total Guests</h3>
            <p>95</p>
        </div>

        <div class="card">
            <h3>Receptionists</h3>
            <p>5</p>
        </div>

        <div class="card">
            <h3>Active Reservations</h3>
            <p>32</p>
        </div>

    </div>

</div>

</body>
</html>