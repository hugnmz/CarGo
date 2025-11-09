/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dto.CarDTO;
import dto.CategoryDTO;
import dto.LocationDTO;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import service.CarService;
import util.MessageUtil;
import util.di.DIContainer;
import util.exception.*;
import util.di.annotation.Autowired;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SearchCarServlet", urlPatterns = {"/searchcar"})
public class SearchCarServlet extends HttpServlet {

    @Autowired
    private CarService carService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
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

            //lay danh sach dia diem
            List<LocationDTO> allLocations = carService.getAllLocation();

            //lay danh sach loai xe
            List<CategoryDTO> allCategories = carService.getAllCategories();

            // truyen danh sach xe xuong jsp
            request.setAttribute("allLocations", allLocations);
            request.setAttribute("allCategories", allCategories);

            // forward den trang home.jsp
            request.getRequestDispatcher("/customer/search-car.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/customer/search-car.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.search.car"));
            request.getRequestDispatcher("/customer/search-car.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //lay danh sach dia diem
            List<LocationDTO> allLocations = carService.getAllLocation();

            //lay danh sach loai xe
            List<CategoryDTO> allCategories = carService.getAllCategories();

            // truyen danh sach xe xuong jsp
            request.setAttribute("allLocations", allLocations);
            request.setAttribute("allCategories", allCategories);

            // Lấy locationId
            String locationParam = request.getParameter("location");
            Integer locationId = (locationParam != null && !locationParam.isEmpty())
                    ? Integer.parseInt(locationParam)
                    : null;

            // Lấy tên xe
            String carName = request.getParameter("carName");
            if (carName != null) {
                carName = carName.trim();
                if (carName.isEmpty()) {
                    carName = null;
                }
            }

            // Lấy categoryId
            String categoryParam = request.getParameter("category");
            Integer categoryId = (categoryParam != null && !categoryParam.isEmpty())
                    ? Integer.parseInt(categoryParam)
                    : null;

            // Lấy giá xe cao nhất
            String priceParam = request.getParameter("price");
            Double price = (priceParam != null && !priceParam.isEmpty())
                    ? Double.parseDouble(priceParam)
                    : null;

            // Gọi service
            List<CarDTO> searchCars = new ArrayList<>();
            if (locationId != null || carName != null || categoryId != null || price != null) {
                searchCars = carService.searchCars(locationId, carName, categoryId, price);
            }

            // Truyền sang JSP
            request.setAttribute("searchCars", searchCars);
            request.getRequestDispatcher("/customer/search-car.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/customer/search-car.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.search.car"));
            request.getRequestDispatcher("/customer/search-car.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}