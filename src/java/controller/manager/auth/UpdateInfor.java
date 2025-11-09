/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.auth;

import dto.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.RoleService;
import util.di.DIContainer;
import service.UserService;
import util.MessageUtil;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import util.exception.ValidationException;

/**
 *
 * @author DELL
 */
@WebServlet(name = "UpdateInfor", urlPatterns = {"/updateinfor"})
public class UpdateInfor extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = DIContainer.get(UserService.class);

        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system"), e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Lấy parameter từ server
        String userIdStr = request.getParameter("userId");
        // Xóa check - service sẽ check và throw WebException
        try {
            // Lấy dữ liệu từ form
            int userId = (userIdStr != null && !userIdStr.isEmpty()) ? Integer.parseInt(userIdStr) : 0;
            String fullName = request.getParameter("fullName").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            int locationId = Integer.parseInt(request.getParameter("locationId"));

            // Tạo đối tượng user mới
            UserDTO user = new UserDTO();
            user.setUserId(userId);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setLocationId(locationId);

            //Cập nhật thông tin user
            userService.updateUser(user);

            //Truyền dữ liệu về server
            request.setAttribute("message", MessageUtil.getError("error.user.update.success"));
            request.getRequestDispatcher("profile").forward(request, response);
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("profile").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.manager.update"));
            request.getRequestDispatcher("profile").forward(request, response);
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