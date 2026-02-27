<h2>Manage Receptionists</h2>

<div class="form-card">

<form method="post"
      action="${pageContext.request.contextPath}/manageReceptionists">

    <input type="hidden" name="id"
           value="${editUser.id}">

    <input type="text" name="fullName"
           placeholder="Full Name"
           value="${editUser.fullName}" required>

    <input type="text" name="username"
           placeholder="Username"
           value="${editUser.username}"
           ${editUser != null ? "readonly" : ""} required>

    <input type="email" name="email"
           placeholder="Email"
           value="${editUser.email}" required>

    <input type="text" name="contact"
           placeholder="Phone"
           value="${editUser.contact}" required>

    <input type="text" name="address"
           placeholder="Address"
           value="${editUser.address}" required>

    <input type="password" name="password"
           placeholder="Password (Re-enter to update)" required>

    <button type="submit">
        ${editUser != null ? "Update Receptionist" : "Add Receptionist"}
    </button>
</form>

</div>

<hr>

<div class="table-card">
<table>
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Username</th>
    <th>Email</th>
    <th>Phone</th>
    <th>Actions</th>
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

    <td>
        <a class="edit-btn"
           href="${pageContext.request.contextPath}/manageReceptionists?action=edit&id=<%= user.getId() %>">
           Edit
        </a>

        <a class="delete-btn"
           onclick="return confirm('Are you sure?')"
           href="${pageContext.request.contextPath}/manageReceptionists?action=delete&id=<%= user.getId() %>">
           Delete
        </a>
    </td>
</tr>

<%
    }
%>

</table>
</div>