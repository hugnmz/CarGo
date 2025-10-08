/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import java.util.Optional;
import model.Orders;

/**
 *
 * @author admin
 */
public interface OrdersDAO {

    List<Orders> getOrdersByCart(Integer cartId);

    Optional<Orders> getOrderById(Integer cartDetailId);

    boolean addOrder(Orders order);

    boolean updateOrder(Orders order);

    boolean deleteOrder(Integer cartDetailId);

    boolean clearOrdersByCart(Integer cartId);

    boolean convertCartToContract(Integer cartId, Integer contractId);
}
