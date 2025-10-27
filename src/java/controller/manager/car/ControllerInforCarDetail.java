///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller.manager.car;
//
//import dao.*;
//import dto.*;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.util.Optional;
//import service.CarService;
//import util.di.DIContainer;
//
///**
// *
// * @author DELL
// */
//@WebServlet(name = "ControllerInforCarDetail", urlPatterns = {"/cardetail"})
//public class ControllerInforCarDetail extends HttpServlet {
//
//    private CarService carService;
//
//    @Override
//    public void init() throws ServletException {
//        try {
//            carService = DIContainer.get(CarService.class);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void showDetailCarForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            String carIdStr = request.getParameter("carId");
//            if (carIdStr != null && !carIdStr.isEmpty()) {
//                int carId = Integer.parseInt(carIdStr);
//
//                Optional<CarDTO> carDTO = carService.getCarById(carId);
//                if (carDTO.isPresent()) {
//                    // Gắn đối tượng car vào request để JSP đọc
//                    request.setAttribute("car", carDTO.get());
//
//                    // Nếu cần, load thêm danh sách category, fuel, seating để đổ vào
//                    List<CategoryDTO> categories = carService.getAllCategories();
//                    List<FuelDTO> fuels = carService.getAllFuels();
//                    List<SeatingDTO> seatings = carService.getAllSeatings();
//
//                    // Lấy danh sách các vehicle thuộc car này
//                    List<VehicleDTO> vehicles = carService.getVehicalByCarId(carId);
//                    List<LocationDTO> locations = carService.getAllLocation();
//                    
//
//                    request.setAttribute("categories", categories);
//                    request.setAttribute("fuels", fuels);
//                    request.setAttribute("seatings", seatings);
//                    request.setAttribute("vehicles", vehicles);
//                    request.setAttribute("locations", locations);
//
//                    // Forward sang JSP
//                    request.getRequestDispatcher("manager/manage_detail_car.jsp").forward(request, response);
//                } else {
//                    request.setAttribute("error", "Không tìm thấy xe có ID: " + carId);
//                    request.getRequestDispatcher("managecar").forward(request, response);
//                }
//            } else {
//                response.sendRedirect("managecar");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Lỗi khi tải form chỉnh sửa: " + e.getMessage());
//            request.getRequestDispatcher("managecar").forward(request, response);
//        }
//    }
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        showDetailCarForm(request, response);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//}
