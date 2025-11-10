package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import util.di.annotation.Column;
import util.di.annotation.Nested;


public class Orders {
    
    @Column()
    private Integer cartDetailId;    // ID chi tiet gio hang
    @Column()
    private Integer cartId;           // ID gio hang
    @Column()
    private Integer vehicleId;        // ID xe
    @Column()
    private LocalDateTime rentStartDate; // Ngày bắt đầu thuê
    @Column()
    private LocalDateTime rentEndDate;   // Ngày kết thúc thuê
    @Column()
    private BigDecimal price;        // Giá tạm tính
    
    // Các đối tượng liên quan
    
    @Nested
    private Carts cart;              // Gio hang
    @Nested
    private Vehicle vehicle;        // Xe
    @Nested
    private Cars car;
    @Nested
    private Location location;
    
    // Constructors
    public Orders() {}

    public Orders( Integer cartId, Integer vehicleId, LocalDateTime rentStartDate, LocalDateTime rentEndDate, BigDecimal price, Carts cart, Vehicle vehicle, Cars car, Location location) {
        this.cartId = cartId;
        this.vehicleId = vehicleId;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.price = price;
        this.cart = cart;
        this.vehicle = vehicle;
        this.car = car;
        this.location = location;
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
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public Cars getCar() {
        return car;
    }

    public void setCar(Cars car) {
        this.car = car;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    
}
