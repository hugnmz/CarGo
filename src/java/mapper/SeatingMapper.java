package mapper;

import dto.SeatingDTO;
import model.Seatings;
import util.di.annotation.Component;

/**
 * SeatingMapper - Chuyển đổi giữa SeatingDTO và Seatings Model
 */
@Component
public class SeatingMapper {

    // Chuyển từ Model sang DTO
    public SeatingDTO toDTO(Seatings seating) {
        if (seating == null) {
            return null;
        }

        SeatingDTO dto = new SeatingDTO();

        // Basic seating fields
        dto.setSeatingId(seating.getSeatingId());
        dto.setSeatingType(seating.getSeatingType());

        // Thống kê (tùy chọn)
        if (seating.getCars() != null) {
            dto.setCars(seating.getCars());
        }

        return dto;
    }

    // Chuyển từ DTO sang Model
    public Seatings toModel(SeatingDTO dto) {
        if (dto == null) {
            return null;
        }

        Seatings seating = new Seatings();

        // Basic seating fields
        seating.setSeatingId(dto.getSeatingId());
        seating.setSeatingType(dto.getSeatingType());

        return seating;
    }

    /**
     * Chuyển từ Model sang DTO với danh sách Cars
     */
    public SeatingDTO toDTOWithCars(Seatings seating) {
        SeatingDTO dto = toDTO(seating);

        if (dto != null && seating.getCars() != null) {
            dto.setCars(seating.getCars());
        }

        return dto;
    }
}
