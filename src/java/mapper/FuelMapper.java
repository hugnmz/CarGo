package mapper;

import dto.FuelDTO;
import model.Fuels;
import util.di.annotation.Component;

/**
 * FuelMapper - Chuyển đổi giữa FuelDTO và Fuels Model
 */
@Component
public class FuelMapper {

    // Chuyển từ Model sang DTO
    public FuelDTO toDTO(Fuels fuel) {
        if (fuel == null) {
            return null;
        }

        FuelDTO dto = new FuelDTO();

        // Basic fuel fields
        dto.setFuelId(fuel.getFuelId());
        dto.setFuelType(fuel.getFuelType());

        // Thống kê hoặc dữ liệu liên quan (tùy chọn)
        if (fuel.getCars() != null) {
            dto.setCars(fuel.getCars());
        }

        return dto;
    }

    // Chuyển từ DTO sang Model
    public Fuels toModel(FuelDTO dto) {
        if (dto == null) {
            return null;
        }

        Fuels fuel = new Fuels();

        // Basic fuel fields
        fuel.setFuelId(dto.getFuelId());
        fuel.setFuelType(dto.getFuelType());

        return fuel;
    }

    /**
     * Chuyển từ Model sang DTO với danh sách Cars
     */
    public FuelDTO toDTOWithCars(Fuels fuel) {
        FuelDTO dto = toDTO(fuel);

        if (dto != null && fuel.getCars() != null) {
            dto.setCars(fuel.getCars());
        }

        return dto;
    }
}
