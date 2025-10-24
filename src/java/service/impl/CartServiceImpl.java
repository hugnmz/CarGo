/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.CartsDAO;
import dao.OrdersDAO;
import dao.VehiclesDAO;
import dao.CarPricesDAO;
import dto.CartDTO;
import dto.OrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mapper.CartMapper;
import mapper.OrderMapper;
import model.Carts;
import model.Orders;
import model.Vehicles;
import service.CartService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 *
 * @author admin
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartsDAO cartsDAO;

    @Autowired
    private OrdersDAO ordersDAO;

    @Autowired
    private VehiclesDAO vehiclesDAO;

    @Autowired
    private CarPricesDAO carPricesDAO;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean addToCart(Integer customerId, Integer vehicleId,
            LocalDateTime rentStartDate, LocalDateTime rentEndDate) {
        try {
            // (1) Validate thời gian
            if (rentStartDate == null || rentEndDate == null) {
                return false;
            }
            if (!rentEndDate.isAfter(rentStartDate)) {
                return false;
            }
            if (java.time.Duration.between(rentStartDate, rentEndDate).toMinutes() < 60) {
                return false;
            }

            // (2) Kiểm tra vehicle có rảnh theo hợp đồng
            if (!vehiclesDAO.isVehicleAvailable(vehicleId, rentStartDate, rentEndDate)) {
                return false;
            }

            // (3) Lấy giỏ hàng
            Optional<Carts> cOpt = cartsDAO.getCartByCustomer(customerId);
            Carts cart;
            if (cOpt.isEmpty()) {
                boolean created = cartsDAO.createCart(customerId);
                if (!created) {
                    return false;
                }
                cart = cartsDAO.getCartByCustomer(customerId).orElse(null);
                if (cart == null) {
                    return false;
                }
            } else {
                cart = cOpt.get();
            }

            // (4) Không trùng trong giỏ (cùng vehicleId + overlap)
            List<OrderDTO> items = getCartItems(customerId); // dùng DTO sẵn có
            boolean overlapInCart = false;
            for (OrderDTO o : items) {
                if (Objects.equals(o.getVehicleId(), vehicleId)
                        && o.getRentStartDate() != null
                        && o.getRentEndDate() != null
                        && o.getRentStartDate().isBefore(rentEndDate)
                        && o.getRentEndDate().isAfter(rentStartDate)) {
                    overlapInCart = true;
                    break;
                }
            }
            if (overlapInCart) {
                return false;
            }

            // (5) Tính giá từng vehicle
            BigDecimal price = calculateRentalPrice(vehicleId, rentStartDate, rentEndDate);
            if (price == null || price.signum() <= 0) {
                return false;
            }

            // (6) Lưu Order
            Orders order = new Orders();
            order.setCartId(cart.getCartId());
            order.setVehicleId(vehicleId);
            order.setRentStartDate(rentStartDate);
            order.setRentEndDate(rentEndDate);
            order.setPrice(price);

            return ordersDAO.addOrder(order);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // xoa khoi gio hang
    @Override
    public boolean removeFromCart(Integer customerId, Integer cartDetailId) {
        try {
            // kiem tra don hang co ton tai hay khong
            Optional<Orders> o = ordersDAO.getOrderById(cartDetailId);
            if (o.isEmpty()) {
                return false;
            }

            // kiem tra xem don hang co thuoc ve gio hang cua khach hang nay khong
            Orders order = o.get();
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty() || !c.get().getCartId().equals(order.getCartId())) {
                return false;
            }

            // xoa don hang
            return ordersDAO.deleteOrder(cartDetailId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean clearCart(Integer customerId) {
        try {
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty()) {
                return true; // gio hang trong
            }

            return cartsDAO.clearCart(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<CartDTO> getCartByCustomer(Integer customerId) {
        try {
            Optional<Carts> cartOptional = cartsDAO.getCartByCustomer(customerId);
            if (cartOptional.isEmpty()) {
                return Optional.empty();
            }

            // chuyen doi tu Model sang DTO
            CartDTO dto = cartMapper.toDTO(cartOptional.get());
            return Optional.of(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<OrderDTO> getCartItems(Integer customerId) {

        try {
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty()) {
                return new ArrayList<>();
            }

            Carts cart = c.get();
            List<Orders> listOrders = ordersDAO.getOrdersByCart(cart.getCartId());
            List<OrderDTO> listDTO = new ArrayList<>();
            for (Orders o : listOrders) {
                OrderDTO dto = orderMapper.toDTO(o);
                listDTO.add(dto);
            }

            return listDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // kiem tra thoi gian co phu hop hay khong
            if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
                return false;
            }

            Optional<Vehicles> v = vehiclesDAO.getVehicleById(vehicleId);
            if (v.isEmpty()) {
                return false; // xe khong ton tai
            }

            Vehicles vehicle = v.get();

            // kiem tra xe co active khong
            if (!vehicle.getIsActive()) {
                return false;
            }

//            // kiem tra xe co bi thue trong khoang thoi gian nay khong
//            // su dung method co san trong VehiclesDAO
//            List<Vehicles> availableVehicles = vehiclesDAO.getAvailableVehiclesByCar(
//                    vehicleId, startDate, endDate);
//
//            // kiem tra xem vehicleId co trong danh sach xe available khong
//            for (Vehicles vh : availableVehicles) {
//                if (vh.getVehicleId().equals(vehicleId)) {
//                    return true;
//                }
//            }
//            return false;
            return vehiclesDAO.isVehicleAvailable(vehicleId, startDate, endDate);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BigDecimal calculateRentalPrice(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // 1. lay thong tin xe
            Optional<Vehicles> vehicleOpt = vehiclesDAO.getVehicleById(vehicleId);
            if (vehicleOpt.isEmpty()) {
                return null; // xe khong ton tai
            }

            Vehicles vehicle = vehicleOpt.get();
            Integer carId = vehicle.getCarId();

            // 2. lay gia hien tai cua model xe
            Optional<BigDecimal> dailyPriceOpt = carPricesDAO.getCurrentDailyPrice(carId);
            if (dailyPriceOpt.isEmpty()) {
                return null; // khong co gia
            }

            BigDecimal dailyPrice = dailyPriceOpt.get();

            // 3. tinh so ngay thue theo gio (lam tron len 24h), toi thieu 1 ngay neu >= 1 gio
            long hours = java.time.Duration.between(startDate, endDate).toHours();
            if (hours < 1) {
                return BigDecimal.ZERO; // khong hop le (da chan o servlet)
            }
            long days = (long) Math.ceil(hours / 24.0);
            if (days <= 0) {
                days = 1;
            }

            // 4. tinh tong tien
            BigDecimal totalPrice = dailyPrice.multiply(BigDecimal.valueOf(days));

            // 5. co the them logic giam gia theo so ngay
            if (days >= 7) {
                // giam 10% neu thue tu 7 ngay tro len
                totalPrice = totalPrice.multiply(BigDecimal.valueOf(0.9));
            } else if (days >= 3) {
                // giam 5% neu thue tu 3 ngay tro len
                totalPrice = totalPrice.multiply(BigDecimal.valueOf(0.95));
            }

            return totalPrice;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
