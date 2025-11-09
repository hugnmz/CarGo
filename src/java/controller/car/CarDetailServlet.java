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
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;


@WebServlet(name = "CarDetailServlet", urlPatterns = {"/car-detail"})
public class CarDetailServlet extends HttpServlet {

    // service xu ly thong tin xe
    private CarService carService;
    // service xu ly thong tin phuong tien
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao car service tu di container
            carService = DIContainer.get(CarService.class);
            // khoi tao vehicle service tu di container
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // lay car id tu tham so request
            String carIdStr = request.getParameter("carId");

            // kiem tra car id co hop le khong
            if (carIdStr == null || carIdStr.trim().isEmpty()) {               
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            // chuyen doi car id tu string sang integer
            Integer carId = Integer.valueOf(carIdStr);

            // lay thong tin xe tu database bang car id
            Optional<CarDTO> carDTO = carService.getCarById(carId);
            if (!carDTO.isPresent()) {
                // neu khong tim thay xe thi dat thong bao loi
                request.setAttribute("error", MessageUtil.getError("error.car.not.found"));
                request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
                return;
            }

            // lay danh sach phuong tien cua xe tu database
            List<VehicleDTO> vehicles = vehicleService.getVehicleByCarId(carId);

            // dat thong tin xe vao request de truyen sang jsp
            request.setAttribute("car", carDTO.get());
            // dat danh sach phuong tien vao request de truyen sang jsp
            request.setAttribute("vehicles", vehicles);

            // chuyen huong den trang chi tiet xe
            request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.car.detail"));
            request.getRequestDispatcher("/customer/car-detail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // method post cung xu ly nhu get
        doGet(request, response);
    }

}
