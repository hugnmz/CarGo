/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.auth;

import dto.LocationDTO;
import dto.UserDTO;
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
import service.UserService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "HomeProfile", urlPatterns = {"/profile"})
public class HomeProfile extends HttpServlet {

    private UserService userService;
    private RoleService RoleService;

    @Override
    public void init() throws ServletException {
        try {
            userService = DIContainer.get(UserService.class);
            RoleService = DIContainer.get(RoleService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Kiểm tra quyền
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null
                || !"MANAGER".equals(session.getAttribute("roleName"))) {
            response.sendRedirect("LoginServlet");
            return;
        }
        //Lấy ID người dùng từ session
        Integer userId = (Integer) session.getAttribute("userId");

        //Lấy thông tin người dùng từ database qua service
        UserDTO user = userService.getUserById(userId);

        //Lấy danh sách địa điểm (thành phố)
        List<LocationDTO> locationList = userService.getAllLocation();

        if (user == null) {
            request.setAttribute("errorMessage", "Không tìm thấy thông tin người dùng.");
            return;
        }

        // 🔹 Gửi dữ liệu user sang JSP
        request.setAttribute("user", user);
        request.setAttribute("locations", locationList);
        request.getRequestDispatcher("/manager/user_profile.jsp").forward(request, response);
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
