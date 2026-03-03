<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page session="true" %>

<%
    com.oceanview.model.User loggedUser =
        (com.oceanview.model.User) session.getAttribute("user");

    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<html>
<head>
    <title>Receptionist Panel</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/receptionist.css">

</head>

<body>

<div class="wrapper">

    <!-- Sidebar -->
    <div class="sidebar">

        <div class="profile-box">
            <%
    String name = loggedUser.getFullName();
    String firstLetter = "R"; // default

    if (name != null && !name.isEmpty()) {
        firstLetter = name.substring(0,1).toUpperCase();
    }
%>

<div class="avatar">
    <%= firstLetter %>
</div>

            <h3>
<%= (loggedUser.getFullName() != null) 
        ? loggedUser.getFullName() 
        : "Receptionist" %>
</h3>
            <p>ID: <%= loggedUser.getId() %></p>
        </div>

        <ul class="menu">
            <li><a href="${pageContext.request.contextPath}/receptionist?action=dashboard">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/manageReservation">Manage Reservation</a></li>
            <li><a href="${pageContext.request.contextPath}/manageReservation?action=manageBookings">Manage Bookings</a></li>
            <li><a href="${pageContext.request.contextPath}/manageReservation?action=reports">Reports</a></li>
            <li><a href="${pageContext.request.contextPath}/receptionist?action=profile">Profile</a></li>
            <li><a href="${pageContext.request.contextPath}/manageReservation?action=help">Help</a></li>
        </ul>

        <div class="logout">
            <a href="${pageContext.request.contextPath}/logout">
                Logout
            </a>
        </div>

    </div>

    <!-- Main Content -->
    <div class="main-content">

        <jsp:include page="${contentPage}" />

    </div>

</div>

</body>
</html>