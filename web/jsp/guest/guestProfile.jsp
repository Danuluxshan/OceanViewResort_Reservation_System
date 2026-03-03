<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.User" %>

<%
User user = (User) session.getAttribute("user");
String successMsg = (String) request.getAttribute("success");
String errorMsg = (String) request.getAttribute("error");
%>

<h2>My Profile</h2>

<!-- ================= PROFILE SECTION ================= -->

<!-- ================= TOAST ================= -->

<% if (successMsg != null) { %>
<div id="toast" class="toast show">
    <%= successMsg %>
</div>
<% } %>

    <% if (errorMsg != null) { %>
        <p class="error"><%= errorMsg %></p>
    <% } %>

    <% if (successMsg != null) { %>
        <p class="success-text"><%= successMsg %></p>
    <% } %>

<div class="profile-card">

    <!-- VIEW MODE -->
    <div id="viewMode">

        <div class="profile-row">
            <label>Username</label>
            <span><%= user.getUsername() %></span>
        </div>

        <div class="profile-row">
            <label>Full Name</label>
            <span><%= user.getFullName() %></span>
        </div>

        <div class="profile-row">
            <label>Email</label>
            <span><%= user.getEmail() %></span>
        </div>

        <div class="profile-row">
            <label>Contact</label>
            <span><%= user.getContact() %></span>
        </div>

        <div class="profile-row">
            <label>Address</label>
            <span><%= user.getAddress() %></span>
        </div>

        <button class="primary-btn" onclick="enableEdit()">
            Edit Profile
        </button>
    </div>

    <!-- EDIT MODE -->
    <div id="editMode" style="display:none;">

        <form method="post"
              action="${pageContext.request.contextPath}/guest">

            <input type="hidden"
                   name="profileAction"
                   value="updateProfile">

            <label>Full Name</label>
            <input type="text"
                   name="fullName"
                   value="<%= user.getFullName() %>" required>

            <label>Email</label>
            <input type="email"
                   name="email"
                   value="<%= user.getEmail() %>" required>

            <label>Contact</label>
            <input type="text"
                   name="contact"
                   value="<%= user.getContact() %>">

            <label>Address</label>
            <textarea name="address"><%= user.getAddress() %></textarea>

            <div class="button-group">
                <button class="success-btn">
                    Save Changes
                </button>

                <button type="button"
                        class="secondary-btn"
                        onclick="cancelEdit()">
                    Cancel
                </button>
            </div>

        </form>
    </div>
</div>


<!-- ================= CHANGE PASSWORD ================= -->

<div class="profile-card">
    <h3>Change Password</h3>

    <form method="post"
          action="${pageContext.request.contextPath}/guest">

        <input type="hidden"
               name="profileAction"
               value="changePassword">

        <label>Current Password</label>
        <input type="password"
               name="currentPassword"
               required>

        <label>New Password</label>
        <input type="password"
               name="newPassword"
               required>

        <label>Confirm New Password</label>
        <input type="password"
               name="confirmPassword"
               required>

        <button class="primary-btn">
            Change Password
        </button>
    </form>
</div>


<!-- ================= DEACTIVATE ACCOUNT ================= -->

<div class="profile-card">
    <h3>Deactivate Account</h3>

    <p class="warning-text">
        This will permanently deactivate your account.
    </p>

    <form method="post"
          action="${pageContext.request.contextPath}/guest"
          onsubmit="return confirmDeactivate();">

        <input type="hidden"
               name="profileAction"
               value="deactivate">

        <button class="danger-btn">
            Deactivate Account
        </button>
    </form>
</div>





<script>

function enableEdit() {
    document.getElementById("viewMode").style.display = "none";
    document.getElementById("editMode").style.display = "block";
}

function cancelEdit() {
    document.getElementById("editMode").style.display = "none";
    document.getElementById("viewMode").style.display = "block";
}

function confirmDeactivate() {
    return confirm("Are you sure you want to deactivate your account?");
}

// Toast auto hide
setTimeout(function() {
    let toast = document.getElementById("toast");
    if (toast) {
        toast.classList.remove("show");
    }
}, 3000);

</script>