
package mapper;

import dto.OrderDTO;
import model.Orders;
import util.di.annotation.Component;

/**
 *
 * @author admin
 */
@Component
public class OrderMapper {

    public OrderDTO toDTO(Orders order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setCartDetailId(order.getCartDetailId());
        dto.setCartId(order.getCartId());
        dto.setVehicleId(order.getVehicleId());
        dto.setRentStartDate(order.getRentStartDate());
        dto.setRentEndDate(order.getRentEndDate());
        dto.setPrice(order.getPrice());

        // Comment removed
        if (order.getVehicle() != null) {
            dto.setPlateNumber(order.getVehicle().getPlateNumber());

            // Comment removed
            if (order.getVehicle().getCar() != null) {
                dto.setCarName(order.getVehicle().getCar().getName());
            }
        }

        return dto;
    }

    public Orders toModel(OrderDTO dto) {
        if (dto == null) {
            return null;
        }

        Orders order = new Orders();
        order.setCartDetailId(dto.getCartDetailId());
        order.setCartId(dto.getCartId());
        order.setVehicleId(dto.getVehicleId());
        order.setRentStartDate(dto.getRentStartDate());
        order.setRentEndDate(dto.getRentEndDate());
        order.setPrice(dto.getPrice());

        return order;
    }
}

