/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.OrdersDAO;
import java.util.List;
import java.util.Optional;
import model.Orders;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author admin
 */
// qly don hang trong gio hang
@Repository
public class OrdersDAOImpl implements OrdersDAO {

    @Override
    public List<Orders> getOrdersByCart(Integer cartId) {
        String sql = "SELECT o.*, v.plateNumber, c.name FROM dbo.Orders o "
                + "LEFT JOIN dbo.Vehicles v ON v.vehicleId = o.vehicleId LEFT JOIN dbo.Cars c ON c.carId = v.carId "
                + "WHERE o.cartId = ?";
        return JdbcTemplateUtil.query(sql, Orders.class, cartId);
    }

    @Override
    public Optional<Orders> getOrderById(Integer cartDetailId) {
        String sql = "SELECT o.*, v.plateNumber, c.name, l.city, l.locationId FROM dbo.Orders o LEFT JOIN dbo.Vehicles v ON v.vehicleId = o.vehicleId "
                + "LEFT JOIN dbo.Cars c ON c.carId = v.carId LEFT JOIN dbo.Locations l ON l.locationId = v.locationId "
                + "WHERE o.cartDetailId = ? ";

        Orders order = JdbcTemplateUtil.queryOne(sql, Orders.class, cartDetailId);
        return Optional.ofNullable(order);
    }

    @Override
    public boolean addOrder(Orders order) {
        String sql = "INSERT INTO dbo.Orders(cartId, vehicleId, rentStartDate, rentEndDate, price) "
                + "VALUES (?, ?, ?, ?, ?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql,
                order.getCartId(),
                order.getVehicleId(),
                order.getRentStartDate(),
                order.getRentEndDate(),
                order.getPrice());
        return result > 0;
    }

    @Override
    public boolean updateOrder(Orders order) {
        String sql = "UPDATE dbo.Orders SET vehicleId = ?, rentStartDate = ?, rentEndDate = ?, price = ? "
                + "WHERE cartDetailId = ?";
        int result = JdbcTemplateUtil.update(sql,
                order.getVehicleId(),
                order.getRentStartDate(),
                order.getRentEndDate(),
                order.getPrice(),
                order.getCartDetailId());
        return result > 0;
    }

    @Override
    public boolean deleteOrder(Integer cartDetailId) {
        String sql = "DELETE FROM dbo.Orders WHERE cartDetailId = ?";
        int result = JdbcTemplateUtil.update(sql, cartDetailId);
        return result > 0;
    }

    @Override
    public boolean clearOrdersByCart(Integer cartId) {
        String sql = "DELETE FROM dbo.Orders WHERE cartId = ?";
        int result = JdbcTemplateUtil.update(sql, cartId);
        return result >= 0;
    }

    @Override
    public boolean convertCartToContract(Integer cartId, Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
