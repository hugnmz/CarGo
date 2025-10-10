package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.CarsDAO;
import dao.VehiclesDAO;
import util.di.DIContainer;
import java.io.IOException;
import java.util.List;
import model.Cars;
import model.Vehicles;

/**
 * HomeServlet - Xử lý trang chủ và hiển thị danh sách xe
 * 
 * MỤC ĐÍCH:
 * - Lấy danh sách xe từ database để hiển thị trên trang chủ
 * - Lấy thông tin xe thực tế có sẵn để thuê
 * - Cung cấp dữ liệu cho trang home.jsp
 * 
 * URL MAPPING: /home
 * METHOD: GET (hiển thị trang)
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private CarsDAO carsDAO;
    private VehiclesDAO vehiclesDAO;

    /**
     * KHỞI TẠO SERVLET
     * 
     * MỤC ĐÍCH:
     * - Inject CarsDAO và VehiclesDAO thông qua DI Container
     * - Đảm bảo các DAO sẵn sàng để xử lý request
     * 
     * HOẠT ĐỘNG:
     * 1. Gọi DIContainer để lấy instance của CarsDAO
     * 2. Gọi DIContainer để lấy instance của VehiclesDAO
     * 3. Nếu lỗi thì throw RuntimeException để fail fast
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            carsDAO = DIContainer.get(CarsDAO.class);
            vehiclesDAO = DIContainer.get(VehiclesDAO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DAOs", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
;
    }
}

