package controller.manager.car;

import dto.VehicleDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import service.VehicleService;
import service.CarService;
import util.di.DIContainer;

@WebServlet(name = "VehicleController", urlPatterns = {"/vehiclecontroller"})
public class VehicleController extends HttpServlet {

    private VehicleService vehicleService;
    private CarService carService;

    @Override
    public void init() throws ServletException {
        try {
            vehicleService = DIContainer.get(VehicleService.class);
            carService = DIContainer.get(CarService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Thêm vehicle mới
    private void addVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            String licensePlate = request.getParameter("licensePlate");
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
            int locationId = Integer.parseInt(request.getParameter("locationId"));

            VehicleDTO v = new VehicleDTO();
            v.setCarId(carId);
            v.setPlateNumber(licensePlate);
            v.setIsActive(isActive);
            v.setLocationId(locationId);

//            boolean added = vehicleService.addVehicle(v);
//            if (added) {
//                request.setAttribute("message", "Thêm vehicle thành công!");
//            } else {
//                request.setAttribute("error", "Thêm vehicle thất bại!");
//            }
//
//            request.getRequestDispatcher("cardetail?action=detail&carId=" + carId)
//                    .forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Lỗi khi thêm vehicle: " + e.getMessage());
//            request.getRequestDispatcher("cardetail?action=detail&carId="
//                    + Integer.parseInt(request.getParameter("carId")))
//                    .forward(request, response);
//        }
            vehicleService.addVehicle(v);

            request.setAttribute("message", "Thêm vehicle thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            // Đẩy lỗi chi tiết ra JSP
            request.setAttribute("error", "Thêm thất bại: " + e.getMessage());
        }
        request.getRequestDispatcher("cardetail?action=detail&carId="
                + Integer.parseInt(request.getParameter("carId")))
                .forward(request, response);
    }

    // Cập nhật vehicle
    private void updateVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            int carId = Integer.parseInt(request.getParameter("carId"));
            String plateNumber = request.getParameter("plateNumber");
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
            int locationId = Integer.parseInt(request.getParameter("locationId"));

            VehicleDTO v = new VehicleDTO();
            v.setVehicleId(vehicleId);
            v.setPlateNumber(plateNumber);
            v.setIsActive(isActive);
            v.setLocationId(locationId);
            v.setCarId(carId);

//            boolean updated = vehicleService.updateVehicle(v);
//            if (updated) {
//                request.setAttribute("message", "Cập nhật vehicle thành công!");
//            } else {
//                request.setAttribute("error", "Cập nhật vehicle thất bại!");
//            }
//
//           request.getRequestDispatcher("cardetail?action=detail&carId=" + carId)
//                    .forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Lỗi khi cập nhật vehicle: " + e.getMessage());
//            request.getRequestDispatcher("cardetail?action=detail&carId=" + 
//                    Integer.parseInt(request.getParameter("carId")))
//                    .forward(request, response);
//        }
            // Gọi service có ném Exception
            vehicleService.updateVehicle(v);

            request.setAttribute("message", "Cập nhật vehicle thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            // Đẩy lỗi chi tiết ra JSP
            request.setAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }

        request.getRequestDispatcher("cardetail?action=detail&carId="
                + Integer.parseInt(request.getParameter("carId")))
                .forward(request, response);
    }

    // Xóa vehicle
    private void deleteVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            int carId = Integer.parseInt(request.getParameter("carId"));

            boolean deleted = vehicleService.deleteVehicle(vehicleId);
            if (deleted) {
                request.setAttribute("message", "Xóa vehicle thành công!");
            } else {
                request.setAttribute("error", "Xóa vehicle thất bại!");
            }

            request.getRequestDispatcher("cardetail?action=detail&carId=" + carId)
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi xóa vehicle: " + e.getMessage());
            request.getRequestDispatcher("cardetail?action=detail&carId="
                    + Integer.parseInt(request.getParameter("carId")))
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            addVehicle(request, response);
        } else if ("update".equalsIgnoreCase(action)) {
            updateVehicle(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            deleteVehicle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ!");
        }
    }
}
