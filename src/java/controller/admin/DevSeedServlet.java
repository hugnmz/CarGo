package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import util.JdbcTemplateUtil;
import util.PasswordUtil;
import util.di.annotation.Column;

@WebServlet(name = "DevSeedServlet", urlPatterns = {"/dev/seed-roles-users"})
public class DevSeedServlet extends HttpServlet {

    static class RoleRow { @Column(name = "roleId") public Integer roleId; @Column(name = "roleName") public String roleName; }
    static class UserRow { @Column(name = "userId") public Integer userId; @Column(name = "username") public String username; }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Very simple guard to avoid accidental seeding
        String token = req.getParameter("token");
        if (!"seedme".equals(token)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().println("Forbidden. Provide ?token=seedme to run the dev seeder.");
            return;
        }

        // Ensure roles exist
        ensureRole("ADMIN");
        ensureRole("MANAGER");
        ensureRole("STAFF");

        // Create or ensure users
        int adminId = ensureUser("admin", "System Admin", "0123456789", "admin@example.com", LocalDate.of(1990,1,1), "Admin@123");
        int managerId = ensureUser("manager", "Branch Manager", "0123456790", "manager@example.com", LocalDate.of(1990,1,1), "Manager@123");
        int staffId = ensureUser("staff", "Rental Staff", "0123456791", "staff@example.com", LocalDate.of(1990,1,1), "Staff@123");

        // Assign roles
        int adminRoleId = getRoleId("ADMIN");
        int managerRoleId = getRoleId("MANAGER");
        int staffRoleId = getRoleId("STAFF");

        ensureUserRole(adminId, adminRoleId);
        ensureUserRole(managerId, managerRoleId);
        ensureUserRole(staffId, staffRoleId);

        resp.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("<h3>Seeded roles and users successfully</h3>");
            out.println("<ul>");
            out.println("<li>ADMIN: username=<b>admin</b>, password=<code>Admin@123</code></li>");
            out.println("<li>MANAGER: username=<b>manager</b>, password=<code>Manager@123</code></li>");
            out.println("<li>STAFF: username=<b>staff</b>, password=<code>Staff@123</code></li>");
            out.println("</ul>");
            out.println("<p>Đăng nhập nội bộ tại <code>/LoginAdmin</code>.</p>");
            out.println("</body></html>");
        }
    }

    private void ensureRole(String roleName) {
        Integer id = getRoleId(roleName);
        if (id == null) {
            JdbcTemplateUtil.insertAndReturnKey("INSERT INTO Roles(roleName) VALUES(?)", roleName);
        }
    }

    private Integer getRoleId(String roleName) {
        RoleRow row = JdbcTemplateUtil.queryOne("SELECT roleId, roleName FROM Roles WHERE roleName=?", RoleRow.class, roleName);
        return row == null ? null : row.roleId;
    }

    private int ensureUser(String username, String fullName, String phone, String email, LocalDate dob, String rawPassword) {
        UserRow existing = JdbcTemplateUtil.queryOne("SELECT userId, username FROM Users WHERE username=?", UserRow.class, username);
        if (existing != null && existing.userId != null) {
            return existing.userId;
        }

        byte[] salt = PasswordUtil.generateSalt();
        byte[][] pair = PasswordUtil.hashPassword(rawPassword, salt);
        byte[] hash = pair[0];

        String sql = "INSERT INTO Users (username, fullName, phone, email, dateOfBirth, locationId, password_hash, password_salt) VALUES (?,?,?,?,?,?,?,?)";
        int userId = JdbcTemplateUtil.insertAndReturnKey(sql,
                username,
                fullName,
                phone,
                email,
                Date.valueOf(dob),
                null,
                hash,
                salt
        );
        return userId;
    }

    private void ensureUserRole(int userId, int roleId) {
        Integer count = JdbcTemplateUtil.count("SELECT COUNT(*) FROM UserRoles WHERE userId=? AND roleId=?", userId, roleId);
        if (count == null || count == 0) {
            JdbcTemplateUtil.insertAndReturnKey("INSERT INTO UserRoles(userId, roleId) VALUES (?,?)", userId, roleId);
        }
    }
}
