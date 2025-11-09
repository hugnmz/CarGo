package controller.manager.auth;

import dto.UserDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import util.exception.ValidationException;

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
        String userIdStr = request.getParameter("userId");
        try {
            int userId = (userIdStr != null && !userIdStr.isEmpty()) ? Integer.parseInt(userIdStr) : 0;
            String fullName = request.getParameter("fullName").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            int locationId = Integer.parseInt(request.getParameter("locationId"));

            UserDTO user = new UserDTO();
            user.setUserId(userId);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setLocationId(locationId);

            userService.updateUser(user);

            // Nạp lại dữ liệu và forward về đúng trang
            UserDTO refreshed = userService.getUserById(userId);
            request.setAttribute("user", refreshed);
            request.setAttribute("locations", userService.getAllLocation());
            request.setAttribute("message", MessageUtil.getError("error.user.update.success"));
            request.getRequestDispatcher("/manager/staffmanage.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            if (userIdStr != null && !userIdStr.isEmpty()) {
                try {
                    int uid = Integer.parseInt(userIdStr);
                    request.setAttribute("user", userService.getUserById(uid));
                } catch (Exception ignore) {}
            }
            request.setAttribute("locations", userService.getAllLocation());
            request.getRequestDispatcher("/manager/staffmanage.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.manager.update"));
            if (userIdStr != null && !userIdStr.isEmpty()) {
                try {
                    int uid = Integer.parseInt(userIdStr);
                    request.setAttribute("user", userService.getUserById(uid));
                } catch (Exception ignore) {}
            }
            request.setAttribute("locations", userService.getAllLocation());
            request.getRequestDispatcher("/manager/staffmanage.jsp").forward(request, response);
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