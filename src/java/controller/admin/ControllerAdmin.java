
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dto.LocationDTO;
import dto.RoleDTO;
import dto.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Users;
import service.RoleService;
import util.di.DIContainer;
import util.EmailUtil;
import util.MessageUtil;
import util.exception.ApplicationException;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.UserService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ControllerAdmin", urlPatterns = {"/ControllerAdmin"})
public class ControllerAdmin extends HttpServlet {

    private UserService userService;
    private RoleService roleService;

    @Override
    public void init() throws ServletException {
        try {
            userService = DIContainer.get(UserService.class);
            roleService = DIContainer.get(RoleService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system"), e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        //Kiểm tra quyền truy cập
        if (session == null || !"ADMIN".equals(session.getAttribute("roleName"))) {
            request.setAttribute("errors", "Bạn không có quyền truy cập trang này!");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);
            return;
        }

        //Lấy action từ server
        String action = request.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            //Nếu action là create thì thêm user mới
            addUser(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            //Nếu action là delete thì xóa user
            deleteUser(request, response);
        } else if ("edit".equalsIgnoreCase(action)) {
            //Nếu edit thì truyền dữ liệu qua trang edit
            showEditForm(request, response);
        } else if ("update".equalsIgnoreCase(action)) {
            //Thực hiện cập nhật bên trang service
            updateUser(request, response);
        }
    }

    // Thêm user
    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy parameter
            String username = request.getParameter("username");
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String roleIdStr = request.getParameter("roleid");
            String locationIdStr = request.getParameter("locationid");

            // Tạo DTO
            UserDTO user = new UserDTO();
            user.setUsername(username);
            user.setFullName(fullname);
            user.setEmail(email);
            user.setPhone(phone);

            if (roleIdStr != null && !roleIdStr.isEmpty()) {
                user.setRoleId(Integer.parseInt(roleIdStr));
            }
            if (locationIdStr != null && !locationIdStr.isEmpty()) {
                user.setLocationId(Integer.parseInt(locationIdStr));
            }

            // Gọi service
            userService.addUser(user, password);

            // Gửi mail
            try {
                String roleName = user.getFullName();
                if (user.getRoleId() == 3) {
                    roleName = "Admin";
                } else if (user.getRoleId() == 1) {
                    roleName = "Staff";
                } else if (user.getRoleId() == 2) {
                    roleName = "Manager";
                }

                if (email != null && !email.isEmpty()) {
                    EmailUtil.sendCredentials(email, fullname, username, password, roleName);
                }
            } catch (Exception e) {
                request.setAttribute("error", MessageUtil.getError("error.system.email.send"));
            }

            request.setAttribute("message", MessageUtil.getError("error.user.add.success"));
            request.getRequestDispatcher("HomeAdmin").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("HomeAdmin").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.admin"));
            request.getRequestDispatcher("HomeAdmin").forward(request, response);
        }
    }

    // Cập nhật user
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            //Lấy parameter từ server
            String userIdStr = request.getParameter("userId");
            //Kiểm tra id của user null hay không
            if (userIdStr == null || userIdStr.isEmpty()) {
                response.sendRedirect("HomeAdmin");
                return;
            }

            //Khởi tạo dto để truyền tham số
            UserDTO user = new UserDTO();
            user.setUserId(Integer.parseInt(userIdStr));
            user.setUsername(request.getParameter("username"));
            user.setFullName(request.getParameter("fullname"));
            user.setEmail(request.getParameter("email"));
            user.setPhone(request.getParameter("phone"));
            String roleIdStr = request.getParameter("roleid");
            if (roleIdStr != null && !roleIdStr.isEmpty()) {
                user.setRoleId(Integer.parseInt(roleIdStr));
            }
            String locationIdStr = request.getParameter("locationid");
            if (locationIdStr != null && !locationIdStr.isEmpty()) {
                user.setLocationId(Integer.parseInt(locationIdStr));
            }

            //Cập nhật thông tin user
            userService.updateUser(user);

            //Truyền dữ liệu về server
            request.setAttribute("message", MessageUtil.getError("error.user.update.success"));
            request.getRequestDispatcher("HomeAdmin").forward(request, response);
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("HomeAdmin").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.admin"));
            request.getRequestDispatcher("HomeAdmin").forward(request, response);
        }
    }

    // Xóa user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Lấy parameter từ server
            String userIdStr = request.getParameter("userId");
            //Xóa thông tin user
            if (userIdStr != null && !userIdStr.isEmpty()) {
                Integer userId = Integer.parseInt(userIdStr);
                userService.deleteUser(userId);
                request.setAttribute("message", MessageUtil.getError("error.user.delete.success"));
            }
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.admin"));
        }
        //Gửi dữ liệu về home admin
        request.getRequestDispatcher("HomeAdmin").forward(request, response);
    }

    // Hiển thị form edit
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Lấy parameter từ server
        String userIdStr = request.getParameter("userId");
        //Truyền dữ liệu sang trang edit 
        if (userIdStr != null && !userIdStr.isEmpty()) {
            Integer userId = Integer.parseInt(userIdStr);
            // Tạo phương thức getUserById ở Service
            UserDTO user = userService.getUserById(userId);
            // Lấy danh sách vai trò
            List<RoleDTO> roleList = roleService.getAllRole();
            // Lấy danh sách địa điểm (thành phố)
            List<LocationDTO> locationList = userService.getAllLocation();

            // Truyền dữ liệu lên server
            request.setAttribute("roles", roleList);
            request.setAttribute("locations", locationList);
            request.setAttribute("editUser", user);
            request.getRequestDispatcher("admin/edituser.jsp").forward(request, response);
        } else {
            response.sendRedirect("HomeAdmin");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
