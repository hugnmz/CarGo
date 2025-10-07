package service.impl;

import dao.CarPricesDAO;
import dao.CartsDAO;
import dao.OrdersDAO;
import dao.VehiclesDAO;
import dto.CartDTO;
import dto.OrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Carts;
import model.Orders;
import model.Vehicles;
import util.Mapper;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import service.CartService;

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
    private Mapper mapper;

    public CartServiceImpl() {
    }

    @Override
    public boolean addToCart(Integer customerId, Integer vehicleId, LocalDateTime rentStartDate, LocalDateTime rentEndDate) {
        try {
            if (rentStartDate.isAfter(rentEndDate) || rentStartDate.isBefore(LocalDateTime.now())) {
                return false;
            }

            Optional<Vehicles> vehicleOptional = vehiclesDAO.getVehicleById(vehicleId);
            if (vehicleOptional.isEmpty()) {
                return false;
            }

            Vehicles vehicle = vehicleOptional.get();
            if (!vehicle.getIsActive()) {
                return false;
            }

            Optional<Carts> cartOptional = cartsDAO.getCartByCustomer(customerId);
            Carts cart;
            if (cartOptional.isEmpty()) {
                if (!cartsDAO.createCart(customerId)) {
                    return false;
                }

                cartOptional = cartsDAO.getCartByCustomer(customerId);
                if (cartOptional.isEmpty()) {
                    return false;
                }
            }

            cart = cartOptional.get();

            BigDecimal price = calculateRentalPrice(vehicleId,
                    rentStartDate, rentEndDate);

            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

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

    @Override
    public boolean removeFromCart(Integer customerId, Integer cartDetailId) {
        try {
            Optional<Orders> orderOptional = ordersDAO.getOrderById(cartDetailId);
            if (orderOptional.isEmpty()) {
                return false;
            }

            Orders order = orderOptional.get();
            Optional<Carts> cartOptional = cartsDAO.getCartByCustomer(customerId);
            if (cartOptional.isEmpty() || cartOptional.get()
                    .getCartId().equals(order.getCartId())) {
                return false;
            }

            return ordersDAO.deleteOrder(cartDetailId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clearCart(Integer customerId) {
        Optional<Carts> cartOptional = cartsDAO.getCartByCustomer(customerId);
        if (cartOptional.isEmpty()) {
            return true;
        }

        return cartsDAO.clearCart(customerId);
    }

    @Override
    public Optional<CartDTO> getCartByCustomer(Integer customerId) {

        try {
            Optional<Carts> cartOptional = cartsDAO.getCartByCustomer(customerId);
            if (cartOptional.isEmpty()) {
                return Optional.empty();
            }

            Carts cart = cartOptional.get();
            CartDTO dto = mapper.map(cart, CartDTO.class);
            
            // Set customer name if customer exists
            if (cart.getCustomer() != null) {
                dto.setCustomerName(cart.getCustomer().getFullName());
            }
            
            return Optional.of(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<OrderDTO> getCartItems(Integer customerId) {
        try {
            Optional<Carts> cartOptional = cartsDAO.getCartByCustomer(customerId);
            if (cartOptional.isEmpty()) {
                return new ArrayList<>();
            }

            List<Orders> orders = ordersDAO.getOrdersByCart(cartOptional.get().getCartId());
            List<OrderDTO> orderDTO = new ArrayList<>();

            for (Orders o : orders) {
                OrderDTO dto = mapper.map(o, OrderDTO.class);
                
                // Set vehicle info if available
                if (o.getVehicle() != null) {
                    dto.setPlateNumber(o.getVehicle().getPlateNumber());
                    
                    if (o.getVehicle().getCar() != null) {
                        dto.setCarName(o.getVehicle().getCar().getName());
                    }
                }
                
                orderDTO.add(dto);
            }
            return orderDTO;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // Comment removed
            // Comment removed
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BigDecimal calculateRentalPrice(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {

        try {
            Optional<Vehicles> vehicleOptional = vehiclesDAO.getVehicleById(vehicleId);
            if (vehicleOptional.isEmpty()) {
                return null;
            }

            Vehicles vehicle = vehicleOptional.get();
            Optional<BigDecimal> dailyPriceOptional = carPricesDAO.getCurrentDailyPrice(vehicle.getCarId());
            if (dailyPriceOptional.isEmpty()) {
                return null;
            }

            BigDecimal dailyPrice = dailyPriceOptional.get();

            long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
            if (days <= 0) {
                days = 1;
            }

            return dailyPrice.multiply(BigDecimal.valueOf(days));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
