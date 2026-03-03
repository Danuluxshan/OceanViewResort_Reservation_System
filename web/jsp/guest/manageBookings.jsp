<%@ page contentType="text/html;charset=UTF-8" %>

<h2>Manage Bookings</h2>

<!-- ================= BOOKINGS TABLE ================= -->

<div class="table-card">

    <table>
        <tr>
            <th>Booking ID</th>
            <th>Reservation No</th>
            <th>Guest</th>
            <th>Room</th>
            <th>Total Amount</th>
            <th>Paid Amount</th>
            <th>Payment Status</th>
            <th>Booking Status</th>
            <th>Action</th>
        </tr>

        <%
            java.util.List<String[]> bookings
                    = (java.util.List<String[]>) request.getAttribute("bookings");

            if (bookings != null && !bookings.isEmpty()) {

                for (String[] b : bookings) {
        %>

        <tr>
            <td><%= b[0]%></td>   <!-- booking_id -->
            <td><%= b[1]%></td>   <!-- reservation_number -->
            <td><%= b[2]%></td>   <!-- guest -->
            <td><%= b[3]%></td>   <!-- room -->
            <td>Rs. <%= b[4]%></td>
            <td>Rs. <%= b[5] == null ? "0.00" : b[5]%></td>
            <td><%= b[6]%></td>   <!-- payment_status -->
            <td><%= b[7]%></td>   <!-- booking_status -->

            <td>

                <!-- If Cancelled -->
                <% if ("CANCELLED".equalsIgnoreCase(b[7])) { %>

                <span style="color:red;font-weight:bold;">
                    Cancelled
                </span>

                <% } else { %>

                <!-- Payment Completed -->
                <% if ("PAID".equalsIgnoreCase(b[6])) {%>

                <a class="edit-btn"
                   href="${pageContext.request.contextPath}/downloadBill?reservationNo=<%= b[1]%>">
                    View Bill
                </a>

                <% } else {%>

                <a class="edit-btn"
                   href="${pageContext.request.contextPath}/manageReservation?action=details&reservationNo=<%= b[1]%>">
                    View / Make Payment
                </a>

                <% }%>
                <% } %>

            </td>
        </tr>

        <%
            }
        } else {
        %>

        <tr>
            <td colspan="9" style="text-align:center;">
                No Bookings Found
            </td>
        </tr>

        <%
            }
        %>

    </table>

</div>