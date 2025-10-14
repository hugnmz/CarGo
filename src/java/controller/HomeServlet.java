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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private CarService carService;

    @Override
    public void init() throws ServletException {
        super.init();
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
            List<CarDTO> allCars = carService.getAllCars();
            
            request.setAttribute("allCars", allCars);
            
            request.getRequestDispatcher("home.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách xe: " + e.getMessage());
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

