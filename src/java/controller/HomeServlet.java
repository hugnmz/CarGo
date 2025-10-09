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

    /**
     * XỬ LÝ REQUEST GET - Hiển thị trang chủ
     * 
     * MỤC ĐÍCH:
     * - Lấy danh sách xe từ database
     * - Lấy thông tin xe thực tế có sẵn
     * - Set dữ liệu vào request để JSP hiển thị
     * - Forward đến home.jsp
     * 
     * HOẠT ĐỘNG:
     * 1. Lấy danh sách tất cả loại xe (Cars)
     * 2. Lấy danh sách xe thực tế có sẵn (Vehicles)
     * 3. Set dữ liệu vào request attributes
     * 4. Forward đến home.jsp để render
     * 
     * ATTRIBUTES SET:
     * - cars: List<Cars> - Danh sách loại xe
     * - vehicles: List<Vehicles> - Danh sách xe thực tế có sẵn
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // BƯỚC 1: LẤY DANH SÁCH LOẠI XE
            // Lấy tất cả loại xe có trong hệ thống
            // Bao gồm thông tin giá hiện tại
            List<Cars> cars = carsDAO.getAllCars();
            
            // BƯỚC 2: LẤY DANH SÁCH XE THỰC TẾ CÓ SẴN
            // Lấy xe thực tế đang active và có sẵn để thuê
            List<Vehicles> vehicles = vehiclesDAO.getAllVehicles();
            
            // BƯỚC 3: SET DỮ LIỆU VÀO REQUEST
            // Set danh sách loại xe vào request attribute
            request.setAttribute("cars", cars);
            
            // Set danh sách xe thực tế vào request attribute
            request.setAttribute("vehicles", vehicles);
            
            // BƯỚC 4: FORWARD ĐẾN TRANG HIỂN THỊ
            // Forward đến home.jsp để render trang chủ
            request.getRequestDispatcher("home.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Xử lý lỗi - log và hiển thị trang lỗi
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải dữ liệu xe");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    /**
     * XỬ LÝ REQUEST POST - Redirect về GET
     * 
     * MỤC ĐÍCH:
     * - Ngăn chặn POST requests không mong muốn
     * - Redirect về GET để hiển thị trang chủ
     * 
     * HOẠT ĐỘNG:
     * 1. Redirect POST requests về GET
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect POST requests về GET
        response.sendRedirect("home");
    }
}

