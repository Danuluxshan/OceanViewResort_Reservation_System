<h2>Reports</h2>

<h3>Monthly Revenue</h3>

<table>
<tr>
    <th>Month</th>
    <th>Revenue</th>
</tr>

<%
    java.util.List<String[]> revenue =
        (java.util.List<String[]>) request.getAttribute("monthlyRevenue");

    if (revenue != null) {
        for (String[] r : revenue) {
%>

<tr>
    <td><%= r[0] %></td>
    <td>Rs. <%= r[1] %></td>
</tr>

<%
        }
    }
%>
</table>

<br><br>

<h3>Room Occupancy Report</h3>

<table>
<tr>
    <th>Room Number</th>
    <th>Total Completed Bookings</th>
</tr>

<%
    java.util.List<String[]> roomReport =
        (java.util.List<String[]>) request.getAttribute("roomReport");

    if (roomReport != null) {
        for (String[] r : roomReport) {
%>

<tr>
    <td><%= r[0] %></td>
    <td><%= r[1] %></td>
</tr>

<%
        }
    }
%>
</table>