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

@WebServlet(name = "AvailableVehiclesServlet", urlPatterns = {"/api/available-vehicles"})
public class AvailableVehiclesServlet extends HttpServlet {

    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String carIdStr = request.getParameter("carId");
        String pickupDate = request.getParameter("pickupDate"); // yyyy-MM-dd
        String returnDate = request.getParameter("returnDate"); // yyyy-MM-dd

        if (carIdStr == null || pickupDate == null || returnDate == null ||
            carIdStr.isBlank() || pickupDate.isBlank() || returnDate.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"missing_params\"}");
            return;
        }

        try {
            Integer carId = Integer.valueOf(carIdStr);
            LocalDate startD = LocalDate.parse(pickupDate);
            LocalDate endD = LocalDate.parse(returnDate);

            // Map date-only to default time window 09:00 - 17:00
            LocalDateTime start = startD.atTime(9, 0);
            LocalDateTime end = endD.atTime(17, 0);

            if (!end.isAfter(start)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"invalid_range\"}");
                return;
            }

            List<VehicleDTO> list = vehicleService.getAvailableVehiclesByCar(carId, start, end);

            // Minimal JSON writer
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (int i = 0; i < list.size(); i++) {
                VehicleDTO v = list.get(i);
                if (i > 0) sb.append(',');
                sb.append('{')
                  .append("\"vehicleId\":").append(v.getVehicleId()).append(',')
                  .append("\"plateNumber\":\"").append(escape(v.getPlateNumber())).append("\",")
                  .append("\"city\":\"").append(escape(v.getCity())).append("\"")
                  .append('}');
            }
            sb.append(']');
            response.getWriter().write(sb.toString());
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"server_error\"}");
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
