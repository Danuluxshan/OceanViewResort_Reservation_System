<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Room" %>

<div class="page-header">
    <h2>Book Your Stay</h2>
    <p>Select dates and choose your perfect room</p>
</div>

<div class="card">

    <!-- Date Search -->
    <form method="get"
          action="${pageContext.request.contextPath}/guestReservation">

        <input type="hidden" name="action" value="searchRooms">

        <div class="form-row">
            <div>
                <label>Check In</label>
                <input type="date" name="checkIn" required>
            </div>

            <div>
                <label>Check Out</label>
                <input type="date" name="checkOut" required>
            </div>
        </div>

        <button class="primary-btn">Check Availability</button>
    </form>
</div>

<%
List<Room> rooms =
        (List<Room>) request.getAttribute("availableRooms");
%>

<% if (rooms != null && !rooms.isEmpty()) { %>

<div class="room-grid">

    <% for (Room room : rooms) { %>

    <div class="room-card">

        <h3>Room <%= room.getRoomNumber() %></h3>
        <p>Capacity: <%= room.getCapacity() %> </p>
        <p class="price">
            Rs. <%= room.getPricePerNight() %> / Night
        </p>
        <p >
            Description: <%= room.getDescription()%>
        </p>

        <form method="post"
              action="${pageContext.request.contextPath}/guestReservation">

            <input type="hidden" name="roomId"
                   value="<%= room.getId() %>">

            <input type="hidden" name="checkIn"
                   value="<%= request.getAttribute("checkIn") %>">

            <input type="hidden" name="checkOut"
                   value="<%= request.getAttribute("checkOut") %>">

            <button class="success-btn">
                Reserve Now
            </button>

        </form>

    </div>

    <% } %>

</div>

<% } %>