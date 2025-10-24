package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CarService;
import util.di.DIContainer;
import java.io.IOException;
import java.util.List;
import dto.CarDTO;

// servlet hien thi trang chu
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private CarService carService;

    @Override
    public void init() throws ServletException {
        super.init();
        // khoi tao carservice tu di container
        try {
            carService = DIContainer.get(CarService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CarService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // lay danh sach tat ca xe
            List<CarDTO> allCars = carService.getAllCars();
            
            // truyen danh sach xe xuong jsp
            request.setAttribute("allCars", allCars);
            
            // forward den trang home.jsp
            request.getRequestDispatcher("/customer/home.jsp").forward(request, response);
            
        } catch (Exception e) {
            // xu ly loi he thong
            e.printStackTrace();
            request.setAttribute("error", "khong the tai danh sach xe: " + e.getMessage());
            request.getRequestDispatcher("/customer/home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // post cung xu ly nhu get
        doGet(request, response);
    }
}

