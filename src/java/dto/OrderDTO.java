package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class OrderDTO {
    private Integer cartDetailId;    // ID chi tiết giỏ hàng
    private Integer cartId;           // ID giỏ hàng
    private Integer vehicleId;        // ID xe
    private String plateNumber;       // Biển số xe
    private String carName;          // Tên xe
    private LocalDateTime rentStartDate; // Ngày bắt đầu thuê
    private LocalDateTime rentEndDate;   // Ngày kết thúc thuê
    private BigDecimal price;        // Giá tạm tính
    
    // Constructors
    public OrderDTO() {}
    
    // Getters and Setters
    public Integer getCartDetailId() { return cartDetailId; }
    public void setCartDetailId(Integer cartDetailId) { this.cartDetailId = cartDetailId; }
    
    public Integer getCartId() { return cartId; }
    public void setCartId(Integer cartId) { this.cartId = cartId; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    
    public LocalDateTime getRentStartDate() { return rentStartDate; }
    public void setRentStartDate(LocalDateTime rentStartDate) { this.rentStartDate = rentStartDate; }
    
    public LocalDateTime getRentEndDate() { return rentEndDate; }
    public void setRentEndDate(LocalDateTime rentEndDate) { this.rentEndDate = rentEndDate; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
