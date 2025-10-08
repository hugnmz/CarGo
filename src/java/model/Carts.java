package model;

import java.time.LocalDateTime;
import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Carts {
    
    @Column()
    private Integer cartId;           // ID gio hang
    
    @Column()
    private Integer customerId;       // ID khách hàng
    
    @Column()
    private LocalDateTime createAt;   // Ngay tao gio hang
    
    // Các đối tượng liên quan
    @Nested
    private Customers customer;       // Khách hàng
    private List<Orders> orders;      // Danh sach don hang trong gio
    
    // Constructors
    public Carts() {}
    
    public Carts(Integer customerId) {
        this.customerId = customerId;
        this.createAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getCartId() { return cartId; }
    public void setCartId(Integer cartId) { this.cartId = cartId; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public Customers getCustomer() { return customer; }
    public void setCustomer(Customers customer) { this.customer = customer; }
    
    public List<Orders> getOrders() { return orders; }
    public void setOrders(List<Orders> orders) { this.orders = orders; }
}
