package mapper;

import dto.FeedbackDTO;
import model.Feedbacks;
import model.Customers;
import model.Vehicles;
import util.di.annotation.Component;

/**
 * FeedbackMapper - Chuyển đổi giữa FeedbackDTO và Feedbacks Model
 */
@Component
public class FeedbackMapper {

    // Chuyen tu Model sang DTO
    public FeedbackDTO toDTO(Feedbacks feedback) {
        if (feedback == null) {
            return null;
        }

        FeedbackDTO dto = new FeedbackDTO();
        
        // Basic feedback fields
        dto.setFeedbackId(feedback.getFeedbackId());
        dto.setCustomerId(feedback.getCustomerId());
        dto.setVehicleId(feedback.getVehicleId());
        dto.setComment(feedback.getComment());
        dto.setCreateAt(feedback.getCreateAt());

        // Customer information (nested)
        if (feedback.getCustomer() != null) {
            Customers customer = feedback.getCustomer();
            dto.setCustomerName(customer.getFullName());
            dto.setCustomerEmail(customer.getEmail());
        }

        // Vehicle information (nested)
        if (feedback.getVehicle() != null) {
            Vehicles vehicle = feedback.getVehicle();
            dto.setPlateNumber(vehicle.getPlateNumber());
            
            // Car information from vehicle
            if (vehicle.getCar() != null) {
                dto.setCarName(vehicle.getCar().getName());
                dto.setCarImage(vehicle.getCar().getImage());
            }
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Feedbacks toModel(FeedbackDTO dto) {
        if (dto == null) {
            return null;
        }

        Feedbacks feedback = new Feedbacks();
        
        // Basic feedback fields
        feedback.setFeedbackId(dto.getFeedbackId());
        feedback.setCustomerId(dto.getCustomerId());
        feedback.setVehicleId(dto.getVehicleId());
        feedback.setComment(dto.getComment());
        feedback.setCreateAt(dto.getCreateAt());

        // Create nested Customer object if customer info is provided
        if (dto.getCustomerName() != null || dto.getCustomerEmail() != null) {
            Customers customer = new Customers();
            customer.setCustomerId(dto.getCustomerId());
            customer.setFullName(dto.getCustomerName());
            customer.setEmail(dto.getCustomerEmail());
            feedback.setCustomer(customer);
        }

        // Create nested Vehicle object if vehicle info is provided
        if (dto.getPlateNumber() != null || dto.getCarName() != null) {
            Vehicles vehicle = new Vehicles();
            vehicle.setVehicleId(dto.getVehicleId());
            vehicle.setPlateNumber(dto.getPlateNumber());
            feedback.setVehicle(vehicle);
        }

        return feedback;
    }
}
