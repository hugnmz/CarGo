
package mapper;

import dto.CartDTO;

import model.Carts;
import util.di.annotation.Component;



/**
 *
 * @author admin
 */
@Component
public class CartMapper {

    // Comment removed
    public CartDTO toDTO(Carts cart) {
        if (cart == null) {
            return null;
        }

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setCustomerId(cart.getCustomerId());
        dto.setCreateAt(cart.getCreateAt());

        // Comment removed
        if (cart.getCustomer() != null) {
            dto.setCustomerName(cart.getCustomer().getFullName());
        }

        return dto;
    }

    // Comment removed
    public Carts toModel(CartDTO dto) {
        if (dto == null) {
            return null;
        }

        Carts cart = new Carts();
        cart.setCartId(dto.getCartId());
        cart.setCustomerId(dto.getCustomerId());
        cart.setCreateAt(dto.getCreateAt());

        return cart;
    }

}

