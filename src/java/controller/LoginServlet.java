
package controller;

import dao.CustomersDAO;
import dao.impl.CustomersDAOImpl;
import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mapper.CustomerMapper;
import model.Customers;
import service.*;
import service.impl.CustomerServiceImpl;
import util.di.DIContainer;



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // Giả lập cơ sở dữ liệu bằng Map
    private static Map<String, String> accounts = new HashMap<>();
    private CustomerService cuss;

    @Override
    public void init() throws ServletException {
        // Lấy bean từ DIContainer thay vì new thủ công
        cuss = DIContainer.get(CustomerService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<CustomerDTO> cu = cuss.loginCustomer(username, password);
        

        if (cu.isPresent()) {
            // Lưu thông tin user vào session
            HttpSession session = request.getSession();
            session.setAttribute("customer", cu.get());
            // Chuyển hướng sang home.jsp
            response.sendRedirect("register.jsp");

        } else {
            // Sai thông tin đăng nhập
            request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
