package mapper;

import dto.FeedbackDTO;
import model.Feedback;
import model.Customer;
import model.Vehicle;
import util.di.annotation.Component;


@Component
public class FeedbackMapper {

    // Chuyen tu Model sang DTO
    public FeedbackDTO toDTO(Feedback feedback) {
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
            Customer customer = feedback.getCustomer();
            dto.setCustomerName(customer.getFullName());
            dto.setCustomerEmail(customer.getEmail());
        }

        // Vehicle information (nested)
        if (feedback.getVehicle() != null) {
            Vehicle vehicle = feedback.getVehicle();
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
    public Feedback toModel(FeedbackDTO dto) {
        if (dto == null) {
            return null;
        }

        Feedback feedback = new Feedback();
        
        // Basic feedback fields
        feedback.setFeedbackId(dto.getFeedbackId());
        feedback.setCustomerId(dto.getCustomerId());
        feedback.setVehicleId(dto.getVehicleId());
        feedback.setComment(dto.getComment());
        feedback.setCreateAt(dto.getCreateAt());

        // Create nested Customer object if customer info is provided
        if (dto.getCustomerName() != null || dto.getCustomerEmail() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(dto.getCustomerId());
            customer.setFullName(dto.getCustomerName());
            customer.setEmail(dto.getCustomerEmail());
            feedback.setCustomer(customer);
        }

        // Create nested Vehicle object if vehicle info is provided
        if (dto.getPlateNumber() != null || dto.getCarName() != null) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleId(dto.getVehicleId());
            vehicle.setPlateNumber(dto.getPlateNumber());
            feedback.setVehicle(vehicle);
        }

        return feedback;
    }
}