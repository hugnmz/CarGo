
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



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // Giả lập cơ sở dữ liệu bằng Map
    private static Map<String, String> accounts = new HashMap<>();
    private CustomerService cuss;

    @Override
    public void init() throws ServletException {
        // Tạo DAO và Mapper
        CustomersDAO customersDAO = new CustomersDAOImpl(); 
        CustomerMapper customerMapper = new CustomerMapper();

        // Khởi tạo Service Impl
        cuss = new CustomerServiceImpl(customersDAO, customerMapper);
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
            session.setAttribute("username", username);
            // Chuyển hướng sang home.jsp
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } else {
            // Sai thông tin đăng nhập
            request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
