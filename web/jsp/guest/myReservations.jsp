<%@ page contentType="text/html;charset=UTF-8" %>

<h2>My Reservations</h2>

<div class="card">

<table class="modern-table">
    <tr>
        <th>Reservation No</th>
        <th>Check In</th>
        <th>Check Out</th>
        <th>Total</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

<%
java.util.List<String[]> list =
        (java.util.List<String[]>) request.getAttribute("reservations");

if (list != null) {
    for (String[] r : list) {
%>

<tr>
    <td><%= r[0] %></td>
    <td><%= r[1] %></td>
    <td><%= r[2] %></td>
    <td>Rs. <%= r[3] %></td>
    <td>
        <span class="status <%= r[4].toLowerCase() %>">
            <%= r[4] %>
        </span>
    </td>
    <td>
        <% if ("PENDING".equalsIgnoreCase(r[4])) { %>

        <a class="danger-btn"
           href="${pageContext.request.contextPath}/guestReservation?action=cancel&reservationNo=<%= r[0] %>">
            Cancel
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