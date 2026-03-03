<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.User" %>

<%
    User loggedUser = (User) session.getAttribute("user");
    String initials = "G";

    if (loggedUser != null && loggedUser.getFullName() != null) {
        initials = loggedUser.getFullName()
                .substring(0, 1).toUpperCase();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Guest Panel</title>

        <style>
            body {
                margin: 0;
                font-family: 'Segoe UI', sans-serif;
                background: #f4f6f9;
            }

            .container {
                display: flex;
            }

            /* SIDEBAR */
            .sidebar {
                width: 260px;
                background: linear-gradient(180deg,#1e3c72,#2a5298);
                color: white;
                min-height: 100vh;
                padding: 20px;
            }

            .hotel-name {
                font-size: 20px;
                font-weight: bold;
                margin-bottom: 30px;
            }

            .profile-box {
                text-align: center;
                margin-bottom: 30px;
            }

            .avatar {
                width: 70px;
                height: 70px;
                background: white;
                color: #1e3c72;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 26px;
                font-weight: bold;
                margin: 0 auto 10px;
            }

            .profile-box h4 {
                margin: 5px 0;
            }

            .profile-box p {
                font-size: 12px;
                opacity: 0.8;
            }

            .sidebar a {
                display: block;
                padding: 12px;
                margin-bottom: 10px;
                color: white;
                text-decoration: none;
                border-radius: 8px;
                transition: 0.3s;
            }

            .sidebar a:hover {
                background: rgba(255,255,255,0.2);
            }

            /* MAIN CONTENT */
            .main-content {
                flex: 1;
                padding: 30px;
            }

            .page-header h2 {
                margin-bottom: 5px;
            }

            .card {
                background: #ffffff;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
                margin-bottom: 25px;
            }

            .form-row {
                display: flex;
                gap: 20px;
                margin-bottom: 15px;
            }

            input {
                padding: 8px;
                width: 200px;
                border-radius: 6px;
                border: 1px solid #ddd;
            }

            .primary-btn {
                background: #1e3c72;
                color: white;
                padding: 10px 16px;
                border: none;
                border-radius: 6px;
            }

            .success-btn {
                background: #28a745;
                color: white;
                padding: 8px 14px;
                border: none;
                border-radius: 6px;
            }

            .danger-btn {
                background: #dc3545;
                color: white;
                padding: 6px 10px;
                border-radius: 6px;
                text-decoration: none;
            }

            .room-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(250px,1fr));
                gap: 20px;
            }

            .room-card {
                background: #fff;
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 3px 12px rgba(0,0,0,0.05);
            }

            .price {
                font-weight: bold;
                color: #1e3c72;
            }

            .modern-table {
                width: 100%;
                border-collapse: collapse;
            }

            .modern-table th {
                background: #1e3c72;
                color: white;
                padding: 10px;
            }

            .modern-table td {
                padding: 10px;
                border-bottom: 1px solid #eee;
            }

            .status {
                padding: 4px 8px;
                border-radius: 6px;
                font-size: 12px;
                font-weight: bold;
            }

            .status.pending {
                background:#fff3cd;
                color:#856404;
            }
            .status.confirmed {
                background:#cce5ff;
                color:#004085;
            }
            .status.cancelled {
                background:#f8d7da;
                color:#721c24;
            }
            .status.paid {
                background:#d4edda;
                color:#155724;
            }
            .status.completed {
                background:#d1ecf1;
                color:#0c5460;
            }
            .profile-card {
                background: #fff;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 3px 12px rgba(0,0,0,0.05);
                margin-bottom: 25px;
                max-width: 500px;
            }

            .profile-card input,
            .profile-card textarea {
                width: 100%;
                padding: 8px;
                margin-bottom: 10px;
                border-radius: 6px;
                border: 1px solid #ddd;
            }

            textarea {
                resize: none;
                height: 80px;
            }

            .error {
                color: red;
                font-weight: bold;
            }

            .success {
                color: green;
                font-weight: bold;
            }
            .profile-card {
                background: #fff;
                padding: 25px;
                border-radius: 14px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
                margin-bottom: 25px;
                max-width: 500px;
            }

            .profile-row {
                margin-bottom: 15px;
            }

            .profile-row label {
                font-weight: 600;
                color: #555;
            }

            .profile-row span {
                display: block;
                margin-top: 4px;
            }

            input, textarea {
                width: 100%;
                padding: 8px;
                margin-bottom: 12px;
                border-radius: 8px;
                border: 1px solid #ddd;
            }

            textarea {
                resize: none;
                height: 80px;
            }

            .button-group {
                display: flex;
                gap: 10px;
            }

            .primary-btn {
                background: #1e3c72;
                color: white;
                padding: 8px 14px;
                border: none;
                border-radius: 8px;
            }

            .success-btn {
                background: #28a745;
                color: white;
                padding: 8px 14px;
                border: none;
                border-radius: 8px;
            }

            .secondary-btn {
                background: #6c757d;
                color: white;
                padding: 8px 14px;
                border: none;
                border-radius: 8px;
            }

            .danger-btn {
                background: #dc3545;
                color: white;
                padding: 8px 14px;
                border: none;
                border-radius: 8px;
            }

            .error {
                color: red;
                font-weight: bold;
            }

            .success-text {
                color: green;
                font-weight: bold;
            }

            .warning-text {
                color: #856404;
                margin-bottom: 10px;
            }

            .toast {
                position: fixed;
                bottom: 30px;
                right: 30px;
                background: #28a745;
                color: white;
                padding: 12px 20px;
                border-radius: 8px;
                opacity: 0;
                transition: opacity 0.5s ease;
            }

            .toast.show {
                opacity: 1;
            }
            .help-card {
                background: #fff;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
                max-width: 700px;
            }

            .help-card h3 {
                margin-top: 20px;
                color: #1e3c72;
            }

            .help-card ul,
            .help-card ol {
                margin-left: 20px;
            }

        </style>
    </head>

    <body>

        <div class="container">

            <div class="sidebar">

                <div class="hotel-name">
                    Ocean View Resort
                </div>

                <div class="profile-box">
                    <div class="avatar"><%= initials%></div>
                    <h4><%= loggedUser.getFullName()%></h4>
                    <p>ID: <%= loggedUser.getId()%></p>
                </div>

                <a href="${pageContext.request.contextPath}/guest?action=dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/guest?action=reservations">My Reservations</a>
                <a href="${pageContext.request.contextPath}/guest?action=addReservation">Add Reservation</a>
                <a href="${pageContext.request.contextPath}/guest?action=bookings">My Bookings</a>
                <a href="${pageContext.request.contextPath}/guest?action=profile">Profile</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
                <a href="${pageContext.request.contextPath}/guest?action=help">Help</a>

            </div>

            <div class="main-content">
                <jsp:include page="${contentPage}" />
            </div>

        </div>

    </body>
</html>