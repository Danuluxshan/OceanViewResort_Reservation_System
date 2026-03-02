<%@ page contentType="text/html;charset=UTF-8" %>

<h2 class="dashboard-title">Receptionist Dashboard</h2>

<div class="dashboard-grid">

    <!-- TODAY RESERVATIONS -->
    <div class="dashboard-card gradient-blue">
        <h3>Today's Reservations</h3>
        <p><%= request.getAttribute("todayReservations") %></p>
    </div>

    <!-- TODAY CHECK INS -->
    <div class="dashboard-card gradient-green">
        <h3>Today's Check-ins</h3>
        <p><%= request.getAttribute("todayCheckins") %></p>
    </div>

    <!-- PENDING PAYMENTS -->
    <div class="dashboard-card gradient-orange">
        <h3>Pending Payments</h3>
        <p><%= request.getAttribute("pendingPayments") %></p>
    </div>

    <!-- TOTAL BOOKINGS -->
    <div class="dashboard-card gradient-purple">
        <h3>Total Bookings</h3>
        <p><%= request.getAttribute("totalBookings") %></p>
    </div>

</div>

<hr>

<h3>Quick Actions</h3>

<div class="quick-actions">

    <a href="${pageContext.request.contextPath}/manageReservation"
       class="action-btn">
        + New Reservation
    </a>

    <a href="${pageContext.request.contextPath}/manageReservation?action=manageBookings"
       class="action-btn">
        Manage Bookings
    </a>

    <a href="${pageContext.request.contextPath}/manageReservation?action=reports"
       class="action-btn">
        View Reports
    </a>

</div>