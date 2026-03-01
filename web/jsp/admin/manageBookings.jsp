<%@ page contentType="text/html;charset=UTF-8" %>

<h2>Manage Bookings</h2>

<div class="table-card">

<table>
<tr>
    <th>Reservation No</th>
    <th>Guest</th>
    <th>Room</th>
    <th>Total Amount</th>
    <th>Paid Amount</th>
    <th>Payment Status</th>
    <th>Action</th>
</tr>

<%
java.util.List<String[]> bookings =
    (java.util.List<String[]>) request.getAttribute("bookings");

if (bookings != null && !bookings.isEmpty()) {

    for (String[] b : bookings) {
%>

<tr>
    <td><%= b[0] %></td>
    <td><%= b[1] %></td>
    <td><%= b[2] %></td>
    <td>Rs. <%= b[3] %></td>
    <td>Rs. <%= b[4] == null ? "0.00" : b[4] %></td>
    <td><%= b[5] %></td>

    <td>

        <% if ("PAID".equalsIgnoreCase(b[5])) { %>

        <a class="edit-btn"
           href="${pageContext.request.contextPath}/downloadBill?reservationNo=<%= b[0] %>">
           Download Bill
        </a>

        <% } else { %>

        <a class="edit-btn"
           href="${pageContext.request.contextPath}/manageReservation?action=details&reservationNo=<%= b[0] %>">
           View / Make Payment
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