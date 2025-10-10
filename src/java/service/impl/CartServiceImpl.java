/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.CartsDAO;
import dao.OrdersDAO;
import dao.VehiclesDAO;
import dto.CartDTO;
import dto.OrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import mapper.CartMapper;
import model.Carts;
import model.Orders;
import model.Vehicles;
import service.CartService;
import util.di.annotation.Autowired;

/**
 *
 * @author admin
 */
public class CartServiceImpl implements CartService {

    @Autowired
    private CartsDAO cartsDAO;

    @Autowired
    private OrdersDAO ordersDAO;

    @Autowired
    private VehiclesDAO vehiclesDAO;
    
    @Autowired
    private CartMapper cartMapper;

    @Override
    public boolean addToCart(Integer customerId, Integer vehicleId, LocalDateTime rentStartDate, LocalDateTime rentEndDate) {

        try {

            // ktra xem thoi gian co hop le hay ko
            if (rentStartDate.isAfter(rentEndDate) || rentStartDate.isBefore(rentEndDate)) {
                return false;
            }

            // ktra xem xe  co ton tai va dang hoat dong hay ko
            Optional<Vehicles> v = vehiclesDAO.getVehicleById(vehicleId);
            if (v.isEmpty()) {
                return false;
            }

            Vehicles vehicle = v.get();
            if (!vehicle.getIsActive()) {
                return false;
            }

            // ktra ngay thue co kha dung trong thoi gian do ko
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
                    return false; // tao gio hang ko dc
                }
            }

            Carts cart = c.get();

            // tinh gia thue xe
            BigDecimal price = calculateRentalPrice(vehicleId, rentStartDate, rentEndDate);
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

            // tao 1 order moi t rong gio hang
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
            // kiem tra don hang co ton tai hay ko
            Optional<Orders> o = ordersDAO.getOrderById(cartDetailId);
            if (o.isEmpty()) {
                return false;
            }

            // kiem tra xem don hang co thuoc ve gio hang cua khach hang nay ko
            Orders order = o.get();
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty() || c.get().getCartId().equals(order.getCartId())) {
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

            // Chuyen doi tu Model sang DTO
            CartDTO dto = cartMapper.toDTO(cartOptional.get());
            return Optional.of(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<OrderDTO> getCartItems(Integer customerId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal calculateRentalPrice(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
