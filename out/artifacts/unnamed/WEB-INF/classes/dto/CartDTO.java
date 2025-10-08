/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;
import java.util.List;

public class CartDTO {
    private Integer cartId;           // ID giỏ hàng
    private Integer customerId;       // ID khách hàng
    private String customerName;      // Tên khách hàng
    private LocalDateTime createAt;   // Ngày tạo giỏ hàng
    private List<OrderDTO> orders;    // Danh sách xe trong giỏ
    
    // Constructors
    public CartDTO() {}
    
    public CartDTO(Integer cartId, Integer customerId, LocalDateTime createAt) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.createAt = createAt;
    }
    
    // Getters và Setters
    public Integer getCartId() { return cartId; }
    public void setCartId(Integer cartId) { this.cartId = cartId; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public List<OrderDTO> getOrders() { return orders; }
    public void setOrders(List<OrderDTO> orders) { this.orders = orders; }
}