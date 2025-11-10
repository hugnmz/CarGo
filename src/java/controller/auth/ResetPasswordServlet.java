package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import util.MessageUtil;
import util.di.DIContainer;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.CustomersService;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    private CustomersService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomersService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("resetUsername");
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/ForgotPasswordServlet");
            return;
        }
        request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String code = request.getParameter("code");
        String newPassword = request.getParameter("newPassword");

        try {
            boolean success = customerService.resetPasswordWithCode(
                    username != null ? username.trim() : null,
                    code != null ? code.trim() : null,
                    newPassword
            );

            if (success) {
                request.getSession().removeAttribute("resetUsername");
                response.sendRedirect(request.getContextPath() + "/auth/login.jsp?reset=success");
            } else {
                request.setAttribute("error", MessageUtil.getError("error.reset.password.code.expired"));
                setFormDate(request, username, code);
                request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            }
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            setFormDate(request, username, code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.system.error"));
            setFormDate(request, username, code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
        }
    }

    private void setFormDate(HttpServletRequest request, String username, String code) {
        request.setAttribute("username", username);
        request.setAttribute("code", code);
    }
}
