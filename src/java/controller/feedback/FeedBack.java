package controller.feedback;

import dto.FeedbackDTO;
import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.FeedbackService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "FeedbackServlet", urlPatterns = {"/feedbacks"})
public class FeedBack extends HttpServlet {

    private FeedbackService feedbackService;

    @Override
    public void init() throws ServletException {
        try {
            feedbackService = DIContainer.get(FeedbackService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int offset = parseIntOr(req.getParameter("offset"), 0);
        int limit = parseIntOr(req.getParameter("limit"), 6);

        List<FeedbackDTO> list = feedbackService.listRecent(offset, limit);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print("[");
            for (int i = 0; i < list.size(); i++) {
                out.print(toJson(list.get(i)));
                if (i < list.size() - 1) {
                    out.print(",");
                }
            }
            out.print("]");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        CustomerDTO customer = (session != null) ? (CustomerDTO) session.getAttribute("c") : null;
        Integer customerId = (customer != null) ? customer.getCustomerId() : null;
        if (customerId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String comment = req.getParameter("comment");
        Integer vehicleId = null;
        try {
            if (req.getParameter("vehicleId") != null && !req.getParameter("vehicleId").isEmpty()) {
                vehicleId = Integer.parseInt(req.getParameter("vehicleId"));
            }
        } catch (NumberFormatException ignore) {
        }

        try {
            FeedbackDTO dto = feedbackService.create(customerId, comment, vehicleId);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.print(toJson(dto));
            }
        } catch (IllegalArgumentException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write(ex.getMessage());
        } catch (ValidationException | BusinessException | DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write(MessageUtil.getErrorFromException(e));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write(MessageUtil.getError("error.system.feedback"));
        }
    }

    private int parseIntOr(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("<", "\\u003c")
                .replace(">", "\\u003e")
                .replace("&", "\\u0026");
    }

    private String toJson(FeedbackDTO dto) {
        // createAt có thể null nếu driver không map, nên trả dạng string nếu có
        String createAt = (dto.getCreateAt() != null) ? dto.getCreateAt().toString() : "";
        return "{"
                + "\"feedbackId\":" + (dto.getFeedbackId() == null ? "null" : dto.getFeedbackId()) + ","
                + "\"customerId\":" + (dto.getCustomerId() == null ? "null" : dto.getCustomerId()) + ","
                + "\"vehicleId\":" + (dto.getVehicleId() == null ? "null" : dto.getVehicleId()) + ","
                + "\"comment\":\"" + escape(dto.getComment()) + "\","
                + "\"createAt\":\"" + escape(createAt) + "\","
                + "\"customerName\":\"" + escape(dto.getCustomerName()) + "\","
                + "\"customerEmail\":\"" + escape(dto.getCustomerEmail()) + "\","
                + "\"plateNumber\":\"" + escape(dto.getPlateNumber()) + "\","
                + "\"carName\":\"" + escape(dto.getCarName()) + "\","
                + "\"carImage\":\"" + escape(dto.getCarImage()) + "\""
                + "}";
    }
}