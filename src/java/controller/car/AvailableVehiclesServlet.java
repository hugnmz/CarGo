package controller.car;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import dto.VehicleDTO;
import service.VehicleService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "AvailableVehiclesServlet", urlPatterns = {"/api/available-vehicles"})
public class AvailableVehiclesServlet extends HttpServlet {

    // service xu ly thong tin phuong tien
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao vehicle service tu di container
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // dat content type la json
        response.setContentType("application/json; charset=UTF-8");

        // lay tham so tu request
        String carIdStr = request.getParameter("carId");
        String pickupDate = request.getParameter("pickupDate"); // yyyy-MM-dd
        String returnDate = request.getParameter("returnDate"); // yyyy-MM-dd

        // kiem tra tham so co day du khong
        if (carIdStr == null || pickupDate == null || returnDate == null ||
            carIdStr.isBlank() || pickupDate.isBlank() || returnDate.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject error = new JSONObject();
            error.put("error", "missing_params");
            response.getWriter().write(error.toString());
            return;
        }

        try {
            // chuyen doi tham so tu string sang integer va localdate
            Integer carId = Integer.valueOf(carIdStr);
            LocalDate startD = LocalDate.parse(pickupDate);
            LocalDate endD = LocalDate.parse(returnDate);

            // chuyen doi ngay sang localdatetime voi gio mac dinh 09:00 - 17:00
            LocalDateTime start = startD.atTime(9, 0);
            LocalDateTime end = endD.atTime(17, 0);

            // kiem tra khoang thoi gian hop le
            if (!end.isAfter(start)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject error = new JSONObject();
                error.put("error", "invalid_range");
                response.getWriter().write(error.toString());
                return;
            }

            // lay danh sach phuong tien co san tu database
            List<VehicleDTO> list = vehicleService.getAvailableVehiclesByCar(carId, start, end);

            // tao json response bang JSONArray
            JSONArray jsonArray = new JSONArray();
            for (VehicleDTO v : list) {
                JSONObject vehicle = new JSONObject();
                vehicle.put("vehicleId", v.getVehicleId());
                vehicle.put("plateNumber", v.getPlateNumber() != null ? v.getPlateNumber() : "");
                vehicle.put("city", v.getCity() != null ? v.getCity() : "");
                jsonArray.put(vehicle);
            }

            // tra ve json array
            response.getWriter().write(jsonArray.toString());
        } catch (ValidationException | BusinessException | DataAccessException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject error = new JSONObject();
            error.put("error", MessageUtil.getErrorFromException(e));
            response.getWriter().write(error.toString());
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject error = new JSONObject();
            error.put("error", MessageUtil.getError("error.system.available.vehicles"));
            response.getWriter().write(error.toString());
        }
    }
}
