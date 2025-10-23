/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.Optional;
import model.Carts;

/**
 *
 * @author admin
 */
public interface CartsDAO {

    Optional<Carts> getCartByCustomer(Integer customerId);

    boolean createCart(Integer customerId);

    boolean deleteCart(Integer cartId);

    boolean clearCart(Integer customerId);
    
    // dao/CartsDAO.java
boolean clearCartByCustomer(Integer customerId);

}
