package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Orders {
    private Integer cartDetailId;    // ID chi tiết giỏ hàng
    private Integer cartId;           // ID giỏ hàng
    private Integer vehicleId;        // ID xe
    private LocalDateTime rentStartDate; // Ngày bắt đầu thuê
    private LocalDateTime rentEndDate;   // Ngày kết thúc thuê
    private BigDecimal price;        // Giá tạm tính
    
    // Các đối tượng liên quan
    private Carts cart;              // Giỏ hàng
    private Vehicles vehicle;        // Xe
    
    // Constructors
    public Orders() {}
    
    public Orders(Integer cartId, Integer vehicleId, LocalDateTime rentStartDate, 
                  LocalDateTime rentEndDate, BigDecimal price) {
        this.cartId = cartId;
        this.vehicleId = vehicleId;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.price = price;
    }
    
    // Getters and Setters
    public Integer getCartDetailId() { return cartDetailId; }
    public void setCartDetailId(Integer cartDetailId) { this.cartDetailId = cartDetailId; }
    
    public Integer getCartId() { return cartId; }
    public void setCartId(Integer cartId) { this.cartId = cartId; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public LocalDateTime getRentStartDate() { return rentStartDate; }
    public void setRentStartDate(LocalDateTime rentStartDate) { this.rentStartDate = rentStartDate; }
    
    public LocalDateTime getRentEndDate() { return rentEndDate; }
    public void setRentEndDate(LocalDateTime rentEndDate) { this.rentEndDate = rentEndDate; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Carts getCart() { return cart; }
    public void setCart(Carts cart) { this.cart = cart; }
    
    public Vehicles getVehicle() { return vehicle; }
    public void setVehicle(Vehicles vehicle) { this.vehicle = vehicle; }
}
