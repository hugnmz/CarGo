/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.car;

import dao.CarPricesDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import dto.CarDTO;
import dto.CategoryDTO;
import dto.FuelDTO;
import dto.LocationDTO;
import dto.SeatingDTO;
import dto.VehicleDTO;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ControllerInfoCar", urlPatterns = {"/controllerinformationcar"})
public class ControllerInfoCar extends HttpServlet {

    private CarService carService;
    private CarPricesDAO carPricesDAO;

    @Override
    public void init() throws ServletException {
        try {
            carService = DIContainer.get(CarService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Thêm xe mới
    private void addCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            CarDTO carDTO = new CarDTO();
            //Set giá trị cho DTO
            carDTO.setName(request.getParameter("name"));
            carDTO.setYear(Integer.parseInt(request.getParameter("year")));
            carDTO.setDescription(request.getParameter("description"));

            double price = Double.parseDouble(request.getParameter("price"));
            double deposit = Double.parseDouble(request.getParameter("deposit"));

            carDTO.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            carDTO.setFuelId(Integer.parseInt(request.getParameter("fuelId")));
            carDTO.setSeatingId(Integer.parseInt(request.getParameter("seatingId")));
            carDTO.setImage(request.getParameter("image"));

            //Thêm xe mới
            int carId = carService.addCarAndGetId(carDTO);

            if (carId > 0) {
                //Gọi service thêm giá xe
                boolean priceAdded = carService.addPriceForCar(carId, price, deposit);

                if (priceAdded) {
                    request.setAttribute("message", "Thêm xe và giá thành công!");
                } else {
                    request.setAttribute("error", "Thêm xe thành công nhưng thêm giá thất bại!");
                }
            } else {
                request.setAttribute("error", "Không lấy được ID xe sau khi thêm!");
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.car.management"));
        }

        request.getRequestDispatcher("managecar").forward(request, response);
    }

    //Xóa một xe
    private void deleteCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Lấy parameter từ server
        String idStr = request.getParameter("carId");
        //Xóa thông tin user
        if (idStr != null && !idStr.isEmpty()) {
            Integer id = Integer.parseInt(idStr);
            boolean success = carService.deleteCar(id);
            if (success) {
                request.setAttribute("message", "Xóa car thành công!");
            } else {
                request.setAttribute("error", "Xóa car thất bại!");
            }
        }
        request.getRequestDispatcher("managecar").forward(request, response);
    }

    
    //Sửa thông tin xe
    private void updateCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            CarDTO carDTO = new CarDTO();

            // Lấy carId và các số nguyên
            String carIdStr = request.getParameter("carId");
            if (carIdStr != null && !carIdStr.isEmpty()) {
                carDTO.setCarId(Integer.parseInt(carIdStr));
            }

            String yearStr = request.getParameter("year");
            if (yearStr != null && !yearStr.isEmpty()) {
                carDTO.setYear(Integer.parseInt(yearStr));
            }

            String categoryIdStr = request.getParameter("categoryId");
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                carDTO.setCategoryId(Integer.parseInt(categoryIdStr));
            }

            String fuelIdStr = request.getParameter("fuelId");
            if (fuelIdStr != null && !fuelIdStr.isEmpty()) {
                carDTO.setFuelId(Integer.parseInt(fuelIdStr));
            }

            String seatingIdStr = request.getParameter("seatingId");
            if (seatingIdStr != null && !seatingIdStr.isEmpty()) {
                carDTO.setSeatingId(Integer.parseInt(seatingIdStr));
            }

            // Lấy các String và tránh null
            String name = request.getParameter("name");
            carDTO.setName(name != null ? name.trim() : "");

            String description = request.getParameter("description");
            carDTO.setDescription(description != null ? description.trim() : "");

            String image = request.getParameter("image");
            carDTO.setImage(image != null ? image.trim() : "");

            // Lấy các số thực, tránh null hoặc rỗng
            String priceStr = request.getParameter("price");
            carDTO.setDailyPrice(priceStr != null && !priceStr.isEmpty()
                    ? Double.parseDouble(priceStr) : 0);

            String depositStr = request.getParameter("deposit");
            carDTO.setDepositAmount(depositStr != null && !depositStr.isEmpty()
                    ? Double.parseDouble(depositStr) : 0);

            boolean success = carService.updateCar(carDTO);
            if (success) {
                request.setAttribute("message", "Cập nhật xe thành công!");
            } else {
                request.setAttribute("error", "Cập nhật xe thất bại!");
            }
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.car.management"));
        }

        request.getRequestDispatcher("managecar").forward(request, response);
    }

    // Hiển thị form edit
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String carIdStr = request.getParameter("carId");
            if (carIdStr != null && !carIdStr.isEmpty()) {
                int carId = Integer.parseInt(carIdStr);

                Optional<CarDTO> carDTO = carService.getCarById(carId);
                if (carDTO.isPresent()) {
                    // Gắn đối tượng car vào request để JSP đọc
                    request.setAttribute("car", carDTO.get());

                    // Nếu cần, load thêm danh sách category, fuel, seating để đổ vào <select>
                    List<CategoryDTO> categories = carService.getAllCategories();
                    List<FuelDTO> fuels = carService.getAllFuels();
                    List<SeatingDTO> seatings = carService.getAllSeatings();

                    request.setAttribute("categories", categories);
                    request.setAttribute("fuels", fuels);
                    request.setAttribute("seatings", seatings);

                    // Forward sang JSP
                    request.getRequestDispatcher("manager/editcar.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy xe có ID: " + carId);
                    request.getRequestDispatcher("managecar").forward(request, response);
                }
            } else {
                response.sendRedirect("managecar");
            }
        } catch (util.exception.ValidationException | util.exception.BusinessException | util.exception.DataAccessException e) {
            request.setAttribute("error", util.MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("managecar").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", util.MessageUtil.getError("error.system.car.management"));
            request.getRequestDispatcher("managecar").forward(request, response);
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

        //Lấy action từ server
        String action = request.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            //Nếu action là create thì thêm user mới
            addCar(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            //Nếu action là delete thì xóa user
            deleteCar(request, response);
        } else if ("edit".equalsIgnoreCase(action)) {
            //Nếu edit thì truyền dữ liệu qua trang edit
            showEditForm(request, response);
        } else if ("update".equalsIgnoreCase(action)) {
            //Thực hiện cập nhật bên trang service
            updateCar(request, response);
        } else if ("detail".equalsIgnoreCase(action)) {
            showDetailCarForm(request, response);
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

    private void showDetailCarForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String carIdStr = request.getParameter("carId");
            if (carIdStr != null && !carIdStr.isEmpty()) {
                int carId = Integer.parseInt(carIdStr);

                Optional<CarDTO> carDTO = carService.getCarById(carId);
                if (carDTO.isPresent()) {
                    // Gắn đối tượng car vào request để JSP đọc
                    request.setAttribute("car", carDTO.get());

                    // Nếu cần, load thêm danh sách category, fuel, seating để đổ vào
                    List<CategoryDTO> categories = carService.getAllCategories();
                    List<FuelDTO> fuels = carService.getAllFuels();
                    List<SeatingDTO> seatings = carService.getAllSeatings();

                    // Lấy danh sách các vehicle thuộc car này
                    List<VehicleDTO> vehicles = carService.getVehicalByCarId(carId);
                    List<LocationDTO> locations = carService.getAllLocation();
                    
                    
                    // Lấy danh sách giá xe theo ngày
                    List<model.CarPrices> carPrices = carService.getPricesByCarId(carId);

                    request.setAttribute("categories", categories);
                    request.setAttribute("fuels", fuels);
                    request.setAttribute("seatings", seatings);
                    request.setAttribute("vehicles", vehicles);
                    request.setAttribute("locations", locations);
                    request.setAttribute("carPrices", carPrices);

                    // Forward sang JSP
                    request.getRequestDispatcher("manager/manage_detail_car.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy xe có ID: " + carId);
                    request.getRequestDispatcher("managecar").forward(request, response);
                }
            } else {
                response.sendRedirect("managecar");
            }
        } catch (util.exception.ValidationException | util.exception.BusinessException | util.exception.DataAccessException e) {
            request.setAttribute("error", util.MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("managecar").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", util.MessageUtil.getError("error.system.car.management"));
            request.getRequestDispatcher("managecar").forward(request, response);
        }
    }

}