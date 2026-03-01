<h2>Reservation Confirmation</h2>

<div class="table-card">

<%
java.util.List<String[]> details =
    (java.util.List<String[]>)
    request.getAttribute("confirmationDetails");

if (details != null && !details.isEmpty()) {

    String[] res = details.get(0);
%>

<p><strong>Reservation No:</strong> <%= res[0] %></p>
<p><strong>Guest:</strong> <%= res[1] %></p>
<p><strong>Room:</strong> <%= res[2] %></p>
<p><strong>Check In:</strong> <%= res[3] %></p>
<p><strong>Check Out:</strong> <%= res[4] %></p>
<p><strong>Total Amount:</strong> Rs. <%= res[5] %></p>
<p><strong>Status:</strong> <%= res[6] %></p>

<hr>

<form method="post"
      action="${pageContext.request.contextPath}/manageReservation">

    <input type="hidden" name="finalConfirm" value="true">
    <input type="hidden" name="reservationNo"
           value="<%= res[0] %>">

    <button type="submit" class="edit-btn">
        Confirm Booking
    </button>

    <a href="${pageContext.request.contextPath}/manageReservation"
       class="delete-btn">
        Cancel
    </a>

</form>

<%
}
%>

</div>