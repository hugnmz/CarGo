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

// Servlet hien thi chi tiet xe
@WebServlet(name = "CarDetailServlet", urlPatterns = {"/car-detail"})
public class CarDetailServlet extends HttpServlet {

    // Khoi tao cac service can thiet
    private CarService carService;
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Khoi tao CarService va VehicleService tu DI Container
        try {
            carService = DIContainer.get(CarService.class);
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET: hien thi trang chi tiet xe
    // Lay carId tu parameter, lay thong tin car tu service, lay danh sach vehicle tu service de hien thi
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lay carId tu parameter
            String carIdStr = request.getParameter("carId");

            // Kiem tra carId co hop le khong
            if (carIdStr == null || carIdStr.trim().isEmpty()) {
                // Them cache control headers de tranh cache
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
                
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            // Chuyen doi carId sang Integer
            Integer carId = Integer.parseInt(carIdStr);

            // Lay thong tin xe tu service
            Optional<CarDTO> carDTO = carService.getCarById(carId);
            if (!carDTO.isPresent()) {
                request.setAttribute("error", "Khong tim thay xe nao");
                
                // Them cache control headers de tranh cache
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
                
                request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
                return;
            }

            // Lay danh sach vehicle cua xe
            List<VehicleDTO> vehicles = vehicleService.getVehicleByCarId(carId);

            // Truyen du lieu xuong JSP
            request.setAttribute("car", carDTO.get());
            request.setAttribute("vehicles", vehicles);

            // Them cache control headers de tranh cache
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // Forward den trang car-detail.jsp
            request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
        } catch (Exception e) {
            // Xu ly loi he thong
            e.printStackTrace();
            request.setAttribute("error", "Khong the tai thong tin xe: " + e.getMessage());
            
            // Them cache control headers de tranh cache
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST cung xu ly nhu GET
        doGet(request, response);
    }

}
