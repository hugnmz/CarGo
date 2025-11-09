package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CarService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import java.io.IOException;
import java.util.List;
import dto.CarDTO;
import dto.CategoryDTO;
import dto.LocationDTO;

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
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // lay danh sach tat ca xe
            List<CarDTO> allCars = carService.getAllCars();

            //lay danh sach dia diem
            List<LocationDTO> allLocations = carService.getAllLocation();

            //lay danh sach loai xe
            List<CategoryDTO> allCategories = carService.getAllCategories();

            // truyen danh sach xe xuong jsp
            request.setAttribute("allCars", allCars);
            request.setAttribute("allLocations", allLocations);
            request.setAttribute("allCategories", allCategories);

            // forward den trang home.jsp
            request.getRequestDispatcher("/customer/home.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/customer/home.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.home"));
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