package mapper;

import dto.CategoryDTO;
import model.Categories;
import util.di.annotation.Component;

/**
 * CategoryMapper - Chuyển đổi giữa CategoryDTO và Categories Model
 * 
 * MỤC ĐÍCH:
 * - Giúp tách biệt tầng DTO và tầng Model
 * - Dễ dàng chuyển đổi dữ liệu giữa các lớp logic và view
 */
@Component
public class CategoryMapper {

    /**
     * Chuyển từ Model (Categories) sang DTO (CategoryDTO)
     */
    public CategoryDTO toDTO(Categories category) {
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setCars(category.getCars()); // Có thể null hoặc danh sách xe

        return dto;
    }

    /**
     * Chuyển từ DTO (CategoryDTO) sang Model (Categories)
     */
    public Categories toModel(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Categories category = new Categories();
        category.setCategoryId(dto.getCategoryId());
        category.setCategoryName(dto.getCategoryName());
        category.setCars(dto.getCars());

        return category;
    }

    /**
     * Chuyển từ Model sang DTO kèm danh sách xe (nếu có)
     * Có thể dùng khi cần trả về thông tin chi tiết danh mục
     */
    public CategoryDTO toDTOWithCars(Categories category) {
        CategoryDTO dto = toDTO(category);
        if (dto != null && category.getCars() != null) {
            dto.setCars(category.getCars());
        }
        return dto;
    }
}
