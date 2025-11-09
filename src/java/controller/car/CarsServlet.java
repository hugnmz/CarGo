package controller.car;

import dto.CarDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import service.CarService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;


@WebServlet(name = "CarsServlet", urlPatterns = {"/cars"})
public class CarsServlet extends HttpServlet {

    // service xu ly thong tin xe
    private CarService carService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao car service tu di container
            carService = DIContainer.get(CarService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // lay danh sach tat ca xe tu database
            List<CarDTO> allCars = carService.getAllCars();
            // dat danh sach xe vao request de truyen sang jsp
            request.setAttribute("allCars", allCars);
            // chuyen huong den trang danh sach xe
            request.getRequestDispatcher("/customer/cars.jsp").forward(request, response);
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/customer/cars.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.cars.list"));
            request.getRequestDispatcher("/customer/cars.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // method post cung xu ly nhu get
        doGet(request, response);
    }
}
