<%@ page contentType="text/html;charset=UTF-8" %>

<h2>My Bookings</h2>

<div class="card">

<table class="modern-table">
    <tr>
        <th>Booking ID</th>
        <th>Reservation No</th>
        <th>Total</th>
        <th>Paid</th>
        <th>Payment</th>
        <th>Status</th>
    </tr>

<%
java.util.List<String[]> list =
        (java.util.List<String[]>) request.getAttribute("bookings");

if (list != null) {
    for (String[] b : list) {
%>

<tr>
    <td><%= b[0] %></td>
    <td><%= b[1] %></td>
    <td>Rs. <%= b[2] %></td>
    <td>Rs. <%= b[3] == null ? "0.00" : b[3] %></td>
    <td>
        <span class="status <%= b[4].toLowerCase() %>">
            <%= b[4] %>
        </span>
    </td>
    <td>
        <span class="status <%= b[5].toLowerCase() %>">
            <%= b[5] %>
        </span>
    </td>
</tr>

<%
    }
}
%>

</table>
</div>