<%@ page contentType="text/html;charset=UTF-8" %>

<h2>Welcome Back 👋</h2>

<div class="dashboard-grid">

    <div class="card blue">
        <h3>Total Reservations</h3>
        <p><%= request.getAttribute("totalReservations") %></p>
    </div>

    <div class="card green">
        <h3>Total Bookings</h3>
        <p><%= request.getAttribute("totalBookings") %></p>
    </div>

</div>

<hr>

<h3>Quick Actions</h3>

<div class="quick-actions">

    <a class="action-btn"
       href="${pageContext.request.contextPath}/guest?action=reservations">
        View Reservations
    </a>

    <a class="action-btn"
       href="${pageContext.request.contextPath}/guest?action=bookings">
        View Bookings
    </a>

</div>

<style>
.dashboard-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit,minmax(250px,1fr));
    gap: 20px;
    margin-top: 20px;
}

.card {
    padding: 25px;
    border-radius: 12px;
    color: white;
    text-align: center;
    font-size: 18px;
    box-shadow: 0 6px 18px rgba(0,0,0,0.15);
    transition: 0.3s;
}

.card:hover {
    transform: translateY(-5px);
}

.card p {
    font-size: 28px;
    font-weight: bold;
    margin-top: 10px;
}

.blue {
    background: linear-gradient(135deg,#00c6ff,#0072ff);
}

.green {
    background: linear-gradient(135deg,#11998e,#38ef7d);
}

.quick-actions {
    margin-top: 20px;
}

.action-btn {
    display: inline-block;
    padding: 12px 18px;
    background: #1e3c72;
    color: white;
    text-decoration: none;
    border-radius: 8px;
    margin-right: 10px;
    transition: 0.3s;
}

.action-btn:hover {
    background: #16335d;
}
</style>