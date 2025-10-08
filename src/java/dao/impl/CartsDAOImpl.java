/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.CartsDAO;
import java.util.Optional;
import model.Carts;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author admin
 */

@Repository
public class CartsDAOImpl implements CartsDAO {

    @Override
    public Optional<Carts> getCartByCustomer(Integer customerId) {
        String sql = "select * from Carts where customerId = ?";
        Carts cart = JdbcTemplateUtil.queryOne(sql, Carts.class, customerId);
        return Optional.ofNullable(cart); // boc carrt co the null
    }

    @Override
    public boolean createCart(Integer customerId) {
        String sql = "INSERT INTO dbo.Carts(customerId,createAt) VALUES (?,GETDATE())";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql, customerId);
        return result > 0;
    
    }

    @Override
    public boolean deleteCart(Integer cartId) {
        String sql = "DELETE FROM dbo.Carts WHERE cartId = ?";
        int result = JdbcTemplateUtil.update(sql, cartId);
        return result > 0;
    }

    @Override
    public boolean clearCart(Integer cartId) {
        String sql = "DELETE FROM dbo.Orders WHERE cartId IN (SELECT cartId FROM dbo.Carts WHERE customerId = ?)";
        int result = JdbcTemplateUtil.update(sql, cartId);
        return result >= 0;
    }

}
