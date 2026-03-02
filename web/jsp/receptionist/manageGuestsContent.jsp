<h2>Manage Guests</h2>

<div class="table-card">

<table>
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Username</th>
    <th>Email</th>
    <th>Phone</th>
    <th>Status</th>
    <th>Action</th>
</tr>

<%
    com.oceanview.pattern.behavioral.ReceptionistIterator iterator =
        (com.oceanview.pattern.behavioral.ReceptionistIterator)
        request.getAttribute("iterator");

    while(iterator != null && iterator.hasNext()) {
        com.oceanview.model.User user = iterator.next();
%>

<tr>
    <td><%= user.getId() %></td>
    <td><%= user.getFullName() %></td>
    <td><%= user.getUsername() %></td>
    <td><%= user.getEmail() %></td>
    <td><%= user.getContact() %></td>
    <td><%= user.getStatus() %></td>

    <td>
        <% if("ACTIVE".equals(user.getStatus())) { %>

        <a class="delete-btn"
           href="${pageContext.request.contextPath}/manageGuests?action=toggle&id=<%= user.getId() %>&status=INACTIVE">
           Deactivate
        </a>

        <% } else { %>

        <a class="edit-btn"
           href="${pageContext.request.contextPath}/manageGuests?action=toggle&id=<%= user.getId() %>&status=ACTIVE">
           Activate
        </a>

        <% } %>
    </td>
</tr>

<%
    }
%>

</table>

</div>