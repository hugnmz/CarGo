
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import dto.*;
import jakarta.servlet.http.HttpSession;
import service.*;
import util.di.DIContainer;

/**
 *
 * @author DELL
 */
@WebServlet(name = "HomeAdmin", urlPatterns = {"/HomeAdmin"})
public class HomeAdmin extends HttpServlet {

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

        HttpSession session = request.getSession(false);

        //Kiểm tra quyền truy cập
        if (session == null || !"ADMIN".equals(session.getAttribute("roleName"))) {
            request.setAttribute("errors", "Bạn không có quyền truy cập trang này!");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);
            return;
        }

        // Lấy danh sách người dùng
        List<UserDTO> list = userService.getAllUser();
        // Lấy danh sách vai trò
        List<RoleDTO> roleList = RoleService.getAllRole();
        // Lấy danh sách địa điểm (thành phố)
        List<LocationDTO> locationList = userService.getAllLocation();

        request.setAttribute("users", list);
        request.setAttribute("roles", roleList);
        request.setAttribute("locations", locationList);
        request.getRequestDispatcher("admin/adminhome.jsp").forward(request, response);

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
