
package controller.manager.car;

import dto.CarDTO;
import dto.CategoryDTO;
import dto.FuelDTO;
import dto.SeatingDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import service.CarService;
import service.VehicleService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

/**
 * HomeManageCar - Servlet quản lý danh sách xe
 * 
 * CHỨC NĂNG:
 * - Lấy danh sách xe từ CarService
 * - Lấy danh sách Category, Fuel, Seating để hiển thị bộ lọc
 * - Chuyển dữ liệu sang trang JSP: manage_cars.jsp
 */
@WebServlet(name = "HomeManageCar", urlPatterns = {"/managecar"})
public class HomeManageCar extends HttpServlet {

    private CarService carService;
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        try {
            carService = DIContainer.get(CarService.class);
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        
        //Kiểm tra quyền
        HttpSession session = request.getSession(false);
        if (session == null || !"MANAGER".equals(session.getAttribute("roleName"))) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            // Lấy danh sách xe
            List<CarDTO> allCars = carService.getAllCars();

            // Lấy danh sách danh mục phụ (nhiên liệu, loại xe, chỗ ngồi)
            List<CategoryDTO> allCategories = carService.getAllCategories();
            List<FuelDTO> allFuels = carService.getAllFuels();
            List<SeatingDTO> allSeatings = carService.getAllSeatings();

            // Đặt dữ liệu vào request scope
            request.setAttribute("allCars", allCars);
            request.setAttribute("allCategories", allCategories);
            request.setAttribute("allFuels", allFuels);
            request.setAttribute("allSeatings", allSeatings);

            //Chuyển tiếp sang JSP hiển thị
            request.getRequestDispatcher("manager/manage_cars.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("manager/manage_cars.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.car.management"));
            request.getRequestDispatcher("manager/manage_cars.jsp").forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Servlet quản lý danh sách xe - hiển thị danh sách và bộ lọc";
    }
}
