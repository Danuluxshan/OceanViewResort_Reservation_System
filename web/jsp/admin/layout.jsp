<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("role") == null
            || !"ADMIN".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Admin Panel</title>
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/dashboard.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/manage.css">
    </head>
    <body>

        <div class="sidebar">
            <h2>OceanView</h2>

            <a href="${pageContext.request.contextPath}/adminDashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/manageReceptionists">Manage Receptionists</a>
            <a href="${pageContext.request.contextPath}/manageGuests">Manage Guests</a>
            <a href="${pageContext.request.contextPath}/manageRooms">Manage Rooms</a>
            <a href="${pageContext.request.contextPath}/manageReservation">Manage Reservations</a>
            <a href="${pageContext.request.contextPath}/manageReservation?action=manageBookings">Manage Bookings</a>
            <a href="#">Reports</a>

            <a href="${pageContext.request.contextPath}/logout" class="logout">
                Logout
            </a>
        </div>

        <div class="main-content">

            <div class="header">
                <h1>Admin Panel</h1>
                <p>Welcome, ${sessionScope.loggedUser.username}</p>
            </div>

            <!-- Dynamic Content Area -->
            <jsp:include page="${contentPage}" />

        </div>

    </body>
</html>