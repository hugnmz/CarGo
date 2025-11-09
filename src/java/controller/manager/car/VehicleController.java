
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
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

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
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
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

            vehicleService.addVehicle(v);

            request.setAttribute("message", MessageUtil.getError("error.vehicle.add.success"));

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.vehicle.management"));
        }
        request.getRequestDispatcher("controllerinformationcar?action=detail&carId="
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

            // Gọi service có ném Exception
            vehicleService.updateVehicle(v);

            request.setAttribute("message", MessageUtil.getError("error.vehicle.update.success"));

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.vehicle.management"));
        }

        request.getRequestDispatcher("controllerinformationcar?action=detail&carId="
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
                request.setAttribute("message", MessageUtil.getError("error.vehicle.delete.success"));
            } else {
                throw new BusinessException("error.vehicle.delete.failed");
            }

            request.getRequestDispatcher("controllerinformationcar?action=detail&carId=" + carId)
                    .forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("controllerinformationcar?action=detail&carId="
                    + Integer.parseInt(request.getParameter("carId")))
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.vehicle.management"));
            request.getRequestDispatcher("controllerinformationcar?action=detail&carId="
                    + Integer.parseInt(request.getParameter("carId")))
                    .forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Kiểm tra quyền
        HttpSession session = request.getSession(false);
        if (session == null || !"MANAGER".equals(session.getAttribute("roleName"))) {
            response.sendRedirect("LoginServlet");
            return;
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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, MessageUtil.getError("error.action.invalid"));
        }
    }
}
