<%@ page contentType="text/html;charset=UTF-8" %>

<h2>Manage Reservation</h2>

<!-- =============================
   CHECK AVAILABILITY
============================== -->

<div class="form-card">
<form method="get"
      action="${pageContext.request.contextPath}/manageReservation">

    <label>Check In Date</label>
    <input type="date" name="checkIn"
           value="<%= request.getParameter("checkIn") != null ? request.getParameter("checkIn") : "" %>"
           required>

    <label>Check Out Date</label>
    <input type="date" name="checkOut"
           value="<%= request.getParameter("checkOut") != null ? request.getParameter("checkOut") : "" %>"
           required>

    <button type="submit">Check Available Rooms</button>
</form>
</div>


<%
String checkIn = request.getParameter("checkIn");
String checkOut = request.getParameter("checkOut");

java.util.List<com.oceanview.model.Room> availableRooms =
    (java.util.List<com.oceanview.model.Room>)
    request.getAttribute("availableRooms");

com.oceanview.model.User foundGuest =
    (com.oceanview.model.User)
    request.getAttribute("foundGuest");
%>

<!-- =============================
   SELECTED DATE DISPLAY
============================== -->

<% if (checkIn != null && checkOut != null) { %>
<div class="table-card">
    <h4>Selected Dates</h4>
    Check In: <b><%= checkIn %></b><br>
    Check Out: <b><%= checkOut %></b>
</div>
<% } %>


<!-- =============================
   EXISTING GUEST SEARCH
============================== -->

<% if (availableRooms != null && !availableRooms.isEmpty()) { %>

<div class="form-card">

<form method="get"
      action="${pageContext.request.contextPath}/manageReservation">

    <input type="hidden" name="checkIn" value="<%= checkIn %>">
    <input type="hidden" name="checkOut" value="<%= checkOut %>">

    <input type="text" name="searchValue"
           placeholder="Search Existing Guest (Email / Contact)">
    <button type="submit">Search Guest</button>

</form>

</div>

<% } %>


<!-- =============================
   RESERVATION POST FORM (ONLY ONE)
============================== -->

<% if (availableRooms != null && !availableRooms.isEmpty()) { %>

<div class="form-card">

<form method="post"
      action="${pageContext.request.contextPath}/manageReservation">

    <input type="hidden" name="checkIn" value="<%= checkIn %>">
    <input type="hidden" name="checkOut" value="<%= checkOut %>">

    <!-- ROOM SELECTION -->
    <label>Select Room</label>
    <select name="roomId" id="roomSelect" required>
        <% for (com.oceanview.model.Room room : availableRooms) { %>
        <option value="<%= room.getId() %>"
                data-price="<%= room.getPricePerNight() %>">
            Room <%= room.getRoomNumber() %> -
            Rs. <%= room.getPricePerNight() %> per night
        </option>
        <% } %>
    </select>

    <br><br>

    <!-- GUEST COUNT -->
    <label>Guest Count</label>
    <input type="number" name="guestCount" min="1" required>

    <hr>

    <% if (foundGuest != null) { %>

        <!-- EXISTING GUEST -->
        <input type="hidden" name="guestType" value="existing">
        <input type="hidden" name="guestId"
               value="<%= foundGuest.getId() %>">

        <h4>Existing Guest Details</h4>

        <label>Full Name</label>
        <input type="text"
               value="<%= foundGuest.getFullName() %>" readonly>

        <label>Email</label>
        <input type="email"
               value="<%= foundGuest.getEmail() %>" readonly>

        <label>Contact</label>
        <input type="text"
               value="<%= foundGuest.getContact() %>" readonly>

        <label>Address</label>
        <input type="text"
               value="<%= foundGuest.getAddress() %>" readonly>

    <% } else { %>

        <!-- NEW GUEST -->
        <input type="hidden" name="guestType" value="new">

        <h4>New Guest Details</h4>

        <label>Username</label>
        <input type="text" name="username" required>

        <label>Full Name</label>
        <input type="text" name="fullName" required>

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Contact</label>
        <input type="text" name="contact" required>

        <label>Address</label>
        <input type="text" name="address" required>

    <% } %>

    <hr>

    <!-- TOTAL AMOUNT -->
    <label>Total Amount</label>
    <input type="text" id="totalAmount"
           name="totalAmount" readonly>

    <br><br>

    <button type="submit">Confirm Reservation</button>

</form>

</div>

<% } %>



<!-- =============================
   RESERVATION LIST
============================== -->

<div class="table-card">

<h3>All Reservations</h3>

<table>
<tr>
    <th>Reservation No</th>
    <th>Guest</th>
    <th>Room</th>
    <th>Check In</th>
    <th>Check Out</th>
    <th>Total</th>
    <th>Status</th>
    <th>Action</th>
</tr>

<%
java.util.List<String[]> reservations =
    (java.util.List<String[]>)
    request.getAttribute("reservations");

if (reservations != null && !reservations.isEmpty()) {
    for (String[] res : reservations) {
%>

<tr>
    <td><%= res[0] %></td>
    <td><%= res[1] %></td>
    <td><%= res[2] %></td>
    <td><%= res[3] %></td>
    <td><%= res[4] %></td>
    <td>Rs. <%= res[5] %></td>
    <td><%= res[6] %></td>

    <td>
        <% if ("PENDING".equals(res[6])) { %>

        <a class="edit-btn"
           href="${pageContext.request.contextPath}/manageReservation?action=confirm&reservationNo=<%= res[0] %>">
           Confirm
        </a>

        <a class="delete-btn"
           href="${pageContext.request.contextPath}/manageReservation?action=cancel&reservationNo=<%= res[0] %>">
           Cancel
        </a>

        <% } else { %>

        <a class="edit-btn"
           href="${pageContext.request.contextPath}/manageReservation?action=details&reservationNo=<%= res[0] %>">
           Detail
        </a>

        <% } %>
    </td>
</tr>

<%
    }
}
%>

</table>
</div>


<!-- =============================
   TOTAL AMOUNT CALCULATION JS
============================== -->

<script>
document.addEventListener("DOMContentLoaded", function () {

    const roomSelect = document.getElementById("roomSelect");
    const totalAmountInput = document.getElementById("totalAmount");

    const checkIn = "<%= checkIn %>";
    const checkOut = "<%= checkOut %>";

    function calculateAmount() {

        if (!roomSelect || !checkIn || !checkOut) return;

        const selectedOption =
            roomSelect.options[roomSelect.selectedIndex];

        const price =
            parseFloat(selectedOption.getAttribute("data-price"));

        const date1 = new Date(checkIn);
        const date2 = new Date(checkOut);

        const diffTime = date2 - date1;
        const days = diffTime / (1000 * 60 * 60 * 24);

        const total = price * (days > 0 ? days : 1);

        totalAmountInput.value = total.toFixed(2);
    }

    if (roomSelect) {
        roomSelect.addEventListener("change", calculateAmount);
        calculateAmount();
    }
});
</script>