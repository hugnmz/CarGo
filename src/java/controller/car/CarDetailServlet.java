/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.car;

import dto.CarDTO;
import dto.VehicleDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import service.CarService;
import service.VehicleService;
import util.di.DIContainer;

/**
 *
 * @author admin
 */
@WebServlet(name = "CarDetailServlet", urlPatterns = {"/car-detail"})
public class CarDetailServlet extends HttpServlet {

    // khoi tao dependency
    private CarService carService;
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        try {
            carService = DIContainer.get(CarService.class);
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get: hien thi trang xe chi tiet
    /*
    lay carId tuw parameter r lay thong tin car tu service, lay danh sach
     vehicle tu service de hien thi
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String carIdStr = request.getParameter("carId");

            if (carIdStr == null || carIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            Integer carId = Integer.parseInt(carIdStr);

            Optional<CarDTO> carDTO = carService.getCarById(carId);
            if (!carDTO.isPresent()) {
                request.setAttribute("error", "ko thay xe nao");
                request.getRequestDispatcher("car-detail.jsp").forward(request, response);
                return;
            }

            List<VehicleDTO> vehicles = vehicleService.getVehicleByCarId(carId);

            request.setAttribute("car", carDTO.get());
            request.setAttribute("vehicles", vehicles);

            request.getRequestDispatcher("car-detail.jsp").forward(request, response);
        } catch (Exception e) {

            e.printStackTrace();
            request.setAttribute("error", "Không thể tải thông tin xe: " + e.getMessage());
            request.getRequestDispatcher("car-detail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
