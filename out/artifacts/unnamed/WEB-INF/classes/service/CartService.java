
package service;

import dto.CartDTO;
import dto.OrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author admin
 */
public interface CartService {
    boolean addToCart(Integer customerId, Integer vehicleId, LocalDateTime rentStartDate,
            LocalDateTime rentEndDate);
    
    boolean removeFromCart(Integer customerId, Integer cartDetailId);
    
    boolean clearCart(Integer customerId);
    
    Optional<CartDTO> getCartByCustomer(Integer customerId);
    
    List<OrderDTO> getCartItems(Integer customerId);
    
    boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
    
    BigDecimal calculateRentalPrice(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}

