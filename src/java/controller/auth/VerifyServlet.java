package controller.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import util.di.DIContainer;
import util.MessageUtil;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import service.CustomersService;


@WebServlet("/VerifyServlet")
public class VerifyServlet extends HttpServlet {
    
    // service xu ly thong tin khach hang
    private CustomersService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomersService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // chuyen huong cac request get den trang xac thuc
        req.getRequestDispatcher("/auth/verify.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // lay session hien tai, neu khong co thi null
            HttpSession session = req.getSession(false);
            // lay username dang cho xac thuc tu session
            String username = (session != null) ? (String) session.getAttribute("pendingUser") : null;
            // lay ma xac thuc tu form
            String inputCode = req.getParameter("code");
            if (inputCode != null) {
                inputCode = inputCode.trim();
            }
            // lay email dang cho xac thuc tu session
            String email = (session != null) ? (String) session.getAttribute("pendingEmail") : null;
            
            // kiem tra username va ma xac thuc co hop le khong
            if (username != null && inputCode != null) {
                
                // goi service de kiem tra ma xac thuc
                boolean isValid = customerService.verifyAccount(username, inputCode);            
                if (isValid) {
                    // neu xac thuc thanh cong thi xoa session pending
                    if (session != null) {
                        session.removeAttribute("pendingUser");
                        session.removeAttribute("pendingEmail");
                    }
                    
                    // dat thong bao thanh cong va chuyen den trang dang nhap
                    req.setAttribute("successMessage", MessageUtil.getMessage("verification.success.login"));
                    req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
                    return;
                } else {
                    // neu ma xac thuc sai thi dat thong bao loi
                    req.setAttribute("errorMessage", MessageUtil.getError("error.verification.code.wrong"));
                }
            } else {
                // neu thieu thong tin thi dat thong bao loi
                req.setAttribute("errorMessage", MessageUtil.getError("error.verification.invalid.info"));
            }
            
        } catch (ValidationException | BusinessException | DataAccessException e) {
            req.setAttribute("errorMessage", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            req.setAttribute("errorMessage", MessageUtil.getError("error.system.verify"));
        }
        
        // hien thi lai trang xac thuc voi thong bao loi
        req.getRequestDispatcher("/auth/verify.jsp").forward(req, resp);
    }
    
}
