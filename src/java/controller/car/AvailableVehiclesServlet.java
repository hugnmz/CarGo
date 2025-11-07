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

    // service xu ly thong tin phuong tien
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao vehicle service tu di container
            vehicleService = DIContainer.get(VehicleService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            response.getWriter().write("{\"error\":\"missing_params\"}");
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
                response.getWriter().write("{\"error\":\"invalid_range\"}");
                return;
            }

            // lay danh sach phuong tien co san tu database
            List<VehicleDTO> list = vehicleService.getAvailableVehiclesByCar(carId, start, end);

            // tao json response
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
            // neu co loi thi tra ve loi server
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"server_error\"}");
        }
    }

    /**
     * method escape ky tu dac biet trong json
     * - thay the ky tu dac biet de tranh loi json
     */
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
