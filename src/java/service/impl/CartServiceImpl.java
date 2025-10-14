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
    public boolean addToCart(Integer customerId, Integer vehicleId, LocalDateTime rentStartDate, LocalDateTime rentEndDate) {

        try {

            // kiem tra thoi gian co hop le hay khong
            if (rentStartDate.isAfter(rentEndDate) || rentStartDate.isEqual(rentEndDate)) {
                return false;
            }

            // kiem tra xe co ton tai va dang hoat dong hay khong
            Optional<Vehicles> v = vehiclesDAO.getVehicleById(vehicleId);
            if (v.isEmpty()) {
                return false;
            }

            Vehicles vehicle = v.get();
            if (!vehicle.getIsActive()) {
                return false;
            }

            // kiem tra ngay thue co kha dung trong thoi gian do khong
            if (!isVehicleAvailable(vehicleId, rentStartDate, rentEndDate)) {
                return false;
            }

            // lay hoac tao them gio hang cho khach
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty()) {
                // tao gio hang moi neu chua co
                if (!cartsDAO.createCart(customerId)) {
                    return false; // tao gio hang that bai
                }
                // lay gio hang ay ra
                c = cartsDAO.getCartByCustomer(customerId);
                if (c.isEmpty()) {
                    return false; // tao gio hang khong duoc
                }
            }

            Carts cart = c.get();

            // tinh gia thue xe
            BigDecimal price = calculateRentalPrice(vehicleId, rentStartDate, rentEndDate);
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

            // tao 1 order moi trong gio hang
            Orders order = new Orders();
            order.setCartId(cart.getCartId());
            order.setVehicleId(vehicleId);
            order.setRentStartDate(rentStartDate);
            order.setRentEndDate(rentEndDate);
            order.setPrice(price);

            return ordersDAO.addOrder(order);

        } catch (Exception e) {
            e.printStackTrace();
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
            if(c.isEmpty()){
                return new ArrayList<>();
            }
            
           Carts cart = c.get();
           List<Orders> listOrders = ordersDAO.getOrdersByCart(cart.getCartId());
           List<OrderDTO> listDTO  = new ArrayList<>();
           for(Orders o : listOrders){
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
            if(startDate.isAfter(endDate) || endDate.isBefore(startDate)){
                return false;
            }
            
            Optional<Vehicles> v = vehiclesDAO.getVehicleById(vehicleId);
            if(v.isEmpty()){
                return false; // xe khong ton tai
            }
            
            Vehicles vehicle = v.get();
            
            // kiem tra xe co active khong
            if (!vehicle.getIsActive()) {
                return false;
            }
            
            // kiem tra xe co bi thue trong khoang thoi gian nay khong
            // su dung method co san trong VehiclesDAO
            List<Vehicles> availableVehicles = vehiclesDAO.getAvailableVehiclesByCar(
                vehicle.getCarId(), startDate, endDate);
            
            // kiem tra xem vehicleId co trong danh sach xe available khong
            for (Vehicles vh : availableVehicles) {
                if (vh.getVehicleId().equals(vehicleId)) {
                    return true;
                }
            }
            return false;
            
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
            
            // 3. tinh so ngay thue
            long days = java.time.Duration.between(startDate, endDate).toDays();
            if (days <= 0) {
                return BigDecimal.ZERO; // thoi gian khong hop le
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
