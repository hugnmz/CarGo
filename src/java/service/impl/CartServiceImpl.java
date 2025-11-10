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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mapper.CartMapper;
import mapper.OrderMapper;
import model.Carts;
import model.Orders;
import model.Vehicle;
import service.CartService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.MessageUtil;
import util.exception.ApplicationException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import util.exception.BusinessException;

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
            // (1) Validate thời gian - SỬA: Theo ngày thay vì giờ
            if (rentStartDate == null || rentEndDate == null) {
                throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
            }
            if (!rentEndDate.isAfter(rentStartDate)) {
                throw new ValidationException(MessageUtil.getError("error.date.format.invalid"));
            }
            
            // Kiểm tra tối thiểu 1 ngày (thay vì 60 phút)
            LocalDate startDate = rentStartDate.toLocalDate();
            LocalDate endDate = rentEndDate.toLocalDate();
            if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
                throw new ValidationException(MessageUtil.getError("error.rental.minimum"));
            }

            // (2) Kiểm tra vehicle có rảnh theo hợp đồng
            if (!vehiclesDAO.isVehicleAvailable(vehicleId, rentStartDate, rentEndDate)) {
                throw new BusinessException(MessageUtil.getError("error.vehicle.unavailable"));
            }

            // (3) Lấy giỏ hàng
            Optional<Carts> cOpt = cartsDAO.getCartByCustomer(customerId);
            Carts cart;
            if (cOpt.isEmpty()) {
                boolean created = cartsDAO.createCart(customerId);
                if (!created) {
                    throw new BusinessException(MessageUtil.getError("error.dataaccess.cart.get.failed"));
                }
                cart = cartsDAO.getCartByCustomer(customerId).orElse(null);
                if (cart == null) {
                    throw new BusinessException(MessageUtil.getError("error.dataaccess.cart.get.failed"));
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
                throw new BusinessException(MessageUtil.getError("error.vehicle.unavailable"));
            }

            // (5) Tính giá từng vehicle
            BigDecimal price = calculateRentalPrice(vehicleId, rentStartDate, rentEndDate);
            if (price == null || price.signum() <= 0) {
                throw new BusinessException(MessageUtil.getError("error.validation.data.invalid"));
            }

            // (6) Lưu Order
            Orders order = new Orders();
            order.setCartId(cart.getCartId());
            order.setVehicleId(vehicleId);
            order.setRentStartDate(rentStartDate);
            order.setRentEndDate(rentEndDate);
            order.setPrice(price);

            boolean result = ordersDAO.addOrder(order);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.cart.add.failed"));
            }
            return true;

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception ex) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.cart.add.failed"), ex);
        }
    }

    // xoa khoi gio hang
    @Override
    public boolean removeFromCart(Integer customerId, Integer cartDetailId) {
        try {
            // kiem tra don hang co ton tai hay khong
            Optional<Orders> o = ordersDAO.getOrderById(cartDetailId);
            if (o.isEmpty()) {
                throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
            }

            // kiem tra xem don hang co thuoc ve gio hang cua khach hang nay khong
            Orders order = o.get();
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty() || !c.get().getCartId().equals(order.getCartId())) {
                throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
            }

            // xoa don hang
            boolean result = ordersDAO.deleteOrder(cartDetailId);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.cart.remove.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.cart.remove.failed"), e);
        }

    }

    @Override
    public boolean clearCart(Integer customerId) {
        try {
            Optional<Carts> c = cartsDAO.getCartByCustomer(customerId);
            if (c.isEmpty()) {
                return true; // gio hang trong
            }

            boolean result = cartsDAO.clearCart(customerId);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.cart.clear.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.cart.clear.failed"), e);
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
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.cart.get.failed"), e);
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
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.cart.get.failed"), e);
        }

    }

    @Override
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // kiem tra thoi gian co phu hop hay khong
            if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
                throw new ValidationException(MessageUtil.getError("error.date.format.invalid"));
            }

            Optional<Vehicle> v = vehiclesDAO.getVehicleById(vehicleId);
            if (v.isEmpty()) {
                throw new ValidationException(MessageUtil.getError("error.vehicle.info.missing"));
            }

            Vehicle vehicle = v.get();

            // kiem tra xe co active khong
            if (!vehicle.getIsActive()) {
                throw new BusinessException(MessageUtil.getError("error.vehicle.unavailable"));
            }

            return vehiclesDAO.isVehicleAvailable(vehicleId, startDate, endDate);

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public BigDecimal calculateRentalPrice(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // 1. lay thong tin xe
            Optional<Vehicle> vehicleOpt = vehiclesDAO.getVehicleById(vehicleId);
            if (vehicleOpt.isEmpty()) {
                throw new ValidationException(MessageUtil.getError("error.vehicle.info.missing"));
            }

            Vehicle vehicle = vehicleOpt.get();
            Integer carId = vehicle.getCarId();

            // 2. lay gia hien tai cua model xe
            Optional<BigDecimal> dailyPriceOpt = carPricesDAO.getCurrentDailyPrice(carId);
            if (dailyPriceOpt.isEmpty()) {
                throw new BusinessException(MessageUtil.getError("error.validation.data.invalid"));
            }

            BigDecimal dailyPrice = dailyPriceOpt.get();

            // 3. Tính số ngày thuê trực tiếp từ LocalDateTime
            long days = ChronoUnit.DAYS.between(
                startDate.toLocalDate(), 
                endDate.toLocalDate()
            );
            if (days <= 0) {
                days = 1; // Tối thiểu 1 ngày
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

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

}
