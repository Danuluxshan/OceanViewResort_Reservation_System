<h2>Manage Rooms</h2>

<div class="form-card">
    <form method="post"
          action="${pageContext.request.contextPath}/manageRooms">

        <input type="hidden" name="id"
               value="${editRoom.id}">

        <input type="text" name="roomNumber"
               placeholder="Room Number"
               value="${editRoom.roomNumber}" required>

        <input type="text" name="roomType"
               placeholder="Room Type"
               value="${editRoom.roomType}" required>

        <input type="number" name="capacity"
               placeholder="Capacity"
               value="${editRoom.capacity}" required>

        <input type="number" step="0.01"
               name="price"
               placeholder="Price per Night"
               value="${editRoom.pricePerNight}" required>

        <input type="text" name="description"
               placeholder="Description"
               value="${editRoom.description}">

        <button type="submit">
            ${editRoom != null ? "Update Room" : "Add Room"}
        </button>
    </form>
</div>

<hr>

<div class="table-card">
    <table>
        <tr>
            <th>ID</th>
            <th>Room No</th>
            <th>Type</th>
            <th>Capacity</th>
            <th>Price</th>
            <th>Action</th>
        </tr>

        <%
            java.util.List<com.oceanview.model.Room> rooms
                    = (java.util.List<com.oceanview.model.Room>) request.getAttribute("rooms");

            for (com.oceanview.model.Room room : rooms) {
        %>

        <tr>
            <td><%= room.getId()%></td>
            <td><%= room.getRoomNumber()%></td>
            <td><%= room.getRoomType()%></td>
            <td><%= room.getCapacity()%></td>
            <td><%= room.getPricePerNight()%></td>
            <td>
                <a class="edit-btn"
                   href="${pageContext.request.contextPath}/manageRooms?action=edit&id=<%= room.getId()%>">
                    Edit
                </a>

                <a class="delete-btn"
                   onclick="return confirm('Are you sure?')"
                   href="${pageContext.request.contextPath}/manageRooms?action=delete&id=<%= room.getId()%>">
                    Delete
                </a>
            </td>
        </tr>

        <%
            }
        %>

    </table>
</div>