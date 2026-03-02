<%@ page contentType="text/html;charset=UTF-8" %>

<h2>Booking Details</h2>

<div class="table-card">

<%
java.util.List<String[]> details =
    (java.util.List<String[]>) request.getAttribute("bookingDetails");

if (details != null && !details.isEmpty()) {

    String[] b = details.get(0);
%>

<p><strong>Reservation No:</strong> <%= b[0] %></p>
<p><strong>Guest:</strong> <%= b[1] %></p>
<p><strong>Room:</strong> <%= b[2] %></p>
<p><strong>Check In:</strong> <%= b[3] %></p>
<p><strong>Check Out:</strong> <%= b[4] %></p>
<p><strong>Total Amount:</strong> Rs. <%= b[5] %></p>
<p><strong>Payment Status:</strong> <%= b[6] %></p>
<p><strong>Paid Amount:</strong> 
    <%= b[7] == null ? "0.00" : b[7] %>
</p>

<hr>

<%
String paymentError =
    (String) request.getAttribute("paymentError");

if (paymentError != null) {
%>
<p style="color:red;"><%= paymentError %></p>
<%
}
%>

<%
Boolean overPayment =
    (Boolean) request.getAttribute("overPayment");

if (overPayment != null && overPayment) {

    Double balance =
        (Double) request.getAttribute("balanceAmount");

    Double entered =
        (Double) request.getAttribute("enteredAmount");
%>

<p style="color:orange;">
Overpayment detected! Balance: Rs. <%= balance %>
</p>

<form method="post"
      action="${pageContext.request.contextPath}/manageReservation">

    <input type="hidden" name="confirmOverPayment" value="yes">
    <input type="hidden" name="reservationNo"
           value="<%= b[0] %>">
    <input type="hidden" name="finalAmount"
           value="<%= entered %>">

    <button type="submit">Confirm Payment</button>
</form>

<%
} else if (!"PAID".equals(b[6])) {
%>

<form method="post"
      action="${pageContext.request.contextPath}/manageReservation">

    <input type="hidden" name="paymentAction" value="pay">
    <input type="hidden" name="reservationNo"
           value="<%= b[0] %>">

    <label>Enter Payment Amount</label>
    <input type="number" step="0.01"
           name="paidAmount" required>

    <button class="edit-btn" type="submit">Make Payment</button>
</form>

<%
} else {
%>

<p style="color:green;"><strong>Payment Completed</strong></p>

<!-- 🔥 DOWNLOAD BILL BUTTON -->
<!--<a href="<%= request.getContextPath() %>/downloadBill?reservationNo=<%= b[0] %>"
   class="edit-btn">
   Download Bill
</a>-->
<a href="<%= request.getContextPath() %>/downloadBill?reservationNo=<%= java.net.URLEncoder.encode(b[0], "UTF-8") %>"
   class="edit-btn">
   Download Bill
</a>

<%
}
%>

<br><br>

<a href="${pageContext.request.contextPath}/manageReservation"
   class="delete-btn">
   Back
</a>

<%
} else {
%>

<p>No Booking Details Found.</p>

<%
}
%>

</div>